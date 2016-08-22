package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.AddAddressActivity;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.AddressList;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.LogUtils;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.adrs_back)
    ImageView adrsBack;
    @BindView(R.id.adrs_delete)
    ImageView adrsDelete;
    @BindView(R.id.adrs_rv)
    XRecyclerView adrsRv;
    @BindView(R.id.adrs_addAddress)
    LinearLayout adrsAddAddress;
    private AddressAdapter adapter;

    private List<Address> adrList = new ArrayList<Address>();
    private SubscriberOnNextListener<AddressList> subListener;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        adrsAddAddress.setOnClickListener(this);
        adrsBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter = new AddressAdapter(this,adrList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adrsRv.setLayoutManager(manager);
        adrsRv.setItemAnimator(new DefaultItemAnimator());
        adrsRv.setAdapter(adapter);
        int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        subListener = new SubscriberOnNextListener<AddressList>() {
            @Override
            public void onNext(AddressList addressList) {
                List<Address> data = addressList.getData();
                LogUtils.e("data.size:" , data.size()+"");
                adrList.addAll(data);
                adapter.notifyDataSetChanged();
            }
        };

        HttpMethods.getInstance().getAddressList(new ProgressSubscriber<AddressList>(subListener,this),String.valueOf(loginId));

    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adrs_back:
                finish();
                break;
            case R.id.adrs_addAddress:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
        }
    }
}
