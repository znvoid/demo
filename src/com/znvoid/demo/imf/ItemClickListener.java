package com.znvoid.demo.imf;

import android.view.View;

public interface ItemClickListener {
	/**
	 * ��Ŀ����¼�
	 * @param view
	 * @param position
	 */
	  void itemOnClick(View view , int position);
	  
	  /**
		 * ��Ŀ������¼�
		 * @param view
		 * @param position
		 */
		  void itemOnLongClick(View view , int position);
	  
}
