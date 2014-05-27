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

public class Register extends BaseNetwork{
	String userName, pw, nickName, phoneNumber, gender;
	public Register(HttpClient pClient, Handler pHandler, String pUserName, 
			String pPassWord, String pNickName, String pPhoneNumber, String pGender) {
		super(pClient, pHandler, "register");
		userName = pUserName;
		pw = pPassWord;
		nickName = pNickName;
		phoneNumber = pPhoneNumber;
		gender = pGender;
		setParamsList();
	}
	@Override
	void setParamsList() {
		paramsList.add(new BasicNameValuePair("username", userName));
        paramsList.add(new BasicNameValuePair("pw", pw));
        paramsList.add(new BasicNameValuePair("nickname", nickName));
        paramsList.add(new BasicNameValuePair("phone", phoneNumber));
        paramsList.add(new BasicNameValuePair("gender", gender));
        paramsList.add(new BasicNameValuePair("portrait", "1"));
	}
	@Override
	protected Bundle parseXML(String stream) throws XmlPullParserException,
			IOException {
		String result = "failed";
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
				break;
			}
			parseEvent = parser.next();
		}
		return bundle;
	}

}
