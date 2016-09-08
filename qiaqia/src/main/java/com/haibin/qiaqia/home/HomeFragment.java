package com.haibin.qiaqia.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseFragment;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.entity.Goods;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.entity.Vp;
import com.haibin.qiaqia.entity.VpArea;
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

import static com.amap.api.col.v.i;

/**
 * A simple {@link } subclass.
 */
public class HomeFragment extends BaseFragment implements OnPageClickListener, ViewPager.OnPageChangeListener,View.OnClickListener {
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.market)
    TextView market;
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
                startActivity(new Intent(getActivity(),SerachActivity.class));
                break;
            case R.id.tv_city:
                startActivity(new Intent(getActivity(),AreaActivity.class));
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
//                    if (refreshLayout!=null&&refreshLayout.isRefreshing()) {
//                        refreshLayout.setRefreshing(false);
//                    }
//                    BaseBean<Jobs> jobs = (BaseBean<Jobs>) msg.obj;
//                    int count = msg.arg1;
//                    if (count == 0) {
//                        listChaoCommodities.clear();
//                    }
//                    jobs.getData().getList_t_job();
//                    listChaoCommodities.addAll(jobs.getData().getList_t_job());
//                    adapter.notifyDataSetChanged();
//                    DataComplete=true;
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
        img_search = (ImageView) header.findViewById(R.id.img_search);
        relaFruit = (RelativeLayout) header.findViewById(R.id.rela_fruit);
        relaMarket = (RelativeLayout) header.findViewById(R.id.rela_market);
        banner = (InfiniteIndicator) header.findViewById(R.id.img_banner);
        tvCity = (TextView) header.findViewById(R.id.tv_city);
        ButterKnife.bind(this, view);

        String locationCode = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_CODE, "");
        String loacationName = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_NAME, "");
        loacationLon = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_LON, "");
        loacationLat = (String) SPUtils.getParam(getActivity(), Constants.USER_INFO, Constants.USER_LOCATION_LAT, "");
        Toast.makeText(getActivity(), "位置：" + loacationName + "  Lon:" + loacationLat + " Lat:" + loacationLon, Toast.LENGTH_LONG).show();
        img_search.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        initView();
        initData();
        return view;
    }

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

    private void initHotData(String areaId){
        SPUtils.setParam(getActivity(),Constants.USER_INFO,Constants.USER_LOCATION_ID,areaId);
        SubListener = new SubscriberOnNextListener<Goods>() {
            @Override
            public void onNext(Goods goodsHttpResult) {
                listChaoCommodities.addAll(goodsHttpResult.getListChaoCommodity());
                adapter.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_LONG).show();
            }

        };
        HttpMethods.getInstance().getHomeData(new ProgressSubscriber<Goods>(SubListener, getActivity()),String.valueOf(loginId),areaId);
    }

    @Override
    public void onStart() {
        super.onStart();

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
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
            }

            @Override
            public void onLoadMore() {
                // load more data here
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
