package com.orient;
import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class GameMissionOverlay extends ItemizedOverlay<OverlayItem> {  
    //��MapView����ItemizedOverlay  
    public GameMissionOverlay(Drawable mark,MapView mapView){  
            super(mark,mapView);  
    }  
    protected boolean onTap(final int index) {  
        //�ڴ˴���item����¼�  
    	final OverlayItem item = getItem(index);
    	//Toast.makeText(GameMap.context, item.getTitle(), 2000).show();
    	if(item.getTitle()=="mission"){
    		GeoPoint missionpos = item.getPoint();
    		GeoPoint mypos = GameMap.context.myposGeoPoint;
    		
    		
    		if(mypos!=null&&GetShortDistance(mypos.getLatitudeE6()/1e6,
    				mypos.getLongitudeE6()/1e6,
    				missionpos.getLatitudeE6()/1e6,
    				missionpos.getLongitudeE6()/1e6)>=300.0)
    		{
    			Toast.makeText(GameMap.context, "��û�ߵ�����Ŷ�������ͣ�", 1000).show();
    			return false;
    		}
    		
    		
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(GameMap.context);
			View view = LayoutInflater.from(GameMap.context).inflate(R.layout.misson,null);
			
			builder.setView(view);
			final AlertDialog alertDialog = builder.create();
			ImageButton imageButton = (ImageButton)(view.findViewById(R.id.misson_button));
			imageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					item.setMarker(GameMap.context.getResources().getDrawable(R.drawable.checkedmission));
					item.setTitle("checkedmission");
					updateItem(item);
					--GameMap.context.missionnum;
					GameMap.context.mMapView.refresh();
					System.out.println("checked!!!!!!");
					int percent=(GameMap.context.missionList.size()-1-GameMap.context.missionnum)*100/(GameMap.context.missionList.size()-1);
					Bundle bundle = new Bundle();
					bundle.putInt("percent", percent);
					Message msg = new Message();
					msg.setData(bundle);
					GameMap.context.mHandler.sendMessage(msg);
						if(GameMap.context.missionnum==0){
							
							System.out.println("������������������������");
							new AlertDialog.Builder(GameMap.context).setMessage("������ɣ�").setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									Intent intent = new Intent();
									intent.setClass(GameMap.context, Game_Final.class);
									GameMap.context.startActivity(intent);
									GameMap.context.finish();
								}
							}).show();
						}
					
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
    	}
        return true;  
    }  
    
  //�������
  		static double DEF_PI = 3.14159265359; // PI
  		static double DEF_2PI= 6.28318530712; // 2*PI
  		static double DEF_PI180= 0.01745329252; // PI/180.0
  		static double DEF_R =6370693.5; // radius of earth
  		public double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
  		{
  			double ew1, ns1, ew2, ns2;
  			double dx, dy, dew;
  			double distance;
  			// �Ƕ�ת��Ϊ����
  			ew1 = lon1 * DEF_PI180;
  			ns1 = lat1 * DEF_PI180;
  			ew2 = lon2 * DEF_PI180;
  			ns2 = lat2 * DEF_PI180;
  			// ���Ȳ�
  			dew = ew1 - ew2;
  			// ���綫��������180 �ȣ����е���
  			if (dew > DEF_PI)
  			dew = DEF_2PI - dew;
  			else if (dew < -DEF_PI)
  			dew = DEF_2PI + dew;
  			dx = DEF_R * Math.cos(ns1) * dew; // �������򳤶�(��γ��Ȧ�ϵ�ͶӰ����)
  			dy = DEF_R * (ns1 - ns2); // �ϱ����򳤶�(�ھ���Ȧ�ϵ�ͶӰ����)
  			// ���ɶ�����б�߳�
  			distance = Math.sqrt(dx * dx + dy * dy);
  			return distance;
  		}
  		public double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
  		{
  			double ew1, ns1, ew2, ns2;
  			double distance;
  			// �Ƕ�ת��Ϊ����
  			ew1 = lon1 * DEF_PI180;
  			ns1 = lat1 * DEF_PI180;
  			ew2 = lon2 * DEF_PI180;
  			ns2 = lat2 * DEF_PI180;
  			// ���Բ�ӻ����������еĽ�(����)
  			distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
  			// ������[-1..1]��Χ�ڣ��������
  			if (distance > 1.0)
  			     distance = 1.0;
  			else if (distance < -1.0)
  			      distance = -1.0;
  			// ���Բ�ӻ�����
  			distance = DEF_R * Math.acos(distance);
  			return distance;
  		}
    
    
    
//        public boolean onTap(GeoPoint pt, MapView mapView){  
//                //�ڴ˴���MapView�ĵ���¼��������� trueʱ  
//        		//Toast.makeText(Room_Second_1_2.context, pt.toString(), 3000).show();
//                super.onTap(pt,mapView);  
//                return false;  
//        }  
         
}          
