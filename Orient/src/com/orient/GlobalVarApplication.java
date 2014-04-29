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
	public Context context = this;
	public Handler getRemoteIdHandler;
	public String insertRouteResponse = "no response";
	public setRouteOverlay uploadRoute = null;
	@Override
	public void onCreate() {
		httpClient = new DefaultHttpClient();
		getRemoteIdHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if (context == null){
					Log.i("lin", "context is null");
				}
               
				switch(msg.what){
				case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
					if (msg.obj.toString().equalsIgnoreCase("not login")){
						insertRouteResponse = "not login";
					}else if (msg.obj.toString().equalsIgnoreCase("point should be paired by lat and long")){
						insertRouteResponse = "point error";
					}else{
						insertRouteResponse = "upload success";
						
						Log.i("lin", "路线成功上传");
						playRouteId = Integer.parseInt(msg.obj.toString());
						Log.i("lin", "Remote Route Id: "+playRouteId);
						new GetRoute(httpClient, playRouteId);
					}
					break;
				case Constant.NETWORK_FAILED_MESSAGE_TAG:
					insertRouteResponse = "network error";
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		super.onCreate();
	}
	
}
