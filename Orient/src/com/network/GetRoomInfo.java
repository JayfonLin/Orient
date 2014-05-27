package com.network;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.util.Room;
import com.util.TeamMemberParcelable;
import com.util.TeamParcelable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

public class GetRoomInfo extends BaseNetwork{
	private int roomid;
	public GetRoomInfo(HttpClient pClient, Handler pHandler, int pRoomId) {
		super(pClient, pHandler, "getroominfo");
		this.roomid = pRoomId;
		setParamsList();
	}

	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("roomid", String.valueOf(roomid)));
	}
	static private String safeNextText(XmlPullParser parser)  
	         throws XmlPullParserException, IOException {  
	     String result = parser.nextText();  
	     if (parser.getEventType() != XmlPullParser.END_TAG) {  
	         parser.nextTag();  
	     }  
	     return result;  
	 }

	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		Bundle bundle = new Bundle();
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = parserFactory.newPullParser();
		parser.setInput(new StringReader(stream));
		int parseEvent = parser.getEventType();
		while (parseEvent != XmlPullParser.END_DOCUMENT){
			switch(parseEvent){
			case XmlPullParser.START_TAG:
				String tag = parser.getName();
				if (tag.equalsIgnoreCase("status")){
					bundle.putString("status", safeNextText(parser));
				}else if (tag.equalsIgnoreCase("info")){
					bundle.putString("status", safeNextText(parser));
				}else if (tag.equalsIgnoreCase("Room")){
					Room room = new Room();
					parseEvent = parser.next();
					while (!(parseEvent == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("Room"))){
						switch(parseEvent){
						case XmlPullParser.START_TAG:
							tag = parser.getName();
							System.out.println("tag in room: "+tag);
							if (tag.equalsIgnoreCase("RoomId"))
								room.setRoomid(Integer.parseInt(safeNextText(parser)));
							else if (tag.equalsIgnoreCase("roomname"))
								room.setRoomName(safeNextText(parser));
							else if (tag.equalsIgnoreCase("RouteId"))
								room.setRouteid(Integer.parseInt(safeNextText(parser)));
							else if (tag.equalsIgnoreCase("founderid"))
								room.setFounderid(safeNextText(parser));
							else if (tag.equalsIgnoreCase("foundername"))
								room.setFounderName(safeNextText(parser));
							else if (tag.equalsIgnoreCase("teammemmax"))
								room.setMaxMem(Integer.parseInt(safeNextText(parser)));
							else if (tag.equalsIgnoreCase("starttime"))
								room.setTime(safeNextText(parser));
							else if (tag.equalsIgnoreCase("teamnum"))
								room.setTeamNum(Integer.parseInt(safeNextText(parser)));
							else if (tag.equalsIgnoreCase("members"))
								room.setMembers(Integer.parseInt(safeNextText(parser)));
							else if (tag.equalsIgnoreCase("position"))
								room.setAddress(safeNextText(parser));
							else if (tag.equalsIgnoreCase("status"))
								room.setStatus(safeNextText(parser));
							break;
						default:
							break;
						}
						
						parseEvent = parser.next();
					}
					bundle.putParcelable("Room", room);
				}else if (tag.equalsIgnoreCase("Teamlist")){
					List<TeamParcelable> teams = new ArrayList<TeamParcelable>();
					parseEvent = parser.next();
					while (!(parseEvent == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("Teamlist"))){
						switch(parseEvent){
						case XmlPullParser.START_TAG:
							tag = parser.getName();
							if (tag.equalsIgnoreCase("Team")){
								TeamParcelable team = new TeamParcelable();
								parseEvent = parser.next();
								while (!(parseEvent == XmlPullParser.END_TAG && 
										parser.getName().equalsIgnoreCase("Team"))){
									switch(parseEvent){
									case XmlPullParser.START_TAG:
										tag = parser.getName();
										if (tag.equalsIgnoreCase("TeamId"))
											team.setTeamid(Integer.parseInt(safeNextText(parser)));
										else if (tag.equalsIgnoreCase("members"))
											team.setMemNum(Integer.parseInt(safeNextText(parser)));
										else if (tag.equalsIgnoreCase("teamname"))
											team.setTeamName(safeNextText(parser));
										else if (tag.equalsIgnoreCase("Teammemberlist")){
											List<TeamMemberParcelable> members = new ArrayList<TeamMemberParcelable>();
											parseEvent = parser.next();
											while (!(parseEvent == XmlPullParser.END_TAG 
													&& parser.getName().equalsIgnoreCase("Teammemberlist"))){
												switch(parseEvent){
												case XmlPullParser.START_TAG:
													tag = parser.getName();
													if (tag.equalsIgnoreCase("Teammember")){
														TeamMemberParcelable member = new TeamMemberParcelable();
														com.util.Location location = new com.util.Location();
														parseEvent = parser.next();
														while (!(parseEvent == XmlPullParser.END_TAG && 
																parser.getName().equalsIgnoreCase("Teammember"))){
															switch(parseEvent){
															case XmlPullParser.START_TAG:
																tag = parser.getName();
																if (tag.equalsIgnoreCase("userid"))
																	member.setUserid(parser.getText());
																else if (tag.equalsIgnoreCase("username"))
																	member.setName(parser.getText());
																else if (tag.equalsIgnoreCase("latitude"))
																	location.setLatitude(Integer.parseInt(safeNextText(parser)));
																else if (tag.equalsIgnoreCase("longitude"))
																	location.setLongitude(Integer.parseInt(safeNextText(parser)));
																else if (tag.equalsIgnoreCase("ready"))
																	member.setReady(safeNextText(parser));
																break;
															}
															parseEvent = parser.next();
														}
														member.setLocation(location);
														members.add(member);
													}
													break;
												}
												parseEvent = parser.next();
											}
											team.setTeamMemberList(members);
										}
										break;
									default:
										break;
									}
									parseEvent = parser.next();
								}
								teams.add(team);
							}
							break;
						default:
							break;
						}
						parseEvent = parser.next();
					}
					bundle.putParcelableArrayList("TeamList", (ArrayList<? extends Parcelable>) teams);
				}
				break;
			}
			parseEvent = parser.next();
		}
		return bundle;
	}

}
