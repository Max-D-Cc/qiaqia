package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.Application;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.home.WebViewActivity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.login.LoginPassWordActivity;
import com.haibin.qiaqia.main.MainActivity;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by cai on 2016/8/23.
 */

public class AboutOursActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.ao_customer)
    RelativeLayout aoCustomer;
    @BindView(R.id.ao_phone)
    TextView aoPhone;
    @BindView(R.id.ao_http)
    TextView aoHttp;
    @BindView(R.id.ao_loginout)
    TextView aoLoginout;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_aboutours);
        ButterKnife.bind(this);
        Application.getInstance().addActivity(this);
    }

    @Override
    public void initViews() {
        allDelete.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(this);
        aoCustomer.setOnClickListener(this);
        aoPhone.setOnClickListener(this);
        aoHttp.setOnClickListener(this);
        aoLoginout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        allTitle.setText("关于我们");
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_back:
                finish();
                break;
            case R.id.ao_customer:

                break;
            case R.id.ao_phone:
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4000705176"));
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.ao_http:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("html", "http://www.jianguojob.com");
                startActivity(intent);
                break;
            case R.id.ao_loginout:
                deleteDialog();

                break;
        }
    }

    private void deleteDialog() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("是否退出登录").setConfirmText("是").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                SPUtils.setParam(AboutOursActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
//                SPUtils.setParam(this, Constants.USER_LOGIN, Constants.LOGIN_TYPE, 0);
                startActivity(new Intent(AboutOursActivity.this, LoginPassWordActivity.class));
                AboutOursActivity.this.finish();
                MainActivity.instance.finish();
                dialog.dismiss();
            }
        }).setCancelText("否").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        }).show();

    }

}


