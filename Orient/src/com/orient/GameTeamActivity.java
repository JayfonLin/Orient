package com.orient;

import java.util.ArrayList;
import java.util.HashMap;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
public class GameTeamActivity extends Activity {
	private ListView listView1;
	private ListView listView2;
	private ListView listView3;
	private ListView listView4;
	
	private Button button1;
	private Button button2;
	private ImageButton button3;
	private ImageButton button4;
	private ImageButton button5;
	private String roomNameString;
	private TextView roomNameTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_team);
    	//传递房间名字
        Intent intent = getIntent();
        roomNameString = intent.getStringExtra("roomName");
        roomNameTextView = (TextView)findViewById(R.id.room_name);
        if (roomNameString == null || roomNameString == "") {
        	 roomNameTextView.setText("维多利亚的宵夜");
        } else {
        	roomNameTextView.setText(roomNameString);
        }
        
        listView1 = (ListView) this.findViewById(R.id.team_one_listview);
        listView2 = (ListView) this.findViewById(R.id.team_two_listview);
        listView3 = (ListView) this.findViewById(R.id.team_three_listview);
        listView4 = (ListView) this.findViewById(R.id.team_four_listview);
        show();
        
        button1 = (Button)findViewById(R.id.back_home);
        button2 = (Button)findViewById(R.id.game_final_start);
        button3 = (ImageButton)findViewById(R.id.team_two_join);
        button4 = (ImageButton)findViewById(R.id.team_three_join);
        button5 = (ImageButton)findViewById(R.id.team_four_join);
        
       button1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(GameTeamActivity.this, HomeActivity.class);
			GameMap.context.finish();
			startActivity(intent);
			finish();
		}
	});
        
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("roomName", roomNameString);
				onBackPressed();
//				intent.setClass(GameTeamActivity.this, GameMap.class);
//				startActivity(intent);
				finish();
			}
		});
		
        
        button3.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GameTeamActivity.this);
				View view = LayoutInflater.from(GameTeamActivity.this).inflate(R.layout.prompt_team2,null);
				builder.setView(view);
				final AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				((Button)view.findViewById(R.id.prompt_team2_cancel)).setOnClickListener(new View.OnClickListener() {						
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				
				((Button)view.findViewById(R.id.prompt_team2_confirm)).setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
			}
		});
        
        button4.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GameTeamActivity.this);
				View view = LayoutInflater.from(GameTeamActivity.this).inflate(R.layout.prompt_team3,null);
				builder.setView(view);
				final AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				((Button)view.findViewById(R.id.prompt_team3_cancel)).setOnClickListener(new View.OnClickListener() {						
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				
				((Button)view.findViewById(R.id.prompt_team3_confirm)).setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
			}
		});
        
        button5.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GameTeamActivity.this);
				View view = LayoutInflater.from(GameTeamActivity.this).inflate(R.layout.prompt_team4,null);
				builder.setView(view);
				final AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				((Button)view.findViewById(R.id.prompt_team4_cancel)).setOnClickListener(new View.OnClickListener() {						
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				
				((Button)view.findViewById(R.id.prompt_team4_confirm)).setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
			}
		});
    }
    
    //在选项菜单中添加退出房间的按钮
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu mainMenu = menu;
		mainMenu.add(0, 1, Menu.NONE, "退出房间");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(GameTeamActivity.this);
		View view = LayoutInflater.from(GameTeamActivity.this).inflate(R.layout.prompt_exit,null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		
		((Button)view.findViewById(R.id.prompt_exit_cancel)).setOnClickListener(new View.OnClickListener() {						
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		
		((Button)view.findViewById(R.id.prompt_exit_comfirm)).setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GameTeamActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		return super.onOptionsItemSelected(item);  
	}
	
  //设置返回键返回主页
  	@Override
  	public boolean onKeyDown(int keyCode, KeyEvent event) {
  		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
  			Intent intent = new Intent();
  			intent.setClass(GameTeamActivity.this, HomeActivity.class);
  			startActivity(intent);
  			finish();
  		}
  		return super.onKeyDown(keyCode, event);
  	}
  	
    private void show() {
        String[] mFrom = new String[]{"game_img","game_name","game_pic"};
        int[] mTo = new int[]{R.id.game_img,R.id.game_final_name,R.id.game_pic};
        ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
        for ( int i = 0; i < 6; i++ ) {
        	HashMap<String,Object> mMap = new HashMap<String,Object>();
        	if ( i % 4 == 0) {
	        	mMap.put("game_img", R.drawable.pic_1);
	        	mMap.put("game_name", "Jackie");
	        	mMap.put("game_pic", R.drawable.teamleader);
        	} else if ( i % 4 == 1 ) {
        		mMap.put("game_img", R.drawable.pic_2);
	        	mMap.put("game_name", "Tom");
	        	mMap.put("game_pic", R.drawable.angel);
        	} else if ( i % 4 == 2 ) {
        		mMap.put("game_img", R.drawable.pic_3);
	        	mMap.put("game_name", "Catharine");
	        	mMap.put("game_pic", R.drawable.angel);
        	} else {
        		mMap.put("game_img", R.drawable.pic_1);
	        	mMap.put("game_name", "Obama");
	        	mMap.put("game_pic", R.drawable.angel);
        	}
        	mList.add(mMap);
        }
        SimpleAdapter mAdapter = new SimpleAdapter(this,mList,R.layout.game_item,mFrom,mTo);
        listView1.setAdapter(mAdapter);
        listView2.setAdapter(mAdapter);
        listView3.setAdapter(mAdapter);
        listView4.setAdapter(mAdapter);
    }
    
}
