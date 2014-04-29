
package com.orient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class gettogetherOverlay extends ItemizedOverlay<OverlayItem> {  
    //用MapView构造ItemizedOverlay  
    public gettogetherOverlay(Drawable mark,MapView mapView){  
            super(mark,mapView);  
    }  
    protected boolean onTap(final int index) {  
        //在此处理item点击事件  
    	
//    	new AlertDialog.Builder(Room_Second_1_2.context.getApplicationContext()).setItems(new String[]{"删除起始点","取消"}, new DialogInterface.OnClickListener() {
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
                //在此处理MapView的点击事件，当返回 true时  
        		//Toast.makeText(Room_Second_1_2.context, pt.toString(), 3000).show();
                super.onTap(pt,mapView);  
                return false;  
        }  
         
}          
