package com.flong.springboot.core.vo;

import lombok.Data;

/**
 * 统一返回包装 Created by lenovo on 2017/7/29.
 */
@Data
public class LiveResp<T> {

    private int code = 200;
    private String message = "";
    private T data;
    /**
     * 响应时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    public LiveResp() {
    }

    public LiveResp(T data) {
        this.data = data;
    }

}
