package com.haibin.qiaqia.service;

import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.AddressList;
import com.haibin.qiaqia.entity.CategoryGoods;
import com.haibin.qiaqia.entity.CollectionList;
import com.haibin.qiaqia.entity.Coupon;
import com.haibin.qiaqia.entity.CouponList;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.HttpResult;
import com.haibin.qiaqia.entity.Market;
import com.haibin.qiaqia.entity.ObjectList;
import com.haibin.qiaqia.entity.OrderTypeList;
import com.haibin.qiaqia.entity.User;
import com.haibin.qiaqia.entity.VpArea;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by invinjun on 2016/6/1.
 */

public interface MethodInterface {
    @POST("I_user_login_Insert_Servlet")
    Observable<User> toRegister(@Query("only") String only, @Query("phone") String phone, @Query("password") String password, @Query("sms_code") String sms_code);

    /**
     * 获取短信验证码
     */
    @GET("I_SMS_Servlet")
    Observable<HttpResult<String>> getSMS(@Query("only") String only, @Query("phone") String phone);

    /**
     * 手机号码密码登陆
     */
    @GET("I_user_login_Login_Servlet")
    Observable<User> toLogin(@Query("only") String only, @Query("phone") String phone, @Query("password") String password);

    /**
     * 超市查看分类
     */
    @GET("I_chao_class_Select_Servlet")
    Observable<HttpResult<User>> getCategory(@Query("only") String only);

    /**
     * 查看商品分类
     */

    @GET("I_chao_commodity_List_Id_Servlet")
    Observable<HttpResult<Goods>> getGoods(@Query("only") String only, @Query("login_id") String loginId, @Query("chao_class_id") String categoryId);

    /**
     * 选购商品
     */
    @POST("I_chao_cart_Insert_Servlet")
    Observable<HttpResult<Goods>> postBuy(@Query("only") String only, @Query("login_id") String loginId, @Query("json") String GoodsJson);

    /**
     * 购物车
     */

    @GET("I_chao_cart_List_Id_Servlet")
    Observable<HttpResult<Goods>> getCarInfo(@Query("only") String only, @Query("login_id") String loginId);

    /**
     * 首页数据拉取
     */
    @GET("I_chao_commodity_List_Hot_Servlet")
    Observable<HttpResult<Goods>> getHomeInfo(@Query("only") String only, @Query("login_id") String login_id, @Query("area_id") String area_id);

    /**
     * <<<<<<< Updated upstream
     * 加减删除购物车商品
     */
    @POST("I_chao_cart_Insert_JJ_Servlet")
    Observable<HttpResult<Goods>> getChangeCarGoods(@Query("only") String only, @Query("login_id") String login_id, @Query("commodity_id") String commodity_id, @Query("count") String count);

    /**
     * 超市分类 chao_class_list
     */
    @GET("I_chao_class_List_Id_Servlet")
    Observable<HttpResult<Market>> getMarketClass(@Query("only") String only, @Query("type") String type, @Query("area_id") String area_id);

    /**
     * 超市修改分类
     */
    @POST("I_chao_class_Update_Servlet")
    Observable<HttpResult<Goods>> updateCategory(@Query("only") String only, @Query("id") String id, @Query("name") String name);

    /**
     * 超市分类商品
     */
    @GET("I_chao_commodity_List_Id_Servlet")
    Observable<HttpResult<CategoryGoods>> getCategoryGoods(@Query("only") String only, @Query("login_id") String login_id, @Query("chao_class_id") String chao_class_id);

    /**
     * 收获地址表
     */
    @GET("I_user_address_All_Servlet")
    Observable<HttpResult<AddressList>> getAddressList(@Query("only") String only, @Query("login_id") String login_id);

    /**
     * 添加地址表
     */
    @POST("I_user_address_Insert_Servlet")
    Observable<HttpResult<Address>> addAddress(@Query("only") String only, @Query("login_id") String login_id, @Query("name") String name, @Query("phone") String phone, @Query("lon") String lon, @Query("lat") String lat, @Query("gps") String gps, @Query("position") String position);

    /**
     * 优惠券接口
     */
    @GET("I_coupon_List_Servlet")
    Observable<HttpResult<CouponList>> getCouponList(@Query("only") String only, @Query("login_id") String login_id);

    /**
     * 建议接口
     */
    @POST("I_opinion_Insert_Servlet")
    Observable<HttpResult<String>> postSuggest(@Query("only") String only, @Query("login_id") String login_id, @Query("content") String content);

    /**
     * 获取我的订单付款,售后等信息
     */
    @GET("I_orders_Type_Servlet")
    Observable<HttpResult<OrderTypeList>> getOrderInfo(@Query("only") String only, @Query("login_id") String login_id, @Query("status") String status);

    /**
     * 获取我收藏
     */
    @GET("I_collection_List_Servlet")
    Observable<HttpResult<CollectionList>> getCollection(@Query("only") String only, @Query("login_id") String login_id);

    /**
     * 首页地址加轮播
     */
    @GET("I_Area_Banner_Servlet")
    Observable<HttpResult<VpArea>> getVpArea(@Query("only") String only, @Query("lon") String lon, @Query("lat") String lat);

    /**
     * 热门搜索数据
     */
    @GET("I_hot_search_All_Servlet")
    Observable<HttpResult<ObjectList>> getHotData(@Query("only") String only);

    /**
     * 搜索商品
     */
    @GET("I_chao_commodity_List_Alias_Servlet")
    Observable<HttpResult<Goods>> getSerachData(@Query("only") String only,@Query("login_id") String login_id,@Query("alias") String alias);

    /**
     * 推送消息
     */
    @GET("I_push_List_Servlet")
    Observable<HttpResult<ObjectList>> getPushData(@Query("only") String only,@Query("login_id") String login_id);

    /**
     * 收藏商品
     */
    @GET("I_collection_Insert_Servlet")
    Observable<HttpResult<String>> addCollection(@Query("only") String only,@Query("login_id") String login_id,@Query("commodity_id") String commodity_id,@Query("type") String type);
}
