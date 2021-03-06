package com.haibin.qiaqia.cart;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.home.MarketActivity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.listener.RecyclerItemClickListener;
import com.haibin.qiaqia.utils.ArithUtil;
import com.haibin.qiaqia.utils.LogUtils;
import com.haibin.qiaqia.utils.SPUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link } subclass.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.cart_sum)
    TextView cartSum;
    @BindView(R.id.cart_submit)
    TextView cartSubmit;
    @BindView(R.id.card_all)
    RelativeLayout cardAll;
    @BindView(R.id.card_good1)
    TextView cardGood1;
    @BindView(R.id.card_good2)
    TextView cardGood2;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;
    @BindView(R.id.all_back)
    ImageView allBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.card_have)
    RelativeLayout cardHave;
    @BindView(R.id.card_null)
    LinearLayout cardNull;
    private CartAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    public List<ListChaoCommodity> listChaoCommodities = new ArrayList<ListChaoCommodity>();
    public List<ListChaoCommodity> listChaoCommodities1 = new ArrayList<ListChaoCommodity>();
    SubscriberOnNextListener<Goods> SubListener;
    private List<Integer> delGoodsList = new ArrayList<Integer>();
    public List<ListChaoCommodity> list = new ArrayList<ListChaoCommodity>();

    private int clickNum = 0;
    private int loginId;
    private Cart1Adapter adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {

        allTitle.setText("购物车");
        allBack.setVisibility(View.GONE);
        adapter = new CartAdapter(getActivity(), listChaoCommodities);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerview.setLayoutManager(mLayoutManager);
        //设置adapter
        recyclerview.setAdapter(adapter);
        //设置Item增加、移除动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        cartSum.setOnClickListener(this);
        cartSubmit.setOnClickListener(this);
        adapter.setOnGoodsListener(new CartAdapter.OnGoodsAMLitener() {
            @Override
            public void minusGoods(int postion) {
                final ListChaoCommodity listChaoCommodity = listChaoCommodities.get(postion);
                int commodityid = listChaoCommodity.getId();
                final int count = listChaoCommodity.getCount();
                if (count == 1) {
                    Toast.makeText(getActivity(), "长按商品删除", Toast.LENGTH_SHORT).show();
                } else {
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
                            listChaoCommodity.setCount(count - 1);
                            refreshAdapter();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count - 1));
                }
                Intent intent = new Intent(Constants.CARD_ACTION);
                getActivity().sendBroadcast(intent);
            }

            @Override
            public void addGoods(int postion, ImageView iv) {

                final ListChaoCommodity listChaoCommodity = listChaoCommodities.get(postion);
                int commodityid = listChaoCommodity.getId();
                final int count = listChaoCommodity.getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
                        listChaoCommodity.setCount(count + 1);
                        refreshAdapter();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
                Intent intent = new Intent(Constants.CARD_ACTION);
                getActivity().sendBroadcast(intent);
            }

            @Override
            public void deleteGoods(int postion, boolean isChecked) {
                if (isChecked) {
                    delGoodsList.add(postion);
                } else {
                    for (int i = 0; i < delGoodsList.size(); i++) {
                        int currPostion = delGoodsList.get(i);
                        if (currPostion == postion) {
                            delGoodsList.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onImgClick(int position) {
                ListChaoCommodity data = listChaoCommodities.get(position);
                DisplayDialog displayDialog = new DisplayDialog(getActivity(), data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog,1);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, getActivity());
            }
        });

        adapter1 = new Cart1Adapter(getActivity(), listChaoCommodities1);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview1.setLayoutManager(manager);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        recyclerview1.setAdapter(adapter1);

        adapter1.setOnGoodsListener(new Cart1Adapter.OnGoodsAMLitener() {
            @Override
            public void minusGoods(int postion) {
                final ListChaoCommodity listChaoCommodity = listChaoCommodities1.get(postion);
                int commodityid = listChaoCommodity.getId();
                final int count = listChaoCommodity.getCount();
                if (count == 1) {
                    Toast.makeText(getActivity(), "长按商品删除", Toast.LENGTH_SHORT).show();
                } else {
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
                            listChaoCommodity.setCount(count - 1);
                            refreshAdapter();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count - 1));
                }
                Intent intent = new Intent(Constants.CARD_ACTION);
                getActivity().sendBroadcast(intent);
            }

            @Override
            public void addGoods(int postion, ImageView iv) {

                final ListChaoCommodity listChaoCommodity = listChaoCommodities1.get(postion);
                int commodityid = listChaoCommodity.getId();
                final int count = listChaoCommodity.getCount();
                SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                    @Override
                    public void onNext(Goods goodsHttpResult) {
                        listChaoCommodity.setCount(count + 1);
                        refreshAdapter();
                    }
                };
                HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(commodityid), String.valueOf(count + 1));
                Intent intent = new Intent(Constants.CARD_ACTION);
                getActivity().sendBroadcast(intent);
            }

            @Override
            public void deleteGoods(int postion, boolean isChecked) {
                if (isChecked) {
                    delGoodsList.add(postion);
                } else {
                    for (int i = 0; i < delGoodsList.size(); i++) {
                        int currPostion = delGoodsList.get(i);
                        if (currPostion == postion) {
                            delGoodsList.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onImgClick(int position) {
                ListChaoCommodity data = listChaoCommodities1.get(position);
                DisplayDialog displayDialog = new DisplayDialog(getActivity(), data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog,1);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, getActivity());
            }
        });


        loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
        SubListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goodsHttpResult) {
                listChaoCommodities.clear();
                listChaoCommodities.addAll(goodsHttpResult.getListChaoCommodity());
                listChaoCommodities1.clear();
                listChaoCommodities1.addAll(goodsHttpResult.getList_chao_commodityer());
                adapter.notifyDataSetChanged();
                adapter1.notifyDataSetChanged();
                LogUtils.e("cai","zou l fangfa");
                refreshUI();
                setCountMoney();
//                Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_LONG).show();
            }
        };
