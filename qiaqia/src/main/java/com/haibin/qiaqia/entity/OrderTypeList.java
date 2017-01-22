package com.haibin.qiaqia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2016/8/25.
 */

public class OrderTypeList implements Serializable{

    private List<OrderType> list_orders;

    public List<OrderType> getList_orders() {
        return list_orders;
    }
    public void setList_orders(List<OrderType> list_orders) {
        this.list_orders = list_orders;
    }
}
