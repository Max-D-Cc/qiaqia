package com.haibin.qiaqia.entity;

/**
 * Created by cai on 2016/8/25.
 */

public class OrderInner {
    private int id;
    private int chao_class_id;
    private String name;
    private String image;
    private double price;
    private String p_date;
    private String shelf_life;
    private String alias;
    private int sales_sum;
    private int sum;
    private int status;
    private int hot;
    private int count;
    private int collection;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChao_class_id() {
        return chao_class_id;
    }

    public void setChao_class_id(int chao_class_id) {
        this.chao_class_id = chao_class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getShelf_life() {
        return shelf_life;
    }

    public void setShelf_life(String shelf_life) {
        this.shelf_life = shelf_life;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getSales_sum() {
        return sales_sum;
    }

    public void setSales_sum(int sales_sum) {
        this.sales_sum = sales_sum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
