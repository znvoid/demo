package com.znvoid.demo.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.znvoid.demo.BookpageActivity;
import com.znvoid.demo.FileListActivity;
import com.znvoid.demo.R;
import com.znvoid.demo.adapt.BookDeskAdapt;
import com.znvoid.demo.daim.BookImf;
import com.znvoid.demo.sql.BooksSqlOpenHelp;
import com.znvoid.demo.util.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class DeskFragment extends Fragment {
	private GridView gridView;
	private BookDeskAdapt adapt;
	private BooksSqlOpenHelp sqlOpenHelp;
	private Context Context;
	private int FILE_SELECT_CODE = 0x10001;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context = getActivity();
		View view = inflater.inflate(R.layout.book_desk, null);
		gridView = (GridView) view.findViewById(R.id.bookShelf);
		adapt = new BookDeskAdapt(Context);

		gridView.setAdapter(adapt);
		sqlOpenHelp = new BooksSqlOpenHelp(Context);
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
					// �����Զ����ļ�����
					// Intent intent=new Intent(Context,
					// FileListActivity.class);
					// startActivity(intent);
					importExcel();

				} else {

					/*
					 * ��ʱ���� Intent intent=new Intent(Context,
					 * BookpageActivity.class); intent.putExtra("path",
					 * bookImf.getPath()); startActivity(intent);
					 */

				}

			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final BookImf bookImf = (BookImf) adapt.getItem(position);
				if (bookImf.getPath().equals("deful")) {

				} else {

					Utils.showDialog(getActivity(), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							sqlOpenHelp.delete(bookImf);
							readData();

						}
					});

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
		ArrayList<BookImf> books = sqlOpenHelp.loadall();

		if (books != null) {
			adapt.setdata(books);
		}

	}

	/*
	 * ����ϵͳ�ļ�������
	 */
	private void importExcel() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		// intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(intent, FILE_SELECT_CODE);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), "���ļ�������ʧ��", Toast.LENGTH_SHORT).show();// �������ӵ������ļ����������������û������ļ�������
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK && requestCode == FILE_SELECT_CODE) {

			String path = data.getData().getPath();
			// System.out.println(path);
			if (path.endsWith(".txt")) {
				if (!sqlOpenHelp.find(path)) {

					adapt.add(new BookImf(path));
					sqlOpenHelp.add(new BookImf(path));

				}

			} else {
				Toast.makeText(getActivity(), "��txt�ļ������ʧ�ܣ���", Toast.LENGTH_SHORT).show();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
