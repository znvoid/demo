package com.znvoid.demo.fragment;

import java.util.ArrayList;

import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.WifiUtil;

import com.znvoid.demo.adapt.MyChatAapter;
import com.znvoid.demo.daim.Chat;


import com.znvoid.demo.net.SearchThread;
import com.znvoid.demo.net.TCPClientThread;
import com.znvoid.demo.net.TCPServerThread;
import com.znvoid.demo.sql.ChatSqlOpenHelp;
import com.znvoid.demo.util.TCPData;
import com.znvoid.demo.util.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.Toast;

public class ChatFragment extends Fragment implements OnClickListener {

	private ListView listView;
	private Spinner spinner;
	private ImageButton sendButton;
	private EditText messageInputEdi;
	private MyChatAapter adapt;
	private ArrayAdapter<String> spinerAdapter;// spinner的adapt
	private List<String> datas;
	private Context context;
	private ProgressDialog progress;
	
	private String myIP;
	private String clientIP = "机器人";
	private ChatSqlOpenHelp sqlOpenHelp;
	private SharedPreferences sharedPreferences;
	private TCPServerThread tcpServerThread;
	private TCPClientThread tcpClientThread;
	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TCPServerThread.SERVER_RECEIVED_MESSAGE:

				Chat chat = TCPData.getChat((String) msg.obj);
				if (chat != null) {
					adapt.add(chat);
					sqlOpenHelp.add(chat);
				}

				break;
			case TCPServerThread.SERVER_SEND_FAIL:

				break;
			case TCPServerThread.SERVER_SEND_SUCCEED:

				break;
			case TCPClientThread.CLIENT_RECEIVED_MESSAGE:
				Chat chat_cr = TCPData.getChat((String) msg.obj);
				if (chat_cr != null) {
					adapt.add(chat_cr);
					sqlOpenHelp.add(chat_cr);
				}
				break;
			case TCPClientThread.CLIENT_SEND_FAIL:
				Log.e("Light", "22222发送失败");
				Toast.makeText(context, "发送失败！", Toast.LENGTH_SHORT).show();
				break;
			case TCPClientThread.CLIENT_SEND_SUCCSSED:

				Chat chat1 = TCPData.getChat((String) msg.obj);

				if (chat1 != null) {
					chat1.setDirection(0);
					chat1.setIp("我");
					adapt.add(chat1);
					sqlOpenHelp.add(chat1);
				}

				break;
			case TCPClientThread.CLIENT_CONN_FIAL:
				Toast.makeText(context, "连接服务器失败！发送失败", Toast.LENGTH_SHORT).show();
				break;
			case TCPClientThread.CLIENT_CONN_SUCC:
				//Toast.makeText(context, "连接成功！", Toast.LENGTH_SHORT).show();
				break;
			case SearchThread.SEARCH_FINSH:
				if (progress != null) {
					progress.dismiss();
				}
				datas.clear();
				datas.add("机器人");
				datas.add("我");
				datas.addAll((List<String>) msg.obj);
				datas.add("点击刷新");
				spinerAdapter.notifyDataSetChanged();
				spinner.setSelection(0);

				break;
			}
		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();

		View view = inflater.inflate(R.layout.chatframgment, null);

		// 获取本机ip
		WifiUtil wifiutil = new WifiUtil(context);
		myIP = wifiutil.getIP();

		// 创建tcpsServer
		tcpServerThread = new TCPServerThread(context,mhandler);
		tcpServerThread.start();
		if (null == progress) {
			progress = new ProgressDialog(context);
		}
		//
		sharedPreferences = context.getSharedPreferences("configs", context.MODE_PRIVATE);
		sqlOpenHelp = new ChatSqlOpenHelp(context);
		messageInputEdi = (EditText) view.findViewById(R.id.messageInput);
		sendButton = (ImageButton) view.findViewById(R.id.sendButton);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		datas = new ArrayList<String>();

		datas.add("机器人");
		datas.add("我");
		datas.add("刷新");
		spinerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, datas);
		spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				if (clientIP != spinerAdapter.getItem(pos)) {
					clientIP = spinerAdapter.getItem(pos);

				}
				if (pos == spinerAdapter.getCount() - 1) {
					new SearchThread(mhandler).start();
					progress.setTitle("正在刷新中...");
					progress.show();

				}
				

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		listView = (ListView) view.findViewById(R.id.listchat);
		adapt = new MyChatAapter(context);
		ChatSqlOpenHelp sqlOpenHelp = new ChatSqlOpenHelp(context);
		adapt.setdata(sqlOpenHelp.loadall());
		listView.setAdapter(adapt);

		sendButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendButton:// 发送按钮 事件

			if ("".equals(messageInputEdi.getText().toString().trim())) {

				break;
			}
			Chat item;
			
			if (clientIP.matches(".+\\..+\\..+\\..+")) {	
				item = new Chat(sharedPreferences.getString("author", myIP),
						messageInputEdi.getText().toString().trim(), 0, sharedPreferences.getString("head", "head_1"),
						myIP,Utils.getSysTime());
				tcpClientThread = new TCPClientThread(mhandler, clientIP, TCPData.getMsg(item));
				tcpClientThread.start();

			} else if (clientIP.equals("我")) {

				item = new Chat(sharedPreferences.getString("author", myIP),
						messageInputEdi.getText().toString().trim(), 0, sharedPreferences.getString("head", "head_1"),
						clientIP,Utils.getSysTime());
				adapt.add(item);

				sqlOpenHelp.add(item);
			} else {
				item = new Chat(sharedPreferences.getString("other", "机器人"), messageInputEdi.getText().toString().trim(),
						1, sharedPreferences.getString("head_other", "head_1"), clientIP,Utils.getSysTime());
				
				adapt.add(item);

				sqlOpenHelp.add(item);
			}

			messageInputEdi.setText("");

			break;
		
		
		}
	}


}
