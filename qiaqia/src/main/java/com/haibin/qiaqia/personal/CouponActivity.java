package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.OrderActivity;
import com.haibin.qiaqia.entity.Coupon;
import com.haibin.qiaqia.entity.CouponList;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.listener.RecyclerItemClickListener;
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
    private double money;
    private int type;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
        money = getIntent().getDoubleExtra("money", 0);
        type = getIntent().getIntExtra("type", 0);
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

        couponRv.addOnItemTouchListener(new RecyclerItemClickListener(CouponActivity.this, couponRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int status = list.get(position-1).getStatus();
                double quota = list.get(position-1).getQuota();
                if (status == 1 && type == 1){
                    if (money >= quota){
                        Intent intent = new Intent(CouponActivity.this, OrderActivity.class);
                        intent.putExtra("useMoney",list.get(position-1).getMoney());
                        setResult(1,intent);
                        finish();
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_back:
                if (type == 1){
                    Intent intent = new Intent(CouponActivity.this, OrderActivity.class);
                    intent.putExtra("useMoney",0);
                    setResult(1,intent);
                    finish();
                }else{
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (type == 1){
//                Intent intent = new Intent(CouponActivity.this, OrderActivity.class);
                Intent intent = new Intent(CouponActivity.this, OrderActivity.class);
                intent.putExtra("useMoney",0);
                setResult(1,intent);
                finish();
                return  true;
            }else{
                finish();
                return true;
            }
        }else{
            finish();
            return true;
        }
    }
}
