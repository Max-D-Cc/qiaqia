package com.haibin.qiaqia.home;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.Vp;
import com.haibin.qiaqia.entity.VpArea;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.fruitvegetables.FruitVegetableActivity;
import com.haibin.qiaqia.http.HttpMethods;
import com.haibin.qiaqia.http.ProgressSubscriber;
import com.haibin.qiaqia.http.SubscriberOnNextListener;
import com.haibin.qiaqia.main.MainActivity;
import com.haibin.qiaqia.utils.PicassoLoader;
import com.haibin.qiaqia.utils.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lightsky.infiniteindicator.InfiniteIndicator;
import cn.lightsky.infiniteindicator.page.OnPageClickListener;
import cn.lightsky.infiniteindicator.page.Page;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link } subclass.
 */
public class HomeFragment extends BaseFragment implements OnPageClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    private ArrayList<Page> pageViews = new ArrayList<Page>();

    private Context context = getActivity();

    //    private String sort[] = {"不限", "默认", "智能", "价格", "发布时间"};
    //    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private List<View> popupViews = new ArrayList<>();
    private HomeAdapter adapter;
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    public List<ListChaoCommodity> listChaoCommodities = new ArrayList<ListChaoCommodity>();
    private String cityid = "1";
    String typeid = "0";
    String areid = "0";
    String filterid = "2";

    private int MSG_GET_SUCCESS = 0;
    private int MSG_GET_FAIL = 1;
    private int MSG_CITY_SUCCESS = 2;
    private int MSG_CITY_FAIL = 3;
    private Handler mHandler = new Myhandler(this.getActivity());
    private int mtype = 0;
    private int position;
    private boolean DataComplete = false;
    SubscriberOnNextListener<Goods> SubListener;
    private View header;
    private ImageView img_friut;
    private RelativeLayout relaFruit;
    private RelativeLayout relaMarket;
    private InfiniteIndicator banner;
    private String loacationLon;
    private String loacationLat;
    private List<Vp> vps = new ArrayList<>();
    private TextView tvCity;
    private int loginId;
    private ImageView img_search;
    private VpArea areaVp = new VpArea();
    private int totalDy;


    @Override
    public void onPageClick(int position, Page page) {
        Vp vp = vps.get(position);
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("html", vp.getUrl());
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                startActivity(new Intent(getActivity(), SerachActivity.class));
                break;
            case R.id.tv_city:
                Intent intent = new Intent(getActivity(), AreaActivity.class);
                intent.putExtra("area", areaVp);
                startActivity(intent);
                break;
        }
    }

    private class Myhandler extends Handler {
        private WeakReference<Context> reference;

        public Myhandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = (MainActivity) reference.get();
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    String sms = (String) msg.obj;
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        header = LayoutInflater.from(getActivity()).inflate(R.layout.header_home, null, false);
        img_friut = (ImageView) header.findViewById(R.id.img_friut);
        img_search = (ImageView) view.findViewById(R.id.img_search);
        relaFruit = (RelativeLayout) header.findViewById(R.id.rela_fruit);
        relaMarket = (RelativeLayout) header.findViewById(R.id.rela_market);
        banner = (InfiniteIndicator) header.findViewById(R.id.img_banner);
        tvCity = (TextView) view.findViewById(R.id.tv_city);
        ButterKnife.bind(this, view);

        String locationCode = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_CODE, "");
        String loacationName = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_NAME, "");
        loacationLon = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_LON, "");
        loacationLat = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_LAT, "");
        img_search.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        initView();
        initData();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void initView() {
        adapter = new HomeAdapter(getActivity(), listChaoCommodities);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview.setHasFixedSize(true);
        //设置布局管理器
        recyclerview.setLayoutManager(mLayoutManager);
        //设置adapter
        recyclerview.setAdapter(adapter);
        //设置Item增加、移除动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addHeaderView(header);
        recyclerview.setPullRefreshEnabled(false);
        recyclerview.setLoadingMoreEnabled(false);
        /*recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerview.refreshComplete();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerview.loadMoreComplete();
                    }
                },2000);
            }
        });*/
        adapter.setOnItemClickListener(new HomeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ListChaoCommodity data) {
//                Toast.makeText(getActivity(), data.getName(),Toast.LENGTH_SHORT).show();
                DisplayDialog displayDialog = new DisplayDialog(getActivity(), data, new DisplayDialog.IDisplayDialogEventListener() {
                    @Override
                    public void displayDialogEvent(int id) {

                    }
                }, R.style.alert_dialog);
                displayDialog.show();
                setDialogWindowAttr(displayDialog, getActivity());
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                totalDy = totalDy + dy;
                int distance = totalDy;
                if (distance > 0 && distance < 500) {
                    rlTop.getBackground().mutate().setAlpha(distance / 2);
                } else if (distance > 500) {
                    rlTop.getBackground().mutate().setAlpha(255);
                } else {
                    rlTop.getBackground().mutate().setAlpha(0);
                }
            }
        });


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
        loginId = (int) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.INFO_ID, 0);
        banner.setImageLoader(new PicassoLoader());
        banner.setPosition(InfiniteIndicator.IndicatorPosition.Center_Bottom);
        banner.setOnPageChangeListener(this);
        banner.setInfinite(true);
        relaMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MarketActivity.class));
            }
        });
        relaFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FruitVegetableActivity.class));
            }
        });

        SubscriberOnNextListener<VpArea> subscriberOnNextListener = new SubscriberOnNextListener<VpArea>() {
            @Override
            public void onNext(VpArea vpArea) {
                List<Vp> banners = vpArea.getList_t_banner();


                SPUtils.setParam(getActivity(), Constants.LOGIN_INFO, Constants.LOGIN_VERSION, Integer.parseInt(vpArea.getVersion()));
                SPUtils.setParam(getActivity(), Constants.LOGIN_INFO, Constants.LOGIN_APK_URL, vpArea.getApk_url());
                areaVp = vpArea;
                vps.addAll(banners);
                initBanner(banners);
                tvCity.setText(vpArea.getArea_name());
                initHotData(vpArea.getArea_id());
            }
        };
        HttpMethods.getInstance().getVpAreaInfo(new ProgressSubscriber<VpArea>(subscriberOnNextListener, getActivity()), loacationLon, loacationLat);
    }

    private void initBanner(List<Vp> banners) {
        for (int i = 0; i < banners.size(); i++) {
            pageViews.add(new Page(String.valueOf(banners.get(i).getId()), banners.get(i).getImage(), this));
        }
        banner.addPages(pageViews);
        banner.start();

    }

    private void initHotData(String areaId) {
        SPUtils.setParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_ID, areaId);
        SubListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goodsHttpResult) {
                listChaoCommodities.addAll(goodsHttpResult.getListChaoCommodity());
                adapter.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_LONG).show();
            }

        };
        HttpMethods.getInstance().getHomeData(new ProgressSubscriber<Goods>(SubListener, getActivity()), String.valueOf(loginId), areaId);
    }

    @Override
    public void onStart() {
        super.onStart();
        String id = (String) SPUtils.getParam(getActivity(), "area", "areaid", "");
        String name = (String) SPUtils.getParam(getActivity(), "area", "areaname", "");
        if (!id.equals("")) {
            initHotData(id);
            tvCity.setText(name);
        }
        SPUtils.setParam(getActivity(), "area", "areaname", "");
        SPUtils.setParam(getActivity(), "area", "areaid", "");
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
//        banner.start();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
