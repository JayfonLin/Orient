package com.orient;

import com.orient.widget.MultiDirectionSlidingDrawer;
import com.orient.R;
import com.util.Location;

import java.util.Date;
import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

public class GameMap extends Activity implements Runnable{
	BMapManager mBMapMan = null;  
	MapView mMapView = null;
	MapController mMapController=null;
	MyLocationOverlay myposOverlay;
	Handler handler;
	GameMissionOverlay missionOverlay;
	GameMissionOverlay gettogetherOverlay;
	ImageButton getlocationbtn;
	
	public static GameMap context;
	private Button backHomeButton;
	private Button memberButton;
	private String roomNameString;
	private TextView roomNameTextView;
	/*public LocationClient mLocationClient = null;
	public BDLocationListener myListener;*/
	private Menu mainMenu;
	int missionnum;
	ArrayList<GeoPoint> missionList;
	GeoPoint myposGeoPoint;
	//下拉列表
	MultiDirectionSlidingDrawer mDrawer;
	
	//计时
	private long time = 0;
	private long startTime;
	private TextView timeView;
	private Handler timeHandler;
	private int routeid;
	//进度条
	public ProgressBar ProgressBar;
	protected static final int STOP = 0x10000;  
    protected static final int NEXT = 0x10001;  
    //private int iCount = 0;
    public TextView process;
    
  //椹块┈蹇姤
    LinearLayout curmesLayout;
	LinearLayout curmesItemLayout;
	Button exit_btn;
	TextView item1;
	TextView item2;
    TextView item3;
	TextView item4;
	TextView item5;
	TextView item6;
    
    //璇煶
    private Button soundButton;
    private RelativeLayout soundLayout;
    Location location = new Location();
    GlobalVarApplication gva;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init("90ehkP9tBULpKYG8rbwXffjG", null);
		setContentView(R.layout.game);
		gva = (GlobalVarApplication) getApplication();
		//传递房间名字
		Intent intent = getIntent();
		roomNameString = intent.getStringExtra("roomName");
		roomNameTextView = (TextView)findViewById(R.id.room_name);
		routeid = Integer.valueOf(intent.getStringExtra("Routeid")).intValue();
		if (roomNameString == null || roomNameString == "") {
       	 roomNameTextView.setText("维多利亚的宵夜");
       } else {
       	roomNameTextView.setText(roomNameString);
       }
		
		mMapView=(MapView)findViewById(R.id.bmapsView);  
		mMapView.setBuiltInZoomControls(false);  
		//设置启用内置的缩放控件  
		mMapController=mMapView.getController();  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
		context = this;
		//myListener = new MyLocationListener(handler);
		Drawable mark = getResources().getDrawable(R.drawable.myposition_big_shine);
		myposOverlay = new MyLocationOverlay(mMapView);
		myposOverlay.setMarker(mark);
		mMapView.getOverlays().add(myposOverlay);
		
		getlocationbtn = (ImageButton)findViewById(R.id.getlocationbtn);
		
		mark = getResources().getDrawable(R.drawable.pinmiddleblue);
		gettogetherOverlay = new GameMissionOverlay(mark, mMapView);
		mMapView.getOverlays().add(gettogetherOverlay);
		
		mark = getResources().getDrawable(R.drawable.nextmission);
		missionOverlay = new GameMissionOverlay(mark, mMapView);
		mMapView.getOverlays().add(missionOverlay);
		
		SQLApi sqlApi = new SQLApi(getApplicationContext());
		missionList = new ArrayList<GeoPoint>();
		missionList = sqlApi.queryMissions(routeid);
		
/*		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.setAK("90ehkP9tBULpKYG8rbwXffjG");
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
*/		
	    missionnum = missionList.size()-1;
	    
