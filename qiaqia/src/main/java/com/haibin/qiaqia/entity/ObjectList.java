package com.haibin.qiaqia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2016/9/6.
 */

public class ObjectList implements Serializable{

    private List<HotSerach> list_hot_search;

    public List<HotSerach> getList_hot_search() {
        return list_hot_search;
    }
    public void setList_hot_search(List<HotSerach> list_hot_search) {
        this.list_hot_search = list_hot_search;
    }
}
