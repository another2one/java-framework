package com.framework;

import com.utils.StringUtil;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAndView {
    public String view;
    public Map<String, Object> model;

    public static String templatePath = "/WEB-INF/templates";

    public ModelAndView(String view) {
        this.view = view;
        this.model = Map.of();
    }

    public ModelAndView(String view, String name, Object value) {
        this.view = view;
        this.model = new HashMap<>();
        this.model.put(name, value);
    }

    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        this.model = new HashMap<>(model);
    }

    public ModelAndView(String view, String name, List<Class<?>> list) {
        this.view = view;
        this.model = new HashMap<>();
        this.model.put(name, list);
    }
}
