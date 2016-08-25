package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.OrderInner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/24.
 */

public class MyOrderInnerAdapter extends RecyclerView.Adapter<MyOrderInnerAdapter.ViewHolder> {



    private Context context;
    private List<ListChaoCommodity> list;

    public MyOrderInnerAdapter(Context context, List<ListChaoCommodity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_item_myorder, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListChaoCommodity orderInner = list.get(position);
        Glide.with(context)
                .load(orderInner.getImage())
                .placeholder(R.drawable.ic_loading_rotate)
                .crossFade()
                .into(holder.itemMoImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_mo_img)
        ImageView itemMoImg;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
