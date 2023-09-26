package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher("/hi").forward(req, resp);
        // 设置响应类型:
        resp.setContentType("text/html");
        resp.setHeader("Content-Type", "text/html; charset=utf-8");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();

        String name = (String) req.getSession().getAttribute("user");
        if (name == null) {
            // 未登录，显示登录链接:
            pw.write("<p><a href=\"/signin\">Sign In</a></p>");
        } else {
            pw.write(String.format("<h1>Hello, %s</h1>", name));
            pw.write("<p><a href=\"/signout\">Sign Out</a></p>");
        }

        // 打印 header
        pw.write("<br/><h4>header</h4>");
        Enumeration<String> HeaderNames = req.getHeaderNames();
        while (HeaderNames.hasMoreElements()) {
            String key = HeaderNames.nextElement();
            String value = req.getHeader(key);
            // cookie 需要url解码
            if (key.equals("cookie")) {
                value = URLDecoder.decode(value, "utf-8");
            }
            pw.write(String.format("%s:%s <br/>", key, value));
        }

        // 打印 session
        pw.write("<br/><h4>session "+req.getSession().getId()+"</h4>");
        Enumeration<String> sessionNames = req.getSession().getAttributeNames();
        while (sessionNames.hasMoreElements()) {
            String key = sessionNames.nextElement();
            // cookie 需要decode
            pw.write(String.format("%s:%s <br/>", key, req.getSession().getAttribute(key)));
        }

        // 设置 cookie
        if (!Arrays.stream(req.getCookies()).toList().contains("time")) {
            String timeFormat = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
            Cookie cookie = new Cookie("time", URLEncoder.encode(timeFormat, "utf-8"));
            System.out.printf("[date] == %s \n", timeFormat);
            cookie.setPath("/");
            cookie.setMaxAge(10);
            resp.addCookie(cookie);
        }

        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}