package com.znvoid.demo.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
public class TCPClientThread extends Thread {
    private String ip = "192.168.199.189";
    private int port = 12345;
    private String TAG = "socket thread";
    private int timeout = 10000;
     private int CLIENT_SEND_SUCCSSED=0X2003;
     private int CLIENT_SEND_FAIL=0X2002;
     private int CLIENT_RECEIVED_MESSAGE=0X2001;
     
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
        Log.i(TAG, "�����߳�socket");
    }
 
    /**
     * ����socket������
     */
    public void conn() {
 
        try {
           
            Log.i(TAG, "�����С���");
            client = new Socket(ip, port);
            client.setSoTimeout(timeout);// ��������ʱ��
            Log.i(TAG, "���ӳɹ�");
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    client.getOutputStream())), true);
            
            Log.i(TAG, "�����������ȡ�ɹ�");
        } catch (UnknownHostException e) {
            Log.i(TAG, "���Ӵ���UnknownHostException ���»�ȡ");
            e.printStackTrace();
            conn();
        } catch (IOException e) {
            Log.i(TAG, "���ӷ�����io����");
            e.printStackTrace();
        } catch (Exception e) {
            Log.i(TAG, "���ӷ���������Exception" + e.getMessage());
            e.printStackTrace();
        }
    }
 
  
 
    /**
     * ʵʱ��������
     */
    @Override
    public void run() {
        Log.i(TAG, "�߳�socket��ʼ����");     //���Ϊ��׿�˽������ݵĺ���
        conn();
        Log.i(TAG, "1.run��ʼ");
        String line = "";
        while (isRun) {
            try {
                if (client != null) {
                    Log.i(TAG, "2.�������");
                    while ((line = in.readLine()) != null) {
                        Log.i(TAG, "3.getdata" + line + " len=" + line.length());
                        Log.i(TAG, "4.start set Message");
                        Message msg = handler.obtainMessage();
                        msg.what=0x2001;
                        msg.obj = line;
                        handler.sendMessage(msg);// ������ظ�UI����
                        Log.i(TAG1, "5.send to handler");
                    }
 
                } else {
                    Log.i(TAG, "û�п�������");
                    conn();
                }
            } catch (Exception e) {
                Log.i(TAG, "���ݽ��մ���" + e.getMessage());
                e.printStackTrace();
            }
        }
     
    }
 
    /**
     * ��������
     * 
     * @param mess
     */
    public void Send(String mess) {
        try {
            if (client != null) {
                Log.i(TAG1, "����" + mess + "��"
                        + client.getInetAddress().getHostAddress() + ":"
                        + String.valueOf(client.getPort()));
                out.println(mess);
                out.flush();
                Log.i(TAG1, "���ͳɹ�");
                Message msg = handler.obtainMessage();
                msg.obj = mess;
                msg.what = CLIENT_SEND_SUCCSSED;
                handler.sendMessage(msg);// ������ظ�UI����
            } else {
                Log.i(TAG, "client ������");
                Message msg = handler.obtainMessage();
                msg.obj = mess;
                msg.what = CLIENT_SEND_FAIL;
                handler.sendMessage(msg);// ������ظ�UI����
                Log.i(TAG, "���Ӳ�������������");
                conn();
            }
 
        } catch (Exception e) {
           Log.i(TAG1, "send error");
            e.printStackTrace();
        } finally {
            Log.i(TAG1, "�������");
 
        }
    }
 
    /**
     * �ر�����
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
 
    }
   
}