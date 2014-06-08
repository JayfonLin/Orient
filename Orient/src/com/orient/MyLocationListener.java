package com.orient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {
	private Handler handler;
	public MyLocationListener(Handler pHandler){
		handler = pHandler;
	}
	public MyLocationListener(){
		this(new Handler());
	}
    @Override
   public void onReceiveLocation(BDLocation location) {
    	Message msg = new Message();
    	Bundle b = new Bundle();
    	String result;
      if (location == null){
	    	  result="nothing";
	    	  b.putString("result", result);
	    	  msg.setData(b);					
	    	  handler.sendMessage(msg);
	          
      }
      else{
    	  result="location";
    	  b.putString("result", result);
    	  b.putDouble("latitude", location.getLatitude());
    	  b.putDouble("longitude", location.getLongitude());
    	  b.putFloat("direction",location.getDerect());
    	  if (location.hasAddr())
    		  b.putString("address", location.getStreet());
    	  else
    		  b.putString("address", "地址未知");
    	  msg.setData(b);	
    	  handler.sendMessage(msg);
    	  //GameMap.context.handler.sendMessage(msg);
      }
      
    }
    public boolean setHandler(Handler pHandler){
    	handler = pHandler;
    	return true;
    }
public void onReceivePoi(BDLocation poiLocation) {
         if (poiLocation == null){
                return ;
          }
}
		

}
