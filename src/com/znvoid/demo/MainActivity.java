package com.znvoid.demo;

import com.znvoid.demo.adapt.Menulistviewadapt;
import com.znvoid.demo.daim.MLvData;
import com.znvoid.demo.fragment.ChatFragment;
import com.znvoid.demo.fragment.NetFragment;
import com.znvoid.demo.fragment.WifilistFragment;
import com.znvoid.demo.view.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CircleImageView cim;
	private String mTitle;
	private LinearLayout mDrawerView;

	private ChatFragment chatFragment = new ChatFragment();;
	private WifilistFragment wifilistFragment = new WifilistFragment();
	private NetFragment netFragment=new NetFragment();
	
	private Fragment mContent = new Fragment();// 当前Fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initsetting();// 初始化设置

	}

	private void initsetting() {
		mTitle = (String) getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerView = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.openDrawerContent,
				R.string.closeDrawerContent) {
			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("菜单");
				invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		cim = (CircleImageView) findViewById(R.id.profile_image);
		// FragmentManager

		// chatfragment初始化
		switchContent(chatFragment);
		// getFragmentManager().beginTransaction().replace(R.id.mian_frame,
		// chatFragment, "chatfragment").commit();

		// mDrawerList添加适配器
		mDrawerList.setAdapter(new Menulistviewadapt(this));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				MLvData data = (MLvData) mDrawerList.getItemAtPosition(position);
				switch (position) {
				case 0:

					break;

				case 1:
					switchContent(chatFragment);
					break;

				case 2:

					switchContent(wifilistFragment);
					break;
				case 3:

					switchContent(netFragment);
					break;
				default:
					break;
				}
				shoutoash(data.getName() + "被点击");
				mDrawerLayout.closeDrawers();
			}
		});
		// 设置头像图片点击事件监听
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
	/*
	 * 切换fragment，
	 */

	public void switchContent(Fragment fragment) {
		if (mContent != fragment) {

			if (!fragment.isAdded()) {
				getFragmentManager().beginTransaction().hide(mContent).add(R.id.mian_frame, fragment).commit();

			} else {
				getFragmentManager().beginTransaction().hide(mContent).show(fragment).commit();
			}

			mContent = fragment;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);
		menu.findItem(R.id.action_websearch).setVisible(!isDrawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_websearch:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://www.baidu.com");
			intent.setData(uri);
			startActivity(intent);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
