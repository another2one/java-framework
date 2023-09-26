package com.listener;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

/**
 * @author lizhi
 */
@WebListener
public class RequestAttrListener implements ServletRequestAttributeListener {
    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        System.out.printf("[attributeAdded] == %s \n", "attributeAdded");
        System.out.printf("[name] == %s \n", srae.getName());
        System.out.printf("[valie] == %s \n", srae.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        System.out.printf("[attributeRemoved] == %s \n", "attributeRemoved");
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        System.out.printf("[attributeReplaced] == %s \n", "attributeReplaced");
    }
}
