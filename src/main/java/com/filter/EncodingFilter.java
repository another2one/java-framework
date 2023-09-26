package com.filter;

import com.framework.ReServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;

/**
 * @author lizhi
 */
@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("EncodingFilter:doFilter");
        byte[] body = req.getInputStream().readAllBytes();
        System.out.printf("[body] == %s \n", new String(body));
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        chain.doFilter(new ReServletRequest((HttpServletRequest) req, body), resp);
    }
}
