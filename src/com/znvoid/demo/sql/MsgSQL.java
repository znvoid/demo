package com.znvoid.demo.sql;

import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.daim.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MsgSQL extends MySQLiteOpenHelper {

	public MsgSQL(Context context) {
		super(context);

	}

	// ����
	public void add(Contact contact) {
		SQLiteDatabase db = getWritableDatabase();
		if (db.isOpen()) {

			ContentValues cont = new ContentValues();
			cont.put("contact", contact.getId());
			cont.put("message", contact.getLastMsg());
			cont.put("time", contact.getTime());
			cont.put("direction", contact.getDirection());
			cont.put("type", contact.getMsgType());

			db.insert("messagedata", null, cont);

			// chatcontacts �����

			Cursor cursor = db.rawQuery("select * from chatContacts where contact = ? ",
					new String[] { contact.getId() });
			if (cursor.moveToFirst()) {
				ContentValues cont1 = new ContentValues();
				// int Contactsid = cursor.getInt(0); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				// String id = cursor.getString(1);// ��ȡ�ڶ��е�ֵ
				// String name = cursor.getString(2);// ��ȡ��3�е�ֵ
				// String head = cursor.getString(3);// ��ȡ��4�е�ֵ
				// String lastMsg = cursor.getString(4);// ��ȡ��5�е�ֵ
				// String time = cursor.getString(5);// ��ȡ��5�е�ֵ
				// String ip = cursor.getString(6);// ��ȡ��5�е�ֵ
				// String type = cursor.getString(7);// ��ȡ��5�е�ֵ
				// int direction = cursor.getInt(8);// ��ȡ�����е�ֵ

				cont1.put("contact", contact.getId());
				cont1.put("name", contact.getName());
				cont1.put("head", contact.getHead());
				cont1.put("ip", contact.getIp());

				db.update("chatContacts", cont1, "contact=?", new String[] { contact.getId() });

			} else {

				ContentValues cont1 = new ContentValues();
				cont1.put("contact", contact.getId());
				cont1.put("name", contact.getName());
				cont1.put("head", contact.getHead());
				cont1.put("ip", contact.getIp());

				db.insert("chatContacts", null, cont1);

				//

			}

			cursor.close();

			db.close();
		}
	}

	// ɾ��
	public void delete(String id, Chat chat) {
		SQLiteDatabase db = getWritableDatabase();
		if (db.isOpen()) {
			// db.execSQL("delete from person where name=?", new
			// Object[]{name});

			db.delete("messageData", "contact=? and message=? and direction=? and time=?",
					new String[] { id, chat.getMessage(), String.valueOf(chat.getDirection()), chat.getTime() });

			db.close();
		}
	}

	// ��ȡ��������
	public List<Contact> loadContacts() {
		List<Contact> listContact = new ArrayList<Contact>();
		Cursor cursor = findAllByCursor();
		while (cursor.moveToNext()) {
			// Contact TEXT,name varchar(20),head varchar(20),lastMsg TEXT,ip
			// TEXT,type varchar(20)"
			int Contactsid = cursor.getInt(0); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
			String id = cursor.getString(1);// ��ȡ�ڶ��е�ֵ
			String name = cursor.getString(2);// ��ȡ��3�е�ֵ
			String head = cursor.getString(3);// ��ȡ��4�е�ֵ
			String ip = cursor.getString(4);// ��ȡ��5�е�ֵ
			String lastMsg = "NULL";
			String time = "NULL";
			String type = "NULL";
			int direction = -1;
			Cursor cursor1 = findAllByCursor(id);
			if (cursor1 != null) {
				if (cursor1.moveToLast()) {

					lastMsg = cursor1.getString(2);
					time = cursor1.getString(3);
					type = cursor1.getString(4);
					direction = cursor1.getInt(5);

					cursor1.close();
				}
			}

			listContact.add(new Contact(id, name, head, lastMsg, time, ip, type, direction));

		}
		cursor.close();

		return listContact;
	}

	/**
	 * 
	 * @param contact
	 * @return
	 */
	public List<Chat> loadMsg(Contact contact) {
		List<Chat> list = new ArrayList<Chat>();
		Cursor cursor = findAllByCursor(contact.getId());
		while (cursor.moveToNext()) {
			//
			int msgid = cursor.getInt(0); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
			// String name = cursor.getString(1);// ��ȡ�ڶ��е�ֵ
			String message = cursor.getString(2);// ��ȡ��3�е�ֵ
			String time = cursor.getString(3);// ��ȡ��4�е�ֵ
			int direction = cursor.getInt(4);// ��ȡ�����е�ֵ

			list.add(new Chat(contact.getName(), message, direction, contact.getHead(), contact.getIp(), time));

		}
		cursor.close();

		return list;
	}

	// ����cursor
	/**
	 * 
	 * @return ����������ϵ��cursor
	 */
	public Cursor findAllByCursor() {
		SQLiteDatabase db = getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("chatContacts", null, null, null, null, null, null);
			return cursor;
		}
		return null;

	}

	// ����cursor
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Cursor findByCursor(String id) {
		SQLiteDatabase db = getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("chatContacts", null, "contact=?", new String[] { id }, null, null, null);
			return cursor;
		}
		return null;

	}

	/**
	 * ������ϵ��id �����е������¼
	 * 
	 * @param id
	 * @return
	 */
	public Cursor findAllByCursor(String id) {
		SQLiteDatabase db = getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("messageData", null, "contact=?", new String[] { id }, null, null, null);
			return cursor;
		}
		return null;

	}

	public Cursor findAllMsgDateByCursor() {
		SQLiteDatabase db = getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("messageData", null, null, null, null, null, null);
			return cursor;
		}
		return null;
	}
/**
 * Ϊ����豸����ʱ����contact ��ip����Ϣ
 * @param contact
 */
	public void updataContact(Contact contact) {
		
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor =db.rawQuery("select * from chatContacts where contact = ? ",
				new String[] { contact.getId() });
		if (cursor.moveToFirst()) {
			ContentValues cont = new ContentValues();
			cont.put("contact", contact.getId());
			cont.put("name", contact.getName());
			cont.put("head", contact.getHead());
			cont.put("ip", contact.getDirection());

			db.update("chatContacts", cont, "contact=?", new String[] { contact.getId() });
			cursor.close();
		}
		
		
		db.close();
	}
	
	
}
