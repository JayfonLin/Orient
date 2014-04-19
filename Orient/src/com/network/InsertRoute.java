package com.network;

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
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.constant.Constant;
import com.orient.GlobalVarApplication;
import com.orient.setRouteOverlay;

public class InsertRoute {
	String routes = "";
	String url = Constant.URL+"insertroute";
	HttpClient client;
	
	private static final String ROUTE_STRING = "routeString";
	private Handler handler;
	public InsertRoute(setRouteOverlay overlay, Handler handler, HttpClient hc){
		if (handler == null){
			Log.i("lin", "handler is null");
		}
		this.handler = handler;
		for (int i = 0; i < overlay.size(); i++) {
			routes += overlay.getItem(i).getPoint().getLongitudeE6();
			routes += ",";
			routes += overlay.getItem(i).getPoint().getLatitudeE6();
			if (i != overlay.size()-1)
				routes += "\n";
		}
		Log.i("lin", routes);
		client = hc;
		new Thread(new InsertRouteRunnable()).start();
	}
	
	class InsertRouteRunnable implements Runnable{

		@Override
		public void run() {
			HttpPost httpPost = new HttpPost(url);
	        HttpResponse response = null;
	        String xml = null;
	        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
	        paramsList.add(new BasicNameValuePair(ROUTE_STRING, routes));
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
	            response = client.execute(httpPost);
	            if (response.getStatusLine().getStatusCode() == 200){
	                xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	                Message msg = new Message();
	                msg.obj = parseXML(xml);
	                msg.what = Constant.NETWORK_SUCCESS_MESSAGE_TAG;
	                handler.sendMessage(msg);
	            }
	            else{
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
	};
	
	private String parseXML(String stream) throws XmlPullParserException, IOException{
		String result = "nothing";
		XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = parserFactory.newPullParser();
		parser.setInput(new StringReader(stream));
		int parseEvent = parser.getEventType();
		while (parseEvent != XmlPullParser.END_DOCUMENT){
			switch(parseEvent){
			case XmlPullParser.START_TAG:
				String tag = parser.getName();
				if (tag.equalsIgnoreCase("info") || tag.equalsIgnoreCase("RouteId")){
					result = parser.nextText();
					break;
				}
			}
			parseEvent = parser.next();
		}
		return result;
	}
}
