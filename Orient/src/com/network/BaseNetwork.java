package com.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.constant.Constant;

public abstract class BaseNetwork implements Runnable{
	protected String url;
	protected HttpClient client;
	protected List<NameValuePair> paramsList;
	protected Handler handler;
	
	public BaseNetwork(HttpClient pClient, Handler pHandler, String tailUrl){
		client = pClient;
		handler = pHandler;
		url = Constant.URL+tailUrl;
		paramsList = new ArrayList<NameValuePair>();
	}
	public BaseNetwork(HttpClient pClient, String tailUrl){
		this(pClient, new Handler(), tailUrl);
	}
	abstract void setParamsList();

	@Override
	public void run() {
		HttpPost httpPost = new HttpPost(url);
        HttpResponse response = null;
        String xml = null;
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200){
                xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                Log.i("lin", xml);
                Message msg = new Message();
                //msg.obj = parseXML(xml);
                msg.setData(parseXML(xml));
                msg.what = Constant.NETWORK_SUCCESS_MESSAGE_TAG;
                handler.sendMessage(msg);
            }
            else{
            	Log.i("lin", "network status: "+response.getStatusLine().getStatusCode());
            	xml = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            	Log.i("lin", xml.toString());
            	Message msg = new Message();
                msg.what = Constant.NETWORK_FAILED_MESSAGE_TAG;
                handler.sendMessage(msg);
            }
            
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}finally{
        	
        }
	}
	
	abstract protected Bundle parseXML(String stream) throws XmlPullParserException, IOException;
	
}
