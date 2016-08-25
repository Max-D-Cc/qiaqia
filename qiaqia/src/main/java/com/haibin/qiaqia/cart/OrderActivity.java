package com.haibin.qiaqia.cart;

import android.content.Intent;
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

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        Goods goods = (Goods) getIntent().getSerializableExtra("balanceData");
        List<ListChaoCommodity> listChaoCommodity = goods.getListChaoCommodity();

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
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Address address = (Address)data.getSerializableExtra("address");
            if(address != null){
                tvName.setText(address.getName() + " " + address.getPhone() + "\n" + address.getPosition());
            }
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

        private List<ListChaoCommodity> list;
        public OrderAdapter(List<ListChaoCommodity> list){

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
