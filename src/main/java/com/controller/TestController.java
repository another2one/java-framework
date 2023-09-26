package com.controller;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.bean.Bean;
import com.bean.School;
import com.framework.ModelAndView;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TestController {
    @GetMapping("/test1")
    public ModelAndView test1() {
        return new ModelAndView("test1.html", "name", "world");
    }

    @PostMapping("/test2")
    public ModelAndView test2(HttpServletRequest req,  HttpServletResponse resp, String name) throws IOException {
        PrintWriter pw = resp.getWriter();

        // 打印 post 参数 dispatcher 里面读取了 不能重复读
        pw.write("</br>打印 post 参数</br>");
//        ServletInputStream = req.getInputStream();
//        pw.write(new String(servletInputStream.readAllBytes()));
//        String s = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        pw.write(s);

        // 打印 get 参数
        pw.write("</br>打印 get 参数</br>");
        Enumeration<String> pNames = req.getParameterNames();
        while (pNames.hasMoreElements()) {
            String pName = pNames.nextElement();
            pw.write(pName + " : " + req.getParameter(pName));
        }
        return new ModelAndView("test2.html", "name", name != null ? name : "world");
    }
}
