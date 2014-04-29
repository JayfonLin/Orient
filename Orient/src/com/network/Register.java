﻿package com.network;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.constant.Constant;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class Register{
	static final String url = Constant.URL+"register";
	static final String USERNAME = "username", PASSWORD = "pw", 
			NICKNAME = "nickname", PHONENUMBER = "phone", 
			GENDER = "gender", PORTRAIT = "portrait";
	String userName, pw, nickName, phoneNumber, gender;
	private HttpClient client;
	Handler handler;
	public Register(Handler pHandler, HttpClient client){
		this.client = client;
		handler = pHandler;
	}
	public void check(String pUserName, String pPassWord, String pNickName,
			String pPhoneNumber, String pGender){
		userName = pUserName;
		pw = pPassWord;
		nickName = pNickName;
		phoneNumber = pPhoneNumber;
		gender = pGender;
		new Thread(new checkRunnable()).start();
	}
	private String parseXML(String stream) throws XmlPullParserException, IOException{
		String result = "failed";
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
					break;
				}
			}
			parseEvent = parser.next();
		}
		return result;
	}
	class checkRunnable implements Runnable{

		@Override
		public void run() {		
			HttpPost httpPost = new HttpPost(url);
	        HttpResponse response = null;
	        String xml = null;
	        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
	        paramsList.add(new BasicNameValuePair(USERNAME, userName));
	        paramsList.add(new BasicNameValuePair(PASSWORD, pw));
	        paramsList.add(new BasicNameValuePair(NICKNAME, nickName));
	        paramsList.add(new BasicNameValuePair(PHONENUMBER, phoneNumber));
	        paramsList.add(new BasicNameValuePair(GENDER, gender));
	        paramsList.add(new BasicNameValuePair(PORTRAIT, "1"));
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
	            response = client.execute(httpPost);
	            if (response.getStatusLine().getStatusCode() == 200){
	                xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	                Log.i("lin", xml);
	                Message msg = new Message();
	                msg.obj = parseXML(xml);
	                msg.what = Constant.NETWORK_SUCCESS_MESSAGE_TAG;
	                handler.sendMessage(msg);
	            }else{
	            	Log.i("lin", "network status: "+response.getStatusLine().getStatusCode());
	            	Message msg = new Message();
	                msg.what = Constant.NETWORK_FAILED_MESSAGE_TAG;
	                handler.sendMessage(msg);
	            }
	            
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	        	
	        }
		}
	}
}
