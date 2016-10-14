package com.haibin.qiaqia.personal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Collection;
import com.haibin.qiaqia.entity.CollectionList;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.OrderInner;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cai on 2016/8/23.
 */

public class CollectionActivity extends BaseActivity {
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.all_delete)
    ImageView allDelete;
    @BindView(R.id.collection_rv)
    XRecyclerView collectionRv;
    private List<Collection> list  = new ArrayList<Collection>();
    private SubscriberOnNextListener<CollectionList> subscriberOnNextListener;
    private CollectionAdapter adapter;
    private int loginId;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
    }

    @Override
    public void initViews() {
        allTitle.setText("收藏");
        allDelete.setVisibility(View.GONE);
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
        adapter = new CollectionAdapter(this,list);
        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        collectionRv.setLayoutManager(layoutManager);
        collectionRv.setItemAnimator(new DefaultItemAnimator());
        collectionRv.setAdapter(adapter);
        loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        subscriberOnNextListener = new SubscriberOnNextListener<CollectionList>() {
            @Override
            public void onNext(CollectionList collectionList) {
                List<Collection> list_collection = collectionList.getList_collection();
                list.addAll(list_collection);
                adapter.notifyDataSetChanged();
            }
        };
        HttpMethods.getInstance().getCollectionList(new ProgressSubscriber<CollectionList>(subscriberOnNextListener,this),String.valueOf(loginId));

        adapter.setOnCollectionListener(new CollectionAdapter.OnCollectionListener() {
            @Override
            public void onAdd(int position) {
                final Collection collection = list.get(position);
                int commodityid = collection.getCommodity().getId();
                final int count = collection.getCommodity().getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
                        collection.getCommodity().setCount(count + 1);
                        adapter.notifyDataSetChanged();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, CollectionActivity.this), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
            }

            @Override
            public void onJian(int position) {
                final Collection collection = list.get(position);
                int commodityid = collection.getCommodity().getId();
                final int count = collection.getCommodity().getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
                        collection.getCommodity().setCount(count - 1);
                        adapter.notifyDataSetChanged();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, CollectionActivity.this), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count - 1));
            }
        });
    }

    @Override
    public void addActivity() {

    }
}
