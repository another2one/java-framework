package com.model;

import com.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * @author lizhi
 */
public class User {
    public long id;

    public static ArrayList<User> users;
    static {
        users = new ArrayList<>();
        users.add(new User("lizhi", "lizhi123", DateUtil.parseDateTime("1994-12-06 02:00:00")));
        users.add(new User("lipan", "lipan123", DateUtil.parseDateTime("1993-09-06 03:00:00")));
        users.add(new User("lihan", "lihan123", DateUtil.parseDateTime("2022-08-13 04:00:00")));
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(String name, String password, LocalDateTime initDate) {
        Random r = new Random();
        this.id = (long) r.nextInt(90000, 100000);
        this.name = name;
        this.password = password;
        this.initDate = initDate;
    }

    public static User findById (long id) {
        User user = null;
        for (User user1: User.users) {
            if (user1.id == id) {
                user = user1;
                break;
            }
        }
        return user;
    }

    public String showPass() {
        return password.substring(0, 3) + "***";
    }

    public String name;
    public String password;
    public LocalDateTime initDate;


}