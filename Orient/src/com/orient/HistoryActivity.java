package com.orient;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.R.integer;
import android.R.string;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
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
import android.widget.TextView; 
import android.widget.Toast;

public class HistoryActivity extends ListActivity implements OnTouchListener {
	
	private Button menuHomeButton;
	private Button menuFriendButton;
	private Button menuRoomButton;

    ImageView menuButton;
    
    private ListView listView;
    
    private LinearLayout menu;
    private LinearLayout content;
    private LinearLayout.LayoutParams menuParams;
    private LinearLayout.LayoutParams contentParams;
    private int rightEdge = 0;
    // menu��ȫ��ʾʱ������content�Ŀ��ֵ��
    private static final int menuPadding = 250;
    private int disPlayWidth;
    DisplayMetrics dm = new DisplayMetrics();
    
    private float xDown;
    private float xUp;
    private float xMove;
    private int leftEdge;
    private boolean mIsShow = false;
    private static final int speed = 50;
    private long exitTime = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		/**
		 * ҳ��˵���Home��ת
		 */
		menuHomeButton = (Button)findViewById(R.id.menu_home);
		menuHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HistoryActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		/**
		 * ҳ��˵���Friend��ת
		 */
		menuFriendButton = (Button)findViewById(R.id.menu_friend);
		menuFriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HistoryActivity.this, FriendActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		/**
		 * ҳ��˵���Room��ת
		 */
		menuRoomButton = (Button)findViewById(R.id.menu_room);
		menuRoomButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
		
		/**
		 * ҳ��˵���Setting��ת
		 */
		/* д��SettingAcitivity����ȡ��ע��
		menuSettingButton = (Button)findViewById(R.id.menu_setting);
		menuSettingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HistoryActivity.this, SettingActivity.class);
				startActivity(intent);
				finish();
			}
		}); 
		*/
		
		/*
		 * ���ò˵�
		 */
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		disPlayWidth = dm.widthPixels;
        menu = (LinearLayout) findViewById(R.id.history_menu);
        content = (LinearLayout) findViewById(R.id.history_content);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        leftEdge = -menuParams.width;
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        findViewById(R.id.layout).setOnTouchListener(this);
        
        menuParams.width = disPlayWidth - menuPadding;
        contentParams.width = disPlayWidth;
		
		/*
		 * ���ý���˵���ť
		 */
		menuButton = (ImageView)findViewById(R.id.history_page_menu);
		menuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
		
		showMenu(!mIsShow);
		
