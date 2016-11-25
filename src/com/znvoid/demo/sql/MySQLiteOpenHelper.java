package com.znvoid.demo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "chatdata.db", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS chatdata (chatid integer primary key autoincrement, Contact varchar(20), message TEXT,direction INT,time TEXT)");
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS chatContacts (Contactsid integer primary key autoincrement, Contact varchar(40),name varchar(20),head varchar(20),lastMsg TEXT");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
