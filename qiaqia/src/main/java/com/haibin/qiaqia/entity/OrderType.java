package com.haibin.qiaqia.entity;

import java.util.List;

/**
 * Created by cai on 2016/8/25.
 */

public class OrderType {

    private int id;
    private int login_id;
    private int address_id;
    private String song_time;
    private String remarks;
    private int song_money;
    private int coupon_id;
    private int pay_type;
    private int status;
    private int sum_money;
    private String number;
    private String reg_time;
    private List<ListChaoCommodity> list_commodity;

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

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getSong_time() {
        return song_time;
    }

    public void setSong_time(String song_time) {
        this.song_time = song_time;
    }

    public int getSong_money() {
        return song_money;
    }

    public void setSong_money(int song_money) {
        this.song_money = song_money;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getSum_money() {
        return sum_money;
    }

    public void setSum_money(int sum_money) {
        this.sum_money = sum_money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<ListChaoCommodity> getList_commodity() {
        return list_commodity;
    }

    public void setList_commodity(List<ListChaoCommodity> list_commodity) {
        this.list_commodity = list_commodity;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