		/*
		 * ��ʾlistview����
		 */
		listView = (ListView)findViewById(android.R.id.list);
		show();
	}
	
	private void show() {
		// TODO Auto-generated method stub
		String[] mFrom = new String[]{"img", "room_name", "begin_place", "begin_day", "begin_hour", "rank", "amount"};
		int[] mTo = new int[]{R.id.img, R.id.room_name, R.id.begin_place, R.id.begin_day, R.id.begin_hour, R.id.rank, R.id.amount};
		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String,Object>>();
		for( int i=0; i<5; i++ ) {
			HashMap<String, Object> mMap = new HashMap<String, Object>();
			if( i % 5 == 0 ) {
				mMap.put("img", R.drawable.ordinary_icon);
				mMap.put("room_name", "Goldaners");
				mMap.put("begin_place", "������ɽ��ѧ������");
				mMap.put("begin_day", "2013-12-04");
				mMap.put("begin_hour", "13:00");
				mMap.put("rank", "2");
				mMap.put("amount", "/"+"4");
			}
			else if ( i % 5 == 1 ) {
				mMap.put("img", R.drawable.ordinary_icon);
				mMap.put("room_name", "Orient�ڲ�");
				mMap.put("begin_place", "���ݴ�ѧ����ɽ��ѧ���Ļ�̳");
				mMap.put("begin_day", "2013-12-10");
				mMap.put("begin_hour", "13:30");
				mMap.put("rank", "1");
				mMap.put("amount", "/"+"5");
			}
			else if ( i % 5 == 2 ) {
				mMap.put("img", R.drawable.potato_button);
				mMap.put("room_name", "����");
				mMap.put("begin_place", "���ݴ�ѧ������������");
				mMap.put("begin_day", "2013-12-15");
				mMap.put("begin_hour", "10:00");
				mMap.put("rank", "3");
				mMap.put("amount", "/"+"5");
			}
			else if ( i % 5 == 3 ) {
				mMap.put("img", R.drawable.ordinary_icon);
				mMap.put("room_name", "Goldaners������");
				mMap.put("begin_place", "���ݺ�������ɽ��ѧ��ʿ��");
				mMap.put("begin_day", "2013-12-18");
				mMap.put("begin_hour", "13:00");
				mMap.put("rank", "1");
				mMap.put("amount", "/"+"6");
			}
			else {
				mMap.put("img", R.drawable.christmas_button);
				mMap.put("room_name", "ӭ��ʥ����");
				mMap.put("begin_place", "���ݴ�ѧ�Ǳ��ڴ�");
				mMap.put("begin_day", "2013-12-20");
				mMap.put("begin_hour", "15:00");
				mMap.put("rank", "2");
				mMap.put("amount", "/"+"7");
			}
			mList.add(mMap);
		}
		lvButtonAdapter mAdapter = new lvButtonAdapter(this,mList,R.layout.history_item,mFrom,mTo);
        listView.setAdapter(mAdapter);
	}

	/*
	 * ��lvButtonAdapter
	 */
	public class lvButtonAdapter extends BaseAdapter {
		private class buttonViewHolder {
			ImageView history_imgImageView;
			TextView room_nameTextView;
			TextView begin_placeTextView;
			TextView begin_dayTextView;
			TextView begin_hourTextView;
			TextView rankTextView;
			TextView amountTextView;
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
        	}
        	else {
        		convertView = mInflater.inflate(R.layout.history_item, null);
        		holder = new buttonViewHolder();
        		holder.history_imgImageView = (ImageView)convertView.findViewById(valueViewID[0]);
        		holder.room_nameTextView = (TextView)convertView.findViewById(valueViewID[1]);
        		holder.begin_placeTextView = (TextView)convertView.findViewById(valueViewID[2]);
        		holder.begin_dayTextView = (TextView)convertView.findViewById(valueViewID[3]);
        		holder.begin_hourTextView = (TextView)convertView.findViewById(valueViewID[4]);
        		holder.rankTextView = (TextView)convertView.findViewById(valueViewID[5]);
        		holder.amountTextView = (TextView)convertView.findViewById(valueViewID[6]);
        		convertView.setTag(holder);
        	}
        	
        	HashMap<String, Object> appInfo = mAppList.get(position);
        	if(appInfo != null) {
        		int img_id = (Integer) appInfo.get(keyString[0]);
        		String name = (String) appInfo.get(keyString[1]);
        		String place = (String) appInfo.get(keyString[2]);
        		String day = (String) appInfo.get(keyString[3]);
        		String hour = (String) appInfo.get(keyString[4]);
        		String rank = (String) appInfo.get(keyString[5]);
        		String amount = (String) appInfo.get(keyString[6]);
        		
        		holder.history_imgImageView.setImageDrawable(holder.history_imgImageView.getResources().getDrawable(img_id));
        		holder.room_nameTextView.setText(name);
        		holder.begin_placeTextView.setText(place);
        		holder.begin_dayTextView.setText(day);
        		holder.begin_hourTextView.setText(hour);
        		holder.rankTextView.setText(rank);
        		holder.amountTextView.setText(amount);
        	}
        	return convertView;
        }
	}
	
	/*
	 * ����"�ٰ�һ���˳�����"
	 */
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

        if((System.currentTimeMillis()-exitTime) > 2000){
            Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();                                exitTime = System.currentTimeMillis();
        }
        else{
            finish();
            System.exit(0);
            }
        return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	/*
	 * ���û�����Ļ������˳��˵�
	 */
	@Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {

        case MotionEvent.ACTION_DOWN:  
            // ��ָ����ʱ����¼����ʱ�ĺ�����  
            xDown = event.getRawX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            // ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬��������಼�ֵ�leftMarginֵ���Ӷ���ʾ��������಼��  
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
            // ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�������಼�֣����ǹ������Ҳ಼��  
           break;
        }
        return true;
    }
	
	/*
	 * �˵����ֺ���showMenu()
	 */
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
}