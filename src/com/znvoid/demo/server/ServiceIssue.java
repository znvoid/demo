package com.znvoid.demo.server;

import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.daim.RequestHead;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class ServiceIssue {
	private RequestHead mRequestHead ;
	private Contact mContact ;
	private Context  mContext ;
	public ServiceIssue(Context mContext, RequestHead mRequestHead, Contact mContact) {
		super();
		this.mContext = mContext;
		this.mRequestHead = mRequestHead;
		this.mContact = mContact;
		init();
	}
	
	private void init() {
		
		
	}
	

	/**
	 * ���͹㲥
	 * 
	 *
	 */
	public static void sendb(Context context,Contact contact,String action) {
		if (action==null) {
			action="com.zn.demo.CHATMESSAGE";
		}
		
		Intent intent = new Intent(action);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("message", contact);
		intent.putExtras(mBundle);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

	}
	
	public static byte[] fileLenghtDecoed(Long i) {
		String string="                      ";
		String mString="File-Length:"+i+string;
		String mString2=mString.substring(0, 30);
		return mString2.getBytes();
	}
}
