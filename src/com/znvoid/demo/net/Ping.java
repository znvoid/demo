package com.znvoid.demo.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.InetAddress;

import android.os.Message;
import android.util.Log;

public class Ping {
	private boolean isOver = false;
	

	public void pingAll(String hostAddress) {
		// ���ȵõ�������IP���õ�����

		int k = 0;
		k = hostAddress.lastIndexOf(".");
		String ss = hostAddress.substring(0, k + 1);
		for (int i = 2; i <= 254; i++) {
			// ��v���о�����Ip
			String iip = ss + i;
			if (!hostAddress.equals(iip)) {
				MyThreadPool.getInstance().submit(new ping(iip));
			}
		}

		

	}



	


	public boolean isEnd() {
		return !isOver;
	}
	class ping implements Runnable{
		private final String ip;
		public ping(String ip) {
			this.ip=ip;
		}
		@Override
		public void run() {
			byte[] sendBuf = "HF-A11ASSISTHREAD".getBytes();
			try {
				DatagramSocket sendSocket = new DatagramSocket();
				InetAddress broadcastAddress = InetAddress.getByName(ip);
				DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, broadcastAddress, 48899);
				sendSocket.send(sendPacket);
				sendSocket.close();
				Log.e("TAG", ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
