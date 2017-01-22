package com.haibin.qiaqia.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.HotSerach;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.ObjectList;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.haibin.qiaqia.widget.FlowLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/9/1.
 */

public class SerachActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.serach_xcf)
    FlowLayout serachXcf;
    @BindView(R.id.serach_rv)
    XRecyclerView serachRv;
    @BindView(R.id.serach_et)
    EditText serachEt;
    @BindView(R.id.serach_cancel)
    TextView serachCancel;
    @BindView(R.id.serach_clear)
    TextView serachClear;
    @BindView(R.id.serach_top)
    RelativeLayout serachTop;
    @BindView(R.id.serach_ll)
    LinearLayout serachLl;
    @BindView(R.id.serach_netRv)
    XRecyclerView serachNetRv;

    private String serachContent = "";
    private SerachNetAdapter netAdapter;

    private List<ListChaoCommodity> list = new ArrayList<ListChaoCommodity>();
    private List<String> strList = new ArrayList<>();
    private List<String> serachRecordList;
    private SerachAdapter adapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_serach);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        serachRecordList = (List<String>) SPUtils.getListSP(this, "serach", "serachList", strList);
    }

    @Override
    public void initListeners() {
        serachCancel.setOnClickListener(this);
        serachClear.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (serachRecordList != null) {
            adapter = new SerachAdapter(this, serachRecordList);
        } else {
            adapter = new SerachAdapter(this, strList);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        serachRv.setLayoutManager(manager);
        serachRv.setItemAnimator(new DefaultItemAnimator());
        serachRv.setAdapter(adapter);
        adapter.setViewClickListener(new SerachAdapter.ViewClickListener() {
            @Override
            public void viewClick(int position) {
                String strName = serachRecordList.get(position);
                serachEt.setText(strName);
            }
        });

        netAdapter = new SerachNetAdapter(this, list);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        serachNetRv.setLayoutManager(manager1);
        serachNetRv.setItemAnimator(new DefaultItemAnimator());
        serachNetRv.setAdapter(netAdapter);
        serachEt.addTextChangedListener(new OnTextListener());
        netAdapter.setOnGoodJJClickListener(new SerachNetAdapter.OnGoodJJClickListener() {
            @Override
            public void onAdd(final int position) {
                int loginId = (int) SPUtils.getParam(SerachActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
                int commodityid = list.get(position).getId();
//                int count = mdata.getCount();
                final int count = list.get(position).getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
//                        Toast.makeText(SerachActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();
                        list.get(position).setCount(count + 1);
                        netAdapter.notifyDataSetChanged();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener ,SerachActivity.this), String.valueOf(loginId),String.valueOf(commodityid),String.valueOf(count + 1) );
            }

            @Override
            public void onReduce(final int position) {
                final int count = list.get(position).getCount();
                if (count >= 1){
                    int loginId = (int) SPUtils.getParam(SerachActivity.this, Constants.USER_INFO, Constants.INFO_ID, 0);
                    int commodityid = list.get(position).getId();
//                int count = mdata.getCount();
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
//                            Toast.makeText(SerachActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                            list.get(position).setCount(count - 1);
                            netAdapter.notifyDataSetChanged();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener ,SerachActivity.this), String.valueOf(loginId),String.valueOf(commodityid),String.valueOf(count - 1) );
                }
            }
        });


        SubscriberOnNextListener<ObjectList> nextListener = new SubscriberOnNextListener<ObjectList>() {
            @Override
            public void onNext(ObjectList objectList) {
                List<HotSerach> list_hot_search = objectList.getList_hot_search();
                if (list_hot_search != null) {
                    initFlow(list_hot_search);
                }
            }
        };

        HttpMethods.getInstance().getHotDataInfo(new ProgressSubscriber<ObjectList>(nextListener, this));
    }

    @Override
    public void addActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serach_cancel:
                if (serachEt.getText().toString().trim().equals("")) {
                    finish();
                } else {
                    serachEt.setText("");
                }
                break;
            case R.id.serach_clear:
                if (serachRecordList != null) {
                    serachRecordList.clear();
                    SPUtils.setListSP(SerachActivity.this, "serach", "serachList", serachRecordList);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initFlow(List<HotSerach> list) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < list.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(list.get(i).getName());
            view.setTextColor(Color.BLACK);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_style));
            serachXcf.addView(view, lp);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = view.getText().toString().trim();
                    serachEt.setText(str);
                   /* if (serachRecordList != null) {
                        serachRecordList.add(str);
                        SPUtils.setListSP(SerachActivity.this, "serach", "serachList", serachRecordList);
                    } else {
                        strList.add(str);
                        SPUtils.setListSP(SerachActivity.this, "serach", "serachList", strList);
                    }*/
                }
            });
        }
    }

    class OnTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!"".equals(s.toString())) {
                serachLl.setVisibility(View.GONE);
                serachNetRv.setVisibility(View.VISIBLE);
                loadSerachData(s.toString());
            } else {
                serachLl.setVisibility(View.VISIBLE);
                serachNetRv.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().trim().equals("")) {
                if (serachRecordList != null) {
                    serachRecordList.add(s.toString().trim());
                    SPUtils.setListSP(SerachActivity.this, "serach", "serachList", serachRecordList);
                } else {
                    strList.add(s.toString().trim());
                    SPUtils.setListSP(SerachActivity.this, "serach", "serachList", strList);
                }
            }

        }
    }

    /**
     * 加载搜索数据
     */
    private void loadSerachData(String alias) {
        int loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        SubscriberOnNextListener<Goods> subscriberOnNextListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goods) {
                if (goods != null) {
                    List<ListChaoCommodity> listChaoCommodity = goods.getListChaoCommodity();
                    list.clear();
                    list.addAll(listChaoCommodity);
                    netAdapter.notifyDataSetChanged();
                }
            }
        };
        HttpMethods.getInstance().getSerachDataInfo(new ProgressSubscriber<Goods>(subscriberOnNextListener, this), String.valueOf(loginId), alias);
    }

}
