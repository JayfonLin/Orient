package com.orient;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.PrivateCredentialPermission;

import com.orient.R.id;
import com.test.TestActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

public class HomeActivity extends Activity implements OnTouchListener
{
	private ListView listView;
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
    Button menuFriendButton;
    Button menuRoomButton;
    Button menuHomeButton;
    ImageButton createRoomImageButton;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        
        listView = (ListView) this.findViewById(R.id.home_listView);
        show();
        
        //测试代码，后期可去掉
        Button testBtn = (Button) findViewById(R.id.testButton);
        testBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, TestActivity.class);
				startActivity(intent);
			}
		});
        //测试代码 完
        
        menuFriendButton = (Button)findViewById(R.id.menu_friend);
        menuFriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, FriendActivity.class);
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
				intent.setClass(HomeActivity.this, HistoryActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        menuHomeButton = (Button)findViewById(R.id.menu_home);
        menuHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
        
        createRoomImageButton = (ImageButton)findViewById(R.id.create_imageButton);
        createRoomImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, Room_Second_1.class);
				startActivity(intent);
				finish();
			}
		});
        
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		disPlayWidth = dm.widthPixels;
        menu = (LinearLayout) findViewById(R.id.home_menu);
        content = (LinearLayout) findViewById(R.id.home_content);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        leftEdge = -menuParams.width;
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        findViewById(R.id.layout).setOnTouchListener(this);
        
        menuParams.width = disPlayWidth - menuPadding;
        contentParams.width = disPlayWidth;
        Button menuButton = (Button)findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
       showMenu(!mIsShow);
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
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

        if((System.currentTimeMillis()-exitTime) > 2000){
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                exitTime = System.currentTimeMillis();
        }
        else{
            finish();
            System.exit(0);
            }

        return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void show() {
    	 String[] mFrom = new String[]{"image","username","roomname","point","time","button","number","routeid"};
         int[] mTo = new int[]{R.id.home_item_image,R.id.home_item_username,R.id.home_item_roomname,R.id.home_item_point,
        		 R.id.home_item_time,R.id.home_item_button,R.id.home_item_number,R.id.routeid};
         SQLApi sqlapi = new SQLApi(getApplicationContext());
         ArrayList<HashMap<String, Object>> dataList = sqlapi.queryRoute();
         ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
         for ( int i = 0; i < dataList.size(); i++ ) {
         	HashMap<String,Object> mMap = new HashMap<String,Object>();
         	
         	if ( i % 6 == 0) {
 	        	mMap.put("image", R.drawable.pic_2);
 	        	mMap.put("username", "Jackie");
 	        	mMap.put("point", "广州大学城中山大学图书馆");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
 	        	if (i== 0) {
 	        		mMap.put("button", R.drawable.joined);
 	        	} else {
 	        		mMap.put("button", R.drawable.join);
 	        	}
 	        	
         	} else if ( i % 6 == 1 ) {
         		
         		mMap.put("image", R.drawable.pic_3);
 	        	mMap.put("username", "David");
 	        	mMap.put("button", R.drawable.join);
 	        	mMap.put("point", "广州大学城中二横路");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
         	} else if ( i % 6 == 2 ) {
         		
         		mMap.put("image", R.drawable.photo_loly);
 	        	mMap.put("username", "虾米");
 	        	mMap.put("button", R.drawable.join);
 	        	mMap.put("point", "广州大学城内环东路");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
         	} else if ( i % 6 == 3 ) {
         		
         		mMap.put("image", R.drawable.photo3);
 	        	mMap.put("username", "Ryan");
 	        	mMap.put("button", R.drawable.join);
 	        	mMap.put("point", "广州大学城华南理工");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
         	} else if ( i % 6 == 4 ) {
         		
         		mMap.put("image", R.drawable.photo3);
 	        	mMap.put("username", "大头");
 	        	mMap.put("button", R.drawable.join);
 	        	mMap.put("point", "广州海珠区中山大学");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
         	} else {
         		
         		mMap.put("image", R.drawable.photo1);
 	        	mMap.put("username", "偲偲");
 	        	mMap.put("button", R.drawable.join);
 	        	mMap.put("point", "广州天河区车陂路");
 	        	mMap.put("roomname", dataList.get(i).get("roomname"));
 	        	mMap.put("number", dataList.get(i).get("numpergroup").toString());
 	        	mMap.put("time", dataList.get(i).get("date"));
 	        	mMap.put("routeid", dataList.get(i).get("Routeid").toString());
         	}
         	mList.add(mMap);
         }
         lvButtonAdapter mAdapter = new lvButtonAdapter(this,mList,R.layout.home_item,mFrom,mTo);
         listView.setAdapter(mAdapter);
        
         listView.setOnItemSelectedListener(  new AdapterView.OnItemSelectedListener(){  
          @Override  
              public void onItemSelected(AdapterView<?> parent, View view,  
                      int position, long id) {  
                  //当此选中的item的子控件需要获得焦点时   
                  listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);  
                //else parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);   
             }  
              @Override  
              public void onNothingSelected(AdapterView<?> parent) {  
                 listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);  
              }  
         });
         
         listView.setOnItemClickListener(new OnItemClickListener() {
        	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//arg0.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				
				Intent intent = new Intent();
				TextView roomNameTextView = (TextView)arg1.findViewById(R.id.home_item_roomname);
				intent.putExtra("roomName", roomNameTextView.getText().toString());
				String routeidString = ((TextView)arg1.findViewById(R.id.routeid)).getText().toString();
				//System.out.println("ayjsfgdjasgdjsadgjsadjadjasgdhjsa"+routeidString);
				intent.putExtra("Routeid", routeidString);
				intent.setClass(HomeActivity.this, GameMap.class);
				startActivity(intent);
				finish();
			}
        	 
		});
    }
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        // TODO Auto-generated method stub
//        super.onListItemClick(l, v, position, id);
//        l.getItemAtPosition(position);
//    }
   
    public class lvButtonAdapter extends BaseAdapter {
        private class buttonViewHolder {
            ImageView appView;
            TextView appUserName;
            TextView appRoomName;
            TextView appPoint;
            TextView appTime;
            ImageButton button;
            TextView appNumber;
            TextView routeidTextView;
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
                convertView = mInflater.inflate(R.layout.home_item, null);
                holder = new buttonViewHolder();
                holder.appView = (ImageView)convertView.findViewById(valueViewID[0]);
                holder.appUserName = (TextView)convertView.findViewById(valueViewID[1]);
                holder.appRoomName = (TextView)convertView.findViewById(valueViewID[2]);
                holder.appPoint = (TextView)convertView.findViewById(valueViewID[3]);
                holder.appTime = (TextView)convertView.findViewById(valueViewID[4]);
                holder.button = (ImageButton)convertView.findViewById(valueViewID[5]);
                holder.appNumber = (TextView)convertView.findViewById(valueViewID[6]);
                holder.routeidTextView = (TextView)convertView.findViewById(valueViewID[7]);
                convertView.setTag(holder);
            }
            
            HashMap<String, Object> appInfo = mAppList.get(position);
            if (appInfo != null) {
                
                int viewId = (Integer)appInfo.get(keyString[0]);
                String aUserName = (String) appInfo.get(keyString[1]);
                String aRoomName = (String) appInfo.get(keyString[2]);
                String aPoint = (String) appInfo.get(keyString[3]);
                String aTime = (String) appInfo.get(keyString[4]);
                int buttonId = (Integer)appInfo.get(keyString[5]);
                String aNumber = (String) appInfo.get(keyString[6]);
                String routeidString = (String) appInfo.get(keyString[7]);
                holder.appUserName.setText(aUserName);
                holder.appRoomName.setText(aRoomName);
                holder.appPoint.setText(aPoint);
                holder.appTime.setText(aTime);
                holder.appNumber.setText(aNumber);
                holder.routeidTextView.setText(routeidString);
                holder.appView.setImageDrawable(holder.appView.getResources().getDrawable(viewId));
                holder.button.setImageDrawable(holder.button.getResources().getDrawable(buttonId));
//                holder.button.setOnClickListener( new lvButtonListener(position));
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
                if (vid == holder.button.getId()) {
                	Intent intent = new Intent();
                	//System.out.println( "gtedhtrhy"+mAppList.get(position).get("roomname").toString());
                	intent.putExtra("Routeid", mAppList.get(position).get("routeid").toString());
					intent.putExtra("roomName", mAppList.get(position).get("roomname").toString());
					intent.setClass(HomeActivity.this, GameMap.class);
					startActivity(intent);
					finish();
                }

            }
        }
    }
}

