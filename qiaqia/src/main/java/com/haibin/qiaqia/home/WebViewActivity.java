package com.haibin.qiaqia.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

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
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    private String htmls;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        htmls = getIntent().getStringExtra("html");
    }

    @Override
    public void initViews() {
        allTitle.setText("页面详情");
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                allTitle.setText(title);
            }
        });
        web.loadUrl(htmls);
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
}
