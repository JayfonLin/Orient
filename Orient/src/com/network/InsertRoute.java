package com.network;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.orient.setRouteOverlay;

import android.os.Bundle;
import android.os.Handler;

public class InsertRoute extends BaseNetwork{
	String routes = "";
	String address;
	public InsertRoute(HttpClient pClient, Handler pHandler, setRouteOverlay overlay, String pAddress) {
		super(pClient, pHandler, "insertroute");
		address = pAddress;
		for (int i = 0; i < overlay.size(); i++) {
			routes += overlay.getItem(i).getPoint().getLatitudeE6();
			routes += ",";
			routes += overlay.getItem(i).getPoint().getLongitudeE6();
			if (i != overlay.size()-1)
				routes += "\n";
		}
		setParamsList();
	}

	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("routeString", routes));
		paramsList.add(new BasicNameValuePair("position", address));
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
				}else if (tag.equalsIgnoreCase("RouteId")){
					bundle.putString("RouteId", parser.nextText());
				}
				break;
			}
			parseEvent = parser.next();
		}
		return bundle;
	}

}
