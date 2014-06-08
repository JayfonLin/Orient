package com.util;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.orient.GameMap;
import com.orient.MyLocationListener;
import com.orient.MySearchListener;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

public class Location {
	private int longitude;
	private int latitude;
	private MKSearch mMKSearch = null; 
	private LocationClient mLocationClient = null;
	private MyLocationListener mll = null;
	public Location(){
		
	}
	
	public Location(int pLongitude, int pLatitude){
		longitude = pLongitude;
		latitude = pLatitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	
    public void positioning(Context context, Handler handler, MyLocationListener pMll, boolean once){
    	mll = pMll;
    	mll.setHandler(handler);
		if (mLocationClient == null){
			mLocationClient = new LocationClient(context);     //声明LocationClient类
			mLocationClient.setAK("90ehkP9tBULpKYG8rbwXffjG");
		    mLocationClient.registerLocationListener( mll );    //注册监听函数

		    LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);
			option.setAddrType("all");//返回的定位结果包含地址信息
			option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
			option.disableCache(false);//禁止启用缓存定位
			//option.setPoiNumber(5);    //最多返回POI个数   
			//option.setPoiDistance(1000); //poi查询距离        
			//option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息 
			
			mLocationClient.setLocOption(option);
			mLocationClient.start();
		}
		

		ContentResolver resolver = context.getContentResolver();
		Boolean on =Settings.Secure.isLocationProviderEnabled(resolver,LocationManager.GPS_PROVIDER);
		
		if(on){
			if(mLocationClient != null && mLocationClient.isStarted())
				mLocationClient.requestLocation();
			else 
				Toast.makeText(context,"在线请求失败，没开GPS？", 2000).show();
		}else{
			if (mLocationClient != null && mLocationClient.isStarted())
				mLocationClient.requestOfflineLocation();
		}
		if (once){
			mLocationClient.stop();
		}

	} 
	
     public void reverseGeocode(Context context, Handler pHandler, int pLongitude,
    		 int pLatitude, MySearchListener pMsl, BMapManager pBMapMan){
		if (mMKSearch == null)
			mMKSearch = new MKSearch();
		pMsl.setHandler(pHandler);
		mMKSearch.init(pBMapMan, pMsl);//注意，MKSearchListener只支持一个，以最后一次设置为准
		mMKSearch.reverseGeocode(new GeoPoint(pLatitude, pLongitude)); //逆地址解析
		//mMKSearch.geocode(key, city);//地址解析
		
	}
     
     public void destroy(){
    	 if (mLocationClient != null){
    		 if (mLocationClient.isStarted()){
    			 mLocationClient.stop();
    		 }
    		 mLocationClient.unRegisterLocationListener(mll);
    		 mLocationClient = null;
    	 }
    	 if (mMKSearch != null){
    		 mMKSearch.destory();
    		 mMKSearch = null;
    	 }
    	 
     }
}
