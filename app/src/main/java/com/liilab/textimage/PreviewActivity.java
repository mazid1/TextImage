package com.liilab.textimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.appsngames.textimage.R;

public class PreviewActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imageView = findViewById(R.id.imageView2);
        imageView.setImageBitmap(CommonStaticClass.getInstance().getmBitmap());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonStaticClass.getInstance().clear();
    }
}
