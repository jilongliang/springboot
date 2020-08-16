package com.flong.springboot.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件集合
 */
@Data
@ApiModel("查询参数集合")
public class Conditions implements Serializable {
    /**
     * 查询条件列表
     */
    @ApiModelProperty(value = "查询条件列表")
    private List<Condition> conditionList;

}
