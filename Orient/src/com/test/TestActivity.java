package com.test;

import com.network.AddTeam;
import com.network.CreateRoom;
import com.network.ExitRoom;
import com.network.GetRoomInfo;
import com.network.GetRoomList;
import com.network.PostPosition;
import com.orient.GlobalVarApplication;
import com.orient.R;
import com.orient.TeamInfoActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {
	Button btn;
	TextView tv;
	GlobalVarApplication gva; 
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		btn = (Button) findViewById(R.id.button1);
		tv = (TextView) findViewById(R.id.textView1);
		gva = (GlobalVarApplication)getApplication();
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//new CreateRoom(gva.httpClient, new Handler(), 0, 0, "team", 2, "2014-02-02 11:11:11");
				//GetRoomList g = new GetRoomList(gva.httpClient, new Handler());
				//Log.i("lin", gva.curRoomId+"");
				//new GetRoomInfo(gva.httpClient, handler, gva.curRoomId);
				//Intent intent = new Intent(TestActivity.this, TeamInfoActivity.class);
				//startActivity(intent);
				//AddTeam at = new AddTeam(gva.httpClient, handler, 2);
				//PostPosition p = new PostPosition(gva.httpClient, new Handler(), 23, 23);
				new ExitRoom(gva.httpClient, handler);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
