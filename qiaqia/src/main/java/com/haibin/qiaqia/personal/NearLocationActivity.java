package com.haibin.qiaqia.personal;

import android.app.Activity;
import android.content.SyncStatusObserver;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.BaseActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cai on 2016/10/6.
 */

public class NearLocationActivity extends Activity implements AMapLocationListener{

    private MapView mapView;
    //声明mLocationOption对象
    private UiSettings mUiSettings;//定义一个UiSettings对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    private AMap aMap;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private XRecyclerView rv;
    private List<PoiItem> list = new ArrayList<>();
    private NearAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearlocation);
        mapView = (MapView) findViewById(R.id.near_map);
        rv = (XRecyclerView) findViewById(R.id.near_rv);
        mapView.onCreate(savedInstanceState);

        if (mapView != null){
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类
        }
        initData();
    }

    private void initData() {
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

        adapter = new NearAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String city = amapLocation.getCity();
                String address = amapLocation.getAddress();
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                LatLng latLng = new LatLng(latitude,longitude);
                if (aMap != null){
                    aMap.setLocationSource(new LocationSource() {
                        @Override
                        public void activate(OnLocationChangedListener onLocationChangedListener) {
                        }
                        @Override
                        public void deactivate() {
                        }
                    });// 设置定位监听
                    mUiSettings.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
                    aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
                    final Marker marker = aMap.addMarker(new MarkerOptions().
                            position(latLng).
                            title(city).
                            snippet(address));

                    query = new PoiSearch.Query(address, "", city);
                    query.setPageSize(10);// 设置每页最多返回多少条poiitem
                    query.setPageNum(0);//设置查询页码
                    poiSearch = new PoiSearch(this, query);
                    poiSearch.setOnPoiSearchListener(new NearOnPoiSearchListener());
                    poiSearch.searchPOIAsyn();
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    class NearOnPoiSearchListener implements PoiSearch.OnPoiSearchListener {
        @Override
        public void onPoiSearched(PoiResult result, int rCode) {
            if (rCode == 1000) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        List<SuggestionCity> suggestionCities = result
                                .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                        list.addAll(poiItems);
                        adapter.notifyDataSetChanged();
                        if (poiItems != null && poiItems.size() > 0) {
                            aMap.clear();// 清理之前的图标
                            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                            poiOverlay.removeFromMap();
                            poiOverlay.addToMap();
                            poiOverlay.zoomToSpan();
                        } else if (suggestionCities != null
                                && suggestionCities.size() > 0) {
                        } else {
                        }
                    }
                } else {
                    Toast.makeText(NearLocationActivity.this,"没有附近相关信息",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NearLocationActivity.this,"没有附近相关信息",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    }
}
