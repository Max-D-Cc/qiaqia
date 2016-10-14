package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.Vp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/10.
 */
class NearAdapter extends RecyclerView.Adapter<NearAdapter.ViewHolder> {


    private List<PoiItem> list;
    private View view;
    private Context context;
    private OnAreaClickLisener lisener;

    public NearAdapter(Context context, List<PoiItem> list) {
        this.list = list;
        this.context = context;
    }

    public interface OnAreaClickLisener{
        void  onClick(int posotion);
    }

    public void setOnAreaClickLisener(OnAreaClickLisener lisener){
        this.lisener = lisener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.item_area, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemAreaArea.setText(list.get(position).getSnippet());

        if (view != null && lisener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.onClick(position);
                }
            });
        }
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
