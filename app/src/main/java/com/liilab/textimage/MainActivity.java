package com.liilab.textimage;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.appsngames.textimage.R;
import com.liilab.textimage.views.ImageEditText;

public class MainActivity extends AppCompatActivity {

    private ImageEditText mImageEditText;
    private ImageView imageView;
    private Bitmap mBitmap;

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
        /* Remove Underline from incorrect words */
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        /* Hide Cursor */
        mImageEditText.setCursorVisible(false);

        /* Get bitmap from EditText */
        mBitmap = Bitmap.createBitmap(mImageEditText.getDrawingCache());
        imageView.setImageBitmap(mBitmap);

        /* Destroy current Drawing Cache */
        mImageEditText.destroyDrawingCache();
        /* Bring back Underline of incorrect words */
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        /* Bring back Cursor */
        mImageEditText.setCursorVisible(true);
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
