package com.orient;



import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;


public class RoomActivity extends TabActivity implements OnTouchListener {
	private TabHost TabHost;
	private static int currentlayout = 0;
	
	private LinearLayout menu;
    private RelativeLayout content;
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
    private Button menuFriendButton;
    private Button menuHomeButton;
    private long exitTime = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.room);
        
        TabHost tabHost=getTabHost();
		tabHost.addTab(tabHost.newTabSpec("RoomHistoryActivity")
				.setIndicator("历史记录", 
				this.getResources().getDrawable(R.drawable.button_invite))
				.setContent(new Intent(this, HistoryGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("RoomCreateActivity")
        		.setIndicator("创建房间", 
        				this.getResources().getDrawable(R.drawable.button_invite))
        				.setContent(new Intent(this, CreateGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("RoomJoinActivity")
        		.setIndicator("加入房间",
        				this.getResources().getDrawable(R.drawable.button_invite))
        				.setContent(new Intent(this,JoinGroup.class)));
        tabHost.setCurrentTab(0);
        //setFragment();
        //changeLayout();
        
        
        
        menuFriendButton = (Button)findViewById(R.id.room_menu_friend);
        menuFriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RoomActivity.this, FriendActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        menuHomeButton = (Button)findViewById(R.id.menu_home);
        menuHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RoomActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		disPlayWidth = dm.widthPixels;
        menu = (LinearLayout) findViewById(R.id.room_menu);
        content = (RelativeLayout) findViewById(R.id.room_content);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        leftEdge = -menuParams.width;
        contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        findViewById(R.id.layout).setOnTouchListener(this);
        
        menuParams.width = disPlayWidth - menuPadding;
        contentParams.width = disPlayWidth;
        ImageView menuButton = (ImageView)findViewById(R.id.room_page_menu);
        menuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMenu(mIsShow);
			}
		});
       showMenu(!mIsShow);
    }
	
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
    
	
	private void changeLayout() {
		TabHost.setCurrentTab(currentlayout);
	}
	
	private void setFragment() {
		//TabHost = (TabHost)findViewById(R.id.tabhost);
		//TabHost.setup();
		
        //设置默认显示布局
        TabHost.setCurrentTab(0);
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
}
