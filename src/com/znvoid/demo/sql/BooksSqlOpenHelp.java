package com.znvoid.demo.sql;

import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.daim.BookImf;
import com.znvoid.demo.daim.Chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksSqlOpenHelp extends SQLiteOpenHelper {

	public BooksSqlOpenHelp(Context context) {
		super(context, "bookdata.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS bookdata (bookid integer primary key autoincrement, name TEXT, path TEXT,ic_path TEXT,mark INT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}
	
	public void add(BookImf book) {
		SQLiteDatabase db = getWritableDatabase();
		if (db.isOpen()) {

			ContentValues cont = new ContentValues();
			cont.put("name", book.getName());
			cont.put("path", book.getPath());
			cont.put("ic_path", book.getIc_path());
			cont.put("mark", book.getMark());
			
			
			db.insert("bookdata", null, cont);
			db.close();
		}
	}

	// ɾ��
		public void delete(BookImf book) {
			SQLiteDatabase db = getWritableDatabase();
			if (db.isOpen()) {
				// db.execSQL("delete from person where name=?", new
				// Object[]{name});

				db.delete("bookdata", "name=? and path=?",
						new String[] { book.getName(),book.getPath()});

				db.close();
			}
		}
		public ArrayList<BookImf> loadall() {
			ArrayList<BookImf> booklist = new ArrayList<BookImf>();
			Cursor cursor = findAllByCursor();
			while (cursor.moveToNext()) {
				//int chatid = cursor.getInt(0); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				String name = cursor.getString(1);// ��ȡ�ڶ��е�ֵ
				String path = cursor.getString(2);// ��ȡ�����е�ֵ
				String  ic_path = cursor.getString(3);// ��ȡ�����е�ֵ
				int mark=cursor.getInt(4);
				booklist.add(new BookImf(path,ic_path,mark));

			}
			cursor.close();

			return booklist;
		}
		// ����cursor
		public Cursor findAllByCursor() {
			SQLiteDatabase db = getReadableDatabase();
			if (db.isOpen()) {
				Cursor cursor = db.query("bookdata", null, null, null, null, null, null);
				return cursor;
			}
			return null;

		}
		public boolean find(String path) {
			 boolean result=false;
		        SQLiteDatabase db=getReadableDatabase();
		        if(db.isOpen()){
		            Cursor cursor=db.rawQuery("select * from bookdata where path=? ", new String[]{path});
		            if(cursor.moveToFirst()){
//		                int index=cursor.getColumnIndex("name");
//		                cursor.getString(index);
		                result=true;

		            }
		            cursor.close();
		            db.close();

		        }

		        return result;
		}
		
		//����
	    public void update(String path,int mark){
	        SQLiteDatabase db=getWritableDatabase();
	        if(db.isOpen()){
	        	 ContentValues cont=new ContentValues();
	        	 Cursor cursor=db.rawQuery("select * from bookdata where path = ? ", new String[]{path});
		            if(cursor.moveToFirst()){
		            	
		                
		            	cont.put("name", cursor.getString(1));
		            	  cont.put("path", path);
		            	  cont.put("ic_path", cursor.getString(3));
		  	            cont.put("mark", mark);
		  	          db.update("bookdata", cont ,"path=?", new String[]{path});
		            }else {
						
		            	
		            	add(new BookImf(path, "deful", mark));
					}
		            cursor.close();
	           
	            db.close();
	        }

	    }
	    
	    public int getMark(String path) {
	    	int mark=0;
	    
	    	 SQLiteDatabase db=getWritableDatabase();
		        if(db.isOpen()){
		        	 
		        	 Cursor cursor=db.rawQuery("select * from bookdata where path = ? ", new String[]{path});
			            if(cursor.moveToFirst()){
			            	
			                mark=cursor.getInt(4);
			           

			            }
			            cursor.close();
		        	}
		        db.close();
		        return mark;
	    }
}
