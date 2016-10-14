package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.ObjectList;
import com.haibin.qiaqia.entity.Push;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/23.
 */

public class PushActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.push_rv)
    XRecyclerView pushRv;
    private List<Push> pushList = new ArrayList<>();
    private PushAdapter adapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_update);
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
        allTitle.setText("消息");
        adapter = new PushAdapter(this,pushList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        pushRv.setLayoutManager(manager);
        pushRv.setItemAnimator(new DefaultItemAnimator());
        pushRv.setAdapter(adapter);

        int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        SubscriberOnNextListener<ObjectList> nextListener = new SubscriberOnNextListener<ObjectList>() {
            @Override
            public void onNext(ObjectList objectList) {
                if (objectList != null){
                    List<Push> list_push = objectList.getList_push();
                    pushList.addAll(list_push);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        HttpMethods.getInstance().getPushDataInfo(new ProgressSubscriber<ObjectList>(nextListener,this),String.valueOf(loginId));
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
