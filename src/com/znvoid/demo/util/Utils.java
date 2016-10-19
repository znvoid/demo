package com.znvoid.demo.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.znvoid.demo.daim.ClientScanResultSO;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utils {
	public static Bitmap getRes(Context context, String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	public static ArrayList<ClientScanResultSO> checkClient(List<ClientScanResultSO> list) {
		
		if (list.size()==0) {
			return null;
		}
		
		ArrayList<ClientScanResultSO> result = getClientList();

		for (int i = 0; i < result.size(); i++) {

			for (int j = 0; j < list.size(); j++) {
				if (result.get(i).getIp().equals(list.get(j).getIp())) {
					result.set(i, list.get(j));
					break;

				}

			}

		}

		return result;
	}

	public static ArrayList<ClientScanResultSO> getClientList() {
		BufferedReader br = null;
		ArrayList<ClientScanResultSO> result = null;

		try {
			result = new ArrayList<ClientScanResultSO>();
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");

				if ((splitted != null) && (splitted.length >= 4)) {

					String mac = splitted[3];

					if (mac.matches("..:..:..:..:..:..") && !"00:00:00:00:00:00".equals(mac)) {

						result.add(new ClientScanResultSO(splitted[0], splitted[3], "head_0", false, "???"));
					}
				}
			}

		} catch (Exception e) {
			Log.e("getClientList", e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Log.e("getClientList", e.getMessage());
			}
		}

		return result;
	}
	public static String getSysTime() {
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("  yyyy年MM月dd日    HH:mm:ss  ");       
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
		String    str    =    formatter.format(curDate);
		return str;
	}
}
