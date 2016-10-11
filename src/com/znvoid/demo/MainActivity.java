package com.znvoid.demo;

import com.znvoid.demo.adapt.Menulistviewadapt;
import com.znvoid.demo.daim.MLvData;
import com.znvoid.demo.view.CircleImageView;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CircleImageView cim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initsetting();// 初始化设置

	}

	private void initsetting() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		cim=(CircleImageView) findViewById(R.id.profile_image);
		
		
		
		mDrawerList.setAdapter(new Menulistviewadapt(this));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MLvData data = (MLvData) mDrawerList.getItemAtPosition(position);
				Log.i("tt", data.getName());
				shoutoash(data.getName() + "被点击");
			}
		});
		
		cim.setOnClickListener(this);
	}

	private void shoutoash(String text) {

		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.profile_image:
			shoutoash("更换头像吗");
			break;

		default:
			break;
		}
	}

	
	
	

}
