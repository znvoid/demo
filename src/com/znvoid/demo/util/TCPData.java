package com.znvoid.demo.util;

import com.znvoid.demo.daim.Chat;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * 根据TCP得到的msg进行分析
 */
public class TCPData {
	public final static String DIV="<== DIV ==>";
	public final static String CHATMESSAGE="CHATMESSAGE V1.0";
	public static String getMsg(Chat chat) {
		String result = CHATMESSAGE;
		
		result = result + DIV + chat.getAuthor() + DIV + chat.getMessage() + DIV + chat.getDirection() + DIV
				+ chat.getHead() + DIV + chat.getIp()+ DIV + chat.getTime();

		return result;
	}
	/**
	 * Sting转Chat,String 为接收到的信息
	 * @param msg
	 * @return
	 */
	public static Chat getChat(String msg) {
		String[] strings=msg.split(DIV);
		Chat chat=null;
		
		
		if (strings.length==7&& strings[0].equals(CHATMESSAGE)) {
			
			chat=new Chat(strings[1], strings[2], 1, strings[4], strings[5],strings[6]);
			
		}
		
		
		return chat;
	}
	
	/**
	 * 为测试连接生成数据
	 * @param context
	 * @return
	 */
	public static String creatTestMessage(Context context) {
		
		String result;
		SharedPreferences sp = context.getSharedPreferences("configs", context.MODE_PRIVATE);

		sp.getString("head", "head_0");
		WifiUtil wifiUtil = new WifiUtil(context);

		result = TCPData.CHATMESSAGE + TCPData.DIV + sp.getString("author", "???") + TCPData.DIV
				+ sp.getString("head", "head") + TCPData.DIV + wifiUtil.getIP() +TCPData.DIV
				+ wifiUtil.getMacAddress();

		return result;
	}
	
}
