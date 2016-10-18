package com.znvoid.demo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {
	public static Bitmap getRes(Context context,String name) {
		ApplicationInfo appInfo = context .getApplicationInfo();
		int resID =context. getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
		}
}
