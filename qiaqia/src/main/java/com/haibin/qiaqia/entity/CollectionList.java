package com.haibin.qiaqia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2016/8/27.
 */

public class CollectionList implements Serializable {

    private List<Collection> list_collection;

    public List<Collection> getList_collection() {
        return list_collection;
    }

    public void setList_collection(List<Collection> list_collection) {
        this.list_collection = list_collection;
    }
}
