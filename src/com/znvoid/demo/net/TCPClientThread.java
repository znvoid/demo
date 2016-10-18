package com.znvoid.demo.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
public class TCPClientThread extends Thread {
   
	public static final int CLIENT_SEND_SUCCSSED=0X2003;
	public static final int CLIENT_SEND_FAIL=0X2002;
	public static final int CLIENT_RECEIVED_MESSAGE=0X2001;
	public static final int CLIENT_CONN_FIAL=0X2004;
	public static final int CLIENT_CONN_SUCC=0X2005;
	
	private String ip = "192.168.199.189";
    private int port = 12345;
    private String TAG = "socket thread";
    private int timeout = 3000;
    private String mMsg;
     
    public Socket client = null;
    PrintWriter out;
    BufferedReader in;
    DataInputStream indata;
    public boolean isRun = true;
    Handler handler;
   
    Context ctx;
    private String TAG1 = "===Send===";
    SharedPreferences sp;
 
    public TCPClientThread(Handler handler,String ip) {
        
        this.handler=handler;
        this.ip=ip;
        Log.i(TAG, "创建线程socket");
    }
 public TCPClientThread(Handler handler,String ip,String mMsg) {
        
        this.handler=handler;
        this.ip=ip;
        this.mMsg=mMsg;
        
    }
 
    /**
     * 连接socket服务器
     */
    public void conn() {
 
        try {
           
            Log.i(TAG, "连接中……");
            //client = new Socket(ip, port);
           // client.setSoTimeout(timeout);// 设置阻塞时间
            client=new Socket();
            SocketAddress remoteAddr=new InetSocketAddress(ip,port);
            client.connect(remoteAddr, timeout);
            
            Log.i(TAG, "连接成功");
            Message msg = handler.obtainMessage();
            msg.what = CLIENT_CONN_SUCC;
            handler.sendMessage(msg);// 
           
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    client.getOutputStream())), true);
            
            Log.i(TAG, "输入输出流获取成功");
            isRun=true;
            if (mMsg!=null) {
            	Send( mMsg);
            	close();
			}
            
            
        } catch (Exception e) {
        	Message msg = handler.obtainMessage();
            msg.what = CLIENT_CONN_FIAL;
            handler.sendMessage(msg);// 
            Log.i(TAG, "连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
            isRun=false;
            close();
        }
    }
 
  
 
    /**
     * 实时接受数据
     */
    @Override
    public void run() {
        Log.i(TAG, "线程socket开始运行");     //这个为安卓端接受数据的函数
        conn();
        Log.i(TAG, "1.run开始");
        String line = "";
        while (isRun) {
        	System.out.println(isRun);
            try {
                if (client != null) {
                    Log.i(TAG, "2.检测数据");
                    while ((line = in.readLine()) != null) {
                        Log.i(TAG, "3.getdata" + line + " len=" + line.length());
                        Log.i(TAG, "4.start set Message");
                        Message msg = handler.obtainMessage();
                        msg.what=CLIENT_RECEIVED_MESSAGE;
                        msg.obj = line;
                        handler.sendMessage(msg);// 结果返回给UI处理
                        Log.i(TAG1, "5.send to handler");
                    }
 
                } else {
                    Log.i(TAG, "没有可用连接");
                   // conn();
                    isRun=false;
                }
            } catch (Exception e) {
                Log.i(TAG, "数据接收错误" + e.getMessage());
                e.printStackTrace();
            }
        }
     
    }
 
    /**
     * 发送数据
     * 
     * @param mess
     */
    public void Send(String mess) {
        try {
            if (client != null) {
                Log.i(TAG1, "发送" + mess + "至"
                        + client.getInetAddress().getHostAddress() + ":"
                        + String.valueOf(client.getPort()));
                out.println(mess);
                out.flush();
                Log.i(TAG1, "发送成功");
                Message msg = handler.obtainMessage();
                msg.obj = mess;
                msg.what = CLIENT_SEND_SUCCSSED;
                handler.sendMessage(msg);// 结果返回给UI处理
            } else {
                Log.i(TAG, "client 不存在");
                Message msg = handler.obtainMessage();
                msg.obj = mess;
                msg.what = CLIENT_SEND_FAIL;
                handler.sendMessage(msg);// 结果返回给UI处理
                Log.i(TAG, "连接不存在重新连接");
                //conn();
            }
 
        } catch (Exception e) {
           Log.i(TAG1, "send error");
            e.printStackTrace();
        } finally {
            Log.i(TAG1, "发送完毕");
 
        }
    }
 
    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (client != null) {
                Log.i(TAG, "close in");
                in.close();
                Log.i(TAG, "close out");
                out.close();
                Log.i(TAG, "close client");
                client.close();
                
            }
        } catch (Exception e) {
            Log.i(TAG, "close err");
            e.printStackTrace();
        }
        isRun=false;
    }
   
}