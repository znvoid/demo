package com.znvoid.demo.bookReading;



import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by zn on 2016/12/12.
 */

public interface BookFactory {






    boolean isfirstPage();



    boolean islastPage();


    Bitmap getCurPage();
    Bitmap getPrePage();
    Bitmap getNextPage();

    void setTarge(BookView bookView);

    void setBookManage(BookManger bookManger);
    
    void setBookFactoryListener(BookFactoryListener bookFactoryListener);


}
