package com.haibin.qiaqia.widget;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.entity.SendGoodsTime;
import com.haibin.qiaqia.widget.time.PickerSize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangle
 * @data: 2015-08-14
 * @version: V1.0
 */

public class SizePickerPopuWin extends PopupWindow implements OnClickListener {

    private Context context;
    private List<String> stringList = new ArrayList<String>();
    private String textStr;
    private Handler mHandler;
    private int type;//三种 鞋码 衣服 身高分别是 0 00 000
    private PickerSize packer;
    private PickerSize packer1;
    private TextView tvNo;
    private TextView tvOk;
    private String date;
    List<String> timeList = new ArrayList<>();
    List<String> timeList1;
    private SendGoodsTime sendGoodsTime;

    //分享相关

    public SizePickerPopuWin(Context context, List<String> list, List<String> timeList, Handler handler, int mType) {
        this.context = context;
        this.stringList = list;
        this.mHandler = handler;
        this.type = mType;
        this.timeList = timeList;
    }

    public void showShareWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.popwindow_picker, null);
        initView(view);
        initListener();
        this.setContentView(view);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    public void initView(View view) {

        tvNo = (TextView) view.findViewById(R.id.tv_no);
        tvOk = (TextView) view.findViewById(R.id.tv_ok);
        packer = (PickerSize) view.findViewById(R.id.pcker);
        packer1 = (PickerSize) view.findViewById(R.id.pcker1);
        timeList1 = new ArrayList<>();
        for (int j = 7; j<23;j++){
            timeList1.add(j + ":00");
        }



        packer.setData(stringList);
        packer1.setData(timeList);
        textStr = "今天";
        date = "立即送出";
        packer.setSelected(0);
        packer1.setSelected(0);
    }


    private String getHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    public void initListener() {
        sendGoodsTime = new SendGoodsTime();
        sendGoodsTime.setDate(textStr);
        sendGoodsTime.setTime(date);
        tvNo.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        packer.setOnSelectListener(new PickerSize.onSelectListener() {
            @Override
            public void onSelect(String text) {
                textStr = text;
                sendGoodsTime.setDate(textStr);
                if (!textStr.equals("今天")) {
                    if (timeList1.size() != 0){
                        packer1.setData(timeList1);
                        packer1.setVisibility(View.VISIBLE);
                    }else{
                        packer1.setVisibility(View.INVISIBLE);
                    }
                } else {
                    packer1.setData(timeList);
                }
            }
        });

        packer1.setOnSelectListener(new PickerSize.onSelectListener() {
            @Override
            public void onSelect(String text) {
                date = text;
                sendGoodsTime.setTime(date);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_no:
                dismiss();
                break;
            case R.id.tv_ok:
                Message message = new Message();
                message.arg1 = type;
                message.obj = sendGoodsTime;
                message.what = 1;
                mHandler.sendMessage(message);
                dismiss();
                break;
        }
    }
}
