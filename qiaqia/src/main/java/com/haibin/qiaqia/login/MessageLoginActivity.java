package com.haibin.qiaqia.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.User;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.main.MainActivity;
import com.haibin.qiaqia.utils.MD5Util;
import com.haibin.qiaqia.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/10/29.
 */

public class MessageLoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_sms)
    Button btnSms;
    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.et_ps1)
    EditText etPs1;
    @BindView(R.id.et_ps2)
    EditText etPs2;
    @BindView(R.id.img_check)
    ImageView imgCheck;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.activity_regiter)
    LinearLayout activityRegiter;

    SubscriberOnNextListener<String> registerSubListener;
    SubscriberOnNextListener<User> registerSubListener1;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_regiter);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        etPs1.setVisibility(View.GONE);
        etPs2.setVisibility(View.GONE);
        imgCheck.setVisibility(View.GONE);
        btnRegister.setText("登录");
    }

    @Override
    public void initListeners() {
        btnSms.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void initData() {
        registerSubListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String user) {
                Toast.makeText(MessageLoginActivity.this, "验证码获取成功", Toast.LENGTH_LONG).show();
            }
        };
        registerSubListener1 = new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                if (user.getCode().equals("200")) {
                    Toast.makeText(MessageLoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PHONE, user.getData().getIUserLogin().getPhone());
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_LOGIN, Constants.LOGIN_PASSWORD, user.getData().getIUserLogin().getPassword());
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_LOGIN, Constants.LOGIN_STATUS, user.getData().getIUserLogin().getStatus());
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_LOGIN, Constants.LOGIN_TYPE, 1);
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_INFO, Constants.INFO_IMG, user.getData().getIUserInfo().getNameImage());
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_INFO, Constants.INFO_ID, user.getData().getIUserInfo().getLoginId());
                    SPUtils.setParam(MessageLoginActivity.this, Constants.USER_INFO, Constants.INFO_NAME, user.getData().getIUserInfo().getName());
                    startActivity(new Intent(MessageLoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(MessageLoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(MessageLoginActivity.this, LoginPassWordActivity.class));
                this.finish();
                break;
            case R.id.btn_sms:
                if (etPhone.getText().toString().trim() == null || etPhone.getText().toString().trim().equals("")) {
                    showShortToast("请输入手机号码");
                    break;
                } else if (etPhone.getText().toString().trim().length() == 10) {
                    showShortToast("手机号码不正确");
                    break;
                }
//                UserRegisterTask userRegisterTask=new UserRegisterTask(etPhone.getText().toString().trim(),
//                        etPs1.getText().toString().trim(),etSms.getText().toString().trim());
//                userRegisterTask.execute();
                HttpMethods.getInstance().getSms(new ProgressSubscriber<String>(registerSubListener, this), etPhone.getText().toString().trim());
                break;
            case R.id.btn_register:
                if (etPhone.getText().toString().trim() == null || etPhone.getText().toString().trim().equals("")) {
                    showShortToast("请输入手机号码");
                    break;
                } else if (etPhone.getText().toString().trim().length() != 11) {
                    showShortToast("手机号码不正确");
                    break;
                } else if (TextUtils.isEmpty(etSms.getText().toString().trim())) {
                    showShortToast("请输入验证码");
                    break;
                }
                HttpMethods.getInstance().smsLogin(new ProgressSubscriber<User>(registerSubListener1,MessageLoginActivity.this),etPhone.getText().toString().trim(),
                        etSms.getText().toString().trim());
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MessageLoginActivity.this, LoginPassWordActivity.class));
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
