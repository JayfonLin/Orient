package com.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.constant.Constant;

public class CreateRoom {
	static final String url = Constant.URL+"createroom";
	static final String LATITUDE = "latitude";
	static final String LONGTITUDE = "longtitude";
	private HttpClient client;
	
	public CreateRoom(HttpClient pClient){
		this.client = pClient;
		
	}
	
	class CreateRoomRunnable implements Runnable{

		@Override
		public void run() {
			HttpPost httpPost = new HttpPost(url);
	        HttpResponse response = null;
	        String xml = null;
	        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
	        //paramsList.add(new BasicNameValuePair(LATITUDE, String.valueOf(routeid)));
	        //paramsList.add(new BasicNameValuePair(LONGTITUDE, String.valueOf(routeid)));
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
	            response = client.execute(httpPost);
	            if (response.getStatusLine().getStatusCode() == 200){
	                xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	                Log.i("lin", xml);
	                /*Message msg = new Message();
	                msg.obj = parseXML(xml);
	                msg.what = Constant.NETWORK_SUCCESS_MESSAGE_TAG;
	                handler.sendMessage(msg);
	                Log.i("lin", xml);*/
	            }
	            else{
	            	Log.i("lin", "network status: "+response.getStatusLine().getStatusCode());
	            	xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	            	Log.i("lin", xml.toString());
	            	/*Message msg = new Message();
	                msg.what = Constant.NETWORK_FAILED_MESSAGE_TAG;
	                handler.sendMessage(msg);*/
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
			} /*catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/finally{
	        	
	        }
		}
		
	}
}
