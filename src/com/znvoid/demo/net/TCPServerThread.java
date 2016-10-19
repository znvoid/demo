package com.znvoid.demo.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;

import java.util.Map;

import com.znvoid.demo.WifiUtil;
import com.znvoid.demo.util.TCPData;

import android.content.Context;
import android.content.SharedPreferences;
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

	private Context context;

	/**
	 * �ͻ���Socket��MAP��
	 */
	private Map<String, Socket> socketMap;

	Handler mHandler;
	public static final int SERVER_RECEIVED_MESSAGE = 0x1001;
	public static final int SERVER_SEND_FAIL = 0x1002;
	public static final int SERVER_SEND_SUCCEED = 0x1003;
	public static final String CHATTEST = "CHATMESSAGE V1.0 CHATTEST";

	public boolean isRun = true;

	public TCPServerThread(Context context,Handler mHandler) {
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
					byte buffer[] = new byte[1024 * 1024];
					int temp = 0;
					Log.e("Light", "--------���ܵ�1------");

					// ��InputStream���ж�ȡ�ͻ��������͵�����
					while ((temp = inputStream.read(buffer)) != -1) {
						String mesg = new String(buffer, 0, temp);
						if (CHATTEST.equals(mesg)) {//�յ���������
							Log.e("Light", "�յ���������");
							OutputStream os=resocket.getOutputStream();
							os.write(creatMsgForTest().getBytes());
							os.flush();
							
						} else {
							Message msg = new Message();
							msg.what = SERVER_RECEIVED_MESSAGE;
							msg.obj = mesg;
							mHandler.sendMessage(msg);
						}

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
		System.out.println("--fasong---" + msocket + "-----");
		if (msocket != null) {
			if (!msocket.isClosed()) {
				try {
					OutputStream os = msocket.getOutputStream();
					os.write(msg.getBytes());
					os.flush();
					Message msg1 = mHandler.obtainMessage();
					msg1.what = SERVER_SEND_SUCCEED;
					msg1.obj = msg;
					mHandler.sendMessage(msg1);

				} catch (IOException e) {

					e.printStackTrace();
				}
			} else {
				Log.e("Light", "closed");
				Message msg1 = mHandler.obtainMessage();
				msg1.obj = msg;
				msg1.what = SERVER_SEND_FAIL;
				mHandler.sendMessage(msg1);// ������ظ�UI����
			}

		} else {

			Message msg1 = mHandler.obtainMessage();

			msg1.obj = msg;
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

	public String creatMsgForTest() {
		String result;
		SharedPreferences sp = context.getSharedPreferences("configs", context.MODE_PRIVATE);

		sp.getString("head", "head");
		WifiUtil wifiUtil = new WifiUtil(context);

		result = TCPData.CHATMESSAGE + TCPData.DIV + sp.getString("author", "???") + TCPData.DIV
				+ sp.getString("head", "head") + TCPData.DIV + wifiUtil.getIP() +TCPData.DIV
				+ wifiUtil.getMacAddress();

		return result;
	}
}
