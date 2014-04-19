package com.orient;

import com.baidu.location.r;
import com.constant.Constant;
import com.database.WriteSimpleInfoDB;
import com.network.Login;
import com.network.Register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText userNameEt, pwEt;
	private String userName, pw;
	private final Context context = this;
	Button loginButton;
	TextView registerButton;
	private long exitTime = 0;
	private ProgressDialog dialog;
	SharedPreferences sharePreferences;
	private final Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			dialog.cancel();
			switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
				if (msg.obj.equals("success")) {
					Editor editor = sharePreferences.edit();
					editor.putString(Constant.SHAREDPREFERENCE_KEY_USERNAME, userName);
					editor.putString(Constant.SHAREDPREFERENCE_KEY_PASSWORD, pw);
					editor.commit();
					Toast.makeText(context, "登录成功", 
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}else if (msg.obj.equals("not exists")){
					Toast.makeText(context, "用户名不存在", 
							Toast.LENGTH_LONG).show();
					userNameEt.requestFocus();
					userNameEt.selectAll();
				}else if (msg.obj.equals("failed")){
					Toast.makeText(context, "请重新登录", Toast.LENGTH_SHORT).show();
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(context, "网络连接有错，请稍后再试",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		
		}
	};
	  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		loginButton = (Button)findViewById(R.id.loginbtn);
		registerButton = (TextView)findViewById(R.id.register);
		userNameEt = (EditText)findViewById(R.id.username);
		pwEt = (EditText)findViewById(R.id.password);
		sharePreferences = getSharedPreferences(
				Constant.USER_SHAREDPREFERENCE, MODE_PRIVATE);
		userName = sharePreferences.getString(Constant.SHAREDPREFERENCE_KEY_USERNAME, "");
		pw = sharePreferences.getString(Constant.SHAREDPREFERENCE_KEY_PASSWORD, "");
		userNameEt.setText(userName);
		pwEt.setText(pw);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = userNameEt.getText().toString();
				pw = pwEt.getText().toString();
				GlobalVarApplication gva = (GlobalVarApplication)getApplication(); 
				new Login(mHandler, gva.httpClient).check(userName, pw);
				dialog.show();
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		dialog = new ProgressDialog(context);
        //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog标题
        dialog.setTitle("登录");
        //设置ProgressDialog提示信息
        dialog.setMessage("正在登录");
        //设置ProgressDialg的进度条是否不明确
        dialog.setIndeterminate(false);
        //设置ProgressDialog是否可以按退回键取消
        dialog.setCancelable(true);
        //设置ProgressDialog的一个Button
        dialog.setButton("返回", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface pDialog, int i){
        		//点击，取消对话框
        		dialog.cancel();
        		
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

	        if((System.currentTimeMillis()-exitTime) > 2000){
	          Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
	        	exitTime = System.currentTimeMillis();
	        	
	        }
	        else{
	            finish();
	            System.exit(0);
	        }
	
	        return true;
	    }
        return super.onKeyDown(keyCode, event);
	}
}
