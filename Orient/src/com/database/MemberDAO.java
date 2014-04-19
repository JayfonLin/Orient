package com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemberDAO extends SQLiteOpenHelper{
	public static final int DB_VERSION = 1;
    public static final String DB_NAME = "member.db";
    public static final String TABLE_NAME = "Members";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "_id";
    private SQLiteDatabase db;
    public MemberDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        this.db.execSQL(" create table " + TABLE_NAME +
                " (_id integer primary key autoincrement, " +KEY_NAME+
                " text, " + KEY_PASSWORD+
                " text )"); 
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        
    }
}
