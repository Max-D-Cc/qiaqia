package com.haibin.qiaqia.fruitvegetables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.CartActivity;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.main.MainActivity;
import com.haibin.qiaqia.utils.CardUtils;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FruitVegetableActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.vp)
    ViewPager vp;

    private final String[] mTitles = new String[]{
            "水果", "蔬菜"
    };
    @BindView(R.id.vf_goodsNum)
    TextView vfGoodsNum;
    @BindView(R.id.vf_gocard)
    RelativeLayout vfGocard;
    @BindView(R.id.vf_back)
    ImageView vfBack;
    private MyPagerAdapter mAdapter;
    private RelativeLayout rl_click;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.activity_fruit_vegetable)
    RelativeLayout activityFruitVegetable;

    private SubscriberOnNextListener<Goods> subListener;
    private List<ListChaoCommodity> list = new ArrayList<>();
    private int goodCount = 0;
    private int loginId;
    private CardUtils utils;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_fruit_vegetable);
        ButterKnife.bind(this);

        rl_click = (RelativeLayout) findViewById(R.id.rl_click);


    }

    @Override
    public void initViews() {
        loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        slidingTab.setViewPager(vp, mTitles);
        loadGoodsNum();
    }

    @Override
    protected void onStart() {
        super.onStart();
        utils = new CardUtils(this);
        utils.setOnIMUpdateListener(new CardUtils.OnCardUpdateListener() {
            @Override
            public void onUpdate() {
                HttpMethods.getInstance().getCarInfo(new ProgressSubscriber<Goods>(subListener, FruitVegetableActivity.this,1), String.valueOf(loginId));
            }
        });
    }

    @Override
    public void initListeners() {
        rl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vfBack.setOnClickListener(this);
        vfGocard.setOnClickListener(this);
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
            case R.id.vf_back:
                finish();
                break;
            case R.id.vf_gocard:
                startActivity(new Intent(FruitVegetableActivity.this, CartActivity.class));
                break;
        }
    }

    private void loadGoodsNum(){

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
                vfGoodsNum.setText(String.valueOf(goodCount));
            }
        };
        HttpMethods.getInstance().getCarInfo(new ProgressSubscriber<Goods>(subListener, this,1), String.valueOf(loginId));
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return FruitFragment.getInstance("1");
            } else {
                return FruitFragment.getInstance("2");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (utils != null){
            utils.destoryReceiver();
        }
    }
}
