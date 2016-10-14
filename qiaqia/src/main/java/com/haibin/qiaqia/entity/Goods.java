package com.haibin.qiaqia.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by invinjun on 2016/6/2.
 */

public class Goods implements Serializable {

    @SerializedName("list_chao_commodity")
    @Expose
    private List<ListChaoCommodity> listChaoCommodity = new ArrayList<ListChaoCommodity>();

    private List<ListChaoCommodity> list_chao_commodityer;


    public List<ListChaoCommodity> getList_chao_commodityer() {
        return list_chao_commodityer;
    }

    public void setList_chao_commodityer(List<ListChaoCommodity> list_chao_commodityer) {
        this.list_chao_commodityer = list_chao_commodityer;
    }

    /**
     *
     * @return
     * The listChaoCommodity
     */
    public List<ListChaoCommodity> getListChaoCommodity() {
        return listChaoCommodity;
    }

    /**
     *
     * @param listChaoCommodity
     * The list_chao_commodity
     */
    public void setListChaoCommodity(List<ListChaoCommodity> listChaoCommodity) {
        this.listChaoCommodity = listChaoCommodity;
    }

}


