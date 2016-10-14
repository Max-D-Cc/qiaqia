package com.haibin.qiaqia.entity;

/**
 * Created by cai on 2016/6/25.
 */

public class ListMarket {

    private int id;
    private String class_id;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Market{" +
                "class_id='" + class_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
