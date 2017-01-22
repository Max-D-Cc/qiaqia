package com.haibin.qiaqia.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.CartActivity;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.ListMarket;
import com.haibin.qiaqia.entity.Market;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.listener.MyItemClickListener;
import com.haibin.qiaqia.login.LoginPassWordActivity;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/6/25.
 */

public class WmActivity extends BaseActivity implements MyItemClickListener {

    @BindView(R.id.market_rv)
    RecyclerView marketRv;
    @BindView(R.id.recyclerview_goods)
    RecyclerView recyclerview_goods;
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.mark_goodsNum)
    TextView markGoodsNum;
    @BindView(R.id.mark_gocard)
    RelativeLayout markGocard;
    private SubscriberOnNextListener<Market> SubListener;
    private SubscriberOnNextListener<Goods> GoodsSubListener;
    private List<ListMarket> list_chao_class;
    private List<ListChaoCommodity> list_goods_class = new ArrayList<ListChaoCommodity>();
    private MarketClassAdapter adapter;
    private MarketGoodsAdapter goodsAdapter;
    private List<ListMarket> marketList = new ArrayList<ListMarket>();
    private int loginId;

    private SubscriberOnNextListener<Goods> subListener;
    private List<ListChaoCommodity> list = new ArrayList<>();
    private int goodCount = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_market);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {

        allTitle.setText("超市");
        adapter = new MarketClassAdapter(this, marketList, this);
        goodsAdapter = new MarketGoodsAdapter(this, list_goods_class);
        LinearLayoutManager manage = new LinearLayoutManager(this);
        marketRv.setLayoutManager(manage);
        marketRv.setAdapter(adapter);
        marketRv.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manage1 = new LinearLayoutManager(this);
        recyclerview_goods.setLayoutManager(manage1);
        recyclerview_goods.setAdapter(goodsAdapter);
        recyclerview_goods.setItemAnimator(new DefaultItemAnimator());
        goodsAdapter.setOnMarkGoodJJClickListener(new MarketGoodsAdapter.OnMarkGoodJJClickListener() {
            @Override
            public void onAdd(final int position) {
                int loginId = (int) SPUtils.getParam(WmActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
                if (loginId == 0) {
                    Intent intent = new Intent(WmActivity.this, LoginPassWordActivity.class);
                    startActivity(intent);
                }else{
                    int commodityid = list_goods_class.get(position).getId();
//                int count = mdata.getCount();
                    final int count = list_goods_class.get(position).getCount();
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
//                        Toast.makeText(SerachActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();
                            list_goods_class.get(position).setCount(count + 1);
                            goodsAdapter.notifyDataSetChanged();
                            loadGoodsNum();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, WmActivity.this), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
                }

            }

            @Override
            public void onReduce(final int position) {
                final int count = list_goods_class.get(position).getCount();
                if (count >= 1) {
                    int loginId = (int) SPUtils.getParam(WmActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
                    int commodityid = list_goods_class.get(position).getId();
//                int count = mdata.getCount();
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
//                            Toast.makeText(SerachActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                            list_goods_class.get(position).setCount(count - 1);
                            goodsAdapter.notifyDataSetChanged();
                            loadGoodsNum();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, WmActivity.this), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count - 1));

                }
            }

            @Override
            public void onImgClick(int position) {
                ListChaoCommodity data = WmActivity.this.list_goods_class.get(position);
                DisplayDialog displayDialog = new DisplayDialog(WmActivity.this, data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog,1);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, WmActivity.this);
            }
        });
    }

    public static void setDialogWindowAttr(Dialog dlg, Context ctx) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dip2px(ctx, 259);//宽高可设置具体大小
        lp.height = dip2px(ctx, 365);
        dlg.getWindow().setAttributes(lp);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        markGocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WmActivity.this, CartActivity.class));
            }
        });
    }

    @Override
    public void initData() {
        String areID = (String) SPUtils.getParam(this, Constants.USER_INFO, Constants.USER_LOCATION_ID, "1");

        loginId = (int) SPUtils.getParam(WmActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
        GoodsSubListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goods) {
//                Toast.makeText(MarketActivity.this, goods.toString(), Toast.LENGTH_SHORT).show();
                list_goods_class.clear();
                list_goods_class.addAll(goods.getListChaoCommodity());
                goodsAdapter.notifyDataSetChanged();
            }
        };
        SubListener = new SubscriberOnNextListener<Market>() {
            @Override
            public void onNext(Market market) {
//                Toast.makeText(MarketActivity.this, market.toString(), Toast.LENGTH_SHORT).show();
                list_chao_class = market.getList_chao_class();
                marketList.addAll(list_chao_class);
                adapter.notifyDataSetChanged();
            }
        };
        HttpMethods.getInstance().getMarketClass(new ProgressSubscriber<Market>(SubListener, this), "2", areID);
        HttpMethods.getInstance().getGoods(new ProgressSubscriber<Goods>(GoodsSubListener, this), String.valueOf(loginId), "19");

        loadGoodsNum();
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onItemClick(View view, int position) {
//        int id = marketList.get(position).getId();
//        Toast.makeText(WmActivity.this,"id:" + id ,Toast.LENGTH_SHORT).show();
        HttpMethods.getInstance().getGoods(new ProgressSubscriber<Goods>(GoodsSubListener, this), String.valueOf(loginId), String.valueOf(marketList.get(position).getId()));
        adapter.changeSelected(position);
        adapter.notifyDataSetChanged();
    }



    private void loadGoodsNum(){
        loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        subListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goodsHttpResult) {
                list.clear();
                List<ListChaoCommodity> listChaoCommodity = goodsHttpResult.getListChaoCommodity();
                List<ListChaoCommodity> list_chao_commodityer = goodsHttpResult.getList_chao_commodityer();
                list.addAll(listChaoCommodity);
                list.addAll(list_chao_commodityer);
                goodCount = 0;
                for (int i = 0; i<list.size(); i++){
                    ListChaoCommodity listChaoCommodity1 = list.get(i);
                    int count = listChaoCommodity1.getCount();
                    goodCount += count;
                }
                markGoodsNum.setText(String.valueOf(goodCount));
            }
        };
        HttpMethods.getInstance().getCarInfo(new ProgressSubscriber<Goods>(subListener, this,1), String.valueOf(loginId));
    }

}
