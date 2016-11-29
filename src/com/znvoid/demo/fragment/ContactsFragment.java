package com.znvoid.demo.fragment;


import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.ContactsAdapter;
import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.imf.ContactsItemTouchHelperCallback;
import com.znvoid.demo.imf.ItemClickListener;
import com.znvoid.demo.sql.MsgSQL;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.util.WifiUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactsFragment extends Fragment implements OnClickListener,ItemClickListener {

	
	private Context context;
	private ItemTouchHelper itemTouchHelper;
	private TextView tv1;
	private TextView tv3;
	private RecyclerView recyclerView ;
	private ContactsAdapter adapter;
	private List<Contact> list=new ArrayList<Contact>();
	private FragmentManager fm;
	private MsgSQL mDB;
	private static final String MESSAGE_NOTIFICATION="com.zn.demo.CHATMESSAGE";
	 private BroadcastReceiver messageReceiver = new BroadcastReceiver()
	    {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            if (intent.getAction() == MESSAGE_NOTIFICATION)
	            {
	                Contact contact = intent.getParcelableExtra("message");
	                if (contact!=null) {
	                	  handleResult(contact);
					}
	              

	            }

	        }

			
	    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fm=getFragmentManager();
		IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_NOTIFICATION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, filter);
		super.onCreate(savedInstanceState);
	}
	
	protected void handleResult(Contact contact) {
		boolean flag=false;
				
		for (int i = 0; i < list.size(); i++) {
			Contact iContact = list.get(i);
			
			if (iContact.getId()==contact.getId()) {
				list.set(i, contact);
				adapter.notifyItemChanged(i);
				flag=true;
				break;
			}
		}
		if (!flag) {
			list.add(0, contact);;
			adapter.notifyItemInserted(0);
		}
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		context=getActivity();
		mDB=new MsgSQL(context);
		View view = inflater.inflate(R.layout.contacts, null);
		
		 recyclerView = (RecyclerView)view.findViewById(R.id.contacts_recyclerview); 
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		
		list=make();
		adapter= new ContactsAdapter(list,context,this);
		recyclerView.setAdapter(adapter);
		//条目触摸帮助类
		ItemTouchHelper.Callback callback = new ContactsItemTouchHelperCallback(adapter);
		itemTouchHelper = new ItemTouchHelper(callback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
		tv1 = (TextView) view.findViewById(R.id.contacts_tv1);
		tv3 = (TextView) view.findViewById(R.id.contacts_tv3);
		tv1.setOnClickListener(this);
		tv3.setOnClickListener(this);
		
		
		return view;
		
		
	}
	
	private List<Contact> make() {
		List<Contact> list=mDB.loadContacts();
		if (list.size()==0) {
			list.add(new Contact(Utils.getId(context), Utils.getName(context), Utils.getHead(context), new WifiUtil(context).getIP()));
		}
		return list;
	}
	@Override
	public void itemOnClick(View view, int position) {
		//准备数据
		  
		Bundle bundle = new Bundle();  
		bundle.putSerializable("contact", list.get(position));  
		bundle.putBoolean("conned", false);
		
		
		
		FragmentTransaction transaction = fm.beginTransaction();
		ChatFragment chatFragment=(ChatFragment) fm.findFragmentByTag("com.znvoid.demo.fragment.ChatFragment");
		if (chatFragment==null) {
			chatFragment=new ChatFragment();
			
		}
		chatFragment.setArguments(bundle);
		if (!chatFragment.isAdded()) {
			System.out.println("1111111");
			transaction.add(R.id.mian_frame, chatFragment,chatFragment.getClass().getName());
			transaction.hide(this);
			transaction.addToBackStack(null); 
			
			transaction.commit();
		} else {
			System.out.println("2222222");
			transaction.show(chatFragment);
			transaction.hide(this);
			transaction.addToBackStack(null); 
			transaction.commit();
		}
		
	}
	@Override
	public void itemOnLongClick(View view, int position) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.contacts_tv1:
			NetBackupFragment netBackupFragment=(NetBackupFragment) fm.findFragmentByTag(NetBackupFragment.class.getName());
			if (netBackupFragment==null) {
				netBackupFragment=new NetBackupFragment();
				
			}
			
			if (!netBackupFragment.isAdded()) {
				
				transaction.add(R.id.mian_frame, netBackupFragment,netBackupFragment.getClass().getName());
				transaction.hide(this);
				
				
				transaction.commit();
			} else {
				
				transaction.show(netBackupFragment);
				transaction.hide(this);
				transaction.commit();
			}
			break;
		case R.id.contacts_tv3:
			LinkFrangemt linkFrangemt=(LinkFrangemt) fm.findFragmentByTag(LinkFrangemt.class.getName());
			if (linkFrangemt==null) {
				linkFrangemt=new LinkFrangemt();
				
			}
			
			if (!linkFrangemt.isAdded()) {
				
				transaction.add(R.id.mian_frame, linkFrangemt,linkFrangemt.getClass().getName());
				transaction.hide(this);
				
				
				transaction.commit();
			} else {
				
				transaction.show(linkFrangemt);
				transaction.hide(this);
				transaction.commit();
			}
			break;
		}
		
		
	}
	
	@Override
	public void onDestroy() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(messageReceiver);
		super.onDestroy();
	}
	
}
