package com.haibin.qiaqia.personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.haibin.qiaqia.R;
import com.haibin.qiaqia.base.Constants;
import com.haibin.qiaqia.cart.AddAddressActivity;
import com.haibin.qiaqia.cart.OrderActivity;
import com.haibin.qiaqia.entity.Address;
import com.haibin.qiaqia.entity.ListChaoCommodity;
import com.haibin.qiaqia.fruitvegetables.DisplayDialog;
import com.haibin.qiaqia.listener.RecyclerItemClickListener;
import com.haibin.qiaqia.utils.LogUtils;
import com.haibin.qiaqia.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AMapV1地图中简单介绍显示定位小蓝点
 */
public class LocationSourceActivity extends Activity implements LocationSource,
		AMapLocationListener,PoiSearch.OnPoiSearchListener , GeocodeSearch.OnGeocodeSearchListener{
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private RecyclerView rv;
	private List<PoiItem> list = new ArrayList<>();
	private NearAdapter adapter;
	private int loginId;
	private PoiSearch.Query query;
	private PoiSearch search;
	private int currNum = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsource_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		loginId = (int) SPUtils.getParam(this, Constants.USER_INFO, Constants.INFO_ID, 0);
		rv = (RecyclerView) findViewById(R.id.near_rvv);
		adapter = new NearAdapter(this,list);
		LinearLayoutManager manager = new LinearLayoutManager(this);
		rv.setLayoutManager(manager);
		rv.setItemAnimator(new DefaultItemAnimator());
		rv.setAdapter(adapter);

		rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(LocationSourceActivity.this, AddAddressActivity.class);
				String address = list.get(position).getSnippet();
				intent.putExtra("adname",address);
				setResult(0,intent);
				finish();
			}

			@Override
			public void onItemLongClick(View view, int position) {

			}
		}));

		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				mlocationClient.stopLocation();
				String city = amapLocation.getCity();
				String address = amapLocation.getAddress();
				double latitude = amapLocation.getLatitude();
				double longitude = amapLocation.getLongitude();
				GeocodeSearch geocoderSearch = new GeocodeSearch(this);
				geocoderSearch.setOnGeocodeSearchListener(this);
				RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200,GeocodeSearch.AMAP);
				geocoderSearch.getFromLocationAsyn(query);
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	private void getNearPositionName(String name,String city){
		query = new PoiSearch.Query(name, "", city);
		query.setPageSize(15);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);//设置查询页码
		search = new PoiSearch(this, query);
		search.setOnPoiSearchListener(this);
		search.searchPOIAsyn();
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
//					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = result
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (currNum == 0){
						list.clear();
						list.addAll(poiItems);
						adapter.notifyDataSetChanged();
						currNum ++ ;
					}
					if (poiItems != null && poiItems.size() > 0) {

						aMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
//						showSuggestCity(suggestionCities);
					} else {
//						ToastUtil.show(PoiKeywordSearchActivity.this,
//								R.string.no_result);
					}
				}
			} else {
//				ToastUtil.show(PoiKeywordSearchActivity.this,
//						R.string.no_result);
			}
		} else {
//			ToastUtil.showerror(this, rCode);
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(LocationSourceActivity.this, AddAddressActivity.class);
			intent.putExtra("adname","");
			setResult(0,intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				String addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				/*LogUtils.e("cai",addressName + result.getRegeocodeAddress().getAois().size() + result.getRegeocodeAddress().getPois().size());
				for (int i = 0; i < result.getRegeocodeAddress().getPois().size(); i++){
					LogUtils.e("cai",result.getRegeocodeAddress().getPois().get(i).getAdName());
					LogUtils.e("cai",result.getRegeocodeAddress().getPois().get(i).getSnippet());
				}*/
				list.clear();
				list.addAll(result.getRegeocodeAddress().getPois());
				adapter.notifyDataSetChanged();
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
