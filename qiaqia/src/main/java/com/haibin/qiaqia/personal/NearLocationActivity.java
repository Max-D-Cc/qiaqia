package com.haibin.qiaqia.personal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.utils.LogUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cai on 2016/10/6.
 */

public class NearLocationActivity extends Activity implements AMapLocationListener, NearbySearch.NearbyListener, GeocodeSearch.OnGeocodeSearchListener {

    private MapView mapView;
    //声明mLocationOption对象
    private UiSettings mUiSettings;//定义一个UiSettings对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    private AMap aMap;
    private XRecyclerView rv;
    private List<PoiItem> list = new ArrayList<>();
    private NearAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearlocation);
        mapView = (MapView) findViewById(R.id.near_map);
        rv = (XRecyclerView) findViewById(R.id.near_rv);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类
        }
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

        adapter = new NearAdapter(this, list);
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
                mlocationClient.stopLocation();
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String city = amapLocation.getCity();
                String address = amapLocation.getAddress();
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
//                LatLng latLng = new LatLng(latitude, longitude);
                GeocodeSearch geocoderSearch = new GeocodeSearch(this);
                geocoderSearch.setOnGeocodeSearchListener(this);
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200,GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
               /* if (aMap != null){
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

                    //获取附近实例（单例模式）
//                    NearbySearch search = NearbySearch.getInstance(getApplicationContext());

                    NearbySearch.getInstance(getApplicationContext()).addNearbyListener(this);
//设置搜索条件
                    NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
//设置搜索的中心点
                    query.setCenterPoint(new LatLonPoint(latitude, longitude));
//设置搜索的坐标体系
                    query.setCoordType(NearbySearch.AMAP);
//设置搜索半径
                    query.setRadius(10000);
//设置查询的时间
                    query.setTimeRange(10000);
//设置查询的方式驾车还是距离
                    query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
//调用异步查询接口
                    NearbySearch.getInstance(getApplicationContext())
                            .searchNearbyInfoAsyn(query);

                }*/
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onUserInfoCleared(int i) {

    }

    @Override
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        if (resultCode == 1000) {
            if (nearbySearchResult != null
                    && nearbySearchResult.getNearbyInfoList() != null
                    && nearbySearchResult.getNearbyInfoList().size() > 0) {
//                list.addAll(nearbySearchResult.getNearbyInfoList());
//                adapter.notifyDataSetChanged();
                Log.e("AmapError", "1" + resultCode);
            } else {
                Log.e("AmapError", "2" + resultCode);
            }
        } else {
            Log.e("AmapError", "3" + resultCode);
        }
    }

    @Override
    public void onNearbyInfoUploaded(int i) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                LogUtils.e("cai",addressName);
                for (int i = 0; i < result.getRegeocodeAddress().getAois().size(); i++){
                    LogUtils.e("cai",result.getRegeocodeAddress().getAois().get(i).getAoiName());
                }
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(latLonPoint), 15));
//                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                ToastUtil.show(ReGeocoderActivity.this, addressName);
            } else {
//                ToastUtil.show(ReGeocoderActivity.this, R.string.no_result);
                LogUtils.e("cai","kong");
            }
        } else {
            LogUtils.e("cai","fail");
//            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
