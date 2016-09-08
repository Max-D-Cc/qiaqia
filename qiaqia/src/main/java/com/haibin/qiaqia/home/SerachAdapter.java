package com.haibin.qiaqia.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haibin.qiaqia.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/8.
 */

public class SerachAdapter extends RecyclerView.Adapter<SerachAdapter.ViewHolder> {


    private Context context;
    private List<String> list;
    private ViewClickListener listener;

    public SerachAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }

    public void setViewClickListener(ViewClickListener listener){
        this.listener = listener;
    }

    private View view;
    public interface ViewClickListener{
        void viewClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.item_serach_record, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.itemSerachName.setText(list.get(position));
        if (view != null && listener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_serach_name)
        TextView itemSerachName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
