package com.znvoid.demo.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.MyChatAapter;
import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.net.LinkThread;
import com.znvoid.demo.net.Ping;
import com.znvoid.demo.net.SearchThread;
import com.znvoid.demo.net.TCPClientThread;
import com.znvoid.demo.net.TCPClinet;
import com.znvoid.demo.net.TCPServer;

import com.znvoid.demo.server.ISevice;
import com.znvoid.demo.server.TCPSevice;

import com.znvoid.demo.sql.MsgSQL;
import com.znvoid.demo.util.TCPData;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.util.WifiUtil;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
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
	private String myid;
	private String clientIP = "机器人";
	private MsgSQL sqlOpenHelp;
	private SharedPreferences sharedPreferences;
	// private TCPServerThread tcpServerThread;
	private TCPServer tcpServerThread;
	private TCPClientThread tcpClientThread;
	// MyConn conn;

	private boolean canLink = false;
	private static final String MESSAGE_NOTIFICATION = "com.zn.demo.CHATMESSAGE";
	private static final int TIME_OUT = 0X12;
	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == MESSAGE_NOTIFICATION) {
				Contact contact = intent.getParcelableExtra("message");
				
				if (mContact.equals(contact.getId())) {
					handleResult(contact);
				}

			}

		}

	};
	private final Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case TCPSevice.TCPSEVICE_RECEIVE:
				// Chat chat = (Chat) msg.obj;
				// adapt.add(chat);
				// if (!datas.contains(chat.getIp())) {
				// datas.add(datas.size() - 1, chat.getIp());
				// spinerAdapter.notifyDataSetChanged();
				// spinner.setSelection(datas.size() - 2);
				// }
				break;
			case TCPClientThread.CLIENT_RECEIVED_MESSAGE:
				// Chat chat_cr = TCPData.getChat((String) msg.obj);
				// if (chat_cr != null) {
				// adapt.add(chat_cr);
				// sqlOpenHelp.add(chat_cr);
				// }
				break;
			case TCPClientThread.CLIENT_SEND_FAIL:
				Log.e("Light", "发送失败");
				canLink = false;
				Toast.makeText(context, "发送失败！", Toast.LENGTH_SHORT).show();
				break;
			case TCPClientThread.CLIENT_SEND_SUCCSSED:

				Contact contact = (Contact) msg.obj;
				contact.setId(mContact.getId());
				contact.setHead(mContact.getHead());
				contact.setIp(mContact.getIp());
				contact.setName(mContact.getName());
				Chat chat1 = TCPData.contact2Chat(contact);
				if (chat1 != null) {
					// chat1.setDirection(0);
					chat1.setAuthor(Utils.getName(context));
					
					adapt.add(chat1);
					sqlOpenHelp.add(contact);
				}

				break;
			case TCPClientThread.CLIENT_CONN_FIAL:
				Toast.makeText(context, "连接服务器失败！发送失败", Toast.LENGTH_SHORT).show();
				break;
			case TCPClientThread.CLIENT_CONN_SUCC:
				// Toast.makeText(context, "连接成功！", Toast.LENGTH_SHORT).show();
				break;
			case LinkThread.LINK_SUCCESED:
				canLink = true;
				// progress.dismiss();
				Contact contact3 = (Contact) msg.obj;
				mContact.setName(contact3.getName());
				mContact.setHead(contact3.getHead());
				Toast.makeText(context, "测试连接成功！可以开始了", Toast.LENGTH_SHORT).show();
				break;
			case LinkThread.LINK_FAIL:
				// System.out.println("LINK_FAIL");
				progress.dismiss();
				refesh();
				progress.setTitle("测试失败，开始尝试搜索......");
				progress.show();

				break;
			case LinkThread.LINK_CLASH:
				refesh();
				progress.setTitle("ip冲突，开始尝试搜索......");
				progress.show();

				break;
			case TIME_OUT:
				new SearchThread(TCPData.creatTestMessage(context), mhandler).start();
				;

				break;
			case SearchThread.SEARCH_FINSH:
				progress.dismiss();
				List<Contact> list = (List<Contact>) msg.obj;

				if (list.size() > 0) {

					for (Contact contact2 : list) {

						if (contact2.getId().equals(mContact.getId())) {
							mContact.setIp(contact2.getIp());
							mContact.setName(contact2.getName());
							mContact.setHead(contact2.getHead());
							canLink = true;
							Toast.makeText(context, "搜索成功", Toast.LENGTH_LONG).show();
							break;
						}
					}

				}
				if (!canLink) {
					Toast.makeText(context, "搜索失败，对象不在服务区", Toast.LENGTH_LONG).show();
				}
				break;
			}
		};

	};
	private Intent intent;
	private TCPClinet clinet;
	private Contact mContact;
	private boolean isRefesh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			mContact = (Contact) bundle.get("contact");
			System.out.println("12--------"+mContact.getIp());
		canLink=	bundle.getBoolean("conned",false);
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(MESSAGE_NOTIFICATION);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, filter);
		super.onCreate(savedInstanceState);
	}

	protected void handleResult(Contact contact) {
		mContact.setIp(contact.getIp());
		mContact.setHead(contact.getHead());
		mContact.setName(contact.getName());

		adapt.add(TCPData.contact2Chat(contact));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = getActivity();

		View view = inflater.inflate(R.layout.chatframgment, null);

		// 获取本机ip
		WifiUtil wifiutil = new WifiUtil(context);
		myIP = wifiutil.getIP();
		myid = Utils.getId(context);
		// 创建tcpsServer
		// tcpServerThread = new TCPServerThread(context,mhandler);

		// tcpServerThread = new TCPServer(context,mhandler);
		// tcpServerThread.start();
		// intent = new Intent(context, TCPSevice.class);
		// conn = new MyConn();
		//
		// context.bindService(intent, conn, context.BIND_AUTO_CREATE);

		if (null == progress) {
			progress = new ProgressDialog(context);
		}
		//
		sharedPreferences = context.getSharedPreferences("configs", context.MODE_PRIVATE);
		sqlOpenHelp = new MsgSQL(context);
		messageInputEdi = (EditText) view.findViewById(R.id.messageInput);
		sendButton = (ImageButton) view.findViewById(R.id.sendButton);
		// spinner = (Spinner) view.findViewById(R.id.spinner1);
		// datas = new ArrayList<String>();
		//
		// datas.add("机器人");
		// datas.add("我");
		// datas.add("刷新");
		// spinerAdapter = new ArrayAdapter<String>(context,
		// android.R.layout.simple_spinner_item, datas);
		// spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinner.setAdapter(spinerAdapter);
		// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int pos,
		// long id) {
		//
		// if (pos == spinerAdapter.getCount() - 1) {
		// // new SearchThread(mhandler).start();
		// progress.setTitle("正在刷新中...");
		// progress.show();
		//
		// }
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });
		listView = (ListView) view.findViewById(R.id.listchat);
		adapt = new MyChatAapter(context, mContact.getId());
		// ChatSqlOpenHelp sqlOpenHelp = new ChatSqlOpenHelp(context);
		sqlOpenHelp = new MsgSQL(context);
		adapt.setdata(sqlOpenHelp.loadMsg(mContact));
		listView.setAdapter(adapt);

		sendButton.setOnClickListener(this);
		if (!myid.equals(mContact.getId())&&!canLink) {
			linkService();
		}

		return view;
	}

	public void linkService() {
		new LinkThread(context, mhandler, mContact).start();
		progress.setTitle("正在尝试连接...");
		progress.show();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sendButton:// 发送按钮 事件
			Log.e("TCPServer", myid+"-------"+myIP);
			if (myid.equals(mContact.getId())) {
				String tString=messageInputEdi.getText().toString().trim();
				Contact contact1=makeSendMsg();
				
				contact1.setName(Utils.getOtherName(context));
				contact1.setHead(Utils.getOtherHead(context));
				
				
				if (tString.startsWith("#")) {
					contact1.setLastMsg(tString.substring(2));
					contact1.setDirection(1);
					
					
				}else {
					
					contact1.setDirection(0);
					
				}
				adapt.add(TCPData.contact2Chat(contact1));
				
				messageInputEdi.setText("");
				
				break;
			}else {
				
				if (!canLink) {
					linkService();
					break;
				}
				
				new TCPClientThread(mhandler, makeSendMsg(),mContact.getIp()).start();
				messageInputEdi.setText("");
			}
			
			
			

		

			
			

			// Chat item;
			// clientIP = (String) spinner.getSelectedItem();
			// if (clientIP.matches(".+\\..+\\..+\\..+")) {
			// item = new Chat(sharedPreferences.getString("author", myIP),
			// messageInputEdi.getText().toString().trim(), 0,
			// sharedPreferences.getString("head", "head_1"),
			// myIP, Utils.getSysTime());
			// tcpClientThread = new TCPClientThread(mhandler, clientIP,
			// TCPData.getMsg(item));
			// tcpClientThread.start();
			//// if (clinet==null) {
			//// clinet = new TCPClinet(mhandler);
			//// clinet.start();
			//// }
			//// clinet.sendMessage(clientIP, TCPData.getMsg(item));
			//
			//
			//
			// } else if (clientIP.equals("我")) {
			//
			// item = new Chat(sharedPreferences.getString("author", myIP),
			// messageInputEdi.getText().toString().trim(), 0,
			// sharedPreferences.getString("head", "head_1"),
			// clientIP, Utils.getSysTime());
			// adapt.add(item);
			//
			// sqlOpenHelp.add(item);
			// } else {
			// item = new Chat(sharedPreferences.getString("other", "机器人"),
			// messageInputEdi.getText().toString().trim(), 1,
			// sharedPreferences.getString("head_other", "head_1"), clientIP,
			// Utils.getSysTime());
			//
			// adapt.add(item);
			//
			// sqlOpenHelp.add(item);
			// }

			

			break;

		}

	}

	// class MyConn implements ServiceConnection {
	//
	// // 绑定一个服务成功的时候 调用 onServiceConnected
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// ISevice iService = (ISevice) service;
	// if (iService != null) {
	//
	// iService.setHandlerr(mhandler);
	// }
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	//
	// }
	// }

	public void refesh() {
		if (!isRefesh) {
			isRefesh = !isRefesh;
			new Thread() {

				public void run() {
					Ping ping = new Ping();
					ping.pingAll(myIP);
				};
			};

			mhandler.sendEmptyMessageAtTime(TIME_OUT, 2000);
		}

	}

	@Override
	public void onDestroy() {
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
		super.onDestroy();
	}
	
	
	public Contact makeSendMsg() {
		String id = Utils.getId(context);
		String name = Utils.getName(context);
		String head = Utils.getHead(context);
		String lastMsg = messageInputEdi.getText().toString().trim();
		String time = Utils.getSysTime();
		String msgType = "message/string";
		int direction = 0;

		return new Contact(id, name, head, lastMsg, time, myIP, msgType, direction);
		
	}
}
