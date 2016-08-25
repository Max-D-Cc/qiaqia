package com.haibin.qiaqia.personal;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity {

    private final String[] mTitles = new String[]{
            "待付款", "待收货", "已完成", "退款/售后"
    };
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.mvp)
    ViewPager mvp;
    @BindView(R.id.msliding_tab)
    SlidingTabLayout mslidingTab;
    @BindView(R.id.activity_myorder)
    RelativeLayout activityMyorder;
    private MyPagerAdapter mAdapter;
    private int currentPage;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_myorder);
        ButterKnife.bind(this);
        currentPage = getIntent().getIntExtra("currentPage", 0);

    }

    @Override
    public void initViews() {
        allTitle.setText("我的订单");
        allDelete.setVisibility(View.GONE);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mvp.setAdapter(mAdapter);
        mslidingTab.setViewPager(mvp, mTitles);
        mslidingTab.setCurrentTab(currentPage);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void addActivity() {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return OrderFragment.getInstance("0");
            } else if (position == 1) {
                return OrderFragment.getInstance("1");
            } else if (position == 2) {
                return OrderFragment.getInstance("2");
            } else {
                return OrderFragment.getInstance("3");
            }
        }
    }
}
