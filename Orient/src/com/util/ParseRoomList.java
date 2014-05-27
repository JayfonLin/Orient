package com.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.network.GetRoomInfo;
import com.orient.GlobalVarApplication;

public class ParseRoomList {
	static private String safeNextText(XmlPullParser parser)  
	         throws XmlPullParserException, IOException {  
	     String result = parser.nextText();  
	     if (parser.getEventType() != XmlPullParser.END_TAG) {  
	         parser.nextTag();  
	     }  
	     return result;  
	 }
	
	static public List<Room> parseRoomList(String xml) throws XmlPullParserException, NumberFormatException, IOException{
		List<Room> rooms = new ArrayList<Room>();
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = parserFactory.newPullParser();
		parser.setInput(new StringReader(xml));
		int parseEvent = parser.getEventType();
		Room room = null;
		String tag;
		while (parseEvent != XmlPullParser.END_DOCUMENT){
			switch(parseEvent){
			case XmlPullParser.START_TAG:
				tag = parser.getName();
				if (tag.equalsIgnoreCase("Room")){
					room = new Room();
				}else if (tag.equalsIgnoreCase("RoomId")){
					if (room == null)
						room = new Room();
					room.setRoomid(Integer.parseInt(safeNextText(parser)));
				}else if (tag.equalsIgnoreCase("roomname")){
					if (room == null)
						room = new Room();
					room.setRoomName(safeNextText(parser));
				}else if (tag.equalsIgnoreCase("RouteId")){
					if (room == null)
						room = new Room();
					room.setRouteid(Integer.parseInt(safeNextText(parser)));
				}else if (tag.equalsIgnoreCase("teamnum")){
					if (room == null)
						room = new Room();
					room.setTeamNum(Integer.parseInt(safeNextText(parser)));
				}else if (tag.equalsIgnoreCase("starttime")){
					if (room == null)
						room = new Room();
					room.setTime(safeNextText(parser));
				}else if (tag.equalsIgnoreCase("founderid")){
					if (room == null)
						room = new Room();
					room.setFounderid(safeNextText(parser));
				}else if (tag.equalsIgnoreCase("foundername")){
					if (room == null)
						room = new Room();
					room.setFounderName(safeNextText(parser));
				}else if (tag.equalsIgnoreCase("position")){
					if (room == null)
						room = new Room();
					room.setAddress(safeNextText(parser));
				}else if (tag.equalsIgnoreCase("distance")){
					if (room == null)
						room = new Room();
					room.setDistance(safeNextText(parser)+"米");
				}else if (tag.equalsIgnoreCase("members")){
					if (room == null)
						room = new Room();
					room.setMembers(Integer.parseInt(safeNextText(parser)));
				}
				break;
			case XmlPullParser.END_TAG:
				tag = parser.getName();
				if (tag.equalsIgnoreCase("Room")){
					rooms.add(room);
					room = null;
				}
				break;
			default:
				break;
			}
			parseEvent = parser.next();
			
		}
		return rooms;
	} 
	/*static public List<Room> parseRooms(List<Integer> pRoomids){
		List<Room> rooms = new ArrayList<Room>();

		for (int i = 0; i < pRoomids.size(); i++){
			GetRoomInfo gri = new GetRoomInfo(pClient, pRoomId);
		}
	}*/
}
