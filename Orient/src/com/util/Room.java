package com.util;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;


public class Room implements Parcelable{
	private int roomid;
	private String roomName;
	private int maxMem;
	private String time;
	private int routeid;
	private com.util.Location location;
	private String founderid;
	private String founderName;
	private String address;
	private int teamNum;
	private int members;
	private String status;
	private List<TeamParcelable> teamList;
	private String distance;

	public Room(String pRoomName, int pMaxMem, String pTime)
	{
		roomName = pRoomName;
		maxMem = pMaxMem;
		time = pTime;
		location = new Location();
		teamList = new ArrayList<TeamParcelable>();
	}
	
	public Room(){
		this(null, 0, null);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(roomName);
		dest.writeInt(maxMem);
		dest.writeString(time);
		dest.writeInt(routeid);
		dest.writeInt(roomid);
		dest.writeString(founderid);
		dest.writeString(founderName);
		dest.writeString(address);
		dest.writeInt(teamNum);
		dest.writeInt(members);
		dest.writeString(status);
		dest.writeString(distance);
		if (location != null){
			dest.writeInt(location.getLongitude());
			dest.writeInt(location.getLatitude());
		}else{
			dest.writeInt(0);
			dest.writeInt(0);
		}
		dest.writeInt(teamList.size());
		for (int i = 0; i < teamList.size(); i++){
			dest.writeParcelable(teamList.get(i), 0);
		}
	}
	
	public static final Parcelable.Creator<Room> CREATOR = new Creator<Room>(){
		public Room createFromParcel(Parcel source){
			Room room = new Room();
			room.roomName = source.readString();
			room.maxMem = source.readInt();
			room.time = source.readString();
			room.routeid = source.readInt();
			room.roomid = source.readInt();
			room.founderid = source.readString();
			room.founderName = source.readString();
			room.address = source.readString();
			room.teamNum = source.readInt();
			room.members = source.readInt();
			room.status = source.readString();
			room.distance = source.readString();
			room.location.setLongitude(source.readInt());
			room.location.setLatitude(source.readInt());
			int teamSize = source.readInt();
			List<TeamParcelable> tl = new ArrayList<TeamParcelable>();
			for (int i = 0; i < teamSize; i++){
				tl.add((TeamParcelable) source.readParcelable(null));
			}
			room.setTeamList(tl);

			return room;
		}
		public Room[] newArray(int size){
			return new Room[size];
		}
	};

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getMaxMem() {
		return maxMem;
	}

	public void setMaxMem(int maxMem) {
		this.maxMem = maxMem;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getRouteid() {
		return routeid;
	}

	public void setRouteid(int routeid) {
		this.routeid = routeid;
	}

	public com.util.Location getLocation() {
		return location;
	}

	public void setLocation(com.util.Location location) {
		this.location = location;
	}

	public String getFounderid() {
		return founderid;
	}

	public void setFounderid(String founderid) {
		this.founderid = founderid;
	}

	public String getFounderName() {
		return founderName;
	}

	public void setFounderName(String founderName) {
		this.founderName = founderName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRoomid() {
		return roomid;
	}

	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}

	public int getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}

	public int getMembers() {
		return members;
	}

	public void setMembers(int members) {
		this.members = members;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TeamParcelable> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamParcelable> teamList) {
		this.teamList = teamList;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
