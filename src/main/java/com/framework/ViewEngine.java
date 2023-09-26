package com.framework;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.Servlet5Loader;
import io.pebbletemplates.pebble.loader.ServletLoader;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class ViewEngine {
    private final PebbleEngine engine;

    public ViewEngine(ServletContext servletContext) {
        // 定义一个ServletLoader用于加载模板:
        Servlet5Loader loader = new Servlet5Loader(servletContext);
        // 模板编码:
        loader.setCharset("UTF-8");
        // 模板前缀，这里默认模板必须放在`/WEB-INF/templates`目录:
        loader.setPrefix(ModelAndView.templatePath);
        // 模板后缀:
        loader.setSuffix("");
        // 创建Pebble实例:
        this.engine = new PebbleEngine.Builder()
                // 默认打开HTML字符转义，防止XSS攻击
                .autoEscaping(true)
                // 禁用缓存使得每次修改模板可以立刻看到效果
                .cacheActive(false)
                .loader(loader).build();
    }

    public void render(ModelAndView mv, Writer writer) throws IOException {
        // 查找模板:
        PebbleTemplate template = this.engine.getTemplate(mv.view);
        // 渲染:
        template.evaluate(writer, mv.model);
    }
}
