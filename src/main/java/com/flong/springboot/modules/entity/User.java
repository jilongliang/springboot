package com.flong.springboot.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("all")
@TableName("t_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    private Long userId;

    /**
     * 用户名
     */

    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 逻辑删除(0-未删除,1-已删除)
     */
    @TableLogic
    private String isDeleted;

    /**
     * 创建时间,允许为空,让数据库自动生成即可
     */
    private Date createTime;
}
