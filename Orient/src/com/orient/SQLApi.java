package com.orient;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLApi extends SQLiteOpenHelper{
	Context context;
	public SQLApi(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public SQLApi(Context context) {
		
		super(context, "Orient.db", null, 1);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL("create table if not exists Route("+
				"RouteId integer primary key autoincrement,"+
				"missions integer,"+
				"numpergroup integer,"+
				"roomname text,"+
				"date text"+
				");");
		
		db.execSQL("create table if not exists Missions("+
					"RouteId integer,"+
					"missionseq integer,"+
					"latitude integer,"+
					"longitude integer);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<HashMap<String, Object>> queryRoute(){
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLApi sqlapi = new SQLApi(context);
		SQLiteDatabase orientDatabase = sqlapi.getReadableDatabase();
		try {
			Cursor cursor=orientDatabase.rawQuery("select * from Route;",null);
			while(cursor.moveToNext()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("Routeid",cursor.getInt(0));
				//System.out.println("qqqqqqqqqqqqqqqqqqqqq"+cursor.getInt(0));
				map.put("numpergroup", cursor.getInt(2));
				map.put("roomname", cursor.getString(3));
				map.put("date", cursor.getString(4));
				list.add(map);
			}
			cursor.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			orientDatabase.close();
			sqlapi.close();
			return list;
		}
	}
	
	
	public int maxrouteid(){
		SQLApi sqlapi = new SQLApi(context);
		SQLiteDatabase orientDatabase = sqlapi.getReadableDatabase();
		int i = 0;
		try {
			Cursor cursor=orientDatabase.rawQuery("select count(*) from Route;",null);
			cursor.moveToFirst();
			i = cursor.getInt(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			
			orientDatabase.close();
			sqlapi.close();
			return i;
		}
	}
	
   public void insertRoute(setRouteOverlay overlay,String roomname,int numpergroup, String date){
	   SQLApi sqlapi = new SQLApi(context);
	   SQLiteDatabase orientDatabase = sqlapi.getWritableDatabase();
	   try {
		   ContentValues cv = new ContentValues();
		   cv.put("missions", overlay.size());
		   cv.put("roomname", roomname);
		   cv.put("numpergroup", numpergroup);
		   cv.put("date", date);
		   long routeid = orientDatabase.insert("Route", null, cv);
		   System.out.println(routeid);
		   cv.clear();
		   System.out.println("overlay.size() = "+overlay.size());
		   for (int i = 0; i < overlay.size(); i++) {
			cv.put("RouteId", routeid);
			cv.put("missionseq", i+1);
			cv.put("latitude", overlay.getItem(i).getPoint().getLatitudeE6());
			cv.put("longitude", overlay.getItem(i).getPoint().getLongitudeE6());
			orientDatabase.insert("Missions", null, cv);
			System.out.println("insert mission"+i);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
	   finally{
		   orientDatabase.close();
		   sqlapi.close();
	   }
   }

   public ArrayList<GeoPoint> queryMissions(int _routeid){
	   ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
	   SQLApi sqlapi = new SQLApi(context);
	   SQLiteDatabase orientDatabase = sqlapi.getReadableDatabase();
	   try {
		Cursor cursor;//=orientDatabase.rawQuery("select count(*) from Route;",null);
		
		
		cursor=orientDatabase.query("Missions", null, "RouteId=?", new String[]{((Integer)_routeid).toString()}, null, null, "missionseq");
		while(cursor.moveToNext()){
			list.add(new GeoPoint(cursor.getInt(2),cursor.getInt(3)));
			System.out.println("add mission");
		}
		cursor.close();
	   } catch (Exception e) {
		// TODO: handle exception
		   System.out.println(e+"\nquerymissions");
	}
	   finally{
		   
		   orientDatabase.close();
		   sqlapi.close();
		   System.out.println("ret  "+list.size());
		   return list;
	   }
   }
   
}
