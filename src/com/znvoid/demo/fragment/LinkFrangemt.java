package com.znvoid.demo.fragment;



import java.util.ArrayList;
import java.util.List;

import com.znvoid.demo.R;
import com.znvoid.demo.adapt.MyAdapt;

import com.znvoid.demo.daim.Contact;
import com.znvoid.demo.net.Ping;
import com.znvoid.demo.net.SearchThread;
import com.znvoid.demo.util.TCPData;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.util.WifiUtil;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LinkFrangemt extends Fragment implements OnClickListener, OnRefreshListener, OnItemClickListener {
	private final int MSG_OVER = 0x10001;
	private final int MSG_STOP = 0x10002;
	private final int MSG_TIMEOUT = 0x10003;

	
	private ListView lv;
	private Context context;
	private List<Contact>list=new ArrayList<Contact>();
	private WifiUtil wifiUtil;
	private MyAdapt<Contact> adapt;
	private SwipeRefreshLayout mSwipRefresh;
	
	
	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case MSG_STOP:
				
				break;
			case MSG_TIMEOUT:
				new SearchThread( TCPData.creatTestMessage(context),handle).start();
				
				break;
			case SearchThread.SEARCH_FINSH:
				isfresh=false;
				List<Contact>list=(List<Contact>) msg.obj;
				mSwipRefresh.setRefreshing(false);
				
				adapt.setdata(list);
				
				
				break;
			default:
				break;
			}


		};

	};
	private FragmentManager fragmentManager;
	private boolean isfresh=false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fragmentManager = getFragmentManager();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.link, null);
		context = getActivity();
		lv = (ListView) view.findViewById(R.id.link_ListView);
		
		wifiUtil = new WifiUtil(context);
		mSwipRefresh=(SwipeRefreshLayout) view.findViewById(R.id.link_refresh);
		mSwipRefresh.setOnRefreshListener(this);  
		adapt = new MyAdapt<Contact>(context, R.layout.contacts_item) {

			@Override
			protected void initlistcell(int position, View listcellview, ViewGroup parent) {
				
				final Contact client=getItem(position);
				TextView tv_id = (TextView) listcellview.findViewById(R.id.contactsItem_tv_lastMsg);
				TextView tv_name = (TextView) listcellview.findViewById(R.id.contactsItem_tv_name);
				TextView tv_ip = (TextView) listcellview.findViewById(R.id.contactsItem_tv_time);
				ImageView im_head = (ImageView) listcellview.findViewById(R.id.contactsItem_iv_head);
				
				listcellview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						doCick(client);
					}
				});
				
				tv_ip.setText(client.getIp());
				tv_name.setText(client.getName());
				tv_id.setText(client.getId());
				im_head.setImageBitmap(Utils.getRes(context, client.getHead()));
			}
		};
	
		mSwipRefresh.post(new Runnable() {
			
			@Override
			public void run() {
				onRefresh();
				mSwipRefresh.setRefreshing(true);
				
			}
		});
		lv.setAdapter(adapt);
		lv.setOnItemClickListener(this);
		TextView tv1 = (TextView) view.findViewById(R.id.contacts_tv1);
		TextView tv2 = (TextView) view.findViewById(R.id.contacts_tv2);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		return view;
	}

	

	



	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.contacts_tv1:
			
			NetBackupFragment netBackupFragment=(NetBackupFragment) fragmentManager.findFragmentByTag(NetBackupFragment.class.getName());
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
			
			
			

		case R.id.contacts_tv2:
			
			ContactsFragment contactsFragment=(ContactsFragment) fragmentManager.findFragmentByTag(ContactsFragment.class.getName());
			if (contactsFragment==null) {
				contactsFragment=new ContactsFragment();
				
			}
			
			if (!contactsFragment.isAdded()) {
				
				transaction.add(R.id.mian_frame, contactsFragment,contactsFragment.getClass().getName());
				transaction.hide(this);
				
				
				transaction.commit();
			} else {
				
				transaction.show(contactsFragment);
				transaction.hide(this);
				transaction.commit();
			}
			break;

		
		}
		
	}

	@Override
	public void onRefresh() {
		
		if (!isfresh) {
			isfresh=!isfresh;
			new Thread(){public void run() {
				Ping ping = new Ping();
				ping.pingAll(wifiUtil.getIP());
				
			};
			};
			
			handle.sendEmptyMessageDelayed(MSG_TIMEOUT, 2000);
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.println("1111111111------");
		Contact item = adapt.getItem(position);
		
		Bundle bundle = new Bundle();  
		bundle.putSerializable("contact", list.get(position));   
		
		
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		ChatFragment chatFragment=(ChatFragment) fragmentManager.findFragmentByTag(ChatFragment.class.getName());
		if (chatFragment==null) {
			chatFragment=new ChatFragment();
			
		}
		chatFragment.setArguments(bundle);
		if (!chatFragment.isAdded()) {
		
			transaction.add(R.id.mian_frame, chatFragment,chatFragment.getClass().getName());
			transaction.hide(this);
			transaction.addToBackStack(null); 
			
			transaction.commit();
		} else {
			
			transaction.show(chatFragment);
			transaction.hide(this);
			transaction.addToBackStack(null); 
			transaction.commit();
		}
		
	}
	public void  doCick(Contact contact) {
		Bundle bundle = new Bundle();  
		bundle.putSerializable("contact", contact);   
		
		bundle.putBoolean("conned", true);
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		ChatFragment chatFragment=(ChatFragment) fragmentManager.findFragmentByTag(ChatFragment.class.getName());
		if (chatFragment==null) {
			chatFragment=new ChatFragment();
			
		}
		chatFragment.setArguments(bundle);
		if (!chatFragment.isAdded()) {
		
			transaction.add(R.id.mian_frame, chatFragment,chatFragment.getClass().getName());
			transaction.hide(this);
			transaction.addToBackStack(null); 
			
			transaction.commit();
		} else {
			
			transaction.show(chatFragment);
			transaction.hide(this);
			transaction.addToBackStack(null); 
			transaction.commit();
		}
	}
}