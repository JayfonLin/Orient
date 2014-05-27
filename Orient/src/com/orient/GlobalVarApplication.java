package com.orient;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

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
	public HttpClient httpClient;
	public static int playRouteId = -1;
	public static int curRoomId = -1;
	public Context context = this;
	public Handler getRemoteIdHandler;
	
	public setRouteOverlay uploadRoute = null;
	@Override
	public void onCreate() {
		httpClient = new DefaultHttpClient();
		
		super.onCreate();
	}
	
}
