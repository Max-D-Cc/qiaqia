package com.haibin.qiaqia.cart;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddressActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.addads_back)
    ImageView addadsBack;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        addadsBack.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addads_back:
                finish();
                break;
        }
    }
}
