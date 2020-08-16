package com.flong.springboot.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@TableName("t_user")
@ApiModel("用户实体")
public class User extends Model<User> implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String passWord;
    /**
     * 逻辑删除(0-未删除,1-已删除)
     */
    @TableLogic
    @ApiModelProperty(value = "逻辑删除(0-未删除,1-已删除)")
    private String delFlag;

    /**
     * 创建时间,允许为空,让数据库自动生成即可
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
