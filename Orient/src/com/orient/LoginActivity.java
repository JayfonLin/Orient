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
					Toast.makeText(context, "��¼�ɹ�", 
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}else if (msg.obj.equals("not exists")){
					Toast.makeText(context, "�û���������", 
							Toast.LENGTH_LONG).show();
					userNameEt.requestFocus();
					userNameEt.selectAll();
				}else if (msg.obj.equals("failed")){
					Toast.makeText(context, "�����µ�¼", Toast.LENGTH_SHORT).show();
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(context, "���������д����Ժ�����",
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
        //���ý�������񣬷��ΪԲ�Σ���ת��
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //����ProgressDialog����
        dialog.setTitle("��¼");
        //����ProgressDialog��ʾ��Ϣ
        dialog.setMessage("���ڵ�¼");
        //����ProgressDialg�Ľ������Ƿ���ȷ
        dialog.setIndeterminate(false);
        //����ProgressDialog�Ƿ���԰��˻ؼ�ȡ��
        dialog.setCancelable(true);
        //����ProgressDialog��һ��Button
        dialog.setButton("����", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface pDialog, int i){
        		//�����ȡ���Ի���
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
	          Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
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
