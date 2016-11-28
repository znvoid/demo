package com.znvoid.demo.fragment;


import com.znvoid.demo.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class NetBackupFragment extends Fragment implements OnClickListener {

	private FragmentManager fragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fragmentManager = getFragmentManager();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.netbackup, null);
		
		TextView tv2 = (TextView) view.findViewById(R.id.contacts_tv2);
		TextView tv3 = (TextView) view.findViewById(R.id.contacts_tv3);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
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
		case R.id.contacts_tv3:
			LinkFrangemt linkFrangemt=(LinkFrangemt) fragmentManager.findFragmentByTag(LinkFrangemt.class.getName());
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
	
	
}
