package com.znvoid.demo;

import com.znvoid.demo.adapt.Menulistviewadapt;
import com.znvoid.demo.daim.MLvData;
import com.znvoid.demo.fragment.AccountFragment;
import com.znvoid.demo.fragment.ChatFragment;
import com.znvoid.demo.fragment.NetFragment;
import com.znvoid.demo.fragment.WifilistFragment;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.view.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CircleImageView cim;
	private TextView textView;
	private String mTitle;
	private LinearLayout mDrawerView;
	private SharedPreferences sp;
	private Context context;
	private ChatFragment chatFragment = new ChatFragment();
	private WifilistFragment wifilistFragment = new WifilistFragment();
	private NetFragment netFragment=new NetFragment();
	private AccountFragment accountFragment=new AccountFragment();
	private String mIP;
	private WifiUtil wifiUtil;
	private Fragment mContent = new Fragment();// ��ǰFragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initsetting();// ��ʼ������

	}

	private void initsetting() {
		sp=getSharedPreferences("configs", MODE_PRIVATE);
		context=this;
		wifiUtil=new WifiUtil(context);
		mIP=wifiUtil.getIP();
		mTitle = (String) getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerView = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.openDrawerContent,
				R.string.closeDrawerContent) {
			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("�˵�");
				invalidateOptionsMenu();
			cim.setImageBitmap(Utils.getRes(context, sp.getString("head", "head_1")));
				textView.setText(sp.getString("author", mIP));
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
		textView=(TextView) findViewById(R.id.profile_tv);
		cim.setImageBitmap(Utils.getRes(context, sp.getString("head", "head_1")));
		textView.setText(sp.getString("author", mIP));
		// FragmentManager

		// chatfragment��ʼ��
		switchContent(chatFragment);
		// getFragmentManager().beginTransaction().replace(R.id.mian_frame,
		// chatFragment, "chatfragment").commit();

		// mDrawerList���������
		mDrawerList.setAdapter(new Menulistviewadapt(this));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				MLvData data = (MLvData) mDrawerList.getItemAtPosition(position);
				switch (position) {
				case 0:
					switchContent(accountFragment);
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
				shoutoash(data.getName() + "�����");
				mDrawerLayout.closeDrawers();
			}
		});
		// ����ͷ��ͼƬ����¼�����
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
			shoutoash("����ͷ����");
			break;

		default:
			break;
		}
	}
	/*
	 * �л�fragment��
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
