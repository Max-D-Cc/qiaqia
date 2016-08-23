package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.Coupon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/14.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {



    private Context context;
    private List<Coupon> list;

    public CouponAdapter(Context context, List<Coupon> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_coupon, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Coupon coupon = list.get(position);
        holder.itemCpMoney.setText(String.valueOf(coupon.getMoney()));
        holder.itemCpTime.setText("有效期至:"+coupon.getReg_time());
        holder.itemCpTv2.setText("单笔满" + coupon.getQuota() + "元适用");
        int status = coupon.getStatus();
        if (status == 0){
            holder.itemCpStatue.setText("已过期");
        }else if (status == 1){
            holder.itemCpStatue.setText("可使用");
        }else{
            holder.itemCpStatue.setText("已使用");
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_cp_money)
        TextView itemCpMoney;
        @BindView(R.id.item_cp_statue)
        TextView itemCpStatue;
        @BindView(R.id.item_cp_tv2)
        TextView itemCpTv2;
        @BindView(R.id.item_cp_time)
        TextView itemCpTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
