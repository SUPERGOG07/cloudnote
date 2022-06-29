package com.superdog.cloudnote.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class R<T> {
    private Integer status;

    private String error;

    private T data;

    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.status = 1;
        r.error = "";
        r.data = object;
        return r;
    }

    public static <T> R<T> error(String error){
        R<T> r = new R<T>();
        r.status = 1;
        r.error = error;
        return r;
    }

    public static <T> R<T> error(Integer status,String error){
        R<T> r = new R<T>();
        r.status = status;
        r.error = error;
        return r;
    }
}
