package com.network;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.Handler;

public class CreateTeam extends BaseNetwork{

	public CreateTeam(HttpClient pClient, Handler pHandler) {
		super(pClient, pHandler, "createteam");
		setParamsList();
	}

	@Override
	void setParamsList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
