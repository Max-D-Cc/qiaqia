package com.haibin.qiaqia.http;

import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.AddressList;
import com.haibin.qiaqia.entity.CollectionList;
import com.haibin.qiaqia.entity.CouponList;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.HttpResult;
import com.haibin.qiaqia.entity.Market;
import com.haibin.qiaqia.entity.ObjectList;
import com.haibin.qiaqia.entity.OrderTypeList;
import com.haibin.qiaqia.entity.User;
import com.haibin.qiaqia.entity.VpArea;
import com.haibin.qiaqia.service.MethodInterface;
import com.haibin.qiaqia.utils.DateUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by invinjun on 2016/6/1.
 */

public class HttpMethods {

    public static final String BASE_URL = Constants.JIANGUO_FACTORY;

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private MethodInterface methodInterface;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        methodInterface = retrofit.create(MethodInterface.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() != 200) {
                throw new ApiException(httpResult.getMessage());
            }
            return httpResult.getData();
        }
    }

    /**
     * 用户注册
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param phone      手机号
     * @param password   密码
     * @param sms        验证码
     */

    public void toRegister(Subscriber<User> subscriber, String phone, String password, String sms) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.toRegister(only, phone, password, sms)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSms(Subscriber<String> subscriber, String phone) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getSMS(only, phone)
                .map(new HttpResultFunc<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取首页数据
     *
     * @param
     * @param
     * @param subscriber
     * @author invinjun
     * created at 2016/6/15 11:30
     */
    public void getHomeData(Subscriber<Goods> subscriber, String login_id, String area_id) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getHomeInfo(only,login_id,area_id)
                .map(new HttpResultFunc<Goods>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void Login(Subscriber<User> subscriber, String phone, String password) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.toLogin(only, phone, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取超市分类
     */
    public void getMarketClass(Subscriber<Market> subscriber, String type,String areaid) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getMarketClass(only, type,areaid)
                .map(new HttpResultFunc<Market>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取超市分类
     */
    public void getGoods(Subscriber<Goods> subscriber, String loginId, String categoryId) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getGoods(only, loginId, categoryId)
                .map(new HttpResultFunc<Goods>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 加减删除购物车商品
     */
    public void getChangeCarGoods(Subscriber<Goods> subscriber, String login_id, String commodity_id, String count) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getChangeCarGoods(only, login_id, commodity_id, count)
                .map(new HttpResultFunc<Goods>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 购物车
     *
     * @param subscriber
     * @param login_id
     */
    public void getCarInfo(Subscriber<Goods> subscriber, String login_id) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getCarInfo(only, login_id)
                .map(new HttpResultFunc<Goods>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取全部收获地址列表
     *
     * @param subscriber
     * @param login_id
     */
    public void getAddressList(Subscriber<AddressList> subscriber, String login_id) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getAddressList(only, login_id)
                .map(new HttpResultFunc<AddressList>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 添加地址到服务器
     * @param subscriber
     * @param login_id
     * @param name
     * @param phone
     * @param lon
     * @param lat
     * @param gps
     * @param position
     */
    public void addAddress(Subscriber<Address> subscriber, String login_id, String name, String phone, String lon, String lat, String gps, String position) {
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.addAddress(only,login_id,name,phone,lon,lat,gps,position)
                .map(new HttpResultFunc<Address>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取优惠券列表
     * @param subscriber
     * @param login_id
     */
    public void getConpon(Subscriber<CouponList> subscriber, String login_id){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getCouponList(only,login_id)
                .map(new HttpResultFunc<CouponList>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 提交意见
     * @param subscriber
     * @param login_id
     * @param content
     */
    public void getSuggest(Subscriber<String> subscriber, String login_id,String content){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.postSuggest(only,login_id,content)
                .map(new HttpResultFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取订单界面所有信息
     * @param subscriber
     * @param login_id
     * @param status
     */
    public void getOrderList(Subscriber<OrderTypeList> subscriber, String login_id, String status){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getOrderInfo(only,login_id,status)
                .map(new HttpResultFunc<OrderTypeList>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取收藏的集合
     * @param subscriber
     * @param login_id
     */
    public void getCollectionList(Subscriber<CollectionList> subscriber, String login_id){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getCollection(only,login_id)
                .map(new HttpResultFunc<CollectionList>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取轮播图还有地区
     * @param subscriber
     * @param
     */
    public void getVpAreaInfo(Subscriber<VpArea> subscriber, String lon, String lat){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getVpArea(only,lon,lat)
                .map(new HttpResultFunc<VpArea>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取热门搜索数据
     * @param subscriber
     */
    public void getHotDataInfo(Subscriber<ObjectList> subscriber){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getHotData(only)
                .map(new HttpResultFunc<ObjectList>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 搜索商品
     * @param subscriber
     * @param login_id
     * @param alias
     */
    public void getSerachDataInfo(Subscriber<Goods> subscriber,String login_id,String alias){
        String only = DateUtils.getDateTimeToOnly(System.currentTimeMillis());
        methodInterface.getSerachData(only,login_id,alias)
                .map(new HttpResultFunc<Goods>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
