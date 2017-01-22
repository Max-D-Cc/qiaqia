package com.haibin.qiaqia.entity;

import java.io.Serializable;

/**
 * Created by cai on 2016/10/23.
 */

public class SendGoodsTime implements Serializable{

    private String date;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
