package com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WriteSimpleInfoDB {
	
	static public void WriteSimpleInfoIntoDatabase(Context context, String pUsername, String pPassword){
		MemberDAO memberDAO;
	    SQLiteDatabase db;
		memberDAO = new MemberDAO(context);
        db = memberDAO.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MemberDAO.KEY_NAME, pUsername);
        contentValues.put(MemberDAO.KEY_PASSWORD, pPassword);
        db.insert(MemberDAO.TABLE_NAME, null, contentValues);
        db.close();
	}
}
