package com.flong.springboot.core.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.flong.springboot.core.vo.Condition;

import java.lang.reflect.Field;
import java.util.List;

import com.flong.springboot.core.enums.JoinType;
import com.flong.springboot.core.exception.BaseException;

/**
 * 将condition数据转换成wrapper
 */
public class BuildConditionWrapper {

    public static <T> QueryWrapper<T> build(List<Condition> conditions, Class<T> clazz) {
        //初始化mybatis条件构造器
        QueryWrapper wrapper = Wrappers.query();
        if (conditions == null || conditions.size() == 0) {
            return wrapper;
        }
        try {
            for (int i = 0; i < conditions.size(); i++) {
                Condition condition = conditions.get(i);

                if (condition.getFieldName() == null) {
                    throw new BaseException("调用搜索接口时，缺少关键字[fieldName]!");
                }
                //列名称
                String columnName = getColumnName(condition.getFieldName(), clazz);

                if (condition == null || condition.getOperation() == null) {
                    throw new BaseException("操作符不能为空!");
                }

                switch (condition.getOperation()) {
                    //等于
                    case EQ:
                        wrapper.eq(columnName, condition.getValue());
                        break;
                    //大于
                    case GT:
                        wrapper.gt(columnName, condition.getValue());
                        break;
                    //小于
                    case LT:
                        wrapper.lt(columnName, condition.getValue());
                        break;
                    //不等于
                    case NEQ:
                        wrapper.ne(columnName, condition.getValue());
                        break;
                    //大于等于
                    case GTANDEQ:
                        wrapper.ge(columnName, condition.getValue());
                        break;
                    //小于等于
                    case LTANDEQ:
                        wrapper.le(columnName, condition.getValue());
                        break;
                    case LIKE:
                        wrapper.like(columnName, condition.getValue());
                        break;
                    case ISNULL:
                        wrapper.isNull(columnName);
                        break;
                    case IN:
                        //value :1,2,3,4,5,6
                        wrapper.inSql(columnName, condition.getValue());
                        break;
                    default:
                        break;
                }
                if (condition.getJoinType() == JoinType.OR && i < conditions.size() - 1) {
                    //下个条件为or连接且非最后一个条件，使用or进行连接
                    wrapper.or();
                }
            }
            return wrapper;
        } catch (Exception e) {
            throw new BaseException("查询条件不存在");
        }
    }

    /**
     * @Descript 此条件构建包装器方法是支持多个表组装成SQL字段的虚拟表，不支持实际存在的表
     * @Date 2019/6/21 13:32
     * @Author liangjl
     */
    public static <T> QueryWrapper<T> buildWarpper(List<Condition> conditions) {
        //初始化mybatis条件构造器
        QueryWrapper wrapper = Wrappers.query();
        if (conditions == null || conditions.size() == 0) {
            return wrapper;
        }
        try {
            for (int i = 0; i < conditions.size(); i++) {
                Condition condition = conditions.get(i);
                if (condition.getFieldName() == null) {
                    throw new BaseException("调用搜索接口时，缺少关键字[fieldName]!");
                }
                //列名称
                String columnName = condition.getFieldName();
                if (condition == null || condition.getOperation() == null) {
                    throw new BaseException("操作符不能为空!");
                }
                switch (condition.getOperation()) {
                    //等于
                    case EQ:
                        wrapper.eq(columnName, condition.getValue());
                        break;
                    //大于
                    case GT:
                        wrapper.gt(columnName, condition.getValue());
                        break;
                    //小于
                    case LT:
                        wrapper.lt(columnName, condition.getValue());
                        break;
                    //不等于
                    case NEQ:
                        wrapper.ne(columnName, condition.getValue());
                        break;
                    //大于等于
                    case GTANDEQ:
                        wrapper.ge(columnName, condition.getValue());
                        break;
                    //小于等于
                    case LTANDEQ:
                        wrapper.le(columnName, condition.getValue());
                        break;
                    case LIKE:
                        wrapper.like(columnName, condition.getValue());
                        break;
                    case IN:
                        //value :1,2,3,4,5,6
                        wrapper.inSql(columnName, condition.getValue());
                        break;
                    default:
                        break;
                }
                if (condition.getJoinType() == JoinType.OR && i < conditions.size() - 1) {
                    //下个条件为or连接且非最后一个条件，使用or进行连接
                    wrapper.or();
                }
            }
            return wrapper;
        } catch (Exception e) {
            throw new BaseException("查询条件不存在");
        }
    }

    /***
     * @Descript 获取指定实体Bean的字段属性
     * @Date 2019/6/19 14:51
     * @Author liangjl
     */
    public static String getColumnName(String fieldName, Class clazz) {
        try {
            //获取泛型类型字段
            Field field = clazz.getDeclaredField(fieldName);
            TableField tableFieldAnno = field.getAnnotation(TableField.class);
            String columnName = "";
            //获取对应数据库字段
            if (tableFieldAnno != null && StrUtil.isNotBlank(tableFieldAnno.value())) {
                //已定义数据库字段，取定义值
                columnName = tableFieldAnno.value();
            } else {
                //未指定数据库字段，默认驼峰转下划线
                columnName = NamingStrategyUtils.camelToUnderline(field.getName());
            }
            return columnName;
        } catch (NoSuchFieldException e) {
            throw new BaseException("查询条件不存在");
        }
    }


}
