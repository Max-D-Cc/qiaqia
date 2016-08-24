package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/24.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {


    private Context context;
    private List list;

    public MyOrderAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_myorder, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyOrderInnerAdapter adapter = new MyOrderInnerAdapter(context,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.itemMoRv.setLayoutManager(layoutManager);
        holder.itemMoRv.setItemAnimator(new DefaultItemAnimator());
        holder.itemMoRv.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_mo_time)
        TextView itemMoTime;
        @BindView(R.id.item_mo_statue)
        TextView itemMoStatue;
        @BindView(R.id.item_mo_rv)
        RecyclerView itemMoRv;
        @BindView(R.id.item_mo_money)
        TextView itemMoMoney;
        @BindView(R.id.item_mo_submit)
        TextView itemMoSubmit;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
