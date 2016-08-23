package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Coupon;
import com.haibin.qiaqia.entity.CouponList;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.coupon_rv)
    XRecyclerView couponRv;
    private SubscriberOnNextListener<CouponList> subscriberOnNextListener;
    private List<Coupon> list = new ArrayList<Coupon>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        allDelete.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        allTitle.setText("优惠券");
        final CouponAdapter adapter = new CouponAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        couponRv.setLayoutManager(manager);
        couponRv.setItemAnimator(new DefaultItemAnimator());
        couponRv.setAdapter(adapter);
        int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        subscriberOnNextListener = new SubscriberOnNextListener<CouponList>() {
            @Override
            public void onNext(CouponList couponList) {

                if (couponList != null){
                    List<Coupon> list_coupon = couponList.getList_coupon();
                    list.addAll(list_coupon);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        HttpMethods.getInstance().getConpon(new ProgressSubscriber<CouponList>(subscriberOnNextListener,this),String.valueOf(loginId));
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
        }
    }
}
