package com.network;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.HttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class GetRoomList extends BaseNetwork{

	public GetRoomList(HttpClient pClient, Handler pHandler) {
		super(pClient, pHandler, "getroomlist");
		setParamsList();
	}

	@Override
	void setParamsList() {
		
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
				}else if (tag.equalsIgnoreCase("Roomlist")){
					bundle.putString("Roomlist", stream);
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