	    backHomeButton = (Button)findViewById(R.id.back_home);
	    backHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(GameMap.this);
				View view = LayoutInflater.from(GameMap.this).inflate(R.layout.prompt_exit,null);
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
						alertDialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(GameMap.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		});
		
		memberButton = (Button)findViewById(R.id.game_member);
		memberButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("roomName", roomNameString);
				intent.putExtra("Routeid", routeid);
				intent.setClass(GameMap.this, GameTeamActivity.class);
				startActivity(intent);
				//finish();
			}
		});
		
		//设置计时
		timeView = (TextView) findViewById(R.id.timeView);
		timeHandler = new Handler();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(GameMap.this);
		View view = LayoutInflater.from(GameMap.this).inflate(R.layout.prompt2,null);
		builder.setView(view);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		
		((Button)view.findViewById(R.id.prompt2_confirm_1)).setOnClickListener(new View.OnClickListener() {						
			@Override
			public void onClick(View arg0) {
				// TODO 鑷姩鐢熸垚鐨勬柟娉曞瓨鏍�
				alertDialog.dismiss();
				startTime = new Date().getTime() - time;
				timeHandler.post(GameMap.this);
				timeView.setText(getFormatTime(time));
				
				ProgressBar.setMax(100);  
                ProgressBar.setProgress(0);  
                  
 
				
			}
		});
		//璁剧疆杩涘害鏉�
		ProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		process = (TextView)findViewById(R.id.process);
				
		//椹块┈蹇姤
		item1 = (TextView)findViewById(R.id.mes_item1);
		item2 = (TextView)findViewById(R.id.mes_item2);
		item3 = (TextView)findViewById(R.id.mes_item3);
		item4 = (TextView)findViewById(R.id.mes_item4);
		item5 = (TextView)findViewById(R.id.mes_item5);
		item6 = (TextView)findViewById(R.id.mes_item6);
		curmesLayout = (LinearLayout)findViewById(R.id.curentmessage);
		curmesItemLayout = (LinearLayout)findViewById(R.id.curmesitem_layout);
		exit_btn = (Button)findViewById(R.id.curmes_exit_btn);
				
		curmesLayout.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			curmesLayout.setVisibility(View.GONE);
			curmesItemLayout.setVisibility(View.VISIBLE);
			item1.setText("18:40:23      Team3已到达关卡2");
			item2.setText("18:32:44      Team2已到达关卡2");
			item3.setText("18:31:17      Team4已到达关卡2");
			item4.setText("18:15:21      Team2已到达关卡1");
			item5.setText("18:12:33      Team3已到达关卡1");
			item6.setText("18:08:12      Team4已到达关卡1");
			}
		});
				
		exit_btn.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					curmesItemLayout.setVisibility(View.GONE);
					}
		});
				
		//璇煶
		soundLayout = (RelativeLayout)findViewById(R.id.soundlayout);
				
		soundButton = (Button)findViewById(R.id.game_voice);
		soundButton.setOnTouchListener(new OnTouchListener() {
					
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							soundLayout.setVisibility(View.VISIBLE);
							break;
					case MotionEvent.ACTION_UP:
							soundLayout.setVisibility(View.GONE);
							break;
						default:
							break;
					}
						return false;
					}
				});
			
			
	}
	
	
	//
	@Override
	public void onContentChanged() {
	   	super.onContentChanged();
	   	mDrawer = (MultiDirectionSlidingDrawer) findViewById( R.id.drawer );
	}
	
	//在选项菜单中添加退出房间的按钮
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu = menu;
		mainMenu.add(0, 1, Menu.NONE, "退出房间");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(GameMap.this);
		View view = LayoutInflater.from(GameMap.this).inflate(R.layout.prompt_exit,null);
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
				intent.setClass(GameMap.this, HomeActivity.class);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(GameMap.this);
			View view = LayoutInflater.from(GameMap.this).inflate(R.layout.prompt_exit,null);
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
					alertDialog.dismiss();
					Intent intent = new Intent();
					intent.setClass(GameMap.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStart() {
		
		/*LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(false);//禁止启用缓存定位
		//option.setPoiNumber(5);    //最多返回POI个数   
		//option.setPoiDistance(1000); //poi查询距离        
		//option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息 
		
		mLocationClient.setLocOption(option);

		ContentResolver resolver = context.getContentResolver();
		Boolean on =Settings.Secure.isLocationProviderEnabled(resolver,LocationManager.GPS_PROVIDER);
		mLocationClient.start();
		if(on){
			if(mLocationClient != null && mLocationClient.isStarted())
				mLocationClient.requestLocation();
			else 
				Toast.makeText(GameMap.context,"在线请求失败，没开GPS？", 2000).show();
		}else{
			if (mLocationClient != null && mLocationClient.isStarted())
				mLocationClient.requestOfflineLocation();
		}*/
		
		handler = new Handler(){
	    	@Override
	 	   public void handleMessage(android.os.Message msg){
	    		
	    		Bundle bundle = msg.getData();						
	    		if(bundle.getString("result").equals("location")){
	    			LocationData locData = new LocationData();  
	    			  
	    			locData.latitude = bundle.getDouble("latitude");  
	    			locData.longitude =  bundle.getDouble("longitude");
	    			locData.direction = bundle.getFloat("direction");
	    			
	    			myposOverlay.setData(locData);
	    			myposGeoPoint = new GeoPoint((int)(locData.latitude*1e6),  (int)(locData.longitude* 1e6));
	    			mMapController.animateTo(myposGeoPoint);
	    			mMapController.setZoom(18);//设置地图zoom级别
	    			mMapController.setOverlooking(-37);
	    			mMapView.refresh();
	    		}else {
					Toast.makeText(context, "努力定位ing...", 2000).show();
				}
	    	}
	    };
	    location.positioning(getApplicationContext(), handler, gva.getMyLocationListener(), false); //定位
		
//		GeoPoint point =new GeoPoint((int)(23.066667* 1E6),(int)(113.397542* 1E6)); 
//		OverlayItem myposItem = new OverlayItem(point, "mypos", "mypos");
//		myposOverlay.removeAll();
//		myposOverlay.addItem(myposItem);
		
		
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		
		mMapController.setZoom(18);//设置地图zoom级别
		mMapController.setOverlooking(-37);
		
		for(int i=0;i<missionList.size();++i){
			if(i!=0){
				missionOverlay.addItem(new OverlayItem(missionList.get(i),"mission", "mission"));
			}else {
				gettogetherOverlay.addItem(new OverlayItem(missionList.get(i),"gettogetherpos", "gettogetherpos"));
			}
		}
		
//		if(missionList.size()!=0){
//			missionOverlay.addItem(new OverlayItem(missionList.get(0),"mission", "mission"));
//			mMapController.animateTo(missionList.get(0));//设置地图中心点  
//		}
		mMapView.refresh();
		//Toast.makeText(GameMap.this, "turn", 1000).show();
		
		super.onStart();
	};
	
	
		
	
	@Override  
	protected void onDestroy(){  
	        mMapView.destroy();  
	        if(mBMapMan!=null){  
	                mBMapMan.destroy();  
	                mBMapMan=null;  
	        }  
	        //mLocationClient.stop();
	        super.onDestroy();  
	}  
	@Override  
	protected void onPause(){  
	        mMapView.onPause();  
	        if(mBMapMan!=null){  
	               mBMapMan.stop();  
	        }  
	        super.onPause();  
	}  
	@Override  
	protected void onResume(){  
	        mMapView.onResume();  
	        if(mBMapMan!=null){  
	                mBMapMan.start();  
	        }  
	       super.onResume();  
	       mMapView.refresh();
	}  
	
	//时间
	private String getFormatTime(long time) {
		long millisecond = time % 1000;
		long second = (time / 1000) % 60;
		long minute = time / 1000 / 60;
		long hour   = time / 1000 / 60 / 60;
		
		//绉掍互涓嬬殑鍙樉绀轰竴浣�
		String strMillisecond = "" + (millisecond / 100);
		//绉掓樉绀轰袱浣�
		String strSecond = ("00" + second).substring(("00" + second).length() - 2);
		//鍒嗘樉绀轰袱浣�
		String strMinute = ("00" + minute).substring(("00" + minute).length() - 2);
		
		String strhour   = ("00" + hour ).substring(("00" + hour).length() - 2);
		return strhour + " : " + strMinute + " : " + strSecond + " : " + strMillisecond;
	}
	@Override
	public void run() {
		timeHandler.postDelayed(this, 200);
		time = new Date().getTime() - startTime + 100000;
		timeView.setText(getFormatTime(time));
	}
	//瀹氫箟涓�釜Handler   
    public Handler mHandler = new Handler(){  
        public void handleMessage(Message msg){  
        	Bundle bundle = msg.getData();
        	int s =bundle.getInt("percent");
           ProgressBar.setProgress(s);
           process.setText(""+s+"%");

        }  
    };  
}
