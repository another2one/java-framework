package com.utils;

import com.bean.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

class JacksonUtilTest {

    @Test
    void testReadString() {
        String s = "[\"name\"]";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readValue(s, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testJson() throws IOException {
        HashMap<String, Object> m1 = new HashMap<>(0);
        m1.put("users", User.users);
        System.out.printf("[json] == %s \n", new Result().success(m1));
    }
}