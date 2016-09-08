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
 * Created by cai on 2016/9/7.
 */

public class SerachNetAdapter extends RecyclerView.Adapter<SerachNetAdapter.ViewHolder> {


    private List<ListChaoCommodity> list;
    private Context context;
    private OnGoodJJClickListener listener;

    public SerachNetAdapter(Context context, List<ListChaoCommodity> list) {
        this.context = context;
        this.list = list;
    }

    //商品加减
    public interface OnGoodJJClickListener{
        void onAdd(int position);
        void onReduce(int position);
    }

    public void setOnGoodJJClickListener(OnGoodJJClickListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_net_serach, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListChaoCommodity listChaoCommodity = list.get(position);
        Glide.with(context)
                .load(listChaoCommodity.getImage())
                .placeholder(R.drawable.ic_loading_rotate)
                .crossFade()
                .into(holder.snImg);
        holder.snName.setText(listChaoCommodity.getName());
        holder.snMoney.setText(listChaoCommodity.getPrice()+"元");
        holder.snNum.setText(String.valueOf(listChaoCommodity.getCount()));
        holder.snAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onAdd(position);
                }
            }
        });
        holder.snJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
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
        @BindView(R.id.sn_img)
        ImageView snImg;
        @BindView(R.id.sn_name)
        TextView snName;
        @BindView(R.id.sn_money)
        TextView snMoney;
        @BindView(R.id.sn_jian)
        ImageView snJian;
        @BindView(R.id.sn_num)
        TextView snNum;
        @BindView(R.id.sn_add)
        ImageView snAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
