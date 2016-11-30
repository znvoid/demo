package com.znvoid.demo.net;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.daim.RequestHead;
import com.znvoid.demo.util.TCPData;

import android.R.integer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 接受文件
 * 
 * @author zn
 *
 */
public class TCPClinetForFile {

	private boolean isConn = false;
	private Selector selector;

	public static final int FILE_FAIL = 0X6001;
	public static final int FILE_SUCCESD = 0X6001;

	private int port = 12345;
	private String ip;
	private Contact mContact;//服务接受到的，接收文件成功返回
	private Contact sContact;//需发送的
	private Handler mHandler;
	private OutputStream outputStream;
	private BufferedReader inr;
	
	public TCPClinetForFile(Contact contact,Handler handler,Contact sContact) {

		this.ip = contact.getIp();
		mContact = contact;
		mHandler=handler;
		
		
	}

	private void initClient(String ip) throws IOException {

		selector = Selector.open();
		Socket nSocket = new Socket();
		SocketAddress remoteAddr = new InetSocketAddress(ip, port);
		nSocket.connect(remoteAddr, 3000);
//		 inputStream=nSocket.getInputStream();
		 inr=	new BufferedReader(new InputStreamReader(
				nSocket.getInputStream())) ;
		 
		outputStream=nSocket.getOutputStream();
		Log.e("socket thread", "TCPClinetForFile:获取双流成功");
		
	}

	private void send() throws Exception {
		 RequestHead requestHead=new RequestHead();
         requestHead.setheadParam("Content-Type", "message/get");
       
         
         String mMsg= TCPData.makeSendDate(requestHead, sContact);
		
		outputStream.write(mMsg.getBytes());
		outputStream.flush();
		Log.e("socket thread", "TCPClinetForFile:输出成功");
	}
	private void receive()throws Exception{
		int length=0;
		int total=0;
		
		String line=inr.readLine();
		if (line.startsWith("Fiel-Lenght:")) {
			total=Integer.parseInt(line.substring(12,line.length()));
			
			char[] cs=new char[1024];
			while (total>=length) {
				inr.read(cs);
				
			}
			
			
		}
		
		
		
		
		
	}
	

}
