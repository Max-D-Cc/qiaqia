package com.haibin.qiaqia.home;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/30.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.web)
    WebView web;
    private String htmls;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        htmls = getIntent().getStringExtra("html");
    }

    @Override
    public void initViews() {
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        web.loadUrl(htmls);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void addActivity() {

    }
}
