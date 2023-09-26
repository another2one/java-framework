package com.utils;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.framework.GetDispatcher;
import com.framework.PostDispatcher;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClassUtilTest {

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
    void getClassesFromPackage() {
        classUtil.getClassesFromPackage("com.controller").forEach((singleClass) -> {
            for (Method method : singleClass.getDeclaredMethods()) {
                GetMapping getAnno = method.getAnnotation(GetMapping.class);

                ArrayList<String> paramNames = new ArrayList<>();
                for (Parameter parameter : method.getParameters()) {
                    paramNames.add(parameter.getName());
                }
                if (getAnno != null) {
                    logger.info("get 请求 {}", getAnno.value());
                    new GetDispatcher(singleClass, method, paramNames.toArray(new String[0]), method.getParameterTypes(),
                            StringUtil.getRegNames(getAnno.value()).toArray(new String[0]));
                }
                PostMapping postAnno = method.getAnnotation(PostMapping.class);
                if (postAnno != null) {
                    logger.info("post 请求 {}", postAnno.value());
//                    dispatcherServlet.addPostMappings(postAnno.value(), new PostDispatcher(singleClass, method, paramNames.toArray(new String[0]), method.getParameterTypes()));
                }

            }
        });
    }
}