package com.znvoid.demo.net;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.znvoid.demo.daim.ClientScanResultSO;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.util.TCPData;
import com.znvoid.demo.util.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class SearchThread extends Thread {
	public static final int SEARCH_FINSH=0x3001;
	public static final int SEARCH_FAIL=0x3002;
	public static final int SEARCH_SUCCESS=0x3003;
	public static final int SEARCH_TEST=0x3004;//测试数据
	
	private static List<String> ipStrings=new ArrayList<String>();
	private static List<Contact> deviceResult=new ArrayList<Contact>();
	private Handler mHandler;
	private static String message;
	private	static  Handler pHandler=new Handler(){
		
		public void handleMessage(Message msg) {
			
				switch (msg.what) {
				case SEARCH_SUCCESS:
					ipStrings.add((String) msg.obj);
						break;
			
				case SEARCH_TEST:
					deviceResult.add((Contact) msg.obj);
					
						break;	
}
			
			
			
		}


		
	};
	
	public SearchThread(String message,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.message=message;
	}
	private ArrayList<String> getFrArp() {
		ArrayList<String> datas = new ArrayList<String>();
		
		ArrayList<ClientScanResultSO> al = Utils.getClientList();
		for (int i = 0; i <  al.size(); i++) {
			
			datas.add( al.get(i).getIp());
		}
		return datas;
		
	}
	 public static void conn(String ip) {
		 
	        try {
	        	Log.e("Light", "开始连接"+ip);
	        	Socket client=new Socket();
	            SocketAddress remoteAddr=new InetSocketAddress(ip,12345);
	            client.connect(remoteAddr, 2000);
	            client.setSoTimeout(4000);
	            Log.e("Light", "连接到"+ip);
	            Message msg = pHandler.obtainMessage();
	            msg.what = SEARCH_SUCCESS;
	            msg.obj=ip;
	            pHandler.sendMessage(msg);
	            
	            
	            
	            byte[] buffer=message.getBytes();
	            OutputStream os=client.getOutputStream();
	            os.write(buffer, 0, buffer.length);
	            os.flush();
	            
	            InputStream  in = client.getInputStream();
	            Log.e("Light", "构建输入流完毕");       
	               byte[] buf=new byte[1024*1024];
	            int temp=0;
	            StringBuffer stringBuffer=new StringBuffer();
	           while ((temp = in.read(buf)) !=-1) {
	            	Log.e("Light", "收到服务器检查链接结果");
	            	
	            	
	            	stringBuffer.append(new String(buf, 0,temp));
	            	
	            	
	            }
	            
	        Contact contact=   TCPData.parseJsonConact(stringBuffer.toString());
	           
	           Log.e("Light", contact.getId());
           	Message rmsg = pHandler.obtainMessage();
	            rmsg.what = SEARCH_TEST;
	            rmsg.obj=contact;
	            pHandler.sendMessage(rmsg);
	            client.close();
	            
	        } catch (Exception e) {
	        	 Log.e("Light", "连接失败"+ip);
	        	Message msg = pHandler.obtainMessage();
	            msg.what = SEARCH_FAIL;
	            msg.obj=ip;
	            pHandler.sendMessage(msg);// 
	            
	        }
	        	
			
	    }
	 @Override
	public void run() {
		 ArrayList<String> datasIP ;
		 
		 datasIP=getFrArp();
		 ipStrings.clear();
		deviceResult.clear();
		 try {
			 CountDownLatch latch=new CountDownLatch(datasIP.size());
			 
			 for (int i = 0; i < datasIP.size(); i++) {
				
				 new connThread(latch, datasIP.get(i)).start();
			}
			latch.await();
			Log.e("Light1", "查找完成");
			Message msg = mHandler.obtainMessage();
            msg.what = SEARCH_FINSH;
            msg.obj=ipStrings;
            mHandler.sendMessage(msg);
            Message msgt = mHandler.obtainMessage();
            msgt.what = SEARCH_TEST;
            msgt.obj=deviceResult;
            mHandler.sendMessage(msgt);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		 
		 
	}
	static class connThread extends Thread{
		CountDownLatch mLatch;
		String ip;
		public connThread(CountDownLatch mLatch, String ip) {
			super();
			this.mLatch = mLatch;
			this.ip = ip;
		}
		@Override
		public void run() {
			
			conn(ip);
			
			mLatch.countDown();
			
		}

	}
	
	private static ClientScanResultSO doMsgToClientSRO(String msg){
		String[] temp=msg.split(TCPData.DIV);
		if (temp.length==5&&TCPData.CHATMESSAGE.equals(temp[0])) {
			
			return new ClientScanResultSO(temp[3], temp[4], temp[2], true, temp[1]);
			
		}
		
		return null;
	}
}
