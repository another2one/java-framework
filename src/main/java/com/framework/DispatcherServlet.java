package com.framework;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.utils.ClassUtil;
import com.utils.StringUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;

/**
 * @author lizhi
 */
@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, GetDispatcher> getMappings = new HashMap<>();
    private Map<String, PostDispatcher> postMappings = new HashMap<>();
    private ViewEngine viewEngine;

    @Override
    public void init() {
        scanControllers();
        this.viewEngine = new ViewEngine(getServletContext());
    }

    public void scanControllers() {
        logger.info("init {}...", getClass().getSimpleName());
        List<Class<?>> classes = (new ClassUtil()).getClassesFromPackage("com.controller");
        logger.info("获取到的class {}", classes.stream().map(Class::getName).collect(Collectors.joining(", ")));
        classes.forEach((singleClass) -> {
            logger.info("处理 class: {}", singleClass.getName());
            for (Method method : singleClass.getDeclaredMethods()) {
                logger.info("    -- 处理 method: {}", method.getName());
                GetMapping getAnno = method.getAnnotation(GetMapping.class);

                ArrayList<String> paramNames = new ArrayList<>();
                for (Parameter parameter : method.getParameters()) {
                    paramNames.add(parameter.getName());
                }
                if (getAnno != null) {
                    logger.info("add get router: {}", getAnno.value());
                    try {
                        String[] names = StringUtil.getRegNames(getAnno.value()).toArray(new String[0]);
                        logger.info("names = {}", String.join(",", names));
                        this.getMappings.put(
                                getAnno.value(),
                                new GetDispatcher(singleClass.getDeclaredConstructor().newInstance(), method,
                                        paramNames.toArray(new String[0]), method.getParameterTypes(), names));
                        for(String s : this.getMappings.get(getAnno.value()).names) {
                            System.out.printf("[name] == %s \n", s);
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                PostMapping postAnno = method.getAnnotation(PostMapping.class);
                if (postAnno != null) {
                    logger.info("add post router: {}", postAnno.value());
                    try {
                        String[] names = StringUtil.getRegNames(postAnno.value()).toArray(new String[0]);
                        logger.info("names = {}", String.join(",", names));
                        this.postMappings.put(
                                postAnno.value(),
                                new PostDispatcher(singleClass.getDeclaredConstructor().newInstance(), method,
                                        method.getParameterTypes(),
                                        names));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp, this.getMappings);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp, this.postMappings);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp,
                         Map<String, ? extends AbstractDispatcher> dispatcherMap) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        logger.info("{}新请求路由：{}", req.getMethod(), path);
        AbstractDispatcher dispatcher = null;
        // 匹配路由
        for (String key : dispatcherMap.keySet()) {
            if (path.matches(key)) {
                logger.info("{} 匹配到路由规则 {}", path, key);
                dispatcher = dispatcherMap.get(key);
                if (dispatcher.getNames() != null) {
                    Pattern pattern = Pattern.compile(key);
                    Matcher matcher = pattern.matcher(path);
                    if (!matcher.find()) {
                        logger.info("命名路由 matcher没有匹配到 reg = {}; string= {}", key, path);
                    }
                    // 匹配命名路由
                    for (String name : dispatcher.getNames()) {
                        try {
                            String nameValue = matcher.group(name);
                            req.setAttribute(name, nameValue);
                            logger.info("命名路由 {} = {}", name, nameValue);
                        } catch (IllegalArgumentException e) {
                            logger.error("{} 没有匹配到 参数 ({}, {})", name, key, path);
                        }
                    }
                }
                break;
            }
        }
        if (dispatcher == null) {
            resp.sendError(404);
            return;
        }
        ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req, resp);
        } catch (ReflectiveOperationException e) {
            throw new ServletException(e);
        }
        if (mv == null) {
            return;
        }
        if (mv.view.startsWith("redirect:")) {
            resp.sendRedirect(mv.view.substring(9));
            return;
        }
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
    }
}

abstract class AbstractDispatcher {
    abstract String[] getNames();

    abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException;
}