package com.haibin.qiaqia.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Collection;
import com.haibin.qiaqia.entity.CollectionList;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.OrderInner;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.listener.RecyclerItemClickListener;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private List<Collection> list = new ArrayList<Collection>();
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
        adapter = new CollectionAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        collectionRv.setLayoutManager(layoutManager);
        collectionRv.setItemAnimator(new DefaultItemAnimator());
        collectionRv.setAdapter(adapter);
        collectionRv.setLoadingMoreEnabled(false);
        collectionRv.setPullRefreshEnabled(false);

        loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
        subscriberOnNextListener = new SubscriberOnNextListener<CollectionList>() {
            @Override
            public void onNext(CollectionList collectionList) {
                List<Collection> list_collection = collectionList.getList_collection();
//                list.clear();
                list.addAll(list_collection);
                adapter.notifyDataSetChanged();
//                collectionRv.refreshComplete();
            }
        };
        HttpMethods.getInstance().getCollectionList(new ProgressSubscriber<CollectionList>(subscriberOnNextListener, this), String.valueOf(loginId));
     /*   collectionRv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                HttpMethods.getInstance().getCollectionList(new ProgressSubscriber<CollectionList>(subscriberOnNextListener, CollectionActivity.this), String.valueOf(loginId));
            }

            @Override
            public void onLoadMore() {
            }
        });*/


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

        collectionRv.addOnItemTouchListener(new RecyclerItemClickListener(CollectionActivity.this, collectionRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                int id = listChaoCommodities.get(position).getId();
//                openDelDialog(1);
//                Toast.makeText(getActivity(),"postion: " + position,Toast.LENGTH_SHORT).show();
                deleteDialog(list.get(position).getId(), position, 0);
            }
        }));
    }

    private void deleteDialog(final int id, final int position, final int type) {
        final SweetAlertDialog dialog = new SweetAlertDialog(CollectionActivity.this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("确认删除").setConfirmText("确认").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                SubscriberOnNextListener<String> listener = new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(CollectionActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                    }
                };
                HttpMethods.getInstance().addCoolectionInfo(new ProgressSubscriber<String>(listener, CollectionActivity.this, 1), String.valueOf(loginId), String.valueOf(list.get(position).getCommodity_id()), String.valueOf(0));
                dialog.dismiss();
            }
        }).setCancelText("否").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    public void addActivity() {

    }
}
