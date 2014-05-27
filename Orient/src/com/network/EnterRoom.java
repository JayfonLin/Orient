package com.network;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.os.Handler;

public class EnterRoom extends BaseNetwork{
	private int roomid;
	public EnterRoom(HttpClient pClient, Handler pHandler, int pRoomid) {
		super(pClient, pHandler, "enterroom");
		roomid = pRoomid;
		setParamsList();
	}

	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("RoomId", String.valueOf(roomid)));
	}

	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		Bundle bundle = new Bundle();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(new StringReader(stream));
		int parseEvent = parser.getEventType();
		while (parseEvent != XmlPullParser.END_DOCUMENT){
			switch(parseEvent){
			case XmlPullParser.START_TAG:
				String tag = parser.getName();
				if (tag.equalsIgnoreCase("status")){
					bundle.putString("status", parser.nextText());
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
