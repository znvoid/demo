package com.znvoid.demo.adapt;

import java.util.ArrayList;

import com.znvoid.demo.R;
import com.znvoid.demo.daim.MLvData;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * ≤‡ª¨≤Àµ•÷–listview  ≈‰∆˜
 */
public class Menulistviewadapt extends BaseAdapter {
	private ArrayList<MLvData> arrayList=new ArrayList<MLvData>();
	private Context context;
	private LayoutInflater inflater;

	public Menulistviewadapt() {
		// TODO Auto-generated constructor stub
	}

	public Menulistviewadapt(ArrayList<MLvData> arrayList, Context context) {
		super();
		this.arrayList = arrayList;
		this.context = context;
	}
	public void setDate(ArrayList<MLvData> arrayList){
		
		this.arrayList = arrayList;
		
	}
	public Menulistviewadapt(Context context) {
		super();
		this.context = context;
		
		arrayList.add(new MLvData(R.drawable.ic_user, "’Àªß"));
		arrayList.add(new MLvData(R.drawable.ic_chat, "¡ƒÃÏ"));
		arrayList.add(new MLvData(R.drawable.ic_wifi, "wifi"));
		arrayList.add(new MLvData(R.drawable.ic_wifi, "æ÷”ÚÕ¯"));
		arrayList.add(new MLvData(R.drawable.ic_desk, "‘ƒ∂¡"));
		arrayList.add(new MLvData(R.drawable.ic_settings, "…Ë÷√"));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.left_menu_listview, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.list_im);
		imageView.setImageResource(arrayList.get(position).getId());
		
		
		
		TextView textView=(TextView) view.findViewById(R.id.list_tv);
		textView.setText(arrayList.get(position).getName());

		return view;
	}

}
