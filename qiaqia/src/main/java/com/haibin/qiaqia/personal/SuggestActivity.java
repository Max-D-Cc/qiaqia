package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/23.
 */

public class SuggestActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.suggest_contenet)
    EditText suggestContenet;
    @BindView(R.id.suggest_submit)
    TextView suggestSubmit;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        allDelete.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        allBack.setOnClickListener(this);
        suggestSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        allTitle.setText("意见反馈");
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
            case R.id.suggest_submit:
                String content = suggestContenet.getText().toString().trim();
                 if (TextUtils.isEmpty(content)){
                    Toast.makeText(SuggestActivity.this,"请输入建议内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
                SubscriberOnNextListener<String> subListener = new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        Toast.makeText(SuggestActivity.this,"提交成功，我们会在最短的时间内给您答复，请耐心等待",Toast.LENGTH_SHORT).show();
                        suggestContenet.setText("");
                    }
                };
                HttpMethods.getInstance().getSuggest(new ProgressSubscriber<String>(subListener,this),String.valueOf(loginId),content);
                break;
        }
    }

}
