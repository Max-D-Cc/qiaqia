package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.OrderInner;
import com.haibin.qiaqia.entity.OrderType;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/24.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {


    private Context context;
    private List<OrderType> list;

    public MyOrderAdapter(Context context, List<OrderType> list) {
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
        OrderType orderType = list.get(position);
        List<ListChaoCommodity> list_commodity = orderType.getList_commodity();
        MyOrderInnerAdapter adapter = new MyOrderInnerAdapter(context,list_commodity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.itemMoRv.setLayoutManager(layoutManager);
        holder.itemMoRv.setItemAnimator(new DefaultItemAnimator());
        holder.itemMoRv.setAdapter(adapter);
        holder.itemMoTime.setText(orderType.getReg_time());
        int status = orderType.getStatus();
        if (status == 0){
            holder.itemMoStatue.setText("代付款");
            holder.itemMoSubmit.setText("去付款");
        }else if (status == 1){
            holder.itemMoStatue.setText("已支付");
            holder.itemMoSubmit.setVisibility(View.GONE);
        }else if (status == 2){
            holder.itemMoStatue.setText("已完成");
            holder.itemMoSubmit.setVisibility(View.GONE);
        }else if (status == 3){
            holder.itemMoStatue.setText("处理中");
            holder.itemMoSubmit.setVisibility(View.GONE);
        }else if (status == 4){
            holder.itemMoStatue.setText("已处理");
            holder.itemMoSubmit.setVisibility(View.GONE);
        }

        holder.itemMoMoney.setText("共" + list_commodity.size()+ "件商品  ￥"+String.valueOf(orderType.getSum_money()));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
