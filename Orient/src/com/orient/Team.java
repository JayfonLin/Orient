package com.orient;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private List<TeamMemberInfo> members;
	private String teamName;
	
	public Team(){
		members = new ArrayList<TeamMemberInfo>();
	}
	public Team(String pTeamName){
		teamName = pTeamName;
		members = new ArrayList<TeamMemberInfo>();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public boolean addMember(int pAvatar, String pName){
		members.add(new TeamMemberInfo(pAvatar, pName));
		return true;
	}
	
	public boolean addMember(TeamMemberInfo member){
		members.add(member);
		return true;
	}
	
	public List<TeamMemberInfo> getTeamMemberList(){
		return members;
	}
}
