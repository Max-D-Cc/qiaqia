package com.haibin.qiaqia.personal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.Address;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/14.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {


    private Context context;
    private List<Address> list;
    private boolean isDelete = false;
    private OnAddressListener listener;


    public AddressAdapter(Context context,List<Address> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnAddressListener{
        void onUpdateAddress(int position);
    }

    public void setOnAddressListener(OnAddressListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_address, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (isDelete){
            holder.itemAdrCb.setVisibility(View.VISIBLE);
        }else{
            holder.itemAdrCb.setVisibility(View.GONE);
        }

        Address address = list.get(position);
        holder.itemAdrNp.setText(address.getName() + "  " + address.getPhone());
        holder.itemAdrLocation.setText(address.getPosition());
        holder.itemAdrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdateAddress(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_adr_cb)
        CheckBox itemAdrCb;
        @BindView(R.id.item_adr_np)
        TextView itemAdrNp;
        @BindView(R.id.item_adr_location)
        TextView itemAdrLocation;
        @BindView(R.id.item_adr_edit)
        ImageView itemAdrEdit;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void isShowChexkBox(boolean isShow){
        isDelete = isShow;
    }
}
