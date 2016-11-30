package com.znvoid.demo.adapt;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.znvoid.demo.R;
import com.znvoid.demo.daim.ImageBean;
import com.znvoid.demo.popup.ShowImagePopup;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

public class SelectorPopupAdapt extends BaseAdapter implements OnCheckedChangeListener, OnClickListener {

	private List<ImageBean> list=new ArrayList<ImageBean>();
	
	private GridView mGridView;
	protected LayoutInflater mInflater;
	private ShowImagePopup showImagePopup;
	
	private ImageLoader imageLoader;
	private CheckBoxClickListener listener;

	public SelectorPopupAdapt(Activity context, GridView gridView) {
		this.mGridView=gridView;
		mInflater = LayoutInflater.from(context);
		showImagePopup=new ShowImagePopup(context);
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(context);
		
		imageLoader=ImageLoader.getInstance();
				imageLoader.init(configuration);
	}

	
public void setdata(List<ImageBean> list) {
	this.list.clear();
	this.list.addAll(list);
	notifyDataSetChanged();
}
	
	public void setcheckBoxClickListener(CheckBoxClickListener listener ) {
		this.listener=listener;
	}
	public interface CheckBoxClickListener{
		
		void checkBoxOnClick(String path);
		
		
	}
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public ImageBean getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder viewHolder;
		ImageBean mImageBean = list.get(position);
		String path = mImageBean.getPath();

		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.select_item, null);
			viewHolder.mImageView=(ImageView) convertView.findViewById(R.id.select_item_image);
			viewHolder. mCheckBox= (CheckBox) convertView.findViewById(R.id.select_item_checkbox);
			
			viewHolder.maskView=convertView.findViewById(R.id.select_item_mask);
			
		
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		viewHolder. mCheckBox.setChecked(mImageBean.isChoosed());
		viewHolder. mCheckBox.setTag(position);
		viewHolder.mImageView.setTag(path);
		viewHolder.maskView.setTag(convertView);
//		if (position==0) {
//			viewHolder.mImageView.setImageResource(R.drawable.default_error);;
//		} else {
		imageLoader.displayImage("file://"+path, viewHolder.mImageView);	
//		}
		
//		 viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeFile(path));
		
		viewHolder.mCheckBox.setOnCheckedChangeListener(this);
		viewHolder.mImageView.setOnClickListener(this);
		viewHolder.update();
		return convertView;
	}
	
	public class ViewHolder{
		public ImageView mImageView;
		public CheckBox mCheckBox;
		public View maskView;
		public void update() {
			maskView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				
				@Override
				public void onGlobalLayout() {
					int position=(Integer) mCheckBox.getTag();
					
					View v = (View) maskView.getTag();
					int width = v.getWidth();
					v.setLayoutParams(new GridView.LayoutParams(
							GridView.LayoutParams.FILL_PARENT,
							width));
					
//					if (position > 0 &&position%2==1) {
//						View v = (View) maskView.getTag();
//						int height = v.getHeight();
//						
//						View view = mGridView.getChildAt(position - 1);
//
//					int lastheight = view.getHeight();
//
//					// 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可	
//					if (height > lastheight) {
//						view.setLayoutParams(new GridView.LayoutParams(
//								GridView.LayoutParams.FILL_PARENT,
//								height));
//					} else if (height < lastheight) {
//						v.setLayoutParams(new GridView.LayoutParams(
//								GridView.LayoutParams.FILL_PARENT,
//								lastheight));
//					}
//
//
//					}
					
				}
			});
			
			
			
		}

		
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position=(Integer) buttonView.getTag();
		listener.checkBoxOnClick(list.get(position).getPath());
		list.get(position).setChoosed(isChecked);
	}


	@Override
	public void onClick(View v) {
		String path=(String) v.getTag();
		Log.e("Light", path);
		showImagePopup.Show(path);
	}
}
