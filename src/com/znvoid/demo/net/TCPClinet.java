package com.znvoid.demo.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
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

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TCPClinet extends Thread
 {

	public static final int CLIENT_SEND_SUCCSSED = 0X2003;
	public static final int CLIENT_SEND_FAIL = 0X2002;
	public static final int CLIENT_RECEIVED_MESSAGE = 0X2001;
	public static final int CLIENT_CONN_FIAL = 0X2004;
	public static final int CLIENT_CONN_SUCC = 0X2005;

	
	private Set<Works> works = new HashSet<TCPClinet.Works>();

	private Map<String, SocketChannel> clentChannels = new HashMap<String, SocketChannel>();

	private static boolean isRun = false;
	private int port = 12345;

	private Set<String> clientset = new HashSet<String>();
	private Selector selector;

	private Handler mHandler;
	

	public TCPClinet(Handler handler) {
		mHandler=handler;
		isRun=true;
	}

	



	public void sendMessage(String ip, String msg) {
		works.add(new Works(ip, msg));
		
		
	}

	private void initClient(String ip) throws IOException {
//		// 获得一个Socket通道
//		SocketChannel clientChannel = SocketChannel.open();
//		// 设置通道为非阻塞
		//clientChannel.configureBlocking(false);
		// SocketAddress remoteAddr=new InetSocketAddress(ip,port);
	//	clientChannel.connect(remoteAddr);	
		
		
		Socket nSocket=new Socket();
         SocketAddress remoteAddr=new InetSocketAddress(ip,port);
         nSocket.connect(remoteAddr, 3000);
         SocketChannel clientChannel=nSocket.getChannel();
		clientChannel.configureBlocking(false);
		

		
		
	
	clientChannel.register( selector, SelectionKey.OP_CONNECT);
		    Log.e("socket thread", "----222-------");
	

		
		
	
		clentChannels.put(ip, clientChannel);
		
	}

	private void send(String ip, String message) throws Exception {

		SocketChannel socketChannel = clentChannels.get(ip);
		socketChannel.write(ByteBuffer.wrap(message.getBytes()));
	}

	@Override
	public void run() {

		try {
			selector = Selector.open();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		while (isRun) {

			if (works != null) {

				Iterator<Works> workIterator = works.iterator();

				if (workIterator.hasNext()) {

					Works work = workIterator.next();
					String ip = work.getIp();
					String sendMessage = work.getMessage();

					if (clentChannels.containsKey(ip)) {

						try {
							send(ip, sendMessage);
							mHandler.sendMessage(mHandler.obtainMessage(CLIENT_SEND_SUCCSSED, sendMessage));
						} catch (Exception e) {
							Log.e("socket thread", "sendfail1");
							mHandler.sendEmptyMessage(CLIENT_SEND_FAIL);
							e.printStackTrace();
						}

					} else {

						try {
							initClient(ip);
							send(ip, sendMessage);
							mHandler.sendMessage(mHandler.obtainMessage(CLIENT_SEND_SUCCSSED, sendMessage));
						} catch (Exception e) {
							Log.e("socket thread", "sendfail2"+e.toString());
							mHandler.sendEmptyMessage(CLIENT_SEND_FAIL);
							e.printStackTrace();
						}

					}

					workIterator.remove();

				}
			}
			int n = 0;
			try {
				n = selector.select();
			} catch (IOException e) {

				e.printStackTrace();
			}

			if (n == 0) {

				continue;
			}

			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();

				if (key.isAcceptable()) {

				} else if (key.isConnectable()) {
					
					//Log.e("socket thread", "isConnectable");
//					SocketChannel socketChannel=(SocketChannel) key.channel();
//					
//					clentChannels.remove(socketChannel.socket().getInetAddress());
					//key.cancel();
				} else if (key.isReadable()) {

				} else if (key.isWritable()) {

				}
				it.remove();
			}

		}

	}

	class Works {
		private String ip;
		private String message;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Works(String ip, String message) {
			super();
			this.ip = ip;
			this.message = message;
		}

	}

}
