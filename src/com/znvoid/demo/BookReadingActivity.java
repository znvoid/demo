package com.znvoid.demo;

import java.io.IOException;

import com.znvoid.demo.bookReading.BookFactory;
import com.znvoid.demo.bookReading.BookFactoryImp;
import com.znvoid.demo.bookReading.BookFactoryListener;
import com.znvoid.demo.bookReading.BookManger;
import com.znvoid.demo.bookReading.BookMangerImp;
import com.znvoid.demo.bookReading.BookView;
import com.znvoid.demo.imf.BookCenterAreaTouchListener;
import com.znvoid.demo.menu.ReaderProgressMenu;
import com.znvoid.demo.menu.ReaderProgressMenu.ReaderProgressChangeListener;
import com.znvoid.demo.menu.ReaderStyleMenu;
import com.znvoid.demo.menu.ReaderStyleMenu.onReaderStyleChangeListener;
import com.znvoid.demo.menu.ReaderTextSize;
import com.znvoid.demo.menu.ReaderTextSize.ReaderTextSizeChangeListener;
import com.znvoid.demo.menu.TxtViewMenu;
import com.znvoid.demo.menu.TxtViewMenu.TxtMenuClockListener;
import com.znvoid.demo.sql.BooksSqlOpenHelp;
import com.znvoid.demo.util.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookReadingActivity extends Activity implements TxtMenuClockListener, ReaderTextSizeChangeListener,
		ReaderProgressChangeListener, onReaderStyleChangeListener, BookFactoryListener {
	/** Called when the activity is first created. */
	private BookView mBookView;
	private BookFactory mBookFactory;
	private BookManger mBookManger;
	TxtViewMenu mMenu;
	private RelativeLayout mView;
	private TextView mTitle;
	private ReaderTextSize readerTextSizeMenu;
	private ReaderProgressMenu readerProgressMenu;
	private ReaderStyleMenu readerStyleMenu;
	private BooksSqlOpenHelp sql;
	String path;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sql = new BooksSqlOpenHelp(getApplicationContext());
		sp = getSharedPreferences("configs", MODE_PRIVATE);
		Utils.init(this);
		path = getIntent().getStringExtra("path");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DisplayMetrics dm = new DisplayMetrics();
		// ȡ�ô�������
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// ���ڵĿ��
		int screenWidth = dm.widthPixels;

		// ���ڸ߶�
		int screenHeight = dm.heightPixels;
		setContentView(R.layout.bookreading);
		mBookView = (BookView) findViewById(R.id.bookreading_bookView);
		mView = (RelativeLayout) findViewById(R.id.bookreading_titlebar);
		mView.setVisibility(View.GONE);
		mTitle = (TextView) findViewById(R.id.title_textview);
		mBookFactory = new BookFactoryImp(screenWidth, screenHeight);

		mBookManger = new BookMangerImp(path);
		mBookFactory.setBookManage(mBookManger);
		getBookAgre();
		mBookFactory.setBookFactoryListener(this);
		mBookView.setBookFactory(mBookFactory);
		mBookView.setBookCenterAreaTouchListener(new PopMenu());
		mTitle.setText(mBookManger.getBookName());

		initMenu();
	}

	public void getBookAgre() {

		int mark = sql.getMark(path);

		int textsize = sp.getInt("booktextsize", 24);
		int sty = sp.getInt("bookstyle", R.drawable.reader__themes__paper);

		mBookManger.setProgress(mark);
		mBookManger.setBackgroud(BitmapFactory.decodeResource(getResources(), sty));
		mBookManger.setTextSize(textsize);
		mBookManger.notifChange();

	}

	private void initMenu() {
		mMenu = new TxtViewMenu(this);
		readerTextSizeMenu = new ReaderTextSize(this, 24);
		readerProgressMenu = new ReaderProgressMenu(this);
		readerStyleMenu = new ReaderStyleMenu(this);
		mMenu.setOnTxtMenuClickListener(this);
		readerTextSizeMenu.setReaderTextSizeChangeListener(this);
		readerProgressMenu.setReaderProgressChangeListener(this);
		readerStyleMenu.setReaderStyleChangeListener(this);
	}

	public void finishactivity(View v) {
		mMenu.dismiss();
		readerTextSizeMenu.dismiss();
		readerProgressMenu.dismiss();
		readerStyleMenu.dismiss();
		finish();
	}

	private class PopMenu implements BookCenterAreaTouchListener {

		@Override
		public void onAreaTouch() {
			mView.setVisibility(View.VISIBLE);
			mMenu.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

		}

		@Override
		public void onOutSideAreaTouch() {
			mView.setVisibility(View.GONE);
			mMenu.dismiss();
			readerTextSizeMenu.dismiss();
			readerProgressMenu.dismiss();
			readerStyleMenu.dismiss();
		}

	}

	// -----------�˵������¼�
	@Override
	public void onTextMenuClicked() {
		mMenu.dismiss();
		readerTextSizeMenu.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onProgressMenuClicked() {
		mMenu.dismiss();
		readerProgressMenu.show(mBookManger.getProgressPercent(), getWindow().getDecorView());
	}

	@Override
	public void onStytleMenuClicked() {
		mMenu.dismiss();
		readerStyleMenu.show(sp.getInt("bookstyle", R.drawable.reader__themes__paper), getWindow().getDecorView());
	}

	@Override
	public void onLightMenuClicked() {
		// mMenu.dismiss();

	}

	// -----------------
	@Override
	public void onTextSizeChange(int size) {
		mBookManger.setTextSize(size);
		mBookManger.notifChange();
		sp.edit().putInt("booktextsize", size).commit();

	}

	@Override
	public void onProgressChange(float progress) {

		mBookManger.setProgress(progress);
		mBookManger.notifChange();
	}

	@Override
	public void onStyleChange(int stylecolor) {
		mBookManger.setBackgroud(BitmapFactory.decodeResource(getResources(), stylecolor));
		mBookManger.notifChange();
		sp.edit().putInt("bookstyle", stylecolor).commit();
		// mBookView.setBackgroundResource(stylecolor);
	}

	@Override
	public void onBookProgressChange(int progress) {
		//System.out.println(progress);
		sql.update(path, progress);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK&&mView.isShown()){
			mView.setVisibility(View.GONE);
			mMenu.dismiss();
			readerTextSizeMenu.dismiss();
			readerProgressMenu.dismiss();
			readerStyleMenu.dismiss();
			return true;
		}
			
			
			return super.onKeyDown(keyCode, event);
	}
}