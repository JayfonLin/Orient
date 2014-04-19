package com.orient;


import com.network.GetRoute;
import com.network.InsertRoute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class Room_Second_2 extends Activity {	
	private ImageButton back, next;	
	private ImageButton inviteButton;
	private ImageButton backHomeImageButton;
	private String roomNameString;
	GlobalVarApplication gva; 
	private Handler inviteHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
		
	};
	int routeid;
	private Context context = this;
	private ProgressDialog dialog;
	private boolean reTry = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.room_second_2);
		gva = (GlobalVarApplication)getApplication();
		dialog = new ProgressDialog(context);
        //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog标题
        dialog.setTitle("邀请");
        //设置ProgressDialog提示信息
        dialog.setMessage("正在发送邀请");
        //设置ProgressDialg的进度条是否不明确
        dialog.setIndeterminate(false);
        //设置ProgressDialog是否可以按退回键取消
        dialog.setCancelable(true);
        //设置ProgressDialog的一个Button
        dialog.setButton("返回", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface pDialog, int i){
        		//点击，取消对话框
        		dialog.cancel();
        		inviteHandler.removeCallbacks(r);
        	}
        });
		//传递房间名字
		Intent intent = getIntent();
		roomNameString = intent.getStringExtra("roomName");
		routeid = intent.getIntExtra("Routeid", 0);
		
		back = (ImageButton) findViewById(R.id.Final_back);
		back.setOnClickListener(aClickListener);
		next = (ImageButton) findViewById(R.id.Final_invite);
		next.setOnClickListener(aClickListener);
		
		inviteButton = (ImageButton)findViewById(R.id.Final_invite);
		inviteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (reTry){
					reTry = false;
					new InsertRoute(gva.uploadRoute, gva.getRemoteIdHandler, gva.httpClient);
				}
				dialog.show();
				inviteHandler.post(r);
			}
		});
		
		backHomeImageButton = (ImageButton)findViewById(R.id.back_home);
		backHomeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Room_Second_2.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private Runnable r = new Runnable(){

		@Override
		public void run() {
			String response = gva.insertRouteResponse;
			if (response.equalsIgnoreCase("no response")){
				inviteHandler.postDelayed(r, 3000);
			}else if (response.equalsIgnoreCase("not login")){
				dialog.cancel();
				Toast.makeText(gva.context, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Room_Second_2.this, LoginActivity.class);
				startActivity(intent);
				SysApplication.getInstance().exit();
			}else if (response.equalsIgnoreCase("point error")){
				dialog.cancel();
				Toast.makeText(context, "关卡信息不全，请返回上层重新设置", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, Room_Second_1_2.class);
				startActivity(intent);
				SysApplication.getInstance().exit();
			}else if (response.equalsIgnoreCase("upload success")){
				dialog.cancel();
				Toast.makeText(context, "路线成功上传", Toast.LENGTH_SHORT).show();
				//invite();
				//getRemoteRouteById(gva.playRouteId);
				Intent intent = new Intent();
				intent.putExtra("roomName", roomNameString);
				intent.putExtra("Routeid", String.valueOf(routeid));
				intent.setClass(Room_Second_2.this, GameMap.class);
				startActivity(intent);
				SysApplication.getInstance().exit();
			}else if (response.equalsIgnoreCase("network error")){
				dialog.cancel();
				Toast.makeText(context, "网络信号差，请重新邀请", 
						Toast.LENGTH_LONG).show();
				reTry = true;
			}
		}
		
	};

	OnClickListener aClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.Final_back:
				CreateGroup.createGroup.back();
				break;
			case R.id.Final_invite:
				break;
			default:
				break;
			}		
		}
	};
	
	private void getRemoteRouteById(final int id){
		new GetRoute(gva.httpClient, id);
	}
	
}