//        HttpMethods.getInstance().getCarInfo(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId));

        recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                int id = listChaoCommodities.get(position).getId();
//                openDelDialog(1);
//                Toast.makeText(getActivity(),"postion: " + position,Toast.LENGTH_SHORT).show();
                deleteDialog(listChaoCommodities.get(position).getId(), position, 0);
            }
        }));
        recyclerview1.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerview1, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                int id = listChaoCommodities.get(position).getId();
//                Toast.makeText(getActivity(),"postion: " + position,Toast.LENGTH_SHORT).show();
//                deleteDialog(listChaoCommodities1.get(position).getId(),position,1);
                deleteDialog(listChaoCommodities1.get(position).getId(), position, 1);
            }
        }));
    }

    public static void setDialogWindowAttr(Dialog dlg, Context ctx) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dip2px(ctx, 259);//宽高可设置具体大小
        lp.height = dip2px(ctx, 365);
        dlg.getWindow().setAttributes(lp);
    }

    //常用适配或提示方法
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dipValue + 0.5f);
    }

    public void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
            HttpMethods.getInstance().getCarInfo(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId));
            LogUtils.e("TAG----------TAG: ", "可见");
        } else {
            LogUtils.e("TAG----------TAG: ", "不可见");
        }
    }

    private void openDelDialog(int id) {
        Dialog dialog = new Dialog(getActivity(), R.style.alert_dialog);
        TextView tv = new TextView(getActivity());
        tv.setPadding(80, 25, 200, 25);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setBackgroundResource(R.color.white);
        tv.setText("删除此商品");
        tv.setTextSize(16);
        tv.setTextColor(getResources().getColor(R.color.textgray));
        dialog.setContentView(tv);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_submit:
                if (listChaoCommodities.size() == 0 && listChaoCommodities1.size() == 0){
                    Toast.makeText(getActivity(),"对不起，您还有选购任何商品",Toast.LENGTH_SHORT).show();
                    return;
                }
                Goods goods = new Goods();
                goods.setListChaoCommodity(listChaoCommodities);
                goods.setList_chao_commodityer(listChaoCommodities1);
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("balanceData", goods);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void deleteDialog(final int id, final int position, final int type) {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("确认删除").setConfirmText("确认").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (type == 0) {
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
                            List<ListChaoCommodity> listChaoCommodity = goodsHttpResult.getListChaoCommodity();
                            listChaoCommodities.clear();
                            listChaoCommodities.addAll(listChaoCommodity);
                            refreshAdapter();
                            Intent intent = new Intent(Constants.CARD_ACTION);
                            getActivity().sendBroadcast(intent);
                            refreshUI();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(id), String.valueOf(0));
                } else {
                    SubscriberOnNextListener SubListener = new SubscriberOnNextListener<Goods>() {
                        @Override
                        public void onNext(Goods goodsHttpResult) {
                            List<ListChaoCommodity> listChaoCommodity = goodsHttpResult.getList_chao_commodityer();
                            listChaoCommodities1.clear();
                            listChaoCommodities1.addAll(listChaoCommodity);
                            refreshAdapter();
                            Intent intent = new Intent(Constants.CARD_ACTION);
                            getActivity().sendBroadcast(intent);
                            refreshUI();
                        }
                    };
                    HttpMethods.getInstance().getChangeCarGoods(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), String.valueOf(id), String.valueOf(0));
                }
                dialog.dismiss();

            }
        }).setCancelText("否").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        }).show();

    }

    private void setCountMoney() {
        double money = countMoney();
        cartSum.setText(Html.fromHtml("合计:" + "<font color='#E34C50'>" + "￥" + money + "</font>"));
    }

    private void refreshAdapter() {
        adapter.notifyDataSetChanged();
        adapter1.notifyDataSetChanged();
        setCountMoney();
    }

    private double countMoney() {
        double sumMoney = 0;
        list.clear();
        list.addAll(listChaoCommodities);
        list.addAll(listChaoCommodities1);
        for (int i = 0; i < list.size(); i++) {
            ListChaoCommodity data = list.get(i);
            double sum = mul(data.getPrice(), data.getCount());
            sumMoney = ArithUtil.add(sumMoney, sum);
        }
        return sumMoney;
    }

    public static double mul(double v1, int v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public boolean isOver(int num) {
        if (num % 2 == 0)
            return true;
        else
            return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        clickNum = 0;
    }

    private void refreshUI(){
        LogUtils.e("cai","zou l fangfa1");
        if (listChaoCommodities.size() == 0 && listChaoCommodities1.size() == 0){
            cardHave.setVisibility(View.GONE);
            cardNull.setVisibility(View.VISIBLE);
            LogUtils.e("cai","zou l fangfa2");
        }else if(listChaoCommodities.size() != 0 && listChaoCommodities1.size() == 0){
            cardGood1.setVisibility(View.VISIBLE);
            cardGood2.setVisibility(View.GONE);
            cardHave.setVisibility(View.VISIBLE);
            cardNull.setVisibility(View.GONE);
            LogUtils.e("cai","zou l fangfa3");
        }else if(listChaoCommodities.size() == 0 && listChaoCommodities1.size() != 0){
            cardGood1.setVisibility(View.GONE);
            cardGood2.setVisibility(View.VISIBLE);
            cardHave.setVisibility(View.VISIBLE);
            cardNull.setVisibility(View.GONE);
            LogUtils.e("cai","zou l fangfa4");
        }else{
            cardGood1.setVisibility(View.VISIBLE);
            cardGood2.setVisibility(View.VISIBLE);
            cardHave.setVisibility(View.VISIBLE);
            cardNull.setVisibility(View.GONE);
            LogUtils.e("cai","zou l fangfa5");
        }
    }
}
