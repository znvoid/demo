package com.znvoid.demo;

import com.znvoid.demo.adapt.Menulistviewadapt;
import com.znvoid.demo.daim.MLvData;
import com.znvoid.demo.fragment.AccountFragm;
import com.znvoid.demo.fragment.ChatFragment;
import com.znvoid.demo.fragment.ContactsFragment;
import com.znvoid.demo.fragment.DeskFragment;
import com.znvoid.demo.fragment.NetFragment;
import com.znvoid.demo.fragment.WifilistFragment;
import com.znvoid.demo.server.TCPSevice;
import com.znvoid.demo.util.Utils;
import com.znvoid.demo.util.WifiUtil;
import com.znvoid.demo.view.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CircleImageView cim;
	private TextView textView;
	private String mTitle;
	private LinearLayout mDrawerView;
	private SharedPreferences sp;
	private Context context;
	private static ChatFragment chatFragment = new ChatFragment();
	private ContactsFragment contactsFragment = new ContactsFragment();
	private WifilistFragment wifilistFragment = new WifilistFragment();
	private NetFragment netFragment = new NetFragment();
	// private AccountFragment accountFragment=new AccountFragment();
	private AccountFragm accountFragment = new AccountFragm();
	private DeskFragment deskFragment = new DeskFragment();

	private String mIP;
	private WifiUtil wifiUtil;
	private Fragment mContent=new Fragment() ;// 当前Fragment
	private Toolbar toolbar;
	SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initsetting();// 初始化设置
		startService(new Intent(this, TCPSevice.class));
	}

	private void initsetting() {
		sp = getSharedPreferences("configs", MODE_PRIVATE);
		context = this;
		wifiUtil = new WifiUtil(context);
		mIP = wifiUtil.getIP();

		if (sp.getString("ID", "NULL").equals("NULL")) {

			Editor editor = sp.edit();
			editor.putString("ID", Utils.getId(context));
			editor.putString("author", Utils.getId(context));
			editor.commit();

		}
		

		toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerView = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawerContent,
				R.string.closeDrawerContent) {
			@Override
			public void onDrawerOpened(View drawerView) {
				mTitle = (String) toolbar.getTitle();

				toolbar.setTitle("菜单");
				invalidateOptionsMenu();
				cim.setImageBitmap(Utils.getRes(context, sp.getString("head", "head_1")));
				textView.setText(sp.getString("author","ID:"+ Utils.getId(context)));
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				if ("菜单".equals(getSupportActionBar().getTitle())) {

					toolbar.setTitle(mTitle);
				}

				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setHomeButtonEnabled(true);
		cim = (CircleImageView) findViewById(R.id.profile_image);
		textView = (TextView) findViewById(R.id.profile_tv);
		cim.setImageBitmap(Utils.getRes(context, sp.getString("head", "head_1")));
		textView.setText(sp.getString("author", "ID:"+ Utils.getId(context)));
		// FragmentManager

		// chatfragment初始化
		//mContent=contactsFragment;
		switchContent(contactsFragment);
		// switchContent(chatFragment);
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
					toolbar.setTitle("账户");
					switchContent(accountFragment);
					break;

				case 1:
					toolbar.setTitle("聊天");
					switchContent(contactsFragment);
					break;

				case 2:
					toolbar.setTitle("WiFi");
					switchContent(wifilistFragment);
					break;
				case 3:
					toolbar.setTitle("局域网");
					switchContent(netFragment);
					break;
				case 4:// 阅读
					toolbar.setTitle("书架");
					switchContent(deskFragment);
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
				getFragmentManager().beginTransaction().hide(mContent)
						.add(R.id.mian_frame, fragment, fragment.getClass().getName()).commit();

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

		MenuItem item = menu.findItem(R.id.action_websearch);
		searchView = (SearchView) MenuItemCompat.getActionView(item);
		final ImageView searchView_close = (ImageView) searchView.findViewById(R.id.search_close_btn);
		searchView.setQueryHint("百度搜索");
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String text) {

				webSercher(text);
				// searchView.clearFocus();
				searchView_close.performClick();
				searchView_close.performClick();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {

				return false;
			}
		});

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
		// if (mDrawerToggle.onOptionsItemSelected(item)) {
		// return true;
		// }
		// switch (item.getItemId()) {
		// case R.id.action_websearch:
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.VIEW");
		// Uri uri = Uri.parse("http://www.baidu.com");
		// intent.setData(uri);
		// startActivity(intent);
		//
		// break;
		//
		// default:
		// break;
		// }

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

	private void webSercher(String string) {
		if ("".equals(string)) {
			return;
		}

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri uri = Uri.parse("http://www.baidu.com/s?wd=" + string);
		intent.setData(uri);
		startActivity(intent);

		// intent.setAction(Intent.ACTION_WEB_SEARCH);
		// intent.putExtra(SearchManager.QUERY, string);
		// startActivity(intent);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		System.out.println(keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK && getFragmentManager().getBackStackEntryCount() != 0) {
			// 退出程序的代码
			getFragmentManager().popBackStack();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
