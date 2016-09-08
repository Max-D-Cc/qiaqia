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
import com.jcodecraeer.xrecyclerview.XRecyclerView;

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
    private List<String> areaList = new ArrayList<String>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_area);
        ButterKnife.bind(this);
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
        areaList.add("东城区");
        areaList.add("海淀区");
        areaList.add("朝阳区");
        areaList.add("西城区");

        AreaAdapter adapter = new AreaAdapter(areaList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        areaLv.setLayoutManager(layoutManager);
        areaLv.setItemAnimator(new DefaultItemAnimator());
        areaLv.setAdapter(adapter);
    }

    @Override
    public void addActivity() {

    }

    class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {


        private List<String> list;

        public AreaAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(AreaActivity.this, R.layout.item_area, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position) {
            holder.itemAreaArea.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_area_area)
            TextView itemAreaArea;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
