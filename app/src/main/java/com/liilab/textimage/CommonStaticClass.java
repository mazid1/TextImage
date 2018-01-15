package com.liilab.textimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Mazid on 15-Jan-18.
 */

public class CommonStaticClass {

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
