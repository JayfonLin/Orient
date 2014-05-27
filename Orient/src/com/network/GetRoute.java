package com.network;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;

public class GetRoute extends BaseNetwork{
	private int routeid;
	public GetRoute(HttpClient pClient, int routeid) {
		super(pClient, "getroute");
		this.routeid = routeid;
		setParamsList();
	}
	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("routeid", String.valueOf(routeid)));
	}
	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
