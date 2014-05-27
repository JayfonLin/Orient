package com.util;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TeamParcelable implements Parcelable{
	private List<TeamMemberParcelable> members;
	private String teamName;
	private int teamid;
	private int memNum;
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(teamName);
		dest.writeInt(teamid);
		dest.writeInt(memNum);
		dest.writeInt(members.size());
		for (int i = 0; i < members.size(); i++){
			dest.writeParcelable(members.get(i), 0);
		}
	}
	public static final Parcelable.Creator<TeamParcelable> CREATOR = new Creator<TeamParcelable>(){
		public TeamParcelable createFromParcel(Parcel source){
			ClassLoader classLoader = this.getClass().getClassLoader();
			TeamParcelable team = new TeamParcelable();
			team.teamName = source.readString();
			team.teamid = source.readInt();
			team.memNum = source.readInt();
			int length = source.readInt();
			List<TeamMemberParcelable> mems = new ArrayList<TeamMemberParcelable>();
			for (int i = 0; i < length; i++){
				mems.add((TeamMemberParcelable) source.readParcelable(classLoader));
			}
			team.members = mems;

			return team;
		}
		public TeamParcelable[] newArray(int size){
			return new TeamParcelable[size];
		}
	};
	public TeamParcelable(){
		members = new ArrayList<TeamMemberParcelable>();
	}
	public TeamParcelable(String pTeamName){
		teamName = pTeamName;
		members = new ArrayList<TeamMemberParcelable>();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public boolean addMember(int pAvatar, String pName){
		members.add(new TeamMemberParcelable(pName, pAvatar));
		return true;
	}
	
	public boolean addMember(TeamMemberParcelable member){
		members.add(member);
		return true;
	}
	
	public List<TeamMemberParcelable> getTeamMemberList(){
		return members;
	}
	public void setTeamMemberList(List<TeamMemberParcelable> pMembers){
		members = pMembers;
	}
	public int getTeamid() {
		return teamid;
	}
	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}
	public int getMemNum() {
		return memNum;
	}
	public void setMemNum(int memNum) {
		this.memNum = memNum;
	}
	
	
}
