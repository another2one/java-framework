package com.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PostDispatcher extends AbstractDispatcher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    Object instance; // Controller实例
    Method method; // Controller方法
    String[] names; // url 命名参数 如：/user/?<id>\d+ => ["id"]
    Class<?>[] parameterClasses; // 方法参数类型
    ObjectMapper objectMapper; // JSON映射

    public PostDispatcher(Object newInstance, Method method, Class<?>[] parameterTypes, String[] names) {
        this.instance = newInstance;
        this.method = method;
        this.parameterClasses = parameterTypes;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.names = names;
    }

    public String[] getNames() {
        return this.names;
    }

    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else if (parameterClass == String.class) {
                try {
                    arguments[i] = new String(request.getInputStream().readAllBytes());
                    logger.info("字符串参数: {}", arguments[i]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 读取JSON并解析为JavaBean:
                try (BufferedReader reader = request.getReader()) {
                    arguments[i] = this.objectMapper.readValue(reader, parameterClass);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return (ModelAndView) this.method.invoke(instance, arguments);
    }
}