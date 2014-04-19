package com.orient;

import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {
    @Override
   public void onReceiveLocation(BDLocation location) {
    	Message msg = new Message();
    	Bundle b = new Bundle();
    	String result;
      if (location == null){
	    	  result="nothing";
	    	  b.putString("result", result);
	    	  msg.setData(b);					
	      	  GameMap.context.handler.sendMessage(msg);
	          
      }
      else{
    	  result="location";
    	  b.putString("result", result);
    	  b.putDouble("latitude", location.getLatitude());
    	  b.putDouble("longitude", location.getLongitude());
    	  b.putFloat("direction",location.getDerect());
    	  
    	  msg.setData(b);					
    	  GameMap.context.handler.sendMessage(msg);
      }
      
    }
public void onReceivePoi(BDLocation poiLocation) {
         if (poiLocation == null){
                return ;
          }
}
		

}
