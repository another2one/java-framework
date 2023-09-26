package com.bean;

/**
 * @author lizhi
 */
public class User extends Bean {
    public long id;
    public String name;
    public School school;

    public User(long id, String name, School school) {
        this.id = id;
        this.name = name;
        this.school = school;
    }
}