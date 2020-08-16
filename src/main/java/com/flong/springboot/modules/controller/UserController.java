package com.flong.springboot.modules.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flong.springboot.core.util.BuildConditionWrapper;
import com.flong.springboot.core.util.DateUtil;
import com.flong.springboot.core.vo.Conditions;
import com.flong.springboot.modules.entity.User;
import com.flong.springboot.modules.mapper.UserMapper;
import com.flong.springboot.modules.service.RedisService;
import com.flong.springboot.modules.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:liangjl
 * @Date:2020-08-16
 * @Description:用户控制层
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final String USER_REDIS_KEY = "QUERY_USER_LIST_REDIS_KEY";
    private final String USER_REDIS_BACKUP_KEY = "USER_REDIS_BACKUP_KEY";
    private final static String CURRENT_USER  = "CURRENT_USER";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    //创建公平锁
    private Lock rock = new ReentrantLock(true);


    /**
     * 添加
     */
    @RequestMapping("/add")
    public void add() {
        userMapper.insert(User.builder().userName("周伯通").passWord("123456").build());
    }

    /**
     * 修改
     * @param user
     */
    @PutMapping("/updateById")
    public void updateById(@RequestBody User user) {
        userMapper.updateById(user);
    }
    /**
     * 删除通过多个主键Id进行删除
     * @param ids
     */
    @DeleteMapping("/deleteByIds")
    public void deleteByIds(@RequestBody List<String> ids) {
        userMapper.deleteBatchIds(ids);
    }

    /**
     * 通过指定Id进行查询
     *
     * @param userId
     */
    @GetMapping("/getOne/{userId}")
    public void getOne(@PathVariable("userId") Long userId) {
        User user = userMapper.selectById(userId);
        System.out.println(JSON.toJSON(user));

    }

    /**
     * 用户分页，参数有多个使用下标索引进行处理.如果有两个参数(如用户名和地址)：conditionList[0].fieldName=userName、 conditionList[0].fieldName=address
     * 未转码请求分页地址: http://localhost:7011/user/page?conditionList[0].fieldName=userName&conditionList[0].operation=LIKE&conditionList[0].value=周
     * 已转码请求分页地址: http://localhost:7011/user/page?conditionList[0].fieldName=userName&conditionList[0].operation=LIKE&conditionList[0].value=%E5%91%A8
     * @param page
     * @param conditions 条件
     */
    @GetMapping("/page")
    public IPage<User> page(Page page, Conditions conditions) {
        QueryWrapper<User> build = BuildConditionWrapper.build(conditions.getConditionList(), User.class);
        build.lambda().orderByDesc(User::getCreateTime);
        return userService.page(page, build);
    }

    /**
     * 通过用户ID进行查询
     * @param userId
     * @return
     */
    @GetMapping
    @RequestMapping("/findOne/{userId}")
    public User findOne(@PathVariable("userId")Long userId) {

        //1-->测试不使用缓存的查询
        //return nonUseRedis(userId);

        //2-->测试使用redis缓存,但没线程synchronized重量级锁)安全的并发查询
        //return nonUseThreadButRedis(userId);

        //3-->使用rock并发和redis进行处理缓存
        return nonUseRockButRedis(userId);

    }
    /**
     * @Description 没使用线程安全查询，但是使用了redis缓存,1000个并发的时候运行一段时间之后还是会挂好几个，虽然减少部分错误.
     *              在方法加了synchronized虽然能解决部分的并发请求，但是超过1000以上一直卡死不动.完全死锁不释放.
     * @param userId
     * @return 参数
     * @return User 返回类型
     */
    private  User nonUseRockButRedis(Long userId) {
        User user = null;
        rock.lock();
        //如果请求拿到cup分配的线程锁就往下走.
        if(rock.tryLock()) {
            try {
                Date startDate = new Date() ;//开始时间
                String startDateStr  = DateUtil.formatDate(startDate, "yyyy-MM-dd HH:mm:ss");
                //从缓存获取数据
                user = (User)redisService.get(USER_REDIS_KEY+"_"+userId);

                //判断缓存是否等于空
                if(ObjectUtil.isEmpty(user)) {
                    //查数据库
                    user = userService.getById(userId);
                    Date endDate = new Date() ;//结束时间
                    redisService.set(USER_REDIS_KEY+"_"+userId, user,20L);

                    //如果数据库的值为空就把它设置一个过期时间
                    if(ObjectUtil.isEmpty(user)) {
                        redisService.set(USER_REDIS_KEY+"_"+userId, user,20L);
                    }else {
                        //放入备份缓存
                        redisService.set(USER_REDIS_BACKUP_KEY+"_"+userId, user,20L);
                    }
                    String endDateStr= DateUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
                    log.info("数据库-->执行情况-->>【开始时间为："+startDateStr+"\t,结束时间为："+endDateStr+
                            "\t,执行秒数："+ DateUtil.getBetweeDay(endDate, startDate)+"】");

                }else {
                    Date endDate = new Date() ;//结束时间
                    String endDateStr= DateUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
                    log.info("缓存-->执行情况-->>【开始时间为："+startDateStr+"\t,结束时间为："+endDateStr+
                            "\t,执行秒数："+DateUtil.getBetweeDay(endDate, startDate)+"】");

                }
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //手动释放锁
                rock.unlock();
            }
        }else {
            //从备份缓存获取springboot初始化.
            user = (User)redisService.get(USER_REDIS_BACKUP_KEY+"_"+userId);
        }

        return user;
    }



    /**
     * @Description 没使用线程安全查询，但是使用了redis缓存,1000个并发的时候运行一段时间之后还是会挂好几个，虽然减少部分错误.
     *              在方法加了synchronized虽然能解决部分的并发请求，但是超过1000以上一直卡死不动.完全死锁不释放.
     * @Author		liangjl
     * @Date		2018年2月3日 下午2:46:14
     * @param id
     * @return 参数
     * @return List<User> 返回类型
     * @throws
     */
    private /*synchronized*/ User nonUseThreadButRedis(Long id) {
        Date startDate = new Date() ;//开始时间

        String startDateStr  = DateUtil.formatDate(startDate, "yyyy-MM-dd HH:mm:ss");
        //从缓存获取数据
        User user = (User)redisService.get(USER_REDIS_KEY+"_"+id);

        //判断缓存是否等于空
        if(ObjectUtil.isEmpty(user)) {
            //查数据库
            user = userService.getById(id);
            Date endDate = new Date() ;//结束时间
            redisService.set(USER_REDIS_KEY+"_"+id, user,20L);

            //如果数据库的值为空就把它设置一个过期时间
            if(ObjectUtil.isEmpty(user)) {
                redisService.setExpire(USER_REDIS_KEY+"_"+id, 300L);
            }

            String endDateStr= DateUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
            log.info("数据库-->执行情况-->>【开始时间为："+startDateStr+"\t,结束时间为："+endDateStr+
                    "\t,执行秒数："+DateUtil.getBetweeDay(endDate, startDate)+"】");

            return user;
        }else {

            Date endDate = new Date() ;//结束时间
            String endDateStr= DateUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
            log.info("缓存-->执行情况-->>【开始时间为："+startDateStr+"\t,结束时间为："+endDateStr+
                    "\t,执行秒数："+DateUtil.getBetweeDay(endDate, startDate)+"】");

            return user;
        }
    }



    /**
     * @Description 不使用缓存的查询,通过jmeter测试,1000个并发,直接挂很多请求
     * @Author		liangjl
     * @Date		2018年2月3日 下午2:34:43
     * @return List<User> 返回类型
     */
    private User nonUseRedis(Long id) {

        Date startDate = new Date() ;//开始时间

        String startDateStr  = DateUtil.formatDate(startDate, "yyyy-MM-dd HH:mm:ss");
        User userList = userService.getById(id);

        Date endDate = new Date() ;//结束时间
        String endDateStr= DateUtil.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");

        log.info("数据库-->执行情况-->>【开始时间为："+startDateStr+"\t,结束时间为："+endDateStr+
                "\t,执行秒数："+DateUtil.getBetweeDay(endDate, startDate)+"】");
        return userList;
    }


}
