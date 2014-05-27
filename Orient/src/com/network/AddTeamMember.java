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

public class AddTeamMember extends BaseNetwork{
	private int roomid, teamid, userid;
	public AddTeamMember(HttpClient pClient, Handler pHandler) {
		super(pClient, pHandler, "addteammember");
		setParamsList();
	}
	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("roomid", String.valueOf(roomid)));
		paramsList.add(new BasicNameValuePair("teamid", String.valueOf(teamid)));
		paramsList.add(new BasicNameValuePair("userid", String.valueOf(userid)));
	}
	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		String result = "nothing";
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
					result = parser.nextText();
				}else if (tag.equalsIgnoreCase("info")){
					result = parser.nextText();
				}
				break;
			}
			parseEvent = parser.next();
		}
		return bundle;
	}

}
