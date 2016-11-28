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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.znvoid.demo.daim.Chat;

import android.R.integer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TCPClinet 
 {

	

	private ClientListener mListener;
	private String ip;
	private ExecutorService mExecutorService;
	private boolean isConn=false;
	private Selector selector;
	
	
	
	
	



	
	private int port = 12345;


	

	
	

	public TCPClinet(final String ip,ClientListener listener){
		mExecutorService=Executors.newCachedThreadPool();
		this.ip=ip;
		mListener=listener;
		mExecutorService.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					initClient(ip);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			}
		});
	}

	



	public void sendMessage(String ip, String msg) {
		
		
		
	}

	private void initClient(String ip) throws IOException {
		// 获得一个Socket通道
		SocketChannel clientChannel = SocketChannel.open();
		// 设置通道为非阻塞
		clientChannel.configureBlocking(false);
		 SocketAddress remoteAddr=new InetSocketAddress(ip,port);
	isConn=	clientChannel.connect(remoteAddr);	
		
//		selector=Selector.open();
//		Socket nSocket=new Socket();
//         SocketAddress remoteAddr=new InetSocketAddress(ip,port);
//         nSocket.connect(remoteAddr, 3000);
//         SocketChannel clientChannel=nSocket.getChannel();
//		clientChannel.configureBlocking(false);
	if (isConn){
		    clientChannel.register( selector, SelectionKey.OP_READ);
		}else{

		    clientChannel.register( selector, SelectionKey.OP_CONNECT);
		}
		
		
		
	clientChannel.register( selector, SelectionKey.OP_CONNECT);
		    Log.e("socket thread", "----222-------");
		    isConn=true;
		  while(isConn) { 
		    
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

	private void send(String ip, String message) throws Exception {

		
	}

	

		
	
	
	public interface ClientListener{
		
		void onComplete(int code,Chat chat,String errString);
		
	}
	

}
