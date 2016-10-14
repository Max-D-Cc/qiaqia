package com.haibin.qiaqia.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.ListMarket;
import com.haibin.qiaqia.entity.Market;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.listener.MyItemClickListener;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/26.
 */

public class MarketFragment extends BaseFragment implements MyItemClickListener {

    @BindView(R.id.market_rv)
    RecyclerView marketRv;
    @BindView(R.id.recyclerview_goods)
    RecyclerView recyclerview_goods;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_split)
    TextView tvSplit;
    @BindView(R.id.gouwudai)
    ImageView gouwudai;
    private SubscriberOnNextListener<Market> SubListener;
    private SubscriberOnNextListener<Goods> GoodsSubListener;
    private List<ListMarket> list_chao_class;
    private List<ListChaoCommodity> list_goods_class = new ArrayList<ListChaoCommodity>();
    private MarketClassAdapter adapter;
    private MarketGoodsAdapter goodsAdapter;
    private List<ListMarket> marketList = new ArrayList<ListMarket>();
    private int loginId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_market,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new MarketClassAdapter(getActivity(), marketList, this);
        goodsAdapter = new MarketGoodsAdapter(getActivity(), list_goods_class);
        LinearLayoutManager manage = new LinearLayoutManager(getActivity());
        marketRv.setLayoutManager(manage);
        marketRv.setAdapter(adapter);
        marketRv.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manage1 = new LinearLayoutManager(getActivity());
        recyclerview_goods.setLayoutManager(manage1);
        recyclerview_goods.setAdapter(goodsAdapter);
        recyclerview_goods.setItemAnimator(new DefaultItemAnimator());
        goodsAdapter.setOnMarkGoodJJClickListener(new MarketGoodsAdapter.OnMarkGoodJJClickListener() {
            @Override
            public void onAdd(final int position) {
                int loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
                int commodityid = list_goods_class.get(position).getId();
//                int count = mdata.getCount();
                final int count = list_goods_class.get(position).getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
//                        Toast.makeText(SerachActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();
                        list_goods_class.get(position).setCount(count + 1);
                        goodsAdapter.notifyDataSetChanged();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
            }

            @Override
            public void onReduce(final int position) {
                final int count = list_goods_class.get(position).getCount();
                if (count >= 1) {
                    int loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
                    int commodityid = list_goods_class.get(position).getId();
//                int count = mdata.getCount();
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
//                            Toast.makeText(SerachActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                            list_goods_class.get(position).setCount(count - 1);
                            goodsAdapter.notifyDataSetChanged();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count - 1));
                }
            }
        });

        initData();
    }

    private void initData() {
        String areID = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_ID, "1");

        loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
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
        HttpMethods.getInstance().getMarketClass(new ProgressSubscriber<Market>(SubListener, getActivity()), "0", areID);
        HttpMethods.getInstance().getGoods(new ProgressSubscriber<Goods>(GoodsSubListener, getActivity()), String.valueOf(loginId), "1");
    }

    @Override
    public void onItemClick(View view, int position) {
        HttpMethods.getInstance().getGoods(new ProgressSubscriber<Goods>(GoodsSubListener, getActivity()), String.valueOf(loginId), marketList.get(position).getClass_id());
//        Toast.makeText(getActivity(),"id为" + marketList.get(position).getClass_id() + "--weizhi :" + position,Toast.LENGTH_SHORT).show();
        adapter.changeSelected(position);
    }
}
