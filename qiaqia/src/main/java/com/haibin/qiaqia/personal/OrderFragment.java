package com.haibin.qiaqia.personal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.home.HomeAdapter;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/9 0009.
 */

public class OrderFragment extends BaseFragment {

    @BindView(R.id.recyclerview_fruit)
    XRecyclerView recyclerviewFruit;
    private LinearLayoutManager mLayoutManager;
    private MyOrderAdapter adapter;
    private RelativeLayout relaFruit;
    private RelativeLayout relaVegetable;
    public List listOrders = new ArrayList();
    SubscriberOnNextListener<Goods> SubListener;
    private String mTitle  = null;

    public static OrderFragment getInstance(String title) {
        OrderFragment fft = new OrderFragment();
        fft.mTitle = title;
        return fft;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fruit, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }



    private void initView() {
        adapter = new MyOrderAdapter(getActivity(), listOrders);
        mLayoutManager = new LinearLayoutManager(getActivity());
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        recyclerviewFruit.setLayoutManager(mLayoutManager);
        //设置adapter
        recyclerviewFruit.setAdapter(adapter);
        //设置Item增加、移除动画
        recyclerviewFruit.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        int loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);

        SubListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goodsHttpResult) {
            }
        };
        HttpMethods.getInstance().getGoods(new ProgressSubscriber<Goods>(SubListener ,getActivity()), String.valueOf(loginId),mTitle );
    }
}
