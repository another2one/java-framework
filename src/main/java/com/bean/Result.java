package com.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author lizhi
 */
public class Result extends Bean {
    public Integer code;
    public String msg;
    public HashMap<String, Object> data;

    public Result() {
    }

    public String success(HashMap<String, Object> data) throws IOException {
        this.code = 200;
        this.msg = "";
        this.data = data;
        return toJson();
    }

    public String toJson() {
        try {
            return (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\\\"code\\\":500,\\\"msg\\\":\\\"json 解析出错\\\",\\\"data\\\":{}}";
        }
    }
}