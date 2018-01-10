package com.liilab.textimage;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.appsngames.textimage.R;
import com.liilab.textimage.views.ImageEditText;

public class MainActivity extends AppCompatActivity {

    ImageEditText mImageEditText;
    ImageView imageView;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageEditText = findViewById(R.id.mImageEditText);
        mImageEditText.setBackground(getResources().getDrawable(R.drawable.bg0));

        mImageEditText.setDrawingCacheEnabled(true);
        imageView = findViewById(R.id.imageView);
    }

    public void onClickCopy(View view) {
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        mBitmap = Bitmap.createBitmap(mImageEditText.getDrawingCache());
        imageView.setImageBitmap(mBitmap);
        mImageEditText.destroyDrawingCache();
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mImageEditText.loadImages(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mImageEditText.unloadImages();
    }
}
