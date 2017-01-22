package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.Push;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/12.
 */

public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {


    private Context context;
    private List<Push> list;

    public PushAdapter(Context context,List<Push> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_pushinfo, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Push push = list.get(position);
        holder.itemPushTitle.setText(push.getTitle());
        holder.itemPushContent.setText(push.getContent());
        holder.itemPushTime.setText(push.getTime());
        holder.itemPushPoint.setText(push.getPoint());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_push_time)
        TextView itemPushTime;
        @BindView(R.id.item_push_point)
        TextView itemPushPoint;
        @BindView(R.id.item_push_title)
        TextView itemPushTitle;
        @BindView(R.id.item_push_content)
        TextView itemPushContent;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
