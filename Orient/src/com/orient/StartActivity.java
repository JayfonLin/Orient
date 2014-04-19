package com.orient;

import com.constant.Constant;
import com.database.MemberDAO;
import com.network.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

public class StartActivity extends Activity{
	MemberDAO memberDAO;
    SQLiteDatabase db;
    String username;
    String pw;
    private Context context = this;
    private ProgressDialog dialog;
    private final Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			//dialog.cancel();
			switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG: 
				if (msg.obj.equals("success")) {
					Toast.makeText(context, "登录成功", 
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(StartActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
				else if (msg.obj.equals("not exists")){
					Toast.makeText(context, "用户名不存在", 
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setClass(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}else if (msg.obj.equals("failed")){
					Toast.makeText(context, "请重新登录", 
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setClass(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(context, "网络连接有错，请稍后再试",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences sharePreferences = getSharedPreferences(
				Constant.USER_SHAREDPREFERENCE, MODE_PRIVATE);
		username = sharePreferences.getString(Constant.SHAREDPREFERENCE_KEY_USERNAME, "");
		pw = sharePreferences.getString(Constant.SHAREDPREFERENCE_KEY_PASSWORD, "");
		GlobalVarApplication gva = (GlobalVarApplication)getApplication();
        if (!username.equals("") && !pw.equals("")){
        	new Login(mHandler, gva.httpClient).check(username, pw);
        }else {
        	Intent intent = new Intent();
			intent.setClass(StartActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
        }
        
	}
	
}
