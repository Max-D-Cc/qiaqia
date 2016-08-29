package com.haibin.qiaqia.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.User;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.main.MainActivity;
import com.haibin.qiaqia.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends BaseActivity implements AMapLocationListener {

    private Context context = SplashActivity.this;

    private SubscriberOnNextListener<User> loginSubListener;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String mCityId ="0";
    private String mCityName ="";


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 0) {

            }
            return false;
        }
    });

    @Override
    public void setContentView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initViews() {
        //初始化SDK
//        ShareSDK.initSDK(this);
//        Picasso.with(context).load(R.mipmap.splash).into(imgSplash);
    }

    @Override
    public void initListeners() {

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        locationClient.setLocationListener(this);
        //初始化定位参数
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        locationOption.setOnceLocation(true);

        if (locationOption.isOnceLocationLatest()) {
            locationOption.setOnceLocationLatest(true);
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
//如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }

//设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);
//给定位客户端对象设置定位参数
        locationClient.setLocationOption(locationOption);
//启动定位
        locationClient.startLocation();

        //处理接口返回的数据
        loginSubListener = new SubscriberOnNextListener<User>() {

            @Override
            public void onNext(User user) {
                if (user.getCode().equals("200")) {
                    Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PHONE, user.getData().getIUserLogin().getPhone());
                    SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PASSWORD, user.getData().getIUserLogin().getPassword());
                    SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_STATUS, user.getData().getIUserLogin().getStatus());
                    SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_TYPE, 1);
                    SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_IMG, user.getData().getIUserInfo().getNameImage());
                    SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_ID, user.getData().getIUserInfo().getLoginId());
                    SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_NAME, user.getData().getIUserInfo().getName());
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else
                    startActivity(new Intent(SplashActivity.this, LoginPassWordActivity.class));
                Toast.makeText(SplashActivity.this, user.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        };
    }

    @Override
    public void initData() {

    }

    @Override
    public void addActivity() {
//        ActivityManager.getActivityManager().addActivity(SplashActivity.this);
    }

    private void saveToSP(User user) {
        SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PHONE, user.getData().getIUserLogin().getPhone());
        SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PASSWORD, user.getData().getIUserLogin().getPassword());
        SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_STATUS, user.getData().getIUserLogin().getStatus());
        SPUtils.setParam(SplashActivity.this, Constants.USER_LOGIN, Constants.LOGIN_TYPE, 1);
        SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_IMG, user.getData().getIUserInfo().getNameImage());
        SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_ID, user.getData().getIUserInfo().getLoginId());
        SPUtils.setParam(SplashActivity.this, Constants.USER_INFO, Constants.INFO_NAME, user.getData().getIUserInfo().getName());
        //暂时关闭果聊功能
//        final ChatManager chatManager = ChatManager.getInstance();
//        if (!TextUtils.isEmpty(String.valueOf(user.getT_user_login().getId()))) {
//            //登陆leancloud服务器 给极光设置别名
////            chatManager.setupManagerWithUserId(this, String.valueOf(user.getT_user_login().getId()));
//            LogUtils.e("jpush","调用jpush");
//            if (JPushInterface.isPushStopped(getApplicationContext())){
//                JPushInterface.resumePush(getApplicationContext());
//            }
//            JPushInterface.setAlias(getApplicationContext(),"jianguo"+user.getT_user_login().getId(), new TagAliasCallback() {
//                @Override
//                public void gotResult(int i, String s, Set<String> set) {
//
//                    LogUtils.e("jpush",s+",code="+i);
//                }
//            });
//        }
//        ChatManager.getInstance().openClient(new AVIMClientCallback() {
//            @Override
//            public void done(AVIMClient avimClient, AVIMException e) {
//                if (null == e) {
//                } else {
//                    showShortToast(e.toString());
//                }
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        // 在当前的界面变为用户可见的时候调用的方法
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                chooseActivity();
            }
        }, 2000);
        super.onStart();
    }


    /**
     * chooseActivity
     * 根据保存的登陆信息 跳转不同界面
     */
    private void chooseActivity() {
        int loginType = (int) SPUtils.getParam(context, Constants.USER_LOGIN, Constants.LOGIN_TYPE, 0);
        if (loginType == 0) {
            startActivity(new Intent(context, LoginPassWordActivity.class));
            finish();
        } else if (loginType == 1) {
            String phone = (String) SPUtils.getParam(context, Constants.USER_LOGIN, Constants.LOGIN_PHONE, "");
            String password = (String) SPUtils.getParam(context, Constants.USER_LOGIN, Constants.LOGIN_PASSWORD, "");
            HttpMethods.getInstance().Login(new ProgressSubscriber<User>(loginSubListener, SplashActivity.this), phone,
                    password);
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                if (mCityName ==null|| mCityName.equals("")){
                    mCityName =aMapLocation.getProvince();//省信息
                }
                Log.e("AmapError","location success, CityCode:"
                        + aMapLocation.getCityCode() + ", Province:"
                        + aMapLocation.getProvince());

                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_CODE, aMapLocation.getCityCode());
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_NAME, aMapLocation.getCity().substring(0,aMapLocation.getCity().length()-1));
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_LON, String.valueOf(aMapLocation.getLongitude()));
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_LAT, String.valueOf( aMapLocation.getLatitude()));
            } else {
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_CODE, "010");
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_NAME, "北京");
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_LON, "0.0");
                SPUtils.setParam(context, Constants.USER_INFO, Constants.USER_LOCATION_LAT, "0.0");
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
