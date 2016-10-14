package com.haibin.qiaqia.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.ListChaoCommodity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/6/25.
 */

public class MarketGoodsAdapter extends RecyclerView.Adapter<MarketGoodsAdapter.ViewHolder> {

    private OnMarkGoodJJClickListener listener;
    private Context context;
    private List<ListChaoCommodity> list;

    public MarketGoodsAdapter(Context context, List<ListChaoCommodity> list) {
        this.context = context;
        this.list = list;
    }

    //商品加减
    public interface OnMarkGoodJJClickListener {
        void onAdd(int position);

        void onReduce(int position);
    }

    public void setOnMarkGoodJJClickListener(OnMarkGoodJJClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_market_goods, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ListChaoCommodity listMarket = list.get(position);
        holder.itemMarkGoodName.setText(listMarket.getName());
        Glide.with(context)
                .load(listMarket.getImage())
                .placeholder(R.drawable.ic_loading_rotate)
                .crossFade()
                .into(holder.itemMarkGoodImg);
        holder.itemMarkGoodNum.setText(String.valueOf(listMarket.getCount()));
        holder.itemMarkGoodPrice.setText(String.valueOf(listMarket.getPrice()));
        holder.itemImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAdd(position);
                }
            }
        });
        holder.itemImgJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReduce(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_mark_good_img)
        ImageView itemMarkGoodImg;
        @BindView(R.id.item_mark_good_name)
        TextView itemMarkGoodName;
        @BindView(R.id.item_mark_good_price)
        TextView itemMarkGoodPrice;
        @BindView(R.id.item_img_jian)
        ImageView itemImgJian;
        @BindView(R.id.item_mark_good_num)
        TextView itemMarkGoodNum;
        @BindView(R.id.item_img_add)
        ImageView itemImgAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
