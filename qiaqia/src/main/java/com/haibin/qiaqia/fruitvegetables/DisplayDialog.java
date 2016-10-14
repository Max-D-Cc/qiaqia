package com.haibin.qiaqia.fruitvegetables;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/10 0010.
 */

public class DisplayDialog extends Dialog {
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_attention)
    ImageView ivAttention;
    @BindView(R.id.tv_describe)
    TextView tvDescribe;
    @BindView(R.id.iv_open)
    ImageView ivOpen;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    SubscriberOnNextListener<Goods> SubListener;
    @BindView(R.id.tv_prodate)
    TextView tvProdate;
    @BindView(R.id.tv_proydate)
    TextView tvProydate;
    @BindView(R.id.tv_dl_sv)
    ScrollView tvDlSv;
//    @BindView(R.id.tv_buySum)
    TextView tvBuySum;
    @BindView(R.id.iv_jian)
    ImageView ivJian;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private int loginId;

    @OnClick({R.id.iv_attention, R.id.iv_open, R.id.iv_add, R.id.iv_jian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_attention:
                final int collection = mdata.getCollection();
                final int chaoClassId = mdata.getId();
                SubscriberOnNextListener<String> listener = new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        if (collection == 1) {
                            ivAttention.setImageResource(R.mipmap.xin2);
                            mdata.setCollection(0);
                        } else {
                            ivAttention.setImageResource(R.mipmap.xin);
                            mdata.setCollection(1);
                        }
                    }
                };
                if (collection == 1) {
                    HttpMethods.getInstance().addCoolectionInfo(new ProgressSubscriber<String>(listener, mContext, 1), String.valueOf(loginId), String.valueOf(chaoClassId), String.valueOf(0));
                } else {
                    HttpMethods.getInstance().addCoolectionInfo(new ProgressSubscriber<String>(listener, mContext, 1), String.valueOf(loginId), String.valueOf(chaoClassId), String.valueOf(1));
                }
                break;
            case R.id.iv_open:
//                tvDescribe.setMaxLines(20);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvDlSv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.iv_add:
                int commodityid = mdata.getId();
                final int count = mdata.getCount();
                SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
//                        Toast.makeText(mContext, "添加购物车成功", Toast.LENGTH_SHORT).show();
                        mdata.setCount(count + 1);
                        tvBuySum.setVisibility(View.VISIBLE);
                        ivJian.setVisibility(View.VISIBLE);
                        tvBuySum.setText(String.valueOf(count + 1));
                        Intent intent = new Intent(Constants.CARD_ACTION);
                        mContext.sendBroadcast(intent);
                        mDisplayDialogEventListener.displayDialogEvent(0);
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, mContext), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
                break;
            case R.id.iv_jian:
                int commodityid1 = mdata.getId();
                final int count1 = mdata.getCount();
                SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
//                        Toast.makeText(mContext, "添加购物车成功", Toast.LENGTH_SHORT).show();
                        mdata.setCount(count1 - 1);
                        tvBuySum.setText(String.valueOf(count1 - 1));
                        if ((count1 - 1) == 0){
                            tvBuySum.setVisibility(View.INVISIBLE);
                            ivJian.setVisibility(View.INVISIBLE);
                        }
                        Intent intent = new Intent(Constants.CARD_ACTION);
                        mContext.sendBroadcast(intent);
                        mDisplayDialogEventListener.displayDialogEvent(0);
                    }
                };
                if (count1 >= 1){
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, mContext), String.valueOf(loginId), String.valueOf(commodityid1), String.valueOf(count1 - 1));
                }
                break;
        }
    }

    //增加一个回调函数,用以从外部接收返回值
    public interface IDisplayDialogEventListener {
        public void displayDialogEvent(int id);
    }

    Context mContext;
    ListChaoCommodity mdata;
    private IDisplayDialogEventListener mDisplayDialogEventListener;

    public DisplayDialog(Context context) {
        super(context);
        mContext = context;
    }

    public DisplayDialog(Context context, ListChaoCommodity data, IDisplayDialogEventListener listener) {
        super(context);
        mContext = context;
        mdata = data;
        mDisplayDialogEventListener = listener;
    }

    public DisplayDialog(Context context, ListChaoCommodity data, IDisplayDialogEventListener listener, int themeResId) {
        super(context, R.style.myDialogTheme);
        mContext = context;
        mdata = data;
        mDisplayDialogEventListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View display_dialog = LayoutInflater.from(mContext).inflate(R.layout.display_dialog, null);
        this.setContentView(display_dialog);
        ButterKnife.bind(this, display_dialog);
        tvBuySum = (TextView) findViewById(R.id.tv_buySum);
        loginId = (int) SPUtils.getParam(mContext, Constants.USER_INFO, Constants.INFO_ID, 0);
        Glide.with(mContext)
                .load(mdata.getImage())
                .placeholder(R.drawable.ic_loading_rotate)
                .crossFade()
                .into(ivPicture);
        tvPrice.setText("￥" + String.valueOf(mdata.getPrice()));
        tvDescribe.setText(mdata.getName());
        int curCount = mdata.getCount();
        if (curCount == 0){
            tvBuySum.setVisibility(View.INVISIBLE);
            ivJian.setVisibility(View.INVISIBLE);
        }else{
            tvBuySum.setVisibility(View.VISIBLE);
            ivJian.setVisibility(View.VISIBLE);
            tvBuySum.setText(String.valueOf(curCount));
        }
        if (mdata.getCollection() == 1) {
            ivAttention.setImageResource(R.mipmap.xin);
        } else {
            ivAttention.setImageResource(R.mipmap.xin2);
        }
    }
}
