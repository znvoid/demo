package com.znvoid.demo.adapt;

import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.daim.Chat;
import com.znvoid.demo.sql.MsgSQL;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.view.CircleImageView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyChatAapter extends BaseAdapter {
	private int delposition = -1;
	private Context context;
	private String mId;
	private List<Chat> datalist = new ArrayList<Chat>();
	private MsgSQL msgSQL;
	public MyChatAapter(Context context,String id) {
		super();
		this.context = context;
		mId=id;
	}

	// 增加条目
	public void add(Chat item) {
		datalist.add(item);
		notifyDataSetChanged();
	}

	// 删除条目
	public void remove(int position) {

		datalist.remove(position);
		notifyDataSetChanged();
	}

	// 设置数据
	public void setdata(List<Chat> list) {
		datalist.clear();
		datalist.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public Chat getItem(int position) {
		// TODO Auto-generated method stub
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Chat chat = getItem(position);
		/*
		 * if (convertView==null) { if
		 * (context.getString(R.string.author).equals(chat.getAuthor())) {
		 * convertView=LayoutInflater.from(context).inflate(R.layout.
		 * chat_message_own, null); }else {
		 * convertView=LayoutInflater.from(context).inflate(R.layout.
		 * chat_message_other, null); }
		 * 
		 * } initlistcell( position, convertView, parent);
		 * 
		 * 
		 * return convertView;
		 */

		ViewHolder holder = null;

		if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != chat.getDirection()) {
			holder = new ViewHolder();
			if (chat.getDirection() == Chat.MESSAGE_SEND) {
				holder.flag = Chat.MESSAGE_SEND;
				convertView = LayoutInflater.from(context).inflate(R.layout.chat_message_own, null);
			} else {
				holder.flag = Chat.MESSAGE_RECEIVE;
				convertView = LayoutInflater.from(context).inflate(R.layout.chat_message_other, null);
			}
			holder.text = (TextView) convertView.findViewById(R.id.message);
			holder.tvauthor = (TextView) convertView.findViewById(R.id.author);
			holder.tvtime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			holder.circleImageView = (CircleImageView) convertView.findViewById(R.id.imageView_author);

			convertView.setTag(holder);
		}
		holder.text.setText(chat.getMessage());
		holder.text.setId(position);
		holder.text.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub

				delposition = v.getId();

				dialog();

				return true;
			}
		});
		if (holder.flag==0) {
			holder.circleImageView.setImageBitmap(getRes(Utils.getHead(context)));
			holder.tvauthor.setText(Utils.getName(context));
		}else {
			holder.tvauthor.setText(chat.getAuthor() );
			holder.circleImageView.setImageBitmap(getRes(chat.getHead()));
		}
		
		holder.tvtime.setText(chat.getTime());
		

		return convertView;
	}

	// 优化listview的Adapter
	static class ViewHolder {
		
		TextView text;
		TextView tvauthor;
		TextView tvtime;
		CircleImageView circleImageView;
		int flag;
	}

	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	protected void dialog() {

		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认删除吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				msgSQL.delete(mId, getItem(delposition));
				remove(delposition);

				dialog.dismiss();

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				delposition = -1;
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
}
