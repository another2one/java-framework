package com.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringUtilTest {

    @Test
    void trimBothEndsChars() {
        String s = "/D:/app/java/embedded/target/classes/com/controller/\\";
        Assertions.assertEquals("D:/app/java/embedded/target/classes/com/controller", StringUtil.trimBothEndsChars(s, "/\\"));
    }

    @Test
    void groupMatch() {
        Integer id = 96699;
        String url = "/user/" + id;
        String nameReg = "/user/(?<id>\\d+)";
//        Map<String, String> groupMap = StringUtil.groupMatch(url, nameReg);
//        Assertions.assertEquals(id, Integer.parseInt(groupMap.get("id")));
//        Assertions.assertTrue(url.matches(nameReg), String.format("路由规则不匹配 %s %s", nameReg, url));

        ArrayList<String> names = StringUtil.getRegNames(nameReg);
        names.forEach(e -> System.out.printf("[e] == %s \n", e));
        for(String s : names.toArray(new String[0])) {
            System.out.printf("[s] == %s \n", s);
        }
    }
}