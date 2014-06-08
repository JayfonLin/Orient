package com.orient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.constant.Constant;
import com.network.ChangeTeamName;
import com.network.EnterRoom;
import com.network.ExitRoom;
import com.network.GetRoomInfo;
import com.util.Room;
import com.util.TeamMemberParcelable;
import com.util.TeamParcelable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RoomNew extends Activity {
	private TextView roomNameTextView;
	private TextView startPointTextView;
	private TextView startTimeTextView;
	private String roomNameString;
	private String startPointString;
	private String startTimeString;
	private String maxMem;
	private ImageButton backHomeImageButton;
	private Button startGameButton;
	private RelativeLayout teamInfoLayout;
	private Button exitRoomBtn, createTeamButton, readyButton;
	private Room room;
	private ProgressDialog dialog;
	GlobalVarApplication gva;
	

	GridView myteam_gv;
	ListView otherteams_lv;
	LinearLayout myteam_ll;
	TeamParcelable myteam;
	TextView myteam_tv, maxMemTv;
	EditText myteam_et;
	List<TeamParcelable> otherTeams;
	ImageView edit_iv;
	boolean isEditTeamName;
	InputMethodManager imm;
	ImageView backImageView;
	List<TeamParcelable> teamList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init();
        updateUI();
        setData();
        enterRoom();
        updataMyTeam();
		updateOtherTeam();

		setOnClicks();
		       	
    }
	//设置返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			Intent intent = new Intent();
			intent.setClass(RoomNew.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.room_new);
        dialog = new ProgressDialog(this);
      //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog标题
        dialog.setTitle("房间");
        //设置ProgressDialog提示信息
        dialog.setMessage("正在处理");
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
        gva = (GlobalVarApplication)getApplication(); 
      //传递参数，最终只需传递房间id
        Intent intent = getIntent();
        room = (Room)intent.getParcelableExtra("com.util.Room");
        if (room == null){
        	System.out.println("room in null");
        	room = new Room();
        }
        
        myteam_gv = (GridView) findViewById(R.id.myteam_gridView);
		myteam_ll = (LinearLayout) findViewById(R.id.myteam_linearLayout);
		myteam_ll.setVisibility(View.VISIBLE);
		otherteams_lv = (ListView) findViewById(R.id.other_teams_listView);
		myteam_tv = (TextView) myteam_ll.findViewById(R.id.myteam_name_tv);
		myteam_et = (EditText) myteam_ll.findViewById(R.id.myteam_name_et);
		edit_iv = (ImageView) findViewById(R.id.edit_teamName);
		maxMemTv = (TextView) findViewById(R.id.max_member_tv);
		otherTeams = new ArrayList<TeamParcelable>();
		isEditTeamName = false;
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        
        
        
	}
	private boolean enterRoom(){
		dialog.setMessage("正在加入房间...");
		
		Log.i("lin", "cur room id : "+gva.curRoomId);
		if (gva.curRoomId == -1){
			dialog.show();
			EnterRoom enterroom = new EnterRoom(gva.httpClient, enterRoomHandler,room.getRoomid());
			new Thread(enterroom).start();
		}else if (gva.curRoomId != -1 && gva.curRoomId !=  room.getRoomid()){
			dialog.show();
			ExitRoom er = new ExitRoom(gva.httpClient, exitEnterRoomHandler);
			new Thread(er).start();
		}
		return true;
	}
	private void setData(){
		myteam = new TeamParcelable("我的队伍名");
		
		for (int i = 0; i < 10; i++){
			int avatar = R.drawable.pic_1;
			TeamMemberParcelable member = new TeamMemberParcelable("队员"+i, avatar);
			myteam.addMember(member);
		}
		for (int i = 0; i < 3; i++){
			TeamParcelable team = new TeamParcelable("队伍"+i);
			int avatar2 = R.drawable.pic_2;
			for (int j = 0; j < 5; j++){
				TeamMemberParcelable member = new TeamMemberParcelable("队员"+j, avatar2);
				team.addMember(member);
			}
			otherTeams.add(team);
		}
	}
	private void updateUI(){
		roomNameString = room.getRoomName();
		roomNameTextView = (TextView)findViewById(R.id.room_new_room_name);
       	roomNameTextView.setText(roomNameString);
       	String distance = "距离未知";
       	if (room.getDistance() != null){
       		distance = room.getDistance();
       	}
       	startPointString = room.getAddress()+" "+distance;
       	startPointTextView = (TextView)findViewById(R.id.room_new_start_point);
       	startPointTextView.setText(startPointString);
       	startTimeString = room.getTime(); 
       	startTimeTextView = (TextView)findViewById(R.id.room_new_start_time);
       	startTimeTextView.setText(startTimeString);
       	
       	//返回主页面
       	backHomeImageButton = (ImageButton)findViewById(R.id.room_new_back_button);
       	backHomeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RoomNew.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
       	
       	exitRoomBtn = (Button)findViewById(R.id.exit_room_button);
       	exitRoomBtn.requestFocus();
       	if (room != null && room.getRoomid() == gva.curRoomId){
       		exitRoomBtn.setText("退出房间");
       		exitRoomBtn.setBackgroundColor(Color.rgb(253, 82, 73));
       	}else {
       		exitRoomBtn.setText("加入房间");
       		exitRoomBtn.setBackgroundColor(Color.rgb(0, 187, 51));
       	}
       	exitRoomBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button)v;
				String buttonText = b.getText().toString();
				if (buttonText == "加入房间") {
					if (gva.curRoomId == -1){
						dialog.setMessage("正在加入房间...");
						dialog.show();
						EnterRoom enterroom = new EnterRoom(gva.httpClient, enterRoomHandler,room.getRoomid());
						new Thread(enterroom).start();
					}
/*					exitRoomBtn.setText("退出房间");
					exitRoomBtn.setBackgroundColor(Color.rgb(253, 82, 73));*/
				} else {
					if (gva.curRoomId != -1){
						dialog.show();
						dialog.setMessage("正在退出房间...");
						ExitRoom exitroom = new ExitRoom(gva.httpClient, exitRoomHandler);
						new Thread(exitroom).start();
					}
				}
			}
		});
       	
       	
       	
       	/*//进入游戏开始的界面
       	startGameButton = (Button)findViewById(R.id.room_new_start_game);
       	startGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RoomNew.this, GameMap.class);
				//传递参数
				intent.putExtra("Routeid", routeidString);
				intent.putExtra("roomName", roomNameString);
				intent.putExtra("point", startPointString);
				intent.putExtra("time", startTimeString);
				intent.putExtra("maxMem", maxMem);
				startActivity(intent);
				finish();
			}
		});
       	
       	//进入队伍信息界面
       	teamInfoLayout = (RelativeLayout)findViewById(R.id.room_info_layout);
       	teamInfoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RoomNew.this, TeamInfoActivity.class);
				intent.putExtra("roomid", room.getRoomid());
				startActivity(intent);
				finish();
				
			}
		});
       	
       	//让“加入房间”的按钮点击之后变成“退出房间”，反之亦然
       	exitRoomBtn = (Button)findViewById(R.id.room_new_button);
       	if (room != null && room.getRoomid() == gva.curRoomId){
       		exitRoomBtn.setText("退出房间");
       		exitRoomBtn.setBackgroundColor(Color.rgb(253, 82, 73));
       	}else {
       		exitRoomBtn.setText("加入房间");
       		exitRoomBtn.setBackgroundColor(Color.rgb(0, 187, 51));
       	}
       	exitRoomBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button)v;
				String buttonText = b.getText().toString();
				if (buttonText == "加入房间") {
					dialog.setMessage("正在加入房间...");
					dialog.show();
					EnterRoom enterroom = new EnterRoom(gva.httpClient, enterRoomHandler,room.getRoomid());
					new Thread(enterroom).start();
					exitRoomBtn.setText("退出房间");
					exitRoomBtn.setBackgroundColor(Color.rgb(253, 82, 73));
				} else {
					if (gva.curRoomId != -1){
						dialog.show();
						dialog.setMessage("正在退出房间...");
						ExitRoom exitroom = new ExitRoom(gva.httpClient, exitRoomHandler);
						new Thread(exitroom).start();
					}
				}
			}
		});*/

	}
	
	private Handler exitRoomHandler = new Handler(){
		@Override
    	public void handleMessage(Message msg){
			dialog.cancel();
			Bundle bundle = msg.getData();
			String status = bundle.getString("status", "no status");
			String info = bundle.getString("info", "no info");
			switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
				
				if (status.equalsIgnoreCase("succeed")){
					exitRoomBtn.setText("加入房间");
					exitRoomBtn.setBackgroundColor(Color.rgb(0, 187, 51));
					gva.curRoomId = -1;
					Intent intent = new Intent();
					intent.setClass(RoomNew.this, HomeActivity.class);
					startActivity(intent);
					finish();
				} else if (status.equalsIgnoreCase("failed")){
					Log.i("lin", "exit room info: "+info);
					if (info.equalsIgnoreCase("not login")){
						Toast.makeText(getApplicationContext(), "尚未登录", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("user not in this room")){
						Toast.makeText(getApplicationContext(), "您不在此房间中", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("room not found")){
						Toast.makeText(getApplicationContext(), "没找到房间", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(getApplicationContext(), "网络连接有错", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	private Handler exitEnterRoomHandler = new Handler(){
		@Override
    	public void handleMessage(Message msg){
			dialog.cancel();
			Bundle bundle = msg.getData();
			String status = bundle.getString("status", "no status");
			String info = bundle.getString("info", "no info");
			switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
				
				if (status.equalsIgnoreCase("succeed")){
					exitRoomBtn.setText("加入房间");
					exitRoomBtn.setBackgroundColor(Color.rgb(0, 187, 51));
					gva.curRoomId = -1;
					EnterRoom er = new EnterRoom(gva.httpClient, enterRoomHandler, gva.curRoomId);
					new Thread(er).start();
				} else if (status.equalsIgnoreCase("failed")){
					Log.i("lin", "exit room info: "+info);
					if (info.equalsIgnoreCase("not login")){
						Toast.makeText(getApplicationContext(), "尚未登录", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("user not in this room")){
						Toast.makeText(getApplicationContext(), "您不在此房间中", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("room not found")){
						Toast.makeText(getApplicationContext(), "没找到房间", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(getApplicationContext(), "网络连接有错", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	private Handler enterRoomHandler = new Handler(){
		@Override
    	public void handleMessage(Message msg){
			dialog.cancel();
			Bundle bundle = msg.getData();
			String status = bundle.getString("status", "no status");
			String info = bundle.getString("info", "no info");
			switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
				
				if (status.equalsIgnoreCase("succeed")){
					exitRoomBtn.setText("退出房间");
					exitRoomBtn.setBackgroundColor(Color.rgb(253, 82, 73));
					gva.curRoomId = room.getRoomid();
					GetRoomInfo gri = new GetRoomInfo(gva.httpClient, getRoomInfoHandler, gva.curRoomId);
					new Thread(gri).start();
				} else if (status.equalsIgnoreCase("failed")){
					Log.i("lin", "enter room info: "+info);
					if (info.equalsIgnoreCase("not login")){
						Toast.makeText(getApplicationContext(), "尚未登录", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("team not found")){
						Toast.makeText(getApplicationContext(), "没找到队伍", Toast.LENGTH_SHORT).show();
					}else if (info.equalsIgnoreCase("room not found")){
						Toast.makeText(getApplicationContext(), "没找到房间", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.setClass(RoomNew.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}else if (info.equalsIgnoreCase("in game")){
						Toast.makeText(getApplicationContext(), "正在游戏中", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(getApplicationContext(), "网络连接有错", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	private Handler getRoomInfoHandler = new Handler(){
		@Override
    	public void handleMessage(Message msg){
    		Bundle bundle = msg.getData();
    		room = bundle.getParcelable("Room");
    		teamList = bundle.getParcelableArrayList("TeamList");
    		if (room != null){
    			System.out.println("roomName: "+room.getRoomName());
        		System.out.println("maxMem: "+room.getMaxMem());
    		}
		}
	};
	private Handler changeTeamNameHandler = new Handler(){
    	@Override
	 	   public void handleMessage(android.os.Message msg){
	    		
	    		Bundle bundle = msg.getData();
	    		String status = bundle.getString("status", "no status");
	    		String info = bundle.getString("info", "no info");
	    		if (status.equalsIgnoreCase("succeed")){
	    			Toast.makeText(getApplicationContext(), "修改队名成功", Toast.LENGTH_SHORT).show();
	    		}else if (status.equalsIgnoreCase("failed")){
	    			if (info.equalsIgnoreCase("not login")){
	    				Toast.makeText(getApplicationContext(), "尚未登录", Toast.LENGTH_SHORT).show();
	    			}else {
	    				Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
	    				Log.i("lin", "change team name: "+info);
	    			}
	    		}
    	}
    };
	
	private void updataMyTeam(){
		myteam_et.setText(myteam.getTeamName());
		ArrayList<HashMap<String, Object>> memItem = new ArrayList<HashMap<String, Object>>();
		ArrayList<TeamMemberParcelable> myteam_mems = (ArrayList<TeamMemberParcelable>) myteam.getTeamMemberList();
		for (int i = 0; i < myteam_mems.size(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("MemImage", myteam_mems.get(i).getAvatar());
			map.put("Name", myteam_mems.get(i).getName());
			memItem.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, memItem, R.layout.team_member_item,
				new String[]{"MemImage", "Name"},
				new int[]{R.id.imageView1, R.id.textView1});
		myteam_gv.setAdapter(adapter);
	}
	
	private void updateOtherTeam(){
		otherteams_lv.setAdapter(new ListViewAdapter(otherTeams));
	}
	
	private class ListViewAdapter extends BaseAdapter{
		View[] itemViews;
		
		public ListViewAdapter(List<TeamParcelable> teams) {
			super();
			itemViews = new View[teams.size()];
			for (int i = 0; i < itemViews.length; i++){
				itemViews[i] = makeItemView(teams.get(i).getTeamName(), teams.get(i).getTeamMemberList());
			}
		}
		public ListViewAdapter(){}

		@Override
		public int getCount() {
			return itemViews.length;
		}

		@Override
		public Object getItem(int arg0) {
			return itemViews[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)  
                return itemViews[arg0];  
            return arg1; 
		}
		
		private View makeItemView(String pTeamName, List<TeamMemberParcelable> pMembers){
			LayoutInflater inflater = getLayoutInflater();
			View itemView = inflater.inflate(R.layout.otherteam_listitem, null);
			TextView teamName_tv = (TextView) itemView.findViewById(R.id.teamName);
			teamName_tv.setText(pTeamName);
			GridView gv = (GridView) itemView.findViewById(R.id.gridView1);
			ArrayList<HashMap<String, Object>> memItem = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < pMembers.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("MemImage", pMembers.get(i).getAvatar());
				map.put("Name", pMembers.get(i).getName());
				memItem.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(RoomNew.this, memItem, R.layout.team_member_item,
					new String[]{"MemImage", "Name"},
					new int[]{R.id.imageView1, R.id.textView1});
			gv.setAdapter(adapter);
			return itemView;
		}
	}
	
	private void setOnClicks(){
		edit_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isEditTeamName){
					isEditTeamName = !isEditTeamName;
					myteam_tv.setVisibility(View.GONE);
					myteam_et.setVisibility(View.VISIBLE);
					myteam_et.setFocusable(true);
					myteam_et.setFocusableInTouchMode(true);
					myteam_et.requestFocus();
					myteam_et.setSelection(myteam_et.getText().length());
					imm = (InputMethodManager)myteam_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			        imm.showSoftInput(myteam_et, 0); //显示软键盘  
			        edit_iv.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
				}
				else {
					isEditTeamName = !isEditTeamName;
					myteam_tv.setVisibility(View.VISIBLE);
					myteam_et.setVisibility(View.GONE);
					myteam_tv.setText(myteam_et.getText());
					imm.hideSoftInputFromWindow(edit_iv.getWindowToken(), 0);//隐藏软键盘 
			        edit_iv.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
			        ChangeTeamName ctn = new ChangeTeamName(gva.httpClient, changeTeamNameHandler, 
			        		myteam_et.getText().toString());
			        new Thread(ctn).start();
				}
			}
		});
	}
	private void setListViewHeightBasedOnChildren(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
	}
	
}
