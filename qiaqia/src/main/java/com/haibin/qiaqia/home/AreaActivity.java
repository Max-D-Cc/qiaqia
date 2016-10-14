package com.haibin.qiaqia.home;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.entity.Vp;
import com.haibin.qiaqia.entity.VpArea;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/1.
 */

public class AreaActivity extends BaseActivity {
    @BindView(R.id.area_back)
    ImageView areaBack;
    @BindView(R.id.area_lv)
    XRecyclerView areaLv;
    private List<Vp> areaList = new ArrayList<Vp>();
    private VpArea vpArea;
    private AreaAdapter adapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_area);
        ButterKnife.bind(this);
        vpArea = (VpArea) getIntent().getSerializableExtra("area");
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        areaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        if (vpArea != null){
            List<Vp> list_area = vpArea.getList_area();
            areaList.addAll(list_area);
            adapter = new AreaAdapter(this,areaList);
        }else{
            adapter = new AreaAdapter(this,areaList);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        areaLv.setLayoutManager(layoutManager);
        areaLv.setItemAnimator(new DefaultItemAnimator());
        areaLv.setAdapter(adapter);
        adapter.setOnAreaClickLisener(new AreaAdapter.OnAreaClickLisener() {
            @Override
            public void onClick(int posotion) {
                Vp vp = areaList.get(posotion);
                int id = vp.getId();
                String name = vp.getName();
                SPUtils.setParam(AreaActivity.this,"area","areaid",String.valueOf(id));
                SPUtils.setParam(AreaActivity.this,"area","areaname",name);
                finish();
            }
        });
    }

    @Override
    public void addActivity() {

    }


}
