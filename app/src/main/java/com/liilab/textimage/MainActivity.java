package com.liilab.textimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsngames.textimage.R;
import com.liilab.textimage.adapter.StickerAdapter;
import com.liilab.textimage.backgroundtasks.SaveImageTask;
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

    private ConstraintLayout.LayoutParams mParams;
    private  DisplayMetrics dm;

    private ProgressBar mProgressBar;
    private SaveImageTask mSaveImageTask;

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = new DisplayMetrics();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageEditText = findViewById(R.id.mImageEditText);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSaveImageTask = new SaveImageTask(this, mProgressBar);
//        mSaveImageTask.setProgressBar(mProgressBar);

//        mImageEditText.setWidth(dm.widthPixels);
//        mImageEditText.setHeight(getResources().getDrawable(R.drawable.bg0).getIntrinsicHeight() * (dm.widthPixels/getResources().getDrawable(R.drawable.bg0).getIntrinsicWidth()));

        mImageEditText.setBackground(getResources().getDrawable(R.drawable.bg0));
//        mImageEditText.addImg(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.st0));

        mImageEditText.setDrawingCacheEnabled(true);
//        imageView = findViewById(R.id.imageView);

        previewIntent = new Intent(this, PreviewActivity.class);
        loadStickerIds();
        mStickerAdapter = new StickerAdapter(getApplicationContext(), ids);

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mStickerAdapter.setHeight((int)Math.floor((184/3)*dm.density), (int)Math.floor(8 * dm.density));
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
        ((GridView)(myPopupView.findViewById(R.id.stickerGrids))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mImageEditText.addImg(getResources(), BitmapFactory.decodeResource(getResources(), ids.get(position)));
                mImageEditText.loadImages(getApplicationContext());
                mImageEditText.invalidate();
            }
        });
        pw.setContentView(myPopupView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CommonStaticClass.REQUEST_FB: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    if(filePath == null) {
//                        //storeImage(((PixelGridView)GamePlayScreen.getGestureView().getChildAt(0)).getmBitmap());
//                        makeVideo(CommonStaticClass.REQUEST_FB);
//                    }
//                    else {
//                        Uri imgUri;
//                        imgUri = Uri.parse(filePath);
//
//                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        shareIntent.setType(getString(R.string.video_type));
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_link) + getPackageName());
//
//                        if (!filterByPackageName(this, shareIntent, getString(R.string.fb_package))) {
//                            String sharerUrl = getString(R.string.fb_sharer_url) + getPackageName();
//                            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
//                        }
//
//                        startActivity(shareIntent);
//                    }
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_FB);

                }
                else {
                    Toast.makeText(this, getString(R.string.file_perm_txt),Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            case CommonStaticClass.REQUEST_GPLUS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    if(filePath == null) {
//                        //storeImage(((PixelGridView)GamePlayScreen.getGestureView().getChildAt(0)).getmBitmap());
//                        makeVideo(CommonStaticClass.REQUEST_GPLUS);
//                    }
//                    else {
//                        Uri imgUri;
//                        imgUri = Uri.parse(filePath);
//
//                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        shareIntent.setType(getString(R.string.video_type));
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_link) + getPackageName());
//
//                        if (filterByPackageName(this, shareIntent, getString(R.string.gplus_package))) {
//                            startActivity(shareIntent);
//                        } else {
//                            Toast.makeText(this, getString(R.string.gplus_not_found), Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_GPLUS);
                }
                else {
                    Toast.makeText(this, getString(R.string.file_perm_txt),Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            case CommonStaticClass.REQUEST_SHARE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    if(filePath == null) {
//                        //storeImage(((PixelGridView)GamePlayScreen.getGestureView().getChildAt(0)).getmBitmap());
//                        makeVideo(CommonStaticClass.REQUEST_SHARE);
//                    } else {
//                        Uri imgUri;
//                        imgUri = Uri.parse(filePath);
//
//                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        shareIntent.setType(getString(R.string.video_type));
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_link) + getPackageName());
//                        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
//                    }
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_SHARE);
                } else {
                    Toast.makeText(this, getString(R.string.file_perm_txt),Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            case CommonStaticClass.REQUEST_SAVE_IMAGE: {
                //makeVideo(CommonStaticClass.REQUEST_SAVE_IMAGE);
                mSaveImageTask.execute(CommonStaticClass.REQUEST_SAVE_IMAGE);
                break;
            }
        }
    }

    public void onClickShare(View view) {
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

        /* Destroy current Drawing Cache */
        mImageEditText.destroyDrawingCache();

        /* Bring back Underline of incorrect words */
        mImageEditText.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        );
        /* Bring back Cursor */
        mImageEditText.setCursorVisible(true);

        mSaveImageTask = new SaveImageTask(this, mProgressBar);

        switch (view.getId()) {
            case R.id.gplusButton:
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "WES shouldShowRequest");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_GPLUS);
                    } else {
                        // No explanation needed, we can request the permission.
                        Log.d(TAG, "WES no explanation need");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_GPLUS);
                    }
                } else {
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_GPLUS);
                }
                break;

            case R.id.twitterButton:
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "WES shouldShowRequest");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_TWITTER);
                    } else {
                        // No explanation needed, we can request the permission.
                        Log.d(TAG, "WES no explanation need");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_TWITTER);
                    }
                } else {
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_TWITTER);
                }
                break;

            case R.id.facebookButton:
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "WES shouldShowRequest");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_FB);
                    } else {
                        // No explanation needed, we can request the permission.
                        Log.d(TAG, "WES no explanation need");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_FB);
                    }
                } else {
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_FB);
                }
                break;

            case R.id.shareButton:
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "WES shouldShowRequest");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_SHARE);
                    } else {
                        // No explanation needed, we can request the permission.
                        Log.d(TAG, "WES no explanation need");
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CommonStaticClass.REQUEST_SHARE);
                    }
                } else {
                    mSaveImageTask.execute(CommonStaticClass.REQUEST_SHARE);
                }
                break;

            default:
                break;
        }
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
        mParams = (ConstraintLayout.LayoutParams) mImageEditText.getLayoutParams();
        mParams.width = dm.widthPixels;
        mParams.height = (int) ((float)getResources().getDrawable(R.drawable.bg0).getIntrinsicHeight()
                * ((float)dm.widthPixels / (float)getResources().getDrawable(R.drawable.bg0).getIntrinsicWidth()));
        mImageEditText.setLayoutParams(mParams);
        mImageEditText.postInvalidate();
        mImageEditText.loadImages(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageEditText.unloadImages();
    }
}
