package com.znvoid.demo.fragment;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.MyAdapt;
import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.view.CircleImageView;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private MyAdapt<Chat> adapt;
	private Context context;

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
		adapt=new MyAdapt<Chat>(context,R.layout.chat_message) {

			@Override
			protected void initlistcell(int position, View listcellview, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				
				Chat chat=getItem(position);
				if (getString(R.string.author).equals(chat.getAuthor())) {
					
					TextView tvown=(TextView) listcellview.findViewById(R.id.author_own);
					TextView tvownMessage=(TextView) listcellview.findViewById(R.id.message_own);
					CircleImageView cimown=(CircleImageView) listcellview.findViewById(R.id.imageView_author_own);
					
					tvown.setText(chat.getAuthor());
					tvownMessage.setText(chat.getMessage());
					cimown.setImageBitmap(getRes(chat.getAuthor()));
				
				} else {
					
					TextView tvAuthor=(TextView) listcellview.findViewById(R.id.author);
					TextView tvMessage=(TextView) listcellview.findViewById(R.id.message);
					CircleImageView cimAuthor=(CircleImageView) listcellview.findViewById(R.id.imageView_author);
					
					tvAuthor.setText(chat.getAuthor());
					tvMessage.setText(chat.getMessage());
					cimAuthor.setImageBitmap(getRes(chat.getAuthor()));

				}
	
			}
			
			public Bitmap getRes(String name) {
				ApplicationInfo appInfo = context .getApplicationInfo();
				int resID = getResources().getIdentifier(name, "drawable", appInfo.packageName);
				return BitmapFactory.decodeResource(getResources(), resID);
				}
		};
		listView.setAdapter(adapt);
		sendButton.setOnClickListener(this);
		authorImage.setOnClickListener(this);
		
		return view;
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendButton:// 发送按钮 事件
			Chat item=new Chat("rad",messageInputEdi.getText().toString());
			adapt.add(item);
			messageInputEdi.setText("");
			break;
		case R.id.circleImageView1:// 头像点击事件

			break;
		default:
			break;
		}
	}
	
}
