package com.util;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.orient.setRouteOverlay;

public class RouteParcelable implements Parcelable{
	private setRouteOverlay overlay;
	public RouteParcelable(){}
	public RouteParcelable(setRouteOverlay pOverlay) {
		overlay = pOverlay;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		int length = overlay.size();
		dest.writeInt(length);
		for (int i = 0; i < length; i++) {
			dest.writeInt(overlay.getItem(i).getPoint().getLatitudeE6());
			dest.writeInt(overlay.getItem(i).getPoint().getLongitudeE6());
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
