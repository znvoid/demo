package com.znvoid.demo.fragment;



import com.znvoid.demo.R;
import com.znvoid.demo.WifiUtil;
import com.znvoid.demo.adapt.MyAdapt;

import com.znvoid.demo.net.ClientScanResultSO;
import com.znvoid.demo.net.MacGetFromArp;
import com.znvoid.demo.net.Ping;

import android.app.Fragment;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NetFragment extends Fragment {
	private final int MSG_OVER = 0x10001;
	private final int MSG_STOP = 0x10002;
	private final int MSG_TIMEOUT = 0x10003;
	private boolean flagrefreshdate=true;
	private TextView tv_ownip;
	private TextView tv1;
	private ListView lv;
	private Context context;
	private ImageView mImageView_refresh;
	private Animation animation;
	private WifiUtil wifiUtil;
	private MyAdapt<ClientScanResultSO> adapt;
	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_OVER:

				break;
			case MSG_STOP:

				break;
			case MSG_TIMEOUT:

				break;

			default:
				break;
			}
			
			flagrefreshdate=!flagrefreshdate;
			MacGetFromArp arp = new MacGetFromArp();
			adapt.setdata(arp.getClientList(true, 1000));
			mImageView_refresh.clearAnimation();

		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wifilistfragment, null);
		context = getActivity();
		lv = (ListView) view.findViewById(R.id.wifi_listView);
		tv_ownip = (TextView) view.findViewById(R.id.tv_own_wife);
		tv1 = (TextView) view.findViewById(R.id.textView1);
		mImageView_refresh = (ImageView) view.findViewById(R.id.imageView_refresh);
		wifiUtil = new WifiUtil(context);

		tv1.setText("局域网设备");
		adapt = new MyAdapt<ClientScanResultSO>(context, R.layout.netlistview) {

			@Override
			protected void initlistcell(int position, View listcellview, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView tv_ip = (TextView) listcellview.findViewById(R.id.tv_ip);
				TextView tv_mac = (TextView) listcellview.findViewById(R.id.tv_mac);
				tv_ip.setText(getItem(position).getIp());
				tv_mac.setText(getItem(position).getHwAddress());

			}
		};
		refreshanimation();
		refreshdata();
		lv.setAdapter(adapt);

		tv_ownip.setText("本机ip：" + wifiUtil.getIP() + "\nmac地址:" + wifiUtil.getMacAddress());
		mImageView_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (flagrefreshdate) {
					handle.sendEmptyMessage(MSG_STOP);
				}else {
					refreshanimation();
					refreshdata();
				}
				flagrefreshdate=!flagrefreshdate;
				// adapt.setdata(refreshdata());

			}
		});

		return view;
	}

	public void refreshanimation() {
		animation = AnimationUtils.loadAnimation(context, R.anim.netdevicerefresh);
		animation.setRepeatCount(-1);
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		mImageView_refresh.startAnimation(animation);

	}

	public void refreshdata() {
		new Thread() {
			public void run() {

				Ping ping = new Ping();
				ping.pingAll(wifiUtil.getIP());

				handle.sendEmptyMessage(MSG_OVER);
			};
		}.start();
		new Thread() {
			public void run() {

				try {
					Thread.sleep(5000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				handle.sendEmptyMessage(MSG_TIMEOUT);
				
			};
		}.start();
		// MacGetFromArp arp=new MacGetFromArp();
		// return arp.getClientList(true, 1000);

	}

}