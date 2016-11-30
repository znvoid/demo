package com.znvoid.demo.popup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.znvoid.demo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class ShowImagePopup extends PopupWindow {
	private ImageView imageView;
	private Activity context;
	private ImageLoader imageLoader ;
	public ShowImagePopup(Activity context) {
		super(context);
		this.context = context;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen","android");
	    int height = context.getResources().getDimensionPixelSize(resourceId);
	    
	    setWidth(metrics.widthPixels);
		setHeight(metrics.heightPixels);
		View rootView = LayoutInflater.from(context).inflate(R.layout.showimage, null);

		init(rootView);

		

		
	}

	public void init(View rootView) {

		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(context);
		
		imageLoader=ImageLoader.getInstance();
				imageLoader.init(configuration);
		
		 imageView = (ImageView) rootView.findViewById(R.id.showimage_image);
		
		rootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						dismiss();

					}
				}, 300);

			}
		});

		
		setContentView(rootView);

		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());

	}

	public void Show(String path) {
		if (isShowing()) {
			dismiss();
		}
		if (path!=null) {
			
			imageLoader.displayImage("file://"+path, imageView);
			showAtLocation(context.getWindow().getDecorView(), Gravity.TOP, 0,0);
		
			
		}
		
	}

	@Override
	public void dismiss() {
		imageView.setImageResource(R.drawable.default_error);
		super.dismiss();
	}
}
