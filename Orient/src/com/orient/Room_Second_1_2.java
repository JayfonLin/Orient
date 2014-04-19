package com.orient;
import android.R.integer;
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.res.Configuration;  
import android.location.LocationManager;
import android.os.Bundle;  
import android.os.Handler;
import android.os.Message;
import android.view.Menu;  
import android.view.Window;
import android.widget.Toast;
import android.provider.Settings;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;  
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;  
import com.baidu.mapapi.map.MapController;  
import com.baidu.mapapi.map.MapPoi;  
import com.baidu.mapapi.map.MapView;  
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.constant.Constant;
import com.network.InsertRoute;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Room_Second_1_2 extends Activity {
	private ImageButton next;
	private ImageButton backHomeImageButton;
	private String roomNameString;
	BMapManager mBMapMan = null;  
	MapView mMapView = null;
	MapController mMapController=null;
	public static Room_Second_1_2 context;
	Handler handler;
	MyLocationOverlay myposOverlay;
	setRouteOverlay missionOverlay;
	setRouteOverlay gettogetherOverlay;
	ImageButton getlocationbtn;
	boolean isgettogetherposset;
	public BDLocationListener myListener;
	public LocationClient mLocationClient = null;
	int numpergroup;
	String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init("Kbe7fy7M05PhdOboeeRkkibv", null);
		setContentView(R.layout.room_second_1_2);
		
		//传递房间名字
		Intent intent = getIntent();
		roomNameString = intent.getStringExtra("roomName");
		numpergroup = intent.getIntExtra("numpergroup",0);
		date = intent.getStringExtra("date");
		
		//System.out.println("yyyyyyyyyyyyy"+roomNameString+numpergroup+date);
		
		mMapView=(MapView)findViewById(R.id.bmapsView);  
		mMapView.setBuiltInZoomControls(false);  
		//设置启用内置的缩放控件  
		mMapController=mMapView.getController();  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
		context = this;
		Drawable mark = getResources().getDrawable(R.drawable.myposition_big_shine);
		myposOverlay = new MyLocationOverlay(mMapView);
		myposOverlay.setMarker(mark);
		mMapView.getOverlays().add(myposOverlay);
		myListener = new _MyLocationListener();
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.setAK("90ehkP9tBULpKYG8rbwXffjG");
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
		
		mark = getResources().getDrawable(R.drawable.nextmission);
		missionOverlay = new setRouteOverlay(mark, mMapView);
		mMapView.getOverlays().add(missionOverlay);
		
		mark = getResources().getDrawable(R.drawable.pinmiddleblue);
		gettogetherOverlay = new setRouteOverlay(mark, mMapView);
		mMapView.getOverlays().add(gettogetherOverlay);
		getlocationbtn = (ImageButton)findViewById(R.id.getlocationbtn);
		isgettogetherposset = false;
		mMapController.setZoom(18);//设置地图zoom级别
		mMapController.setOverlooking(-37);
		
		MKMapTouchListener mapTouchListener = new MKMapTouchListener(){  
	        @Override  
	        public void onMapClick(GeoPoint point) {  
	            //在此处理地图单击事件  
	        	//Toast.makeText(Room_Second_1_2.context, point.toString(), 3000).show();
	        	
	        }  
	  
	        @Override  
	        public void onMapDoubleClick(GeoPoint point) {  
	            //在此处理地图双击事件  
	        }  
	  
	        @Override  
	        public void onMapLongClick(final GeoPoint point) {  
	            //在此处理地图长按事件   
	        	String[] string = !isgettogetherposset?new String[]{"设置召集地点","取消"}: new String[]{"设置关卡","取消"};
	        	//Toast.makeText(Room_Second_1_2.this.getParent(), point.toString(), 3000).show();
	        	//这里要注意是第二层，有两层parent要get！！！！！！
	        	new AlertDialog.Builder(Room_Second_1_2.this).setItems(string, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						switch (arg1) {
						case 0:{
							if(isgettogetherposset){
								//Drawable _mark = getResources().getDrawable(R.drawable.misson);
								OverlayItem missionItem=new OverlayItem(point, "mission", "mission");
								//missionItem.setMarker(_mark);
								missionOverlay.addItem(missionItem);
								mMapView.refresh();
							}else {
								//Drawable _mark = getResources().getDrawable(R.drawable.missionstart);
								OverlayItem missionItem=new OverlayItem(point, "gettogetherpos", "gettogetherpos");
								//missionItem.setMarker(_mark);
								gettogetherOverlay.addItem(missionItem);
								isgettogetherposset = true;
								mMapView.refresh();
							}
							break;
						}
						
						default:
							break;
						}
					}
				}).show();
	        		
	        	
	        }  
	    };  
	    mMapView.regMapTouchListner(mapTouchListener);  
	    
	    //“上一步”按钮会导致页面混乱，所以已经去掉，代码暂时不改动
