package com.znvoid.demo.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;

import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TCPServerThread extends Thread {
	/**
	 * Ĭ�Ϸ������˿�
	 */
	private static final Integer DEFAULT_PORT = 12345;

	/**
	 * ������Socket
	 */
	private static ServerSocket serverSocket;

	/**
	 * �ͻ��˵�IP
	 */

	/**
	 * �ͻ���Socket��MAP��
	 */
	private Map<String, Socket> socketMap;

	Handler mHandler;
	public static final int SERVER_RECEIVED_MESSAGE = 0x1001;
	public static final int SERVER_SEND_FAIL = 0x1002;
	public static final int SERVER_SEND_SUCCEED = 0x1003;

	public boolean isRun = true;

	public TCPServerThread(Handler mHandler) {
		super();
		this.mHandler = mHandler;

	}

	/**
	 * ���ö˿�
	 * 
	 * @param port
	 */
	private void init(Integer port) {
		try {
			serverSocket = new ServerSocket(port);

			socketMap = new HashMap<String, Socket>();
		} catch (IOException e) {// ���ܱ��˿�ռ�õĴ���
			e.printStackTrace();
		}
	}

	/**
	 * �ȴ�����
	 * 
	 * @throws IOException
	 */
	private void waitConnect() throws IOException {
		System.out.println("=========�ȴ�����=========");
		Socket clientSocket = serverSocket.accept();
		// receiveMessage(clientSocket);//�ȴ�ӡ����Ϣ�����������߳�
		saveClientSocket(clientSocket);
		receiveMessage(clientSocket);
	}

	/**
	 * ����socket
	 * 
	 * @param socket
	 */
	private void saveClientSocket(Socket socket) {
		String clientAddress = socket.getInetAddress().getHostAddress();
		socketMap.put(clientAddress, socket);

		// clientAddresses.add(clientAddress);
		System.out.println("++++++++++++�ͻ���" + socket + "�����Ϸ�����++++++++++++");

		// if(socketMap.size() == 1){
		// //startSend();
		// }
	}

	/**
	 * ������Ϣ
	 * 
	 * @param clientSocket
	 */
	private void receiveMessage(Socket socket) {
		ReMesssage reMesssage = new ReMesssage(socket);
		reMesssage.start();

	}

	private class ReMesssage extends Thread {
		private Socket resocket;

		public ReMesssage(Socket resocket) {
			super();
			this.resocket = resocket;
		}

		@Override
		public void run() {
			
			Log.e("Light", "--------�ȴ�����------");

			while (!resocket.isClosed()) {
				Log.e("Light", "--------" + resocket + "------");
				Log.e("Light", "--------��ʼ����------");
				try {
					InputStream inputStream = resocket.getInputStream();
					byte buffer[] = new byte[1024*1024];
					int temp = 0;
					Log.e("Light", "--------���ܵ�1------");
					
					// ��InputStream���ж�ȡ�ͻ��������͵�����
					while ((temp = inputStream.read(buffer)) != -1) {
						
						Message msg = new Message();
						msg.what = SERVER_RECEIVED_MESSAGE;
						msg.obj = new String(buffer, 0, temp,"gbk");
						mHandler.sendMessage(msg);
					}
					
					

				} catch (IOException e) {
					Log.e("Light", "--------�쳣�ر�------");
					try {
						resocket.close();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}

			super.run();
		}

	}

	public void sendMessage(String ip, String msg) {
		Socket msocket = socketMap.get(ip);
		System.out.println("--fasong---"+msocket+"-----");
		if (msocket != null) {
			if (!msocket.isClosed()) {
				try {
					OutputStream os = msocket.getOutputStream();
					os.write(msg.getBytes());
					os.flush();
					Message msg1 = mHandler.obtainMessage();
					msg1.what =SERVER_SEND_SUCCEED ;
					msg1.obj=msg;
					mHandler.sendMessage(msg1);

				} catch (IOException e) {

					e.printStackTrace();
				}
			} else {
				Log.e("Light", "closed");
				Message msg1 = mHandler.obtainMessage();
				msg1.obj=msg;
				msg1.what = SERVER_SEND_FAIL;
				mHandler.sendMessage(msg1);// ������ظ�UI����
			}

		} else {

			Message msg1 = mHandler.obtainMessage();

			msg1.obj=msg;
			msg1.what = SERVER_SEND_FAIL;
			mHandler.sendMessage(msg1);// ������ظ�UI����
		}

	}

	@Override
	public void run() {
		init(DEFAULT_PORT);
		boolean flagr = true;
		while (isRun) {
			try {
				waitConnect();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public void closeServer() {

		try {
			serverSocket.close();
			socketMap.clear();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
