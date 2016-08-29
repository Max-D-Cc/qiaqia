package com.haibin.qiaqia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2016/8/29.
 */

public class Collection implements Serializable{

    private int id;
    private int login_id;
    private int commodity_id;
    private OrderInner commodity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public OrderInner getCommodity() {
        return commodity;
    }

    public void setCommodity(OrderInner commodity) {
        this.commodity = commodity;
    }
}
