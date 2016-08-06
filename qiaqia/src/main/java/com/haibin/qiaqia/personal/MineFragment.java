package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;

import butterknife.ButterKnife;

public class MineFragment extends BaseFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
//        initView();
//        initData();
        return view;
    }
}
