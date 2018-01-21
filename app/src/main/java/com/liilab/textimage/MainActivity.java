package com.liilab.textimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.appsngames.textimage.R;
import com.liilab.textimage.adapter.StickerAdapter;
import com.liilab.textimage.views.ImageEditText;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private ImageEditText mImageEditText;
//    private ImageView imageView;
//    private Bitmap mBitmap;
    private Intent previewIntent;
    private View stickerPickView = null;
    private View myPopupView;
    private PopupWindow pw;
    private LayoutInflater inflater;
    private StickerAdapter mStickerAdapter;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageEditText = findViewById(R.id.mImageEditText);
        mImageEditText.setBackground(getResources().getDrawable(R.drawable.bg0));
        mImageEditText.addImg(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.st0));

        mImageEditText.setDrawingCacheEnabled(true);
//        imageView = findViewById(R.id.imageView);

        previewIntent = new Intent(this, PreviewActivity.class);
        loadStickerIds();
        mStickerAdapter = new StickerAdapter(getApplicationContext(), ids);
        mStickerAdapter.setHeight(66, 8);
        loadAllQuickAction();
    }

    private void loadStickerIds() {
        if(ids == null) {
            ids = new ArrayList<Integer>();
        }
        ids.clear();

        ids.add(R.drawable.sticker_1);
        ids.add(R.drawable.sticker_2);
        ids.add(R.drawable.sticker_3);
        ids.add(R.drawable.sticker_4);
        ids.add(R.drawable.sticker_5);
        ids.add(R.drawable.sticker_6);
        ids.add(R.drawable.sticker_7);
        ids.add(R.drawable.sticker_8);
        ids.add(R.drawable.sticker_9);
        ids.add(R.drawable.sticker_11);
        ids.add(R.drawable.sticker_12);
        ids.add(R.drawable.sticker_13);
        ids.add(R.drawable.sticker_14);
        ids.add(R.drawable.sticker_15);
        ids.add(R.drawable.sticker_16);
        ids.add(R.drawable.sticker_17);
        ids.add(R.drawable.sticker_18);
        ids.add(R.drawable.sticker_19);
        ids.add(R.drawable.sticker_20);
    }

    private void loadAllQuickAction() {
        pw = new PopupWindow(getApplicationContext());
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    System.gc();
                    return true;
                }

                return false;
            }
        });

        pw.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        pw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        pw.setOutsideTouchable(false);
        pw.setAnimationStyle(android.R.style.Animation_Dialog);
    }

    public void showPopup(View view) {

        switch (view.getId()) {
            case R.id.buttonStiker:
                if(stickerPickView==null){
                    stickerPickView = inflater.inflate(R.layout.sticker_pick_menu, null, false);
                }
                loadAction(stickerPickView);
                pw.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }

    private void loadAction(View v) {
        myPopupView = v;
        ((GridView)(myPopupView.findViewById(R.id.stickerGrids))).setAdapter(mStickerAdapter);
        pw.setContentView(myPopupView);
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
        CommonStaticClass.getInstance().setmBitmap(Bitmap.createBitmap(mImageEditText.getDrawingCache()));
//        mBitmap = Bitmap.createBitmap(mImageEditText.getDrawingCache());
//        imageView.setImageBitmap(mBitmap);

        /* Destroy current Drawing Cache */
        mImageEditText.destroyDrawingCache();
        /* Bring back Underline of incorrect words */
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        /* Bring back Cursor */
        mImageEditText.setCursorVisible(true);

        startActivity(previewIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageEditText.loadImages(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageEditText.unloadImages();
    }
}
