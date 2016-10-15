package com.znvoid.demo.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;



import android.os.Message;
import android.util.Log;

public class Ping {
	private boolean isOver=false;
	private int threadCount;
	public void pingAll(String hostAddress)
	{
		// ���ȵõ�������IP���õ�����
		
			int k = 0;
			k = hostAddress.lastIndexOf(".");
			String ss = hostAddress.substring(0, k + 1);
			for (int i = 1; i <= 250; i++)
			{
				// ��v���о�����Ip
				String iip = ss + i;
				if(!hostAddress.equals(iip)){
				  ping1(iip);
				}
			}

			// ��������Ping����
			while (!isOver)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (threadCount == 0)
				{
					isOver = true;
					
					
				}
			}
		
	}

	public void ping1(String ip)
	{
		// ���30���߳�
		try
		{
			while (threadCount > 255)
			{
				Thread.sleep(2);
			}
			threadCount += 1;
			runPingIPprocess(ip);
		} catch (Exception e)
		{
			Log.e("Light1", e.getLocalizedMessage());
		}
	}

	public void runPingIPprocess(final String ipString)
	{
		final Thread pingThread = new Thread()
		{
			@Override
			public void run()
			{
				byte[] sendBuf="HF-A11ASSISTHREAD".getBytes();
				try
				{
					//InetAddress.getByName(ipString).isReachable(500);
//					 URL url = new URL(ipString);
//					 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					 conn.connect();
//					 sleep(5);
//					 //conn.disconnect();
					// ����
					DatagramSocket sendSocket = new DatagramSocket();                   
				    InetAddress broadcastAddress=InetAddress.getByName(ipString);                    
				    DatagramPacket sendPacket=new DatagramPacket(sendBuf,sendBuf.length,broadcastAddress,48899);    
				    sendSocket.send(sendPacket);
				    sleep(200);
				    sendSocket.close();
				    // ����
				    /*byte[] receiveBuf = new byte[64];
				    DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);                   
				    sendSocket.receive(receivePacket); 
				    // ���Wifiģ��ظ�������IP
				    InetAddress address = receivePacket.getAddress();
				    String data = receivePacket.getData().toString();
				    System.out.println(address);*/
				    
					threadCount--;
				} catch (Exception e)
				{
					threadCount--;
					Log.e("Light", e.getLocalizedMessage());
				} finally
				{
					
				}
			}
		};
		pingThread.start();
	}

	/** �����l�͵���ϢMessage **/
	public static Message createMessage(int what, Object object)
	{
		Message message = new Message();
		message.what = what;
		message.obj = object;
		return message;
	}
	public boolean isEnd() {
		return !isOver;
	}
	
}
