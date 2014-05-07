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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.os.Message;
import android.util.Log;

import com.constant.Constant;

public class CreateRoom {
	static final String url = Constant.URL+"createroom";
	static final String LATITUDE = "latitude";
	static final String LONGTITUDE = "longitude";
	static final String ROOM_NAME = "roomname";
	static final String TEAM_MEM_MAX = "teammemmax";
	static final String START_TIME = "starttime";
	private HttpClient client;
	private int longtitude, latitude, teammemmax;
	private String roomName, starttime;
	public CreateRoom(HttpClient pClient, int pLongtitude, int pLatitude, 
			String pRoomName, int pTeamMem, String pTime){
		this.client = pClient;
		longtitude = pLongtitude;
		latitude = pLatitude;
		roomName = pRoomName;
		teammemmax = pTeamMem;
		starttime = pTime;
		new Thread(new CreateRoomRunnable()).start();
	}
	
	class CreateRoomRunnable implements Runnable{

		@Override
		public void run() {
			HttpPost httpPost = new HttpPost(url);
	        HttpResponse response = null;
	        String xml = null;
	        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
	        paramsList.add(new BasicNameValuePair(LONGTITUDE, String.valueOf(longtitude)));
	        paramsList.add(new BasicNameValuePair(LATITUDE, String.valueOf(latitude)));
	        paramsList.add(new BasicNameValuePair(ROOM_NAME, roomName));
	        paramsList.add(new BasicNameValuePair(TEAM_MEM_MAX, String.valueOf(teammemmax)));
	        paramsList.add(new BasicNameValuePair(START_TIME, starttime));
	        try {
	            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
	            response = client.execute(httpPost);
	            if (response.getStatusLine().getStatusCode() == 200){
	                xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	                Log.i("lin", xml.toString());
	                Message msg = new Message();
	                msg.obj = parseXML(xml);
	                msg.what = Constant.NETWORK_SUCCESS_MESSAGE_TAG;
	                //handler.sendMessage(msg);
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
			}*/ catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	        	
	        }
		}
	}
	
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
				if (tag.equalsIgnoreCase("RoomId")){
					result = parser.nextText();
				}else if (tag.equalsIgnoreCase("info")){
					result = parser.nextText();
				}
				break;
			}
			parseEvent = parser.next();
		}
		return result;
	}
}
