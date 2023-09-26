package com.controller;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.bean.Result;
import com.framework.ModelAndView;
import com.model.User;
import com.utils.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class UserController {
    private ArrayList<User> users;
    @GetMapping("/user/list")
    public ModelAndView list() {
        return new ModelAndView("user/list.html", "users", User.users);
    }

    @GetMapping("/user/(?<id>\\d+)")
    public ModelAndView info(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = 0;
        System.out.printf("[req.getAttribute(\"id\")] == %s \n", req.getAttribute("id"));
        if (req.getAttribute("id") != null) {
            id = Integer.parseInt((String) req.getAttribute("id"));
        }
        if (id <= 0) {
            return new ModelAndView("/500.html", "msg", "参数错误");
        }
        User user = User.findById(id);
        if (user == null) {
            return new ModelAndView("/500.html", "msg", "用户未找到");
        }
        return new ModelAndView("user/info.html", Map.of("user", user));
    }

    @PostMapping("/user/add")
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        User.users.add(new User(name, password, LocalDateTime.now()));
        resp.setContentType("text/json");
        resp.getWriter().write((new Result()).success(new HashMap<>(0)));
    }

    @PostMapping("/user/update/(?<id>\\d+)")
    @GetMapping("/user/update/(?<id>\\d+)")
    public ModelAndView update(HttpServletRequest req) {
        // 参数处理
        Integer id = 0;
        System.out.printf("[req.getAttribute(\"id\")] == %s \n", req.getAttribute("id"));
        if (req.getAttribute("id") != null) {
            id = Integer.parseInt((String) req.getAttribute("id"));
        }
        if (id <= 0) {
            return new ModelAndView("/500.html", "msg", "参数错误");
        }
        // 查找用户
        User user = User.findById(id);
        if (user == null) {
            return new ModelAndView("/500.html", "msg", "用户未找到");
        }
        if ("get".equalsIgnoreCase(req.getMethod())) {
            return new ModelAndView("user/update.html", Map.of("user", user));
        }
        // 更新
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if (name == null || password == null || "".equals(name) || "".equals(password)) {
            return new ModelAndView("/500.html", "msg", "参数错误");
        }
        user.update(name, password);
        return new ModelAndView("redirect:/user/list");
    }
}
