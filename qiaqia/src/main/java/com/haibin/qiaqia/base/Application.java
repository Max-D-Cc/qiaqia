package com.haibin.qiaqia.base;


import android.app.Activity;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by invinjun on 2016/3/2.
 */
public class Application extends android.app.Application {
    private boolean isGPS=false;//是否已经定位选择过城市

    public boolean isGPS() {
        return isGPS;
    }

    public void setGPS(boolean GPS) {
        isGPS = GPS;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        init();
        instance = this;
        // 初始化参数依次为 this, AppId, AppKey，暂时关闭果聊，第二处splash和quick界面的账户登录
//        AVOSCloud.initialize(this,"AtwJtfIJPKQFtti8D3gNjMmb-gzGzoHsz","spNrDrtGWAXP633DkMMWT65B");
//        MessageHandler msgHandler = new MessageHandler(this);
//        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, msgHandler);

//        CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //
//        MultiDex.install(this);//方法数过多 导致
    }

    private void init(){
//        JPushInterface.init(getApplicationContext());
    }


    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    public static Application instance;
    //构造方法
    public Application(){}
    //实例化一次
    public synchronized static Application getInstance(){
        if (null == instance) {
            instance = new Application();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
