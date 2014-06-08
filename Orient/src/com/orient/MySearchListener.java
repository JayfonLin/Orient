package com.orient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

public class MySearchListener implements MKSearchListener {  
	private Handler handler;
	int i = 0;
	public MySearchListener(Handler pHandler){
		handler = pHandler;

	}
	public MySearchListener(){
		this(new Handler());
	}
	public boolean setHandler(Handler pHandler){
		handler = pHandler;
		Log.i("lin", "set Handler i: "+i++);
		return true;
	}
    @Override  
    public void onGetAddrResult(MKAddrInfo result, int iError) {  
           //返回地址信息搜索结果  
    	String myresult;
    	Message msg = new Message();
		Bundle b = new Bundle();
		Log.i("lin", "onGetAddrResult");
        if (result == null){
        	  myresult="nothing";
  	    	  b.putString("result", myresult);
  	    	  msg.setData(b);					
  	    	  handler.sendMessage(msg);
  	          return;
        }
    	if (iError != 0) {
    		String str = String.format("错误号：%d", iError);
    		Log.i("lin", str);
    		myresult= str;
	    	  b.putString("result", myresult);
	    	  msg.setData(b);					
	    	  handler.sendMessage(msg);
    		return;
    	}
    	myresult= "succeed";
  	    b.putString("result", myresult);
		String addr = result.strAddr;
		b.putString("address", addr);
		msg.setData(b);
		handler.sendMessage(msg);
    }  
    @Override  
    public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
            //返回驾乘路线搜索结果  
    }  
    @Override  
    public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
            //返回poi搜索结果  
    }  
    @Override  
    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
            //返回公交搜索结果  
    }  
    @Override  
    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
            //返回步行路线搜索结果  
    }  
    @Override      
    public void onGetBusDetailResult(MKBusLineResult result, int iError) {  
            //返回公交车详情信息搜索结果  
    }  
    @Override  
    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {  
            //返回联想词信息搜索结果  
    }
     @Override 
     public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {
           //在此处理短串请求返回结果. 
    }
	@Override
	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	} 
}

