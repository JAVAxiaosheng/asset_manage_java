package com.graduation.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类
 *
 * @param<T>
 */
@Data
public class R<T> {
    private Integer code;
    private String message;
    private T data;
    private long total;
    private Map map = new HashMap();
    private static final int successCode = 0;

    public static <T> R<T> success(T object, long total) {
        R<T> r = new R<T>();
        r.code = successCode;
        r.message = "";
        r.data = object;
        r.total = total;
        return r;
    }

    public static <T> R<T> error(String message) {
        R<T> r = new R<T>();
        r.message = message;
        r.code = 1;
        r.total = 0L;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
