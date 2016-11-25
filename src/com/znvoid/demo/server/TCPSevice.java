package com.znvoid.demo.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;


import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.sql.ChatSqlOpenHelp;
import com.znvoid.demo.util.TCPData;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class TCPSevice extends Service {

	private Handler mHandler;
	
	private Context context;
	
	public static final int TCPSEVICE_RECEIVE=0X1FF;
	
	ServerSocket serverSocket;
	Selector selector;

	private ChatSqlOpenHelp sqlOpenHelp;
	@Override
	public void onCreate() {
		Log.e("TCPServer", "onCreate");
		context=getApplicationContext();
		sqlOpenHelp=new ChatSqlOpenHelp(context);
		new TCPThread().start();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Log.e("TCPServer", "onDestroy");
		try {
			serverSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			selector.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		
		Log.e("TCPServer", "bandign");
		return new MyIBiner();
	}

	
	@Override
	public void unbindService(ServiceConnection conn) {
		Log.e("TCPServer", "unbindService");
		super.unbindService(conn);
	}
	
	class MyIBiner  extends Binder implements  ISevice{

		@Override
		public void setHandlerr(Handler handler) {
			
			addHandler( handler);
		}

		
		
		
	}
	
	private void addHandler(Handler mHandler) {
		
		this.mHandler=mHandler;
	}
	private void clearHandler() {
		mHandler=null;
	}
	
	

	
	
	 class TCPThread extends Thread {
		/**
		 * 默认服务器端口
		 */
		

		 private  final int DEFAULT_PORT = 12345;
		public static final String CHATTEST = "CHATMESSAGE V1.0 CHATTEST";
		public final String TAG = "TCPServer";
		
		private boolean isRun = true;
		

		

		private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

	

		public TCPThread() {
			super();

		}

		@Override
		public void run() {
			super.run();
			try {
				doit();
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				//isRun = false;
				
				try {
					serverSocket.close();
					Log.e(TAG, "server stop");
					//handmessage("server stop", SERVER_STOP);
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		}

		
		private void doit() throws Exception {

			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			 serverSocket = serverChannel.socket();

			serverSocket.bind(new InetSocketAddress(DEFAULT_PORT));

			serverChannel.configureBlocking(false);

			 selector = Selector.open();

			// Register the ServerSocketChannel with the Selector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (isRun) {
				
				int n = selector.select();
			
				if (n == 0) {
					// nothing to do
					continue;
				}

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();

				while(it.hasNext()){
				SelectionKey key = (SelectionKey) it.next();

				if (key.isAcceptable()) {
					Socket socket = ((ServerSocketChannel) key.channel()).accept().socket();
					Log.e(TAG, "接收到" + socket);
					SocketChannel sc = socket.getChannel();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
				} else if (key.isConnectable()) {
					Log.e(TAG, "Connectable");
					
					String ip=((SocketChannel) key.channel()).socket().getInetAddress().getHostAddress();
					
					//handmessage(ip, SERVER_CLIENT_UNCOON);
					
					
					
					key.cancel();
				} else if (key.isReadable()) {
					Log.e(TAG, "Readable");
					readDataFromSocket(key);

				} else if (key.isWritable()) {

				}
				it.remove();
				}
			}

		}

		private void readDataFromSocket(SelectionKey key) throws Exception {

			SocketChannel socketChannel = (SocketChannel) key.channel();
			if (!socketChannel.isConnected()) {
				return;
			}
			int count;
			buffer.clear();

			while ((count = socketChannel.read(buffer)) > 0) {
				buffer.flip(); // Make buffer readable

//				String re = Charset.forName("gbk").decode(buffer).toString();
				 String re=Charset.forName("UTF-8").decode(buffer).toString();
				if (CHATTEST.equals(re)) {
					
					socketChannel.write(ByteBuffer.wrap(TCPData.creatTestMessage(context).getBytes()));
					key.cancel();
					
				}else {
					Chat chat = TCPData.getChat(re);
					Log.e(TAG, re);
					if (chat!=null) {
						sqlOpenHelp.add(chat);
						if (mHandler!=null) {
							
							Message msg = mHandler.obtainMessage();
							msg.what = TCPSEVICE_RECEIVE;
							msg.obj = chat;
							mHandler.sendMessage(msg);
						}
					}
					
					
				}
				

				buffer.clear(); // Empty buffer

			}
			if (count < 0) {
				// Close channel on EOF, invalidates the key
				socketChannel.close();
			}

		}

		
	 }
	
}
