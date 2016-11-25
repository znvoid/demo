package com.znvoid.demo.adapt;

import java.util.Collections;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.imf.ItemTouchMoveListener;
import com.znvoid.demo.util.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends  Adapter<ContactsAdapter.MyViewHolder> implements ItemTouchMoveListener {
	private List<Contact> list;
	private Context context;
	
	public ContactsAdapter(List<Contact> list,Context context) {
		this.context=context;
		this.list=list;
	}
	
	
	class MyViewHolder extends ViewHolder{

		private ImageView iv_head;
		private TextView tv_name;
		private TextView tv_Msg;
		private TextView tv_time;

		public MyViewHolder(View itemView) {
			super(itemView);
			iv_head = (ImageView)itemView.findViewById(R.id.contactsItem_iv_head);
			tv_name = (TextView)itemView.findViewById(R.id.contactsItem_tv_name);
			tv_Msg = (TextView)itemView.findViewById(R.id.contactsItem_tv_lastMsg);
			tv_time = (TextView)itemView.findViewById(R.id.contactsItem_tv_time);
		}
		
	}

	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		Collections.swap(list, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
		return true;
	}

	@Override
	public boolean onItemRemove(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		return true;
	}

	@Override
	public int getItemCount() {
		
		return list.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewHolder, int position) {
		Contact contact = list.get(position);
		viewHolder.iv_head.setImageBitmap(Utils.getRes(context, contact.getHead()) );
		viewHolder.tv_name.setText(contact.getName());
		viewHolder.tv_Msg.setText(contact.getLastMsg());
		viewHolder.tv_time.setText(contact.getTime());
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
		return new MyViewHolder(view);
	}


}
