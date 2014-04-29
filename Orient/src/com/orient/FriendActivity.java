package com.orient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
//import android.content.DialogInterface.OnClickListener;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ListView;
import android.view.KeyEvent;
import android.view.LayoutInflater;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.View.OnTouchListener;
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;    
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView; 
import android.widget.Toast;
import android.widget.ViewAnimator;

public class FriendActivity extends ListActivity implements OnTouchListener {
	private ListView listView;
	private ImageButton button;
	
	private LinearLayout menu;
    private LinearLayout content;
    private LinearLayout.LayoutParams menuParams;
    private LinearLayout.LayoutParams contentParams;
    private int rightEdge = 0;
    // menu完全显示时，留给content的宽度值。
    private static final int menuPadding = 250;

    // 分辨率
    private int disPlayWidth;
    DisplayMetrics dm = new DisplayMetrics();

    private float xDown;
    private float xUp;
    private float xMove;
    private int leftEdge;
    private boolean mIsShow = false;
    private static final int speed = 50;
    ImageView menuButton;
    private Button menuHomeButton;
    private Button menuRoomButton;
    private Button menuFriendButton;
    
    private long exitTime = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        
        menuHomeButton = (Button)findViewById(R.id.menu_home);
        menuHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FriendActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        menuRoomButton = (Button)findViewById(R.id.menu_room);
        menuRoomButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FriendActivity.this, HistoryActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        menuFriendButton = (Button)findViewById(R.id.menu_friend);
        menuFriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
        
        listView = (ListView) this.findViewById(android.R.id.list);
        show();
       
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		disPlayWidth = dm.widthPixels;
        menu = (LinearLayout) findViewById(R.id.friend_menu);
        content = (LinearLayout) findViewById(R.id.friend_content);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        leftEdge = -menuParams.width;
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        findViewById(R.id.layout).setOnTouchListener(this);
        
        menuParams.width = disPlayWidth - menuPadding;
        contentParams.width = disPlayWidth;
        menuButton = (ImageView)findViewById(R.id.friend_page_menu);
        menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
       showMenu(!mIsShow);
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
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {

        case MotionEvent.ACTION_DOWN:  
            // 手指按下时，记录按下时的横坐标  
            xDown = event.getRawX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整左侧布局的leftMargin值，从而显示和隐藏左侧布局  
            xMove = event.getRawX();  
            int distanceX = (int) (xMove - xDown);  
            if(mIsShow==false){
            	if(distanceX>=menuParams.width/2)
            		showMenu(mIsShow);
            }else {
            	if(distanceX<= - menuParams.width/2)
            		showMenu(mIsShow);
			}
        case MotionEvent.ACTION_UP:  
            // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到左侧布局，还是滚动到右侧布局  
           break;
        }
        return true;
    }

    private void showMenu(boolean isShow)
    {
        if (isShow)
        {
            mIsShow = false;
            menuParams.leftMargin = - menuParams.width;
        } else
        {
            mIsShow = true;
            menuParams.leftMargin = 0 ;
        }
        menu.setLayoutParams(menuParams);
    }


    private void show() {
    	/*
    	 List<Person> persons = 
    	 */
        String[] mFrom = new String[]{"img","name","group","imageButton1"};
        int[] mTo = new int[]{R.id.img,R.id.name,R.id.group,R.id.item_button};
        ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
        for ( int i = 0; i < 10; i++ ) {
        	HashMap<String,Object> mMap = new HashMap<String,Object>();
        	if ( i % 3 == 0) {
	        	mMap.put("img", R.drawable.pic_1);
	        	mMap.put("name", "Jackie");
	        	mMap.put("group", "Super_Orient");
	        	mMap.put("imageButton1", R.drawable.add);
        	} else if ( i % 3 == 1 ) {
        		mMap.put("img", R.drawable.pic_2);
	        	mMap.put("name", "Tom");
	        	mMap.put("group", "Small_House");
	        	mMap.put("imageButton1", R.drawable.add);
        	} else {
        		mMap.put("img", R.drawable.pic_3);
	        	mMap.put("name", "Angela");
	        	mMap.put("group", "Ubuntu");
	        	mMap.put("imageButton1", R.drawable.add);
        	}
        	mList.add(mMap);
        }
        lvButtonAdapter mAdapter = new lvButtonAdapter(this,mList,R.layout.friend_item,mFrom,mTo);
        listView.setAdapter(mAdapter);       
    }   
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        l.getItemAtPosition(position);
    }
    
    public class lvButtonAdapter extends BaseAdapter {
        private class buttonViewHolder {
            ImageView appIcon;
            TextView appName;
            TextView appGroup;
            ImageButton buttonadd;
        }
        
        private ArrayList<HashMap<String, Object>> mAppList;
        private LayoutInflater mInflater;
        private Context mContext;
        private String[] keyString;
        private int[] valueViewID;
        private buttonViewHolder holder;
        
        public lvButtonAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource,
                String[] from, int[] to) {
            mAppList = appList;
            mContext = c;
            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            keyString = new String[from.length];
            valueViewID = new int[to.length];
            System.arraycopy(from, 0, keyString, 0, from.length);
            System.arraycopy(to, 0, valueViewID, 0, to.length);
        }
        
        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void removeItem(int position){
            mAppList.remove(position);
            this.notifyDataSetChanged();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                holder = (buttonViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.friend_item, null);
                holder = new buttonViewHolder();
                holder.appIcon = (ImageView)convertView.findViewById(valueViewID[0]);
                holder.appName = (TextView)convertView.findViewById(valueViewID[1]);
                holder.appGroup = (TextView)convertView.findViewById(valueViewID[2]);
                holder.buttonadd = (ImageButton)convertView.findViewById(valueViewID[3]);
                convertView.setTag(holder);
            }
            
            HashMap<String, Object> appInfo = mAppList.get(position);
            if (appInfo != null) {
                String aname = (String) appInfo.get(keyString[1]);
                int iconid = (Integer)appInfo.get(keyString[0]);
                int addiconid = (Integer)appInfo.get(keyString[3]);
                String groupString = (String)appInfo.get(keyString[2]);
                holder.appName.setText(aname);
                holder.appGroup.setText(groupString);
                holder.appIcon.setImageDrawable(holder.appIcon.getResources().getDrawable(iconid));
                holder.buttonadd.setImageDrawable(holder.buttonadd.getResources().getDrawable(addiconid));
                holder.buttonadd.setOnClickListener( new lvButtonListener(position));
            }        
            return convertView;
        }

        class lvButtonListener implements View.OnClickListener {
            private int position;

            lvButtonListener(int pos) {
                position = pos;
            }
            
            public void onClick(View v) {
                int vid=v.getId();
                if (vid == holder.buttonadd.getId()){
                	AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
    				View view = LayoutInflater.from(FriendActivity.this).inflate(R.layout.prompt,null);
    				
    				((TextView)view.findViewById(R.id.game_final_name)).setText("确定加入"+mAppList.get(position).get("name").toString()+"所在的房间？");
    				
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
							//跳到一个房间
							Intent intent = new Intent();
							intent.setClass(FriendActivity.this, GameTeamActivity.class);
							startActivity(intent);
							finish();
						}
					});
                }                
            }
	
        }
    }
}