//		back = (ImageButton) findViewById(R.id.Back);
//		back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent backIntent = new Intent();
//				backIntent.setClass(Room_Second_1_2.this, Room_Second_1.class);
//		        startActivity(backIntent);
//			}
//		});
		next = (ImageButton) findViewById(R.id.Next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(missionOverlay.size()==0||gettogetherOverlay.size()==0){
					new AlertDialog.Builder(context).setMessage("至少设置起始地点和一个关卡").setPositiveButton("确定", null).create().show();
					return;
				}
				SQLApi sqlApi = new SQLApi(getApplicationContext());
				gettogetherOverlay.addItem(missionOverlay.getAllItem());
				//missionOverlay.addItem(gettogetherOverlay.getItem(0));
				sqlApi.insertRoute(gettogetherOverlay,roomNameString,numpergroup,date);
				GlobalVarApplication gva = (GlobalVarApplication)getApplication();
				gva.uploadRoute = gettogetherOverlay;
				new InsertRoute(gva.uploadRoute, gva.getRemoteIdHandler, gva.httpClient);
				Intent backIntent = new Intent();
				backIntent.putExtra("roomName", roomNameString);
				int routeid = sqlApi.maxrouteid();
				backIntent.putExtra("Routeid", routeid);
				backIntent.setClass(Room_Second_1_2.this, Room_Second_2.class);
		        startActivity(backIntent);
		        //finish();
			}
		});
		
		backHomeImageButton = (ImageButton)findViewById(R.id.back_home);
		backHomeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Room_Second_1_2.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
	protected void onStart() {
		
		LocationClientOption option = new LocationClientOption();
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
//			else 
//				Toast.makeText(GameMap.context,"在线请求失败，没开GPS？", 2000).show();
		}else{
			if (mLocationClient != null && mLocationClient.isStarted())
				mLocationClient.requestOfflineLocation();
		}
		
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
	    			
	    			mMapController.animateTo(new GeoPoint((int)(locData.latitude*1e6),  
	    					(int)(locData.longitude* 1e6)));
	    			mMapController.setZoom(18);//设置地图zoom级别
	    			mMapController.setOverlooking(-37);
	    			mMapView.refresh();
	    		}else {
					Toast.makeText(context, "努力定位ing...", 2000).show();
				}
	    	}
		};

		

		getlocationbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ContentResolver resolver = context.getContentResolver();
				Boolean on =Settings.Secure.isLocationProviderEnabled(resolver,LocationManager.GPS_PROVIDER);
				if(on){
					if(mLocationClient != null && mLocationClient.isStarted())
						mLocationClient.requestLocation();
					else 
//						Toast.makeText(GameMap.context,"在线请求失败，没开GPS？", 2000).show();
						Toast.makeText(GameMap.context,"在线请求失败，没开GPS？", Toast.LENGTH_SHORT).show();
				}else{
					if (mLocationClient != null && mLocationClient.isStarted())
						mLocationClient.requestOfflineLocation();
				}
			}
		});
		
		
		
		
//		GeoPoint point =new GeoPoint((int)(23.066667* 1E6),(int)(113.397542* 1E6)); 
//		OverlayItem myposItem = new OverlayItem(point, "mypos", "mypos");
//		myposOverlay.removeAll();
//		myposOverlay.addItem(myposItem);
//		mMapView.refresh();
//		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
//		mMapController.setCenter(point);//设置地图中心点  
//		mMapController.setZoom(18);//设置地图zoom级别
//		mMapController.setOverlooking(-37);
		super.onStart();
	};
	
	@Override  
	protected void onDestroy(){  
	        mMapView.destroy();  
	        if(mBMapMan!=null){  
	                mBMapMan.destroy();  
	                mBMapMan=null;  
	        }  
	        mLocationClient.stop();
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
	
	
	class _MyLocationListener implements BDLocationListener {
		
	    @Override
	   public void onReceiveLocation(BDLocation location) {
	    	Message msg = new Message();
	    	Bundle b = new Bundle();
	    	String result;
	      if (location == null){
		    	  result="nothing";
		    	  b.putString("result", result);
		    	  msg.setData(b);					
		      	  GameMap.context.handler.sendMessage(msg);
		          
	      }
	      else{
	    	  result="location";
	    	  b.putString("result", result);
	    	  b.putDouble("latitude", location.getLatitude());
	    	  b.putDouble("longitude", location.getLongitude());
	    	  b.putFloat("direction",location.getDerect());
	    	  
	    	  msg.setData(b);					
	    	  Room_Second_1_2.context.handler.sendMessage(msg);
	      }
	      
	    }
	public void onReceivePoi(BDLocation poiLocation) {
	         if (poiLocation == null){
	                return ;
	          }
	}
			

	}

}