package com.orient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.xmlpull.v1.XmlPullParserException;

import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.constant.Constant;
import com.network.GetRoomList;
import com.network.PostPosition;
import com.orient.R.id;
import com.orient.RefreshableView.PullToRefreshListener;
import com.test.TestActivity;
import com.util.Location;
import com.util.ParseRoomList;
import com.util.Room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.util.Log;
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
    GlobalVarApplication gva; 
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
    //private List<Integer> roomids;
    private List<Room> rooms;
    //下拉刷新列表
    RefreshableView refreshableView;
    Location location = new Location();
    private Handler getRoomListHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		Bundle bundle = msg.getData();
    		String status = bundle.getString("status", "no status");
    		String info = bundle.getString("info", "no info");
    		String roomlist = bundle.getString("Roomlist", "no room list");
    		switch(msg.what){
			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
				if (status.equalsIgnoreCase("succeed")){
					try {
						rooms = ParseRoomList.parseRoomList(roomlist);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					show();
				} else if (status.equalsIgnoreCase("failed")){
					if (info.equalsIgnoreCase("not login")){
						Toast.makeText(getApplicationContext(), "尚未登录", 
								Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "未知错误", 
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case Constant.NETWORK_FAILED_MESSAGE_TAG:
				Toast.makeText(getApplicationContext(), "网络连接有错，请稍后再试",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
    	}
    };
    private Handler positioningHandler = new Handler(){
    	@Override
	 	   public void handleMessage(android.os.Message msg){
	    		
	    		Bundle bundle = msg.getData();						
	    		if(bundle.getString("result").equals("location")){
	    			LocationData locData = new LocationData();  
	    			  
	    			locData.latitude = bundle.getDouble("latitude");  
	    			locData.longitude =  bundle.getDouble("longitude");
	    			locData.direction = bundle.getFloat("direction");
	    			
	    			Log.i("lin", "get location succeed");
	    			Log.i("lin", "longitude: "+locData.longitude+" latitude: "+locData.latitude);
	    			Log.i("lin", "address: "+bundle.getString("address"));
	    			uploadPosition((int)(locData.longitude*1e6), (int)(locData.latitude*1e6));
	    		}else {
					Toast.makeText(getApplicationContext(), "努力定位ing...", 2000).show();
				}
	    	}
    };
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        gva = (GlobalVarApplication)getApplication();
        //roomids = new ArrayList<Integer>();
        rooms = new ArrayList<Room>();
        location.positioning(getApplicationContext(), positioningHandler, gva.getMyLocationListener(), false);
        //下拉刷新列表
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);  
        listView = (ListView) this.findViewById(R.id.home_listView);
        
        //下拉刷新监听器
        refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
            @Override  
            public void onRefresh() {
            	
            	location.positioning(getApplicationContext(), positioningHandler, gva.getMyLocationListener(), false);
                try {  
                    Thread.sleep(3000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                refreshableView.finishRefreshing();  
            }  
        }, 0);  
        
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
    
    public boolean uploadPosition(int pLongitude, int pLatitude){
    	final Handler handler2 = new Handler(){
    		
    		@Override
    		public void handleMessage(Message msg){
    			Bundle bundle = msg.getData();
    			String status = bundle.getString("status");
    			String info = bundle.getString("info", "no info");
    			switch(msg.what){
    			case Constant.NETWORK_SUCCESS_MESSAGE_TAG:
    				if (status.equalsIgnoreCase("succeed")) {
System.out.println("get room list");
    					GetRoomList g = new GetRoomList(gva.httpClient, getRoomListHandler);
    					new Thread(g).start();
    				}else if (status.equalsIgnoreCase("failed")){
    					if (info.equalsIgnoreCase("not login")){
    						Toast.makeText(getApplicationContext(), "尚未登录", 
    								Toast.LENGTH_SHORT).show();
    					}else{
    						Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG).show();
    					}
    				}else{
    					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG).show();
    				}
    				break;
    			case Constant.NETWORK_FAILED_MESSAGE_TAG:
    				Log.i("lin", "经纬度暂时不能上传");
    				break;
    			default:
    				break;
    			}
    		
    		}
    	};
    	Handler handler3 = new Handler(){

    		@Override
    		public void handleMessage(Message msg){
    			Bundle bundle = msg.getData();						
	    		if(bundle.getString("result").equals("succeed")){
	    			String addr = bundle.getString("address");
	    			Log.i("lin", "addr: "+addr);
	    		}else {
					Toast.makeText(getApplicationContext(), "努力转换地址信息ing...", 2000).show();
				}
    		
    		}
    	};
    	PostPosition p = new PostPosition(gva.httpClient, handler2, pLongitude, pLatitude);
    	new Thread(p).start();
    	//Location.reverseGeocode(getApplicationContext(), handler3, pLongitude, pLatitude);
    	return true;
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
    	 String[] mFrom = new String[]{"image","username","roomname","point","time","button","number"};
         int[] mTo = new int[]{R.id.home_item_image,R.id.home_item_username,R.id.home_item_roomname,R.id.home_item_point,
        		 R.id.home_item_time,R.id.home_item_button,R.id.home_item_number};
         
         ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
         for (int i = 0; i < rooms.size(); i++){
        	 HashMap<String,Object> mMap = new HashMap<String,Object>();
        	 Room room = rooms.get(i);
        	 mMap.put("image", R.drawable.photo6);
        	 mMap.put("username", room.getFounderName());
        	 if (gva.curRoomId == room.getRoomid()){
         		 mMap.put("button", R.drawable.joined);
         	 }else{
         		 mMap.put("button", R.drawable.join);
         	 }
         	 mMap.put("point", room.getAddress()+" "+room.getDistance());
         	 mMap.put("roomname", room.getRoomName());
         	 mMap.put("number", String.valueOf(room.getMembers()));
         	 mMap.put("time", room.getTime());
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

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putParcelable("com.util.Room", rooms.get(arg2));
				intent.putExtras(bundle);
				/*TextView roomNameTextView = (TextView)arg1.findViewById(R.id.home_item_roomname);
				intent.putExtra("roomName", roomNameTextView.getText().toString());
				TextView pointTextView = (TextView)arg1.findViewById(R.id.home_item_point);
				intent.putExtra("point", pointTextView.getText().toString());
				TextView timeTextView = (TextView)arg1.findViewById(R.id.home_item_time);
				intent.putExtra("time", timeTextView.getText().toString());
				String roomid = ((TextView)arg1.findViewById(R.id.roomid)).getText().toString();
				intent.putExtra("roomid", roomid);
				String maxMem = ((TextView) arg1.findViewById(R.id.home_item_number)).getText().toString();
				intent.putExtra("maxMem", maxMem);*/
				intent.setClass(HomeActivity.this, RoomNew.class);
				startActivity(intent);
				finish();
			}
        	 
		});
    }

   
    public class lvButtonAdapter extends BaseAdapter {
        private class buttonViewHolder {
            ImageView appView;
            TextView appUserName;
            TextView appRoomName;
            TextView appPoint;
            TextView appTime;
            ImageButton button;
            TextView appNumber;
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
                holder.appUserName.setText(aUserName);
                holder.appRoomName.setText(aRoomName);
                holder.appPoint.setText(aPoint);
                holder.appTime.setText(aTime);
                holder.appNumber.setText(aNumber);
                holder.appView.setImageDrawable(holder.appView.getResources().getDrawable(viewId));
                holder.button.setImageDrawable(holder.button.getResources().getDrawable(buttonId));

            }         
            return convertView;
        }
      
    }
}

