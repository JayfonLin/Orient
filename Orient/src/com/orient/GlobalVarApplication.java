﻿package com.orient;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.constant.Constant;
import com.network.GetRoute;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class GlobalVarApplication extends Application{
	public HttpClient httpClient = null;
	public static int playRouteId = -1;
	public static int curRoomId = -1;
	public Context context = null;
	public Handler getRemoteIdHandler;
	private static MySearchListener msi = null; 
	private BMapManager mBMapMan = null; 
	private static MyLocationListener mll = null;
	//private static LocationClient locationClient= null;
	public setRouteOverlay uploadRoute = null;
	@Override
	public void onCreate() {
		context = this;
		httpClient = new DefaultHttpClient();
		msi = new MySearchListener();
		mBMapMan = new BMapManager(getApplicationContext());
		mBMapMan.init("Kbe7fy7M05PhdOboeeRkkibv", null);
		mll = new MyLocationListener();
		super.onCreate();
	}
	
	public MySearchListener getMKSearchListener(){
		return msi;
	}
	public BMapManager getBMapManager(){
		return mBMapMan;
	}
	/*public LocationClient getLocationClient(){
		return locationClient;
	}*/
	public MyLocationListener getMyLocationListener(){
		return mll;
	}

	@Override
	public void onTerminate() {
		mBMapMan.destroy();
		super.onTerminate();
	}
	
}
