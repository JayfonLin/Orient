
package com.orient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class gettogetherOverlay extends ItemizedOverlay<OverlayItem> implements Parcelable{  
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
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			int length = this.size();
			dest.writeInt(length);
			for (int i = 0; i < length; i++) {
				dest.writeInt(this.getItem(i).getPoint().getLatitudeE6());
				dest.writeInt(this.getItem(i).getPoint().getLongitudeE6());
			}
		}  
		
		public static final Parcelable.Creator<setRouteOverlay> CREATOR = new Creator<setRouteOverlay>(){
			public setRouteOverlay createFromParcel(Parcel source){
				setRouteOverlay route = new setRouteOverlay();
				int length = source.readInt();
				for (int i = 0; i < length; i++){
					GeoPoint point = new GeoPoint(source.readInt(), source.readInt()); //纬度，经度
					OverlayItem missionItem;
					if (0 == i){
						missionItem = new OverlayItem(point, "gettogetherpos", "gettogetherpos");
					}
					missionItem = new OverlayItem(point, "mission", "mission");
					route.addItem(missionItem);
				}
				
				return route;
			}
			public setRouteOverlay[] newArray(int size){
				return new setRouteOverlay[size];
			}
		};
         
}          
