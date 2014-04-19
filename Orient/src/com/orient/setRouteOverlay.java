package com.orient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class setRouteOverlay extends ItemizedOverlay<OverlayItem>{  
    //��MapView����ItemizedOverlay
	static Drawable mark;
	static MapView mapView;
    public setRouteOverlay(Drawable mark,MapView mapView){  
        super(mark,mapView);  
        this.mark = mark;
    	this.mapView = mapView;
    }  

    protected boolean onTap(final int index) {  
        //�ڴ˴���item����¼�  
    	OverlayItem tmp = getItem(index);
    	String[] string = tmp.getTitle().equals("mission")? new String[]{"ɾ���˹ؿ�","ȡ��"}:new String[]{"ɾ���ټ���","ȡ��"};
    	new AlertDialog.Builder(Room_Second_1_2.context).setItems(string, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:{
					if(getItem(index).getTitle().equals("gettogetherpos"))
						Room_Second_1_2.context.isgettogetherposset = false;
						removeItem(getItem(index));
						mMapView.refresh();
						break;
				}
				
				default:
					break;
				}
			}
		}).show();  
        return true;  
    } 
    public boolean onTap(GeoPoint pt, MapView mapView){  
            //�ڴ˴���MapView�ĵ���¼��������� trueʱ  
    		//Toast.makeText(Room_Second_1_2.context, pt.toString(), 3000).show();
            super.onTap(pt,mapView);  
            return false;  
    }
}          
