package com.controller;

import com.annot.GetMapping;
import com.annot.PostMapping;
import com.framework.ModelAndView;
import com.model.User;
import com.utils.DateUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Map;

public class CommonController {
    @GetMapping("/error500")
    public ModelAndView update(HttpServletRequest req) {
        return new ModelAndView("/500.html");
    }
}
