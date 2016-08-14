package com.haibin.qiaqia.cart;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.top) RelativeLayout top;
    @BindView(R.id.iv_name) ImageView ivName;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.et_order_nname) EditText etOrderNname;
    @BindView(R.id.iv_name_edit) ImageView ivNameEdit;
    @BindView(R.id.iv_phone) ImageView ivPhone;
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.iv_phone_edit) ImageView ivPhoneEdit;
    @BindView(R.id.et_order_phone) EditText etOrderPhone;
    @BindView(R.id.iv_location) ImageView ivLocation;
    @BindView(R.id.tv_location) TextView tvLocation;
    @BindView(R.id.et_order_location) EditText etOrderLocation;
    @BindView(R.id.iv_location_edit) ImageView ivLocationEdit;
    @BindView(R.id.tv_send_method) TextView tvSendMethod;
    @BindView(R.id.rl_send_time) RelativeLayout rlSendTime;
    @BindView(R.id.tv_add_require) TextView tvAddRequire;
    @BindView(R.id.rl_beizhu) RelativeLayout rlBeizhu;
    @BindView(R.id.rcv_order_cart) RecyclerView rcvOrderCart;
    @BindView(R.id.iv_weixin) ImageView ivWeixin;
    @BindView(R.id.cb_wxpay) CheckBox cbWxpay;
    @BindView(R.id.rl_wxpay) RelativeLayout rlWxpay;
    @BindView(R.id.iv_alipay) ImageView ivAlipay;
    @BindView(R.id.cb_alipay) CheckBox cbAlipay;
    @BindView(R.id.rl_alipay) RelativeLayout rlAlipay;
    @BindView(R.id.cb_send_money) CheckBox cbSendMoney;
    @BindView(R.id.btn_order) Button btnOrder;
    @BindView(R.id.activity_order) RelativeLayout activityOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        Goods goods = (Goods) getIntent().getSerializableExtra("balanceData");
        List<ListChaoCommodity> listChaoCommodity = goods.getListChaoCommodity();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addActivity() {

    }

    @OnClick(R.id.btn_order)
    public void onClick() {
    }
}
