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
    // menu��ȫ��ʾʱ������content�Ŀ��ֵ��
    private static final int menuPadding = 250;

    // �ֱ���
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
				.setIndicator("��ʷ��¼", 
				this.getResources().getDrawable(R.drawable.button_invite))
				.setContent(new Intent(this, HistoryGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("RoomCreateActivity")
        		.setIndicator("��������", 
        				this.getResources().getDrawable(R.drawable.button_invite))
        				.setContent(new Intent(this, CreateGroup.class)));
        tabHost.addTab(tabHost.newTabSpec("RoomJoinActivity")
        		.setIndicator("���뷿��",
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
		
        //����Ĭ����ʾ����
        TabHost.setCurrentTab(0);
	}

	
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
}
