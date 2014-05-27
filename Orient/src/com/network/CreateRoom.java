package com.network;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.os.Handler;

public class CreateRoom extends BaseNetwork{
	private int longitude, latitude, teammemmax, routeid;
	private String roomName, starttime, address;
	public CreateRoom(HttpClient pClient, Handler pHandler, int pLongitude, int pLatitude, 
			String pRoomName, int pTeamMem, String pTime, int pRouteId, String pAddress) {
		super(pClient, pHandler, "createroom");
		longitude = pLongitude;
		latitude = pLatitude;
		roomName = pRoomName;
		teammemmax = pTeamMem;
		starttime = pTime;
		routeid = pRouteId;
		address = pAddress;
		setParamsList();
	}
	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        paramsList.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        paramsList.add(new BasicNameValuePair("position", address));
        paramsList.add(new BasicNameValuePair("roomname", roomName));
        paramsList.add(new BasicNameValuePair("teammemmax", String.valueOf(teammemmax)));
        paramsList.add(new BasicNameValuePair("starttime", starttime));
        paramsList.add(new BasicNameValuePair("routeid", String.valueOf(routeid)));
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
					bundle.putString("status", parser.nextText());
				}
				else if (tag.equalsIgnoreCase("RoomId")){
					bundle.putString("RoomId", parser.nextText());
				}else if (tag.equalsIgnoreCase("info")){
					bundle.putString("info", parser.nextText());
				}
				break;
			}
			parseEvent = parser.next();
		}
		return bundle;
	}

}
