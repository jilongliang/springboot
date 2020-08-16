package com.flong.springboot.core.util;

import java.util.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description 专门处理对象的一个帮助类
 * @Author liangjl
 * @Version V1.0
 * @Copyright (c) All Rights Reserved, .
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * @param obj
     * @return boolean 返回类型(不空的时候返回true，其他返回false)
     * @Description 判断对象为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null || obj.toString().length() == 0) {
            //System.out.println("obj");
            return true;
        } else if (obj instanceof String) {
            //System.out.println("String");
            return ((String) obj).isEmpty() || ((String) obj).length() == 0;
        } else if (obj instanceof Map) {
            return ((Map) obj).size() == 0 || ((Map) obj).isEmpty();
        } else if (obj instanceof HashMap) {
            return ((HashMap) obj).size() == 0 || ((HashMap) obj).isEmpty();
        } else if (obj instanceof Vector) {
            return ((Vector) obj).size() == 0 || ((Vector) obj).isEmpty();
        } else if ((obj instanceof List)) {
            //System.out.println("List");
            return ((List) obj).size() == 0 || ((List) obj).isEmpty();
        } else if ((obj instanceof ArrayList)) {
            // System.out.println("ArrayList");
            return ((ArrayList) obj).size() == 0 || ((ArrayList) obj).isEmpty();
        } /*else if (obj instanceof R) {
            R r = (R) obj;
            Object data = r.getData();
            return isEmpty(data);
        }*/
        return false;
    }

    /**
     * @param obj
     * @return boolean 返回类型(不为空的时候返回true，其他返回false)
     * @Description 判断对象不能为空
     * @Author liangjilong
     * @Date 2016年12月26日 上午10:47:05
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * @param objs
     * @return boolean 返回类型(不为空的时候返回true，其他返回false)
     * @Description 判断数组对象不能为空
     */
    public static boolean isNotEmpty(Object[] objs) {
        boolean reflag = true;
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                boolean flag = isNotEmpty(obj);
                if (flag) {
                    reflag = true;
                    break;
                }
            }
        } else {
            reflag = false;
        }
        return reflag;
    }

    /**
     * @param objs
     * @return boolean 返回类型(不为空的时候返回true，其他返回false)
     * @Description 判断数组对象不能为空
     */
    public static boolean isNotEmpty(List<Object> objs) {
        boolean reflag = true;
        if (objs != null && objs.size() > 0) {
            for (Object obj : objs) {
                boolean flag = isNotEmpty(obj);
                if (flag) {
                    reflag = true;
                    break;
                }
            }
        } else {
            reflag = false;
        }
        return reflag;
    }

    /**
     * 计算字符串内某个字符出现的次数
     */
    public static int searchCount(String str, String searchStr) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return 0;
        }

        int found = 0;
        int index = -1;
        while ((index = str.indexOf(searchStr, index)) > -1) {
            index += 1;
            found++;
        }
        return found;
    }


    /**
     * 获取字符串长度（中文算2个)
     *
     * @param str
     * @return
     */
    public static long getWordLength(String str) {
        if (str == null || "".equals(str)) {
            return 0;
        }

        int length = 0;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }


    /**
     * jsonstring 转换成map
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object o = map.get(key);
                o = (o == null ? "" : o.toString());
                map.put(key, o.toString().trim());
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * @param array
     * @return boolean false表示有重复,true表示没重复的数据
     * @Description 检查是否重复的数据
     * @Author liangjl
     * @Date 2018年8月8日 下午2:55:16
     */
    public static boolean checkRepeat(Object[] array) {
        HashSet<Object> hashSet = new HashSet<Object>();
        for (int i = 0; i < array.length; i++) {
            hashSet.add(array[i]);
        }
        if (hashSet.size() == array.length) {
            return true; //没重复的值
        } else {
            return false; //表示有重复的值
        }
    }

    /**
     * @param @param  list
     * @param @return 参数
     * @return List 返回类型
     * @Description 去掉重复的值
     * https://www.cnblogs.com/cainiao-Shun666/p/7911142.html
     * @Author liangjl
     * @Date 2018年5月31日 下午5:13:48
     */
    public static List removeRepeatValue(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * @Descript 拼接in的参数1, 2
     * @Date 2019/7/23 11:00
     * @Author liangjl
     */
    public static String sqlInStr(List<String> params) {
        String values = "";
        for (String param : params) {
            values += param + ",";
        }
        return values.substring(0, values.length() - 1);
    }

}