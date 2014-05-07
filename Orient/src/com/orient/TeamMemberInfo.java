package com.orient;

public class TeamMemberInfo {
	private int avatar;
	private String name;
	public TeamMemberInfo(int pAvatar, String pName){
		avatar = pAvatar;
		name = pName;
	}
	public TeamMemberInfo() {
		// TODO Auto-generated constructor stub
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
