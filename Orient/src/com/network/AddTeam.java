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

public class AddTeam extends BaseNetwork{
	private int roomid;
	public AddTeam(HttpClient pClient, Handler pHandler, int pRoomId) {
		super(pClient, pHandler, "addteam");
		roomid = pRoomId;
		setParamsList();
	}

	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("roomid", String.valueOf(roomid)));
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
