package com.haibin.qiaqia.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.utils.LogUtils;
import com.haibin.qiaqia.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/12/25.
 */

public class PayFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.pay_img1)
    ImageView payImg1;
    @BindView(R.id.pay_tv1)
    TextView payTv1;
    @BindView(R.id.pay_cb1)
    CheckBox payCb1;
    @BindView(R.id.pay_weixin)
    RelativeLayout payWeixin;
    @BindView(R.id.pay_img2)
    ImageView payImg2;
    @BindView(R.id.pay_tv2)
    TextView payTv2;
    @BindView(R.id.pay_cb2)
    CheckBox payCb2;
    @BindView(R.id.pay_zfb)
    RelativeLayout payZfb;
    @BindView(R.id.weixin_des)
    TextView weixinDes;
    @BindView(R.id.zfb_des)
    TextView zfbDes;
    private String title;
    private View view;

    public static PayFragment getInstance(String title) {
        PayFragment fft = new PayFragment();
        fft.title = title;
        return fft;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_pay, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        payWeixin.setOnClickListener(this);
        payZfb.setOnClickListener(this);
        if (title.equals("2")){
            weixinDes.setText("分享到微信扫码支付");
            zfbDes.setText("分享到支付宝扫码支付");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_weixin:
                if (title.equals("1")) {
                    LogUtils.e("cai","zou l 1");
                    payCb1.setChecked(true);
                    payCb2.setChecked(false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype1", true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype2", false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype", true);
                } else {
                    LogUtils.e("cai","zou l 2");
                    payCb1.setChecked(true);
                    payCb2.setChecked(false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype3", true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype4", false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype", false);
                }
                break;
            case R.id.pay_zfb:
//                payV2();
                if (title.equals("1")) {
                    LogUtils.e("cai","zou l 3");
                    payCb1.setChecked(false);
                    payCb2.setChecked(true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype1", false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype2", true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype", true);
                } else {
                    LogUtils.e("cai","zou l 4");
                    payCb1.setChecked(false);
                    payCb2.setChecked(true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype3", false);
                    SPUtils.setParam(getActivity(), "PAY", "paytype4", true);
                    SPUtils.setParam(getActivity(), "PAY", "paytype", false);
                }
                break;
        }
    }
}

