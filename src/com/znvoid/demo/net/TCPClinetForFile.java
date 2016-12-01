package com.znvoid.demo.net;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.daim.RequestHead;
import com.znvoid.demo.util.FileUtils;
import com.znvoid.demo.util.TCPData;
import android.os.Handler;
import android.util.Log;

/**
 * �����ļ�
 * 
 * @author zn
 *
 */
public class TCPClinetForFile {

	public static final int FILE_FAIL = 0X6001;
	public static final int FILE_SUCCESD = 0X6002;

	private int port = 12345;
	private String ip;
	private Contact mContact;// ������ܵ��ģ������ļ��ɹ�����
	private Contact sContact;// �跢�͵�
	private Handler mHandler;
	private OutputStream outputStream;
	private InputStream in;
	

	public TCPClinetForFile(Contact contact, Handler handler, Contact sContact) {

		this.ip = contact.getIp();
		mContact = contact;
		mHandler = handler;
		this.sContact=sContact;
		
	}

	public void strat() {
		
		new Thread(){@Override
		public void run() {
			
			try {
				initClient(ip);
				send();
				receive();
				
				
			} catch (Exception e) {
				Log.e("TAG", "TCPClinetForFile"+e.toString());
				mHandler.sendMessage(mHandler.obtainMessage(FILE_FAIL));
				
			}
			
			
			
			super.run();
		}}.start();
		
		
		
	}
	
	private void initClient(String ip) throws IOException {

		Selector.open();
		Socket nSocket = new Socket();
		
		
		SocketAddress remoteAddr = new InetSocketAddress(ip, port);
		nSocket.connect(remoteAddr, 3000);
		nSocket.setSoTimeout(2000);
		in = nSocket.getInputStream();
		// inr= new BufferedReader(new InputStreamReader(
		// nSocket.getInputStream())) ;

		outputStream = nSocket.getOutputStream();
		Log.e("socket thread", "TCPClinetForFile:��ȡ˫���ɹ�");

	}

	private void send() throws Exception {
		RequestHead requestHead = new RequestHead();
		requestHead.setheadParam("Content-Type", "message/get");

		String mMsg = TCPData.makeSendDate(requestHead, sContact);
		
		outputStream.write(mMsg.getBytes());
		outputStream.flush();
		Log.e("socket thread", "TCPClinetForFile:����ɹ�");
	}

	private void receive() throws Exception {

		Log.e("TAG", "��ʼ����");
		int length = 0;
		int total = 0;
		byte[] buffer = new byte[30];
		int i = in.read(buffer);
		Log.e("TAG", "�����ײ�"+ new String(buffer));
		if (i == 30) {
			String line = new String(buffer).trim();
			if (line.startsWith("File-Length:")) {
				total = Integer.parseInt(line.substring(12));
				String path = mContact.getLastMsg();
				String fileName = path.substring(path.lastIndexOf("/") + 1);
				File file = FileUtils.creatFileStream(fileName);
				Log.e("TAG","�ļ�·��"+ file.getAbsolutePath());
				mContact.setLastMsg(file.getAbsolutePath());
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int n = 0;
				while (total > length) {
					
					n = in.read(buf);
						length = length + n;
						Log.e("TAG","���ȣ�"+length/(total*1.0));
						fileOutputStream.write(buf, 0, n);
					

				}
				fileOutputStream.flush();
				fileOutputStream.close();
				mHandler.sendMessage(mHandler.obtainMessage(FILE_SUCCESD, mContact));
			}else {
				Log.e("TAG","�ײ�����");
				mHandler.sendMessage(mHandler.obtainMessage(FILE_FAIL));
			}
		}else {
			Log.e("TAG","buff!=30");
			mHandler.sendMessage(mHandler.obtainMessage(FILE_FAIL));
		}

	}

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
}
