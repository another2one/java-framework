package com.utils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取 package 下所有类
     * @param packageName 包名
     * @return
     */
    public List<Class<?>> getClassesFromPackage(String packageName) {
        logger.info("getClassesFromPackage .........");
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace(".", "/");
        try {
            // 循环整个包文件
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            Set<String> urls = new HashSet<>();
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                logger.info("遍历文件：" + url.getFile());
                String protocol = url.getProtocol();
                String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                String urlName = protocol + ":" + StringUtil.trimBothEndsChars(filePath, "/\\");
                if (urls.contains(urlName)) {
                    logger.info("重复出现的文件 {}", urlName);
                    continue;
                }
                urls.add(urlName);
                logger.info("debug 目录 {}", urlName);
                if ("file".equals(protocol)) {
                    classes.addAll(findClassByDirectory(packageName, filePath));
                } else if ("jar".equals(protocol)) {
                    classes.addAll(findClassInJar(packageName, url));
                } else {
                    logger.info("protocol错误: " + protocol);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    public List<Class<?>> findClassByDirectory(String packageName, String packagePath) {
        logger.info(String.format("开始通过目录查找: packageName = %s; packagePath = %s", packageName, packagePath));
        List<Class<?>> classes = new ArrayList<>();

        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.info("目录错误: " + dir.getAbsolutePath());
            return classes;
        }

        File[] files = dir.listFiles();
        if (files.length == 0) {
            logger.info("目录下没有文件: " + dir.getAbsolutePath());
            return classes;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                // 递归查找目录
                classes.addAll(findClassByDirectory(packageName + "." + fileName, file.getAbsolutePath()));
            } else if (fileName.endsWith(".class")) {
                // 类文件
                String className = fileName.substring(0,fileName.length()-6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    public List<Class<?>> findClassInJar(String packageName, URL url) {

        List<Class<?>> classes = new ArrayList<Class<?>>();

        String packageDirName = packageName.replace('.', '/');
        // 定义一个JarFile
        JarFile jar;
        try {
            // 获取jar
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }

                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageDirName) && name.endsWith(".class")) {
                    // 去掉后面的".class"
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    try {
                        // 添加到classes
                        classes.add(Class.forName(className));
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }
}
