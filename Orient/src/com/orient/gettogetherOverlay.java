
package com.orient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class gettogetherOverlay extends ItemizedOverlay<OverlayItem> {  
    //��MapView����ItemizedOverlay  
    public gettogetherOverlay(Drawable mark,MapView mapView){  
            super(mark,mapView);  
    }  
    protected boolean onTap(final int index) {  
        //�ڴ˴���item����¼�  
    	
//    	new AlertDialog.Builder(Room_Second_1_2.context.getApplicationContext()).setItems(new String[]{"ɾ����ʼ��","ȡ��"}, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				switch (arg1) {
//				case 0:{
//						removeItem(getItem(index));
//						mMapView.refresh();
//						break;
//				}
//				
//				default:
//					break;
//				}
//			}
//		}).show();  
        return true;  
    }  
        public boolean onTap(GeoPoint pt, MapView mapView){  
                //�ڴ˴���MapView�ĵ���¼��������� trueʱ  
        		//Toast.makeText(Room_Second_1_2.context, pt.toString(), 3000).show();
                super.onTap(pt,mapView);  
                return false;  
        }  
         
}          
