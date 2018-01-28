package com.liilab.textimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Mazid on 15-Jan-18.
 */

public class CommonStaticClass {

    public static final String FOLDER_NAME = "TextImage";
    public static final int REQUEST_WES = 3;
    public static final int REQUEST_FB = 4;
    public static final int REQUEST_GPLUS = 5;
    public static final int REQUEST_TWITTER = 6;
    public static final int REQUEST_SHARE = 7;
//    public static final int REQUEST_INSTA = 8;
//    public static final int REQUEST_PIN = 9;
    public static final int REQUEST_SAVE_IMAGE = 10;

    private static CommonStaticClass mCommonStaticClass;

    private Bitmap mBitmap;

    private CommonStaticClass() {

    }

    public static CommonStaticClass getInstance() {
        if(mCommonStaticClass == null) {
            mCommonStaticClass = new CommonStaticClass();
        }
        return mCommonStaticClass;
    }

    public void setmBitmap(Bitmap bitmap) {
        mCommonStaticClass.mBitmap = bitmap;
    }

    public Bitmap getmBitmap() {
        return mCommonStaticClass.mBitmap;
    }

    public void clear() {
        mCommonStaticClass.mBitmap.recycle();
        mCommonStaticClass.mBitmap = null;
    }
}
