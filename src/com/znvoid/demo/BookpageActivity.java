package com.znvoid.demo;



import java.io.IOException;

import com.znvoid.demo.daim.BookCenterAreaTouchListener;
import com.znvoid.demo.menu.TxtViewMenu;
import com.znvoid.demo.view.BookPageFactory;
import com.znvoid.demo.view.BookPageWidget;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;



public class BookpageActivity extends Activity {
	/** Called when the activity is first created. */
	private BookPageWidget mPageWidget;
	//当前页、下一页图像
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	TxtViewMenu mMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//FileUtils fileUtils=new FileUtils();
		//String text=getResources().getString(R.string.text);
		//fileUtils.write2SDFromInput("", "test.txt", text);
		//去掉标题
		String path=getIntent().getStringExtra("path");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		   DisplayMetrics  dm = new DisplayMetrics();  
		      //取得窗口属性  
		      getWindowManager().getDefaultDisplay().getMetrics(dm);  
		       
		      //窗口的宽度  
		      int screenWidth = dm.widthPixels;  
		       
		      //窗口高度  
		      int screenHeight = dm.heightPixels; 
		
		mPageWidget = new BookPageWidget(this);
		mPageWidget.setScreen(screenWidth,screenHeight);
		setContentView(mPageWidget);

		
		pagefactory = new BookPageFactory(screenWidth, screenHeight);
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.book_theme));

		try {
			if (path!=null) {
				pagefactory.openbook(path);
//				pagefactory.openbook("/sdcard/test.txt");
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
			Toast.makeText(this, "电子书不存在,请将《test.txt》放在SD卡根目录下",
					Toast.LENGTH_SHORT).show();
		}

		mPageWidget.setBookPageFactory(pagefactory);
		mPageWidget.setBookCenterAreaTouchListener(new PopMenu());
		initMenu()	;
	}
	
	private void initMenu() {
		mMenu = new TxtViewMenu(this);
		
		
	}

	private class PopMenu implements BookCenterAreaTouchListener{

		@Override
		public void onAreaTouch() {
			mMenu.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
			
		}

		@Override
		public void onOutSideAreaTouch() {
			
			mMenu.dismiss();
		}
		
	}
	
}