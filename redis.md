### 1、Redis是简介

- [redis官方网](https://links.jianshu.com/go?to=https%3A%2F%2Fredis.io%2F)
- Redis是一个开源的使用ANSI [C语言](https://links.jianshu.com/go?to=http%3A%2F%2Fbaike.baidu.com%2Fview%2F1219.htm)编写、支持网络、可基于内存亦可持久化的日志型、Key-Value[数据库](https://links.jianshu.com/go?to=http%3A%2F%2Fbaike.baidu.com%2Fview%2F1088.htm)，并提供多种语言的API。从2010年3月15日起，Redis的开发工作由VMware主持。从2013年5月开始，Redis的开发由Pivotal赞助
- 工程基于基础架构 [SpringBoot+Gradle+ MyBatisPlus3.x搭建企业级的后台分离框架](https://www.jianshu.com/p/d8f06bffdbd4)进行完善

### 2、Redis开发者

- redis 的作者，叫Salvatore Sanfilippo，来自意大利的西西里岛，现在居住在卡塔尼亚。目前供职于Pivotal公司。他使用的网名是antirez。

### 3、Redis安装

- Redis安装与其他知识点请参考几年前我编写文档 `Redis Detailed operating instruction.pdf`，这里不做太多的描述，主要讲解在kotlin+SpringBoot然后搭建Redis与遇到的问题

### 4、Redis应该学习那些？

- 列举一些常见的内容

  ![img](https://upload-images.jianshu.io/upload_images/14586304-5567b7ff8752061b.png?imageMogr2/auto-orient/strip|imageView2/2/w/436/format/webp)

- Redis常用命令

  ![img](https://upload-images.jianshu.io/upload_images/14586304-147851dfc3974e44.png?imageMogr2/auto-orient/strip|imageView2/2/w/716/format/webp)

  Redis常用命令.png

### 6、 Redis常见应用场景

![img](https://upload-images.jianshu.io/upload_images/14586304-4ff8b4d82e79ef2d.png?imageMogr2/auto-orient/strip|imageView2/2/w/975/format/webp)

应用场景.png

### 7、 Redis常见的几种特征

- Redis的哨兵机制
- Redis的原子性
- Redis持久化有RDB与AOF方式

### 8、工程结构

![red](C:\Users\jilon\Desktop\red.png)



### 9、Redis的代码实现

- ##### Redis 依赖的Jar配置

  ```groovy
  compile "org.springframework.boot:spring-boot-starter-data-redis:${spring_boot_version}"
  
  compile "org.springframework.boot:spring-boot-starter-data-redis-reactive:${spring_boot_version}"
  
  compile "redis.clients:jedis:${redis_version}"
  
  ```

#####  yml配置
```yaml
  # ====================整合redis====================
  redis:
    # Redis服务器地址
    host: 192.168.1.104
    # Redis服务器连接端口
    port: 6379
    # Redis服务器连接密码（默认为空）
    password:
    # 连接超时时间（毫秒）
    timeout: 0
    # Redis数据库索引（默认为0）
    database: 0
    pool:
      # 连接池最大连接数（使用负值表示没有限制）
      max-active: 8
      # 连接池最大阻塞等待时间（使用负值表示没有限制）
      max-wait: -1
      # 连接池中的最大空闲连接
      max-idle: 8
      # 连接池中的最小空闲连接
      min-idle: 0
```

  

##### Redis 封装Service

  ```java
  
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.redis.core.*;
  import org.springframework.stereotype.Service;
  
  import java.io.Serializable;
  import java.util.List;
  import java.util.Set;
  import java.util.concurrent.TimeUnit;
  
   
  @Service
  public class RedisService {
      @Autowired
      private RedisTemplate redisTemplate;
  
      /**
       * 写入缓存
       *
       * @param key
       * @param value
       * @return
       */
      public boolean set(final String key, Object value) {
          boolean result = false;
          try {
              ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
              operations.set(key, value);
              result = true;
          } catch (Exception e) {
              e.printStackTrace();
          }
          return result;
      }
  
      /**
       * 失效
       *
       * @param key
       * @param timeout
       * @return
       */
      public boolean setExpire(final String key, Long timeout) {
          boolean result = false;
          try {
              redisTemplate.boundGeoOps(key).expire(timeout, TimeUnit.SECONDS);
              result = true;
          } catch (Exception e) {
              e.printStackTrace();
          }
          return result;
      }
  
      /**
       * 写入缓存设置时效时间
       *
       * @param key
       * @param value
       * @return
       */
      public boolean set(final String key, Object value, Long expireTime) {
          boolean result = false;
          try {
              ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
              operations.set(key, value);
              redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
              result = true;
          } catch (Exception e) {
              e.printStackTrace();
          }
          return result;
      }
  
      /**
       * 批量删除对应的value
       *
       * @param keys
       */
      public void remove(final String... keys) {
          for (String key : keys) {
              remove(key);
          }
      }
  
      /**
       * 批量删除key
       *
       * @param pattern
       */
      public void removePattern(final String pattern) {
          Set<Serializable> keys = redisTemplate.keys(pattern);
          if (keys.size() > 0) {
              redisTemplate.delete(keys);
          }
      }
  
      /**
       * 删除对应的value
       *
       * @param key
       */
      public void remove(final String key) {
          if (exists(key)) {
              redisTemplate.delete(key);
          }
      }
  
      /**
       * 判断缓存中是否有对应的value
       *
       * @param key
       * @return
       */
      public boolean exists(final String key) {
          return redisTemplate.hasKey(key);
      }
  
      /**
       * 读取缓存
       *
       * @param key
       * @return
       */
      public Object get(final String key) {
          Object result = null;
          ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
          result = operations.get(key);
          return result;
      }
  
      /**
       * 哈希 添加
       *
       * @param key
       * @param hashKey
       * @param value
       */
      public void hmSet(String key, Object hashKey, Object value) {
          HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
          hash.put(key, hashKey, value);
      }
  
      /**
       * 哈希获取数据
       *
       * @param key
       * @param hashKey
       * @return
       */
      public Object hmGet(String key, Object hashKey) {
          HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
          return hash.get(key, hashKey);
      }
  
      /**
       * 列表添加
       *
       * @param k
       * @param v
       */
      public void lPush(String k, Object v) {
          ListOperations<String, Object> list = redisTemplate.opsForList();
          list.rightPush(k, v);
      }
  
      /**
       * 列表获取
       *
       * @param k
       * @param l
       * @param l1
       * @return
       */
      public List<Object> lRange(String k, long l, long l1) {
          ListOperations<String, Object> list = redisTemplate.opsForList();
          return list.range(k, l, l1);
      }
  
      /**
       * 集合添加
       *
       * @param key
       * @param value
       */
      public void add(String key, Object value) {
          SetOperations<String, Object> set = redisTemplate.opsForSet();
          set.add(key, value);
      }
  
      /**
       * 集合获取
       *
       * @param key
       * @return
       */
      public Set<Object> setMembers(String key) {
          SetOperations<String, Object> set = redisTemplate.opsForSet();
          return set.members(key);
      }
  
      /**
       * 有序集合添加
       *
       * @param key
       * @param value
       * @param scoure
       */
      public void zAdd(String key, Object value, double scoure) {
          ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
          zset.add(key, value, scoure);
      }
  
      /**
       * 有序集合获取
       *
       * @param key
       * @param scoure
       * @param scoure1
       * @return
       */
      public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
          ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
          return zset.rangeByScore(key, scoure, scoure1);
      }
  }
  ```

  

##### Controller 代码

```java
  @Slf4j
  @RestController
  @RequestMapping("/user")
  public class UserController {
      private final String USER_REDIS_KEY = "QUERY_USER_LIST_REDIS_KEY";
      private final String USER_REDIS_BACKUP_KEY = "USER_REDIS_BACKUP_KEY";
      private final static String CURRENT_USER  = "CURRENT_USER";
  
      /**
       * 通过用户ID进行查询
       * @param userId
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
  ```

* 

### 10、访问与工程代码
* http://localhost:7011/user/findOne/{用户Id}
```
http://localhost:7011/user/findOne/1294870340966600706
```
* 工程代码在 redis 分支 https://github.com/jilongliang/springboot/tree/redis