package com.flong.springboot.core.vo;

import com.flong.springboot.core.enums.JoinType;
import com.flong.springboot.core.enums.Operation;
import lombok.Data;


/**
 * 查询条件
 */
@Data
//@ApiModel("查询条件")
public class Condition {
    /**
     * 字段名称
     */
    //@ApiModelProperty(value = "字段名称")
    private String fieldName;
    /**
     * 操作符
     */
    //@ApiModelProperty(value = "操作符")
    private Operation operation;
    /**
     * 值
     */
    //@ApiModelProperty(value = "值")
    private String value;
    /**
     * 与下一条件连接方式
     */
    //@ApiModelProperty(value = "与下一条件连接方式")
    private JoinType joinType;
}