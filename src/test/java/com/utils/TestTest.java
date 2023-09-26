package com.utils;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.framework.GetDispatcher;
import com.model.User;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

class TestTest {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    private ClassUtil classUtil;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.classUtil = new ClassUtil();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void testSomething() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("lizhi", "lizhi123", DateUtil.parseDateTime("1994-12-06 02:00:00")));
        users.add(new User("lipan", "lipan123", DateUtil.parseDateTime("1993-09-06 03:00:00")));
        users.add(new User("lihan", "lihan123", DateUtil.parseDateTime("2022-08-13 04:00:00")));
        users.stream().map((user) -> {
            user.password = user.password.substring(0, 3) + "***";
            return user;
        }).forEach(user -> {
            logger.info("user({}) password id {}", user.name, user.password);
        });
    }
}