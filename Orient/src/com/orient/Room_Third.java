package com.orient;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Button;;

public class Room_Third extends FragmentActivity implements OnTouchListener {
	ImageButton joinButton;
    @Override
    protected void onCreate(Bundle arg0) {
    	// TODO 自动生成的方法存根
    	super.onCreate(arg0);
    	setContentView(R.layout.room_third);
    	
    	joinButton = (ImageButton)findViewById(R.id.room_third_imageButton);
        joinButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//先提示，确定后加入房间
				AlertDialog.Builder builder = new AlertDialog.Builder(Room_Third.this.getParent());
				View view = LayoutInflater.from(Room_Third.this.getParent()).inflate(R.layout.prompt,null);
				builder.setView(view);
				final AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				((Button)view.findViewById(R.id.prompt_cancel)).setOnClickListener(new View.OnClickListener() {						
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				
				((Button)view.findViewById(R.id.prompt_confirm)).setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						//跳转到主页
						Intent intent = new Intent();
						intent.setClass(Room_Third.this, GameTeamActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		});
    }
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}