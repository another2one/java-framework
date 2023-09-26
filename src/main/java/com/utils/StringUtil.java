package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 这个方法意外情况有点多，特别是特殊字符的时候
     * @param srcStr
     * @param splitter
     * @return
     */
    public static String trimBothEndsChars(String srcStr, String splitter) {
        StringBuilder sb = new StringBuilder();
        int strLen = splitter.length();
        for (int i = 0; i < strLen; i++) {
            String s = String.valueOf(splitter.charAt(i));
            if ("\\".equals(s)) {
                s = "\\\\";
            } else if ("|".equals(s)) {
                logger.error("出现了|字符, srcStr = {}, splitter = {}", srcStr, splitter);
                continue;
            }
            sb = strLen - 1 == i ? sb.append(s) : sb.append(s + "|");
        }
        String reg = sb.toString();
        logger.debug("[reg] == {} \n", reg);
        String regex = "^[\\\\|/]*|[\\\\|/]*$";
        srcStr =  srcStr.replaceAll(regex, "");
        return srcStr;
    }

    /**
     * 匹配命名规则到 map 例如 /user/(?<id>\d+) => ["id" => ""]
     * @param srcStr
     * @param reg
     * @return
     */
    public static Map<String, String> groupMatch(String srcStr, String reg) {
        Map<String, String> groupMap = new java.util.HashMap<>(Map.of());
        ArrayList<String> names = getRegNames(reg);

        // 规则匹配
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(srcStr);
        if (!matcher.find()) {
            logger.error("{} {} 不匹配", srcStr, reg);
            return groupMap;
        }

        // 组装数组
        names.forEach(name -> {
            try {
                groupMap.put(name, matcher.group(name));
            } catch (IllegalArgumentException e) {
                logger.error("{} 没有匹配到 参数 ({}, {})", name, srcStr, reg);
            }
        });
        return groupMap;
    }

    public static ArrayList<String> getRegNames(String reg) {
        ArrayList<String> names = new ArrayList<>();

        String nameReg = ".*?<(\\w+)>";

        Pattern namePattern = Pattern.compile(nameReg);
        Matcher nameMatcher = namePattern.matcher(reg);
        if (!nameMatcher.find()) {
            logger.error("{} 不是命名pattern", reg);
            return names;
        }
        for (int i = 1; i <= nameMatcher.groupCount(); i++) {
            names.add(nameMatcher.group(i));
        }
        return names;
    }

}
