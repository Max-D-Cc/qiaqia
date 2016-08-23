package com.haibin.qiaqia.cart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.AddressList;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.personal.AddressActivity;
import com.haibin.qiaqia.utils.LogUtils;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.addads_back)
    ImageView addadsBack;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_door)
    EditText etDoor;
    @BindView(R.id.addads_save)
    TextView addadsSave;
    private String name;
    private String phone;
    private String location;
    private int type;
    private SubscriberOnNextListener<Address> subListener;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        location = getIntent().getStringExtra("location");
        type = getIntent().getIntExtra("type", 0);

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        addadsBack.setOnClickListener(this);
        addadsSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (type == 1) {
            etName.setText(name);
            etPhone.setText(phone);
            etAddress.setText(location);
        }
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
            case R.id.addads_save:
                submitAddress();
                break;
        }
    }

    private void submitAddress() {
        String subName = etName.getText().toString().trim();
        String subPhone = etPhone.getText().toString().trim();
        String subAddress = etAddress.getText().toString().trim();
        String subDoor = etDoor.getText().toString().trim();

        if (subAddress.equals(location) && subName.equals(name) && subPhone.equals(phone)) {
            finish();
        } else if (!TextUtils.isEmpty(subName) && !TextUtils.isEmpty(subPhone) && !TextUtils.isEmpty(subAddress)) {

            int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
            subListener = new SubscriberOnNextListener<Address>() {
                @Override
                public void onNext(Address addressList) {
                    Toast.makeText(AddAddressActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            };
            HttpMethods.getInstance().addAddress(new ProgressSubscriber<Address>(subListener, this), String.valueOf(loginId), subName, subPhone, String.valueOf(0.0),String.valueOf(0.0), "", subAddress + subDoor);

        } else {
            Toast.makeText(this, "请将地址信息完善", Toast.LENGTH_SHORT).show();
        }
    }


}
