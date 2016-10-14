package com.haibin.qiaqia.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import rx.Subscriber;

/**
 * Created by invinjun on 2016/6/4.
 */

public class ProgressSubscriber<T> extends Subscriber<T>{

    private SubscriberOnNextListener subscriberOnNextListener;
    private Context mContext;
    private ProgressDialog progressDialog;
    private int isShow = 0;
    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        this.subscriberOnNextListener = subscriberOnNextListener;
        mContext=context;
    }

    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context,int isShow) {
        this.subscriberOnNextListener = subscriberOnNextListener;
        mContext=context;
        this.isShow = isShow;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShow !=1){
            progressDialog=new ProgressDialog(mContext);
            progressDialog.show();
        }

    }

    @Override
    public void onCompleted() {
        if (isShow != 1){
            progressDialog.dismiss();
        }


//        Toast.makeText(mContext, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(mContext, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        if (isShow != 1){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onNext(T t) {
        subscriberOnNextListener.onNext(t);

    }
}
