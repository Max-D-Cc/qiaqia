package com.haibin.qiaqia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2016/8/30.
 */

public class VpArea implements Serializable {

    private String area_id;
    private List<Vp> list_t_banner;
    private String area_name;
    private List<Vp> list_area;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public List<Vp> getList_t_banner() {
        return list_t_banner;
    }

    public void setList_t_banner(List<Vp> list_t_banner) {
        this.list_t_banner = list_t_banner;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public List<Vp> getList_area() {
        return list_area;
    }

    public void setList_area(List<Vp> list_area) {
        this.list_area = list_area;
    }
}
