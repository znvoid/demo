package com.znvoid.demo;

import com.znvoid.demo.adapt.Menulistviewadapt;
import com.znvoid.demo.daim.MLvData;
import com.znvoid.demo.fragment.ChatFragment;
import com.znvoid.demo.fragment.WifilistFragment;
import com.znvoid.demo.view.CircleImageView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

	
	private ChatFragment chatFragment = new ChatFragment();;
	private WifilistFragment wifilistFragment = new WifilistFragment();
	private  Fragment mContent = new Fragment() ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initsetting();// 初始化设置

	}

	private void initsetting() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_listview);
		cim = (CircleImageView) findViewById(R.id.profile_image);
		// FragmentManager

		
		// chatfragment初始化
		switchContent(chatFragment);
		//getFragmentManager().beginTransaction().replace(R.id.mian_frame, chatFragment, "chatfragment").commit();

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
					switchContent( chatFragment);
					break;
					
				case 2:
					
					switchContent( wifilistFragment);
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

	public void changfragment(Fragment from, Fragment to) {
		if (from == null) {
			
			getFragmentManager().beginTransaction().add(R.id.mian_frame, to).commit();
			from = to;
			return;
		}
		
		if (from != to) {
			
			if (!to.isAdded()) {
				getFragmentManager().beginTransaction().hide(from).add(R.id.mian_frame, to).commit();
				
			} else {
				getFragmentManager().beginTransaction().hide(from).show(to).commit();
			}
			
			
			from = to;
		}

	}
	public void switchContent(Fragment fragment) {
        if(mContent != fragment) {
        	
        	if (!fragment.isAdded()) {
				getFragmentManager().beginTransaction().hide(mContent).add(R.id.mian_frame, fragment).commit();
				
			} else {
				getFragmentManager().beginTransaction().hide(mContent).show(fragment).commit();
			}
        	
        	
            mContent = fragment;
            
        }
    }
}
