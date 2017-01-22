package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.cart.OrderActivity;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.OrderType;
import com.haibin.qiaqia.entity.OrderTypeList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/12/20.
 */


public class DetailsActivity extends BaseActivity {

    @BindView(R.id.order_back)
    ImageView orderBack;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.tv_send_method)
    TextView tvSendMethod;
    @BindView(R.id.rl_send_time)
    RelativeLayout rlSendTime;
    @BindView(R.id.tv_beizhu)
    TextView tvBeizhu;
    @BindView(R.id.et_add_require)
    EditText etAddRequire;
    @BindView(R.id.rl_beizhu)
    RelativeLayout rlBeizhu;
    @BindView(R.id.rcv_order_cart)
    RecyclerView rcvOrderCart;
    @BindView(R.id.order_dg)
    TextView orderDg;
    @BindView(R.id.rcv_order_dg)
    RecyclerView rcvOrderDg;
    @BindView(R.id.tv_send_money)
    TextView tvSendMoney;
    @BindView(R.id.tv_order_yhq)
    TextView tvOrderYhq;
    @BindView(R.id.tv_count_money)
    TextView tvCountMoney;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.activity_order)
    RelativeLayout activityOrder;
    @BindView(R.id.order_bottom)
    RelativeLayout orderBottom;
    private OrderType data;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        data = (OrderType) getIntent().getSerializableExtra("balanceData");
    }

    @Override
    public void initViews() {
        if (data != null){
            etAddRequire.setText(data.getRemarks());
            etAddRequire.setFocusable(false);
            tvName.setText(data.getAddress().getName() + "  " + data.getAddress().getPhone() + "\n\n" + data.getAddress().getGps() + "  " + data.getAddress().getPosition());
            OrderAdapter adapter = new OrderAdapter(data.getList_commodity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rcvOrderCart.setLayoutManager(layoutManager);
            rcvOrderCart.setItemAnimator(new DefaultItemAnimator());
            rcvOrderCart.setAdapter(adapter);
            OrderAdapter1 adapter1 = new OrderAdapter1(data.getList_commodityer());
            LinearLayoutManager manage = new LinearLayoutManager(this);
            rcvOrderDg.setLayoutManager(manage);
            rcvOrderDg.setItemAnimator(new DefaultItemAnimator());
            rcvOrderDg.setAdapter(adapter1);
        }
        orderBottom.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void addActivity() {

    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
        private List<ListChaoCommodity> list;

        public OrderAdapter(List<ListChaoCommodity> list) {
            this.list = list;
        }

        @Override
        public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(DetailsActivity.this, R.layout.item_order, null);
            OrderAdapter.ViewHolder holder = new OrderAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
            ListChaoCommodity listChaoCommodity = list.get(position);
            holder.itemOrderName.setText(listChaoCommodity.getName());
            holder.itemOrderPrice.setText("￥" + mul(listChaoCommodity.getPrice(), listChaoCommodity.getCount()));
            holder.itemOrderSum.setText(String.valueOf(listChaoCommodity.getCount()));
        }

        public double mul(double v1, int v2) {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.multiply(b2).doubleValue();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_order_name)
            TextView itemOrderName;
            @BindView(R.id.item_order_price)
            TextView itemOrderPrice;
            @BindView(R.id.item_order_sum)
            TextView itemOrderSum;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class OrderAdapter1 extends RecyclerView.Adapter<OrderAdapter1.ViewHolder> {
        private List<ListChaoCommodity> list;

        public OrderAdapter1(List<ListChaoCommodity> list) {
            this.list = list;
        }

        @Override
        public OrderAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(DetailsActivity.this, R.layout.item_order, null);
            OrderAdapter1.ViewHolder holder = new OrderAdapter1.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(OrderAdapter1.ViewHolder holder, int position) {
            ListChaoCommodity listChaoCommodity = list.get(position);
            holder.itemOrderName.setText(listChaoCommodity.getName());
            holder.itemOrderPrice.setText("￥" + mul(listChaoCommodity.getPrice(), listChaoCommodity.getCount()));
            holder.itemOrderSum.setText(String.valueOf(listChaoCommodity.getCount()));
        }

        public double mul(double v1, int v2) {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.multiply(b2).doubleValue();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_order_name)
            TextView itemOrderName;
            @BindView(R.id.item_order_price)
            TextView itemOrderPrice;
            @BindView(R.id.item_order_sum)
            TextView itemOrderSum;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
