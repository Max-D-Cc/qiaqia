package com.haibin.qiaqia.cart;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.personal.AddressActivity;
import com.haibin.qiaqia.utils.ArithUtil;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tv_send_money)
    TextView tvSendMoney;
    @BindView(R.id.tv_count_money)
    TextView tvCountMoney;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.cb_wxpay)
    CheckBox cbWxpay;
    @BindView(R.id.rl_wxpay)
    RelativeLayout rlWxpay;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.cb_alipay)
    CheckBox cbAlipay;
    @BindView(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @BindView(R.id.cb_send_money)
    CheckBox cbSendMoney;
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
    private List<ListChaoCommodity> listChaoCommodity;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        Goods goods = (Goods) getIntent().getSerializableExtra("balanceData");
        listChaoCommodity = goods.getListChaoCommodity();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        tvName.setOnClickListener(this);
    }

    @Override
    public void initData() {
        OrderAdapter adapter = new OrderAdapter(listChaoCommodity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvOrderCart.setLayoutManager(layoutManager);
        rcvOrderCart.setItemAnimator(new DefaultItemAnimator());
        rcvOrderCart.setAdapter(adapter);

        double money = countMoney();
        String sendMoney = tvSendMoney.getText().toString().trim();
        String tvMoney = sendMoney.substring(1,sendMoney.length()-1);
        String totalMoney = String.valueOf(money + Integer.parseInt(tvMoney));
        tvCountMoney.setText("￥"+ totalMoney);

    }

    private double countMoney() {
        double sumMoney = 0;
        for (int i = 0; i < listChaoCommodity.size(); i++) {
            ListChaoCommodity data = listChaoCommodity.get(i);
            double sum = mul(data.getPrice(), data.getCount());
            sumMoney = ArithUtil.add(sumMoney, sum);
        }
        return sumMoney;
    }

    public static double mul(double v1, int v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_name:
                Intent intent = new Intent();
                intent.setClass(this, AddressActivity.class);
                intent.putExtra("goType", 1);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Address address = (Address) data.getSerializableExtra("address");
            if (address != null) {
                tvName.setText(address.getName() + " " + address.getPhone() + "\n" + address.getPosition());
            }
        }
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
        private List<ListChaoCommodity> list;

        public OrderAdapter(List<ListChaoCommodity> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(OrderActivity.this, R.layout.item_order, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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
