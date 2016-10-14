package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.Collection;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.OrderInner;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/27.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {


    private Context context;
    private List<Collection> list;
    private OnCollectionListener listener;

    public interface OnCollectionListener{
        void onAdd(int position);
        void onJian(int position);
    }

    public void setOnCollectionListener(OnCollectionListener listener){
        this.listener = listener;
    }

    public CollectionAdapter(Context context, List<Collection> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.item_cart, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Collection data = list.get(position);
        holder.tvItemName.setText(data.getCommodity().getName());
        holder.tvColorName.setVisibility(View.GONE);
        holder.tvItemColor.setVisibility(View.GONE);
        holder.tvItemPrice.setText(mul(data.getCommodity().getPrice(), data.getCommodity().getCount()) + "元");
        holder.tvItemCount.setText(String.valueOf(data.getCommodity().getCount()));
        Glide.with(context)
                .load(data.getCommodity().getImage())
                .placeholder(R.drawable.ic_loading_rotate)
                .crossFade()
                .into(holder.ivItemPic);
        holder.ivItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onAdd(position);
                }
            }
        });
        holder.ivItemDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onJian(position);
                }
            }
        });

        if (data.getCommodity().getCount() == 0){
            holder.tvItemCount.setVisibility(View.GONE);
            holder.ivItemDown.setVisibility(View.GONE);
        }else{
            holder.tvItemCount.setVisibility(View.VISIBLE);
            holder.ivItemDown.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cart_cb)
        CheckBox cartCb;
        @BindView(R.id.iv_item_pic)
        ImageView ivItemPic;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_color)
        TextView tvItemColor;
        @BindView(R.id.tv_color_name)
        TextView tvColorName;
        @BindView(R.id.tv_item_price)
        TextView tvItemPrice;
        @BindView(R.id.iv_item_add)
        ImageView ivItemAdd;
        @BindView(R.id.tv_item_count)
        TextView tvItemCount;
        @BindView(R.id.iv_item_down)
        ImageView ivItemDown;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static double mul(double v1, int v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
}
