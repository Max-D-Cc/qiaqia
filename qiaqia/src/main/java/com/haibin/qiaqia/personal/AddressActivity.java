package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.AddAddressActivity;
import com.haibin.qiaqia.cart.OrderActivity;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private List<Integer> delAddressList = new ArrayList<Integer>();

    private List<Address> adrList = new ArrayList<Address>();
    private SubscriberOnNextListener<AddressList> subListener;
    private int clickNum = 0;
    private int goType;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        goType = getIntent().getIntExtra("goType", 0);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        adrsAddAddress.setOnClickListener(this);
        adrsBack.setOnClickListener(this);
        adrsDelete.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter = new AddressAdapter(this,adrList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adrsRv.setLayoutManager(manager);
        adrsRv.setItemAnimator(new DefaultItemAnimator());
        adrsRv.setAdapter(adapter);
        adapter.setOnAddressListener(new AddressAdapter.OnAddressListener() {
            @Override
            public void onUpdateAddress(int position) {
                Address address = adrList.get(position);
                Intent intent = new Intent();
                intent.setClass(AddressActivity.this,AddAddressActivity.class);
                intent.putExtra("name",address.getName());
                intent.putExtra("phone",address.getPhone());
                intent.putExtra("location",address.getPosition());
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int postion) {
                Intent intent = new Intent(AddressActivity.this, OrderActivity.class);
                Address address = adrList.get(postion);
                intent.putExtra("address",address);
                setResult(0,intent);
                finish();
            }
        });

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
                if (goType == 0 ){
                    finish();
                }else{
                    Intent intent = new Intent(AddressActivity.this, OrderActivity.class);
                    setResult(0,intent);
                    finish();
                }
                break;
            case R.id.adrs_addAddress:
                Intent intent = new Intent();
                intent.setClass(AddressActivity.this,AddAddressActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.adrs_delete:
                boolean over = isOver(clickNum);
                if (over) {
                    adapter.isShowChexkBox(true);
                    adapter.notifyDataSetChanged();
                    clickNum++;
                } else {
                    if (delAddressList.size() != 0){
                        deleteDialog();
                    }else{
                        adapter.isShowChexkBox(false);
                        adapter.notifyDataSetChanged();
                    }
                    clickNum++;
                }
                break;
        }
    }

    private void deleteDialog() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("确认删除").setConfirmText("确认").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                for (int i = 0; i < delAddressList.size(); i++) {
                    int position = delAddressList.get(i);
                    adrList.remove(position);
                }
                adapter.isShowChexkBox(false);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        })/*.setCancelText("否").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        })*/.show();
    }


    public boolean isOver(int num) {
        if (num % 2 == 0)
            return true;
        else
            return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        clickNum = 0;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            if (goType == 0){
                finish();
            }else{
                Intent intent = new Intent(AddressActivity.this, OrderActivity.class);
                setResult(0,intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
