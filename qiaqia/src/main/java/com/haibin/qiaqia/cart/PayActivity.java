package com.haibin.qiaqia.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/11/13.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    //    @BindView(R.id.pay_tv)
//    TextView payTv;
//    @BindView(R.id.pay_money)
//    TextView payMoney;
//    @BindView(R.id.pay_img1)
//    ImageView payImg1;
//    @BindView(R.id.pay_tv1)
//    TextView payTv1;
//    @BindView(R.id.pay_cb1)
//    CheckBox payCb1;
//    @BindView(R.id.pay_weixin)
//    RelativeLayout payWeixin;
//    @BindView(R.id.pay_img2)
//    ImageView payImg2;
//    @BindView(R.id.pay_tv2)
//    TextView payTv2;
//    @BindView(R.id.pay_cb2)
//    CheckBox payCb2;
//    @BindView(R.id.pay_zfb)
//    RelativeLayout payZfb;
    @BindView(R.id.pay_sure)
    TextView paySure;
    @BindView(R.id.pay_yf)
    TextView payYf;
    @BindView(R.id.pay_time)
    TextView payTime;
    @BindView(R.id.pay_psf)
    TextView payPsf;
    @BindView(R.id.pay_yhq)
    TextView payYhq;
    @BindView(R.id.dadaada)
    TextView dadaada;
    @BindView(R.id.sjzf)
    TextView sjzf;
    @BindView(R.id.pay_tabHost)
    com.flyco.tablayout.SlidingTabLayout payTabHost;
    @BindView(R.id.pay_Pager)
    ViewPager payPager;
    private String[] titles = {"支付", "扫码付"};//"果聊",

    private double pMoney;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        pMoney = getIntent().getDoubleExtra("money", 0);
//        RSA_PRIVATE
        SubscriberOnNextListener<String> subscriberOnNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                RSA_PRIVATE = s;
            }
        };
        HttpMethods.getInstance().getZ(new ProgressSubscriber<String>(subscriberOnNextListener, PayActivity.this));


        SPUtils.setParam(PayActivity.this, "PAY", "paytype1", false);
        SPUtils.setParam(PayActivity.this, "PAY", "paytype2", false);
        SPUtils.setParam(PayActivity.this, "PAY", "paytype3", false);
        SPUtils.setParam(PayActivity.this, "PAY", "paytype4", false);
        SPUtils.setParam(PayActivity.this, "PAY", "paytype", false);
    }

    @Override
    public void initViews() {
        allTitle.setText("支付订单");
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(this);
//        payWeixin.setOnClickListener(this);
//        payZfb.setOnClickListener(this);
        paySure.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        payPager.setAdapter(adapter);
        payTabHost.setViewPager(payPager,titles);
        payTabHost.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0){
                    SPUtils.setParam(PayActivity.this, "PAY", "paytype", true);
                }else{
                    SPUtils.setParam(PayActivity.this, "PAY", "paytype", false);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        payPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    SPUtils.setParam(PayActivity.this, "PAY", "paytype", true);
                }else{
                    SPUtils.setParam(PayActivity.this, "PAY", "paytype", false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        payMoney.setText("￥" + pMoney);
    }

    @Override
    public void addActivity() {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PayFragment.getInstance("1");           //直播榜
                case 1:
                    return PayFragment.getInstance("2");
//                ;          //话题榜
                default:
                    return PayFragment.getInstance("1");
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_back:
                finish();
                break;
            case R.id.pay_weixin:
//                payCb1.setChecked(true);
//                payCb2.setChecked(false);
                break;
            case R.id.pay_zfb:
//                payV2();
//                payCb1.setChecked(false);
//                payCb2.setChecked(true);
                break;
            case R.id.pay_sure:

                boolean paytype = (boolean)SPUtils.getParam(this,"PAY","paytype",true);

                if (paytype){
                    boolean paytype1= (boolean)SPUtils.getParam(this,"PAY","paytype1",false);
                    boolean paytype2 = (boolean)SPUtils.getParam(this,"PAY","paytype2",false);
                    if (paytype1){
                            Toast.makeText(PayActivity.this,"微信支付暂未开通",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (paytype2){
                        payV2();
                        return;
                    }
                    Toast.makeText(PayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();

                }else{
                    boolean paytype3= (boolean)SPUtils.getParam(this,"PAY","paytype3",false);
                    boolean paytype4 = (boolean)SPUtils.getParam(this,"PAY","paytype4",false);

                    if (paytype3){
                        Toast.makeText(PayActivity.this,"微信分享暂未开通",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (paytype4){
                        Toast.makeText(PayActivity.this,"支付宝分享暂未开通",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(PayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                }
//                if (payCb1.isChecked()) {
//
//                } else if (payCb2.isChecked()) {
//                    payV2();
//                } else {
//                    Toast.makeText(PayActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016101102091577";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088012331465250";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "changhaibin123@163.com";

    /**
     * 商户私钥，pkcs8格式
     */
//    public static final String RSA_PRIVATEPRIVATE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/G1+D0kOkURDANlttyzcf7N20lexihj3/lWMNGJzQiYMwN6jhwuHX6hStHSf2peC2W8OoABXKjrOkNXW4Y7H4FkYGbzDQIzEps4sb3YLcdNHgaEFNXPcpLRPy64JhwNcs31BgwGVNfSGmGRRxhAwcYncPzWCuDLBs15HAnmnX2wIDAQAB";
    private String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, pMoney);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param
     */
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, RSA_PRIVATE);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

}
