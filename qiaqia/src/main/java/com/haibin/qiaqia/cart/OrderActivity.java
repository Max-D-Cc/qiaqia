package com.haibin.qiaqia.cart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.SendGoodsTime;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.listener.RecyclerItemClickListener;
import com.haibin.qiaqia.personal.AddressActivity;
import com.haibin.qiaqia.personal.CouponActivity;
import com.haibin.qiaqia.utils.ArithUtil;
import com.haibin.qiaqia.widget.SizePickerPopuWin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_order_yhq)
    TextView tvOrderYhq;
    @BindView(R.id.order_back)
    ImageView orderBack;
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
    @BindView(R.id.order_dg)
    TextView orderDg;
    @BindView(R.id.rcv_order_dg)
    RecyclerView rcvOrderDg;
    private List<ListChaoCommodity> listChaoCommodity;
    private List<ListChaoCommodity> list_chao_commodityer;
    private List<ListChaoCommodity>  all = new ArrayList<>();
    private double money;
    private double oneMoney;//选购

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    SendGoodsTime sendGoodsTime = (SendGoodsTime) msg.obj;
                    if (sendGoodsTime != null){
                        tvSendMethod.setText(sendGoodsTime.getDate()+":"+sendGoodsTime.getTime());
                    }
                    break;
            }
        }
    };

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        Goods goods = (Goods) getIntent().getSerializableExtra("balanceData");
        listChaoCommodity = goods.getListChaoCommodity();
        list_chao_commodityer = goods.getList_chao_commodityer();
        oneMoney = oneMoney();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        tvName.setOnClickListener(this);
        rlSendTime.setOnClickListener(this);
        tvSendMethod.setOnClickListener(this);
        orderBack.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        tvOrderYhq.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (list_chao_commodityer != null){
            if (list_chao_commodityer.size() == 0){
                orderDg.setVisibility(View.GONE);
            }else{
                orderDg.setVisibility(View.VISIBLE);
            }
        }else{
            orderDg.setVisibility(View.GONE);
        }

        OrderAdapter adapter = new OrderAdapter(listChaoCommodity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvOrderCart.setLayoutManager(layoutManager);
        rcvOrderCart.setItemAnimator(new DefaultItemAnimator());
        rcvOrderCart.setAdapter(adapter);
        OrderAdapter1 adapter1 = new OrderAdapter1(list_chao_commodityer);
        LinearLayoutManager manage = new LinearLayoutManager(this);
        rcvOrderDg.setLayoutManager(manage);
        rcvOrderDg.setItemAnimator(new DefaultItemAnimator());
        rcvOrderDg.setAdapter(adapter1);

        rcvOrderCart.addOnItemTouchListener(new RecyclerItemClickListener(this, rcvOrderCart, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ListChaoCommodity data = OrderActivity.this.listChaoCommodity.get(position);
                DisplayDialog displayDialog = new DisplayDialog(OrderActivity.this, data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog,1);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, OrderActivity.this);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        rcvOrderDg.addOnItemTouchListener(new RecyclerItemClickListener(this, rcvOrderDg, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ListChaoCommodity data = OrderActivity.this.list_chao_commodityer.get(position);
                DisplayDialog displayDialog = new DisplayDialog(OrderActivity.this, data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog,1);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, OrderActivity.this);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        double money = countMoney();
        String sendMoney = tvSendMoney.getText().toString().trim();
        String tvMoney = sendMoney.substring(1, sendMoney.length() - 1);
//        String totalMoney = String.valueOf(money + Integer.parseInt(tvMoney));
        tvCountMoney.setText("待支付" + " ￥" + money);

    }

    public static void setDialogWindowAttr(Dialog dlg, Context ctx) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dip2px(ctx, 259);//宽高可设置具体大小
        lp.height = dip2px(ctx, 365);
        dlg.getWindow().setAttributes(lp);
    }

    //选购商品的价格
    private double oneMoney(){
        if (listChaoCommodity != null){
            double sumMoney = 0;
            for (int i = 0; i < all.size(); i++) {
                ListChaoCommodity data = all.get(i);
                double sum = mul(data.getPrice(), data.getCount());
                sumMoney = ArithUtil.add(sumMoney, sum);
            }
            return sumMoney;
        }
        return 0;
    }

    private double countMoney() {
        all.clear();
        all.addAll(listChaoCommodity);
        all.addAll(list_chao_commodityer);
        double sumMoney = 0;
        for (int i = 0; i < all.size(); i++) {
            ListChaoCommodity data = all.get(i);
            double sum = mul(data.getPrice(), data.getCount());
            sumMoney = ArithUtil.add(sumMoney, sum);
        }
        money = sumMoney;
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
            case R.id.rl_send_time:
                List<String> list1 = new ArrayList<>();
                List<String> list2 = new ArrayList<>();
                String hour = getHour();
                if (Integer.parseInt(hour) > 22){
                    list1.add("明天");
                    list1.add("后天");
                }else{
                    list1.add("今天");
                    list1.add("明天");
                    list1.add("后天");
                }

                for (int j = 7; j<23;j++){
                    list2.add(j + ":00");
                }

                List<String> timeList1 = new ArrayList<>();
                char[] chars = hour.toCharArray();
                if (chars[0] == '0') {
                    hour = String.valueOf(chars[1]);
                }
                int i = Integer.parseInt(hour);
                if (i > 7 && i < 22) {
                    for (int n = 0; n < 22 - i; n++) {
                        timeList1.add((i + n + 1) + ":00");
                    }
                } else if (i >= 22 && i < 24) {
                } else {
                    timeList1.addAll(list2);
                }
                if (i < 23 && i > 6) {
                    timeList1.add(0, "立即送出");
                }


                SizePickerPopuWin pickerPopuWin = new SizePickerPopuWin(OrderActivity.this,list1,timeList1,mHandler,0);
                pickerPopuWin.showShareWindow();
                pickerPopuWin.showAtLocation(View.inflate(OrderActivity.this,R.layout.activity_order,null), Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,0);
                break;
            case R.id.order_back:
                finish();
                break;
            case R.id.btn_order:
                Intent intent2 = new Intent(OrderActivity.this,PayActivity.class);
                intent2.putExtra("money",money);
                startActivity(intent2);
                break;
            case R.id.tv_order_yhq:
                Intent intent1 = new Intent(OrderActivity.this,CouponActivity.class);
                intent1.putExtra("money",oneMoney);
                intent1.putExtra("type",1);
                startActivityForResult(intent1,1);
                break;
        }
    }

    private String getHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Address address = (Address) data.getSerializableExtra("address");
            if (address != null) {
                tvName.setText(address.getName() + " " + address.getPhone() + "\n" + address.getPosition());
            }
        }else if(resultCode == 1){
            try{
                double useMoney = data.getDoubleExtra("useMoney", 0);
                tvOrderYhq.setText("- ￥"+String.valueOf(useMoney));
                tvCountMoney.setText(String.valueOf(money - useMoney));
                money = money - useMoney;
            }catch (Exception e){
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

    class OrderAdapter1 extends RecyclerView.Adapter<OrderAdapter1.ViewHolder> {
        private List<ListChaoCommodity> list;

        public OrderAdapter1(List<ListChaoCommodity> list) {
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
