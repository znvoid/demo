package com.znvoid.demo.adapt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyAdapt<T> extends BaseAdapter {

	private Context context;
	// ��Դ����xml�ļ�
	private int layoutid;
	private List<T> datalist = new ArrayList<T>();

	public MyAdapt(Context context, int resId) {
		super();
		this.context = context;
		layoutid = resId;
	}
//������Ŀ
	public void add(T item) {
		datalist.add(item);
		notifyDataSetChanged();
	}
	//ɾ����Ŀ
public void remove(int position) {
	datalist.remove(position);
	notifyDataSetChanged();
}
//��������
public void setdata(List<T> list) {
	datalist.clear();
	datalist.addAll(list);
	notifyDataSetChanged();
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(layoutid, null);
		}
		initlistcell(position, convertView,  parent);
		return convertView;
	}
	/*
	 * ��ʼ��listview��Ŀ
	 */
protected abstract void initlistcell(int position, View listcellview, ViewGroup parent);

}
