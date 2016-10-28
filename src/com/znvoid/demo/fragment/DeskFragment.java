package com.znvoid.demo.fragment;


import java.util.ArrayList;
import com.znvoid.demo.BookpageActivity;
import com.znvoid.demo.FileListActivity;
import com.znvoid.demo.R;
import com.znvoid.demo.adapt.BookDeskAdapt;
import com.znvoid.demo.daim.BookImf;
import com.znvoid.demo.sql.BooksSqlOpenHelp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class DeskFragment extends Fragment {
	private GridView gridView;
	private BookDeskAdapt adapt;
private BooksSqlOpenHelp sqlOpenHelp;
private Context Context;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context=getActivity();
		View view = inflater.inflate(R.layout.book_desk, null);
		gridView = (GridView) view.findViewById(R.id.bookShelf);
		adapt = new BookDeskAdapt(Context);
	
		gridView.setAdapter(adapt);
		sqlOpenHelp=new BooksSqlOpenHelp(Context);
		setClickListener();
		readData();
		return view;
	}

	public void setClickListener() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BookImf bookImf = (BookImf) adapt.getItem(position);
				if (bookImf.getPath().equals("deful")) {
					Intent intent=new Intent(Context, FileListActivity.class);
					
					
					startActivity(intent);
					
					
				}else {
					Intent intent=new Intent(Context, BookpageActivity.class);
					intent.putExtra("path", bookImf.getPath());
					
					startActivity(intent);
					
				}
				
				
				

			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				BookImf bookImf = (BookImf) adapt.getItem(position);
				if (bookImf.getPath().equals("deful")) {

				}else {
					adapt.remove(position);
					sqlOpenHelp.delete(bookImf);
				}
				
				return false;
			}
		});
	}
	@Override
	public void onResume() {
		
		readData();
		super.onResume();
	}
	public void readData() {
		ArrayList<BookImf> books=sqlOpenHelp.loadall();
		
		if (books!=null) {
			adapt.setdata(books);
		}
	
	}
}
