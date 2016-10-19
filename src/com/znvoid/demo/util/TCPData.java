package com.znvoid.demo.util;

import com.znvoid.demo.daim.Chat;

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
	
	public static Chat getChat(String msg) {
		String[] strings=msg.split(DIV);
		Chat chat=null;
		
		
		if (strings.length==7&& strings[0].equals(CHATMESSAGE)) {
			
			chat=new Chat(strings[1], strings[2], 1, strings[4], strings[5],strings[6]);
			
		}
		
		
		return chat;
	}
	
}
