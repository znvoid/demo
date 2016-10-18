package com.znvoid.demo.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.MyAdapt;
import com.znvoid.demo.adapt.MyChatAapter;
import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.sql.ChatSqlOpenHelp;
import com.znvoid.demo.view.CircleImageView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ChatFragment extends Fragment implements OnClickListener {
	private ListView listView;
	private CircleImageView authorImage;
	private ImageButton sendButton;
	private EditText messageInputEdi;
	private MyChatAapter adapt;
	private Context context;
	private boolean flag_author=true;
	private ServerSocket serverSocket = null; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context=getActivity();
		View view = inflater.inflate(R.layout.chatframgment, null);
		messageInputEdi = (EditText) view.findViewById(R.id.messageInput);
		sendButton =  (ImageButton) view.findViewById(R.id.sendButton);
		authorImage = (CircleImageView) view.findViewById(R.id.circleImageView1);
		listView = (ListView) view.findViewById(R.id.listchat);
		adapt=new MyChatAapter(context) ;
		ChatSqlOpenHelp sqlOpenHelp=new ChatSqlOpenHelp(context);
		adapt.setdata(sqlOpenHelp.loadall());
		listView.setAdapter(adapt);
		
		sendButton.setOnClickListener(this);
		authorImage.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
 * 通过文件名获取drawable目录下图片
 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = context .getApplicationInfo();
		int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(getResources(), resID);
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
			if (flag_author) {
				item=new Chat(getString(R.string.author),messageInputEdi.getText().toString(),0);
			}else {
				item=new Chat(getString(R.string.other),messageInputEdi.getText().toString(),1);
			}	
			adapt.add(item);
			messageInputEdi.setText("");
			ChatSqlOpenHelp sqlOpenHelp=new ChatSqlOpenHelp(context);
			sqlOpenHelp.add(item);
			break;
		case R.id.circleImageView1:// 头像点击事件
			if (flag_author) {
				authorImage.setImageBitmap(getRes(getString(R.string.other)));
				flag_author=!flag_author;
			}else {
				authorImage.setImageBitmap(getRes(getString(R.string.author)));
				flag_author=!flag_author;
			}
			break;
		default:
			break;
		}
	}
	
	
}
