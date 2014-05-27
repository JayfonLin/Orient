package com.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TeamMemberParcelable implements Parcelable{
	private String userid;
	private String name;
	private com.util.Location location;
	private String ready;
	private int avatar;
	public TeamMemberParcelable(){
		this(null,0);
	}
	public TeamMemberParcelable(String pUserName, int pAvatar){
		location = new Location();
		avatar = pAvatar;
		location = new Location();
	}
	public TeamMemberParcelable(String pUserName){
		this(pUserName, 0);
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userid);
		dest.writeString(name);
		dest.writeString(ready);
		dest.writeInt(avatar);
		if (location != null){
			dest.writeInt(location.getLongitude());
			dest.writeInt(location.getLatitude());
		}else{
			dest.writeInt(0);
			dest.writeInt(0);
		}
	}
	public static final Parcelable.Creator<TeamMemberParcelable> CREATOR = new Creator<TeamMemberParcelable>(){
		public TeamMemberParcelable createFromParcel(Parcel source){
			TeamMemberParcelable member = new TeamMemberParcelable();
			member.userid = source.readString();
			member.name = source.readString();
			member.ready = source.readString();
			member.avatar = source.readInt();
			member.location.setLongitude(source.readInt());
			member.location.setLatitude(source.readInt());

			return member;
		}
		public TeamMemberParcelable[] newArray(int size){
			return new TeamMemberParcelable[size];
		}
	};
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getReady() {
		return ready;
	}
	public void setReady(String ready) {
		this.ready = ready;
	}
	public com.util.Location getLocation() {
		return location;
	}
	public void setLocation(com.util.Location location) {
		this.location = location;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
}
