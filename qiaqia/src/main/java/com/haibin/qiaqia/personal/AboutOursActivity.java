package com.haibin.qiaqia.personal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/23.
 */

public class AboutOursActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_aboutours);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        allDelete.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        allTitle.setText("关于我们");
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_back:
                finish();
                break;
        }
    }
}
