package com.haibin.qiaqia.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.haibin.qiaqia.base.Constants;

/**
 * 用来通知购物车数量改变的工具类
 * Created by cai on 2016/9/25.
 */

public class CardUtils {

    private Context context;
    private OnCardUpdateListener listener;
    private UpdateCardReceicer receiver;

    public CardUtils(Context context){
        this.context = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CARD_ACTION);
        receiver = new UpdateCardReceicer();
        context.registerReceiver(receiver, filter);
    }

    public interface OnCardUpdateListener{
        void onUpdate();
    }

    public void setOnIMUpdateListener(OnCardUpdateListener listener){
        this.listener = listener;
    }

    private class UpdateCardReceicer extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.CARD_ACTION)) {
                if (listener != null) {
                    listener.onUpdate();
                }else{
                }
            }
        }
    }

    public void destoryReceiver(){
        if (receiver != null){
            context.unregisterReceiver(receiver);
        }
    }
}
