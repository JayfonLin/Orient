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
import android.util.Log;

public class PostPosition extends BaseNetwork{
	int longitude, latitude;
	public PostPosition(HttpClient pClient, Handler pHandler, int pLongitude, int pLatitude) {
		super(pClient, pHandler, "postposition");
		longitude = pLongitude;
		latitude = pLatitude;
		setParamsList();
	}

	@Override
	void setParamsList() {
		Log.i("lin", "la: "+latitude);
		Log.i("lin", "lo: "+longitude);
		paramsList.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		paramsList.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
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
