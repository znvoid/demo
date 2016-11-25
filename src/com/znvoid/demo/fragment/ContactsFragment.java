package com.znvoid.demo.fragment;

import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.ContactsAdapter;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.imf.ContactsItemTouchHelperCallback;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsFragment extends Fragment {

	
	private Context context;
	private ItemTouchHelper itemTouchHelper;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		context=getActivity();
		View view = inflater.inflate(R.layout.contacts, null);
		
		RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.contacts_recyclerview); 
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		
		List<Contact> list=make();
		ContactsAdapter adapter = new ContactsAdapter(list,context);
		recyclerView.setAdapter(adapter);
		//条目触摸帮助类
		ItemTouchHelper.Callback callback = new ContactsItemTouchHelperCallback(adapter);
		itemTouchHelper = new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
		
		return view;
		
		
	}
	
	private List<Contact> make() {
		List<Contact> list=new ArrayList<Contact>();
		list.add(new Contact("123", "机器人", "head_1", "什么人", "12:21"));
		list.add(new Contact("121", "wo", "head_4", "what you name", "14:21"));
		list.add(new Contact("124", "老人人", "head_7", "热哈方法", "21:21"));
		list.add(new Contact("123", "机器人", "head_1", "什么人", "12:21"));
		list.add(new Contact("121", "wo", "head_4", "what you name", "14:21"));
		list.add(new Contact("124", "老人人", "head_7", "热哈方法", "21:21"));
		list.add(new Contact("123", "机器人", "head_1", "什么人", "12:21"));
		list.add(new Contact("121", "wo", "head_4", "what you name", "14:21"));
		list.add(new Contact("124", "老人人", "head_7", "热哈方法", "21:21"));
		list.add(new Contact("123", "机器人", "head_1", "什么人", "12:21"));
		list.add(new Contact("121", "wo", "head_4", "what you name", "14:21"));
		list.add(new Contact("124", "老人人", "head_7", "热哈方法", "21:21"));
		
		return list;
	}
}
