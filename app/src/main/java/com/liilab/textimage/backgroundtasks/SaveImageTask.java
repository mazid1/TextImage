package com.liilab.textimage.backgroundtasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsngames.textimage.R;
import com.liilab.textimage.CommonStaticClass;
import com.liilab.textimage.GlobalApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.SeekableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mazid on 23-Jan-18.
 */

public class SaveImageTask extends AsyncTask<Integer, Integer, Integer> {

    private ProgressBar progressBar;
    private String path;
    private String filePath;
    private File imageFile;
    private Context mContext;

    public SaveImageTask(Context context, ProgressBar pb) {
        super();
        mContext = context;
        progressBar = pb;
    }

    public void setProgressBar(ProgressBar pb) {
        progressBar = pb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        File outputDir = new File(Environment.getExternalStorageDirectory() + File.separator + CommonStaticClass.FOLDER_NAME);

        if(!outputDir.exists()){
            outputDir.mkdirs();
        }

        path = Environment.getExternalStorageDirectory()
                + File.separator
                + CommonStaticClass.FOLDER_NAME
                + File.separator
                + GlobalApplication.getContext().getString(R.string.app_short_name)
                + "_"
                + new SimpleDateFormat(GlobalApplication.getContext().getString(R.string.time_stamp)).format(new Date())
                + ".png";

        imageFile = new File(path);
        filePath = "file://" + imageFile.getPath();
//        ShareScreen.setFilePath(filePath);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(GlobalApplication.getContext(), GlobalApplication.getContext().getString(R.string.wait_message), Toast.LENGTH_LONG).show();
    }

//    private File getOutputFile(){
//        File outputDir = new File(Environment.getExternalStorageDirectory() + File.separator + CommonStaticClass.FOLDER_NAME);
//
//        if(!outputDir.exists()){
//            outputDir.mkdirs();
//        }
//
//        String timeStamp = new SimpleDateFormat(GlobalApplication.getContext().getString(R.string.time_stamp)).format(new Date());
//        String fileName = GlobalApplication.getContext().getString(R.string.app_short_name) + "_"+ timeStamp +".png";
//        File outputFile = new File(outputDir.getPath() + File.separator + fileName);
//        ShareScreen.setFilePath("file://" + outputFile.getPath());
//        //filePath = "file://" + outputFile.getPath();
//        return outputFile;
//    }

    private void storeImage(Bitmap image) {
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            MediaScannerConnection.scanFile(
                    GlobalApplication.getContext(),
                    new String[]{imageFile.getPath()},
                    new String[]{GlobalApplication.getContext().getString(R.string.image_type)},
                    null
            );
        } catch (Exception e) {
            Log.d("SaveImageTask", "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    protected Integer doInBackground(Integer... flags) {
        storeImage(CommonStaticClass.getInstance().getmBitmap());
        return flags[0];
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.INVISIBLE);
        if(result == CommonStaticClass.REQUEST_SAVE_IMAGE) {
            Toast.makeText(GlobalApplication.getContext(), "Image saved into SD card.", Toast.LENGTH_LONG).show();
        }
        else if(result == CommonStaticClass.REQUEST_SHARE) {
            Uri imgUri;
            imgUri = Uri.parse(filePath);

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName());
            mContext.startActivity(Intent.createChooser(shareIntent, GlobalApplication.getContext().getString(R.string.share_via)));
        }
        else if(result == CommonStaticClass.REQUEST_FB) {
            Uri imgUri;
            imgUri = Uri.parse(filePath);

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName());

            if (!filterByPackageName(GlobalApplication.getContext(), shareIntent, GlobalApplication.getContext().getString(R.string.fb_package))) {
                String sharerUrl = GlobalApplication.getContext().getString(R.string.fb_sharer_url) + GlobalApplication.getContext().getPackageName();
                shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            }

            mContext.startActivity(shareIntent);
        }
        else if(result == CommonStaticClass.REQUEST_GPLUS) {
            Uri imgUri;
            imgUri = Uri.parse(filePath);

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName());

            if (filterByPackageName(GlobalApplication.getContext(), shareIntent, GlobalApplication.getContext().getString(R.string.gplus_package))) {
                mContext.startActivity(shareIntent);
            } else {
                Toast.makeText(GlobalApplication.getContext(), GlobalApplication.getContext().getString(R.string.gplus_not_found), Toast.LENGTH_SHORT).show();
            }
        }
        else if(result == CommonStaticClass.REQUEST_TWITTER) {
            Uri imgUri;
            imgUri = Uri.parse(filePath);

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName());

            if (filterByPackageName(GlobalApplication.getContext(), shareIntent, GlobalApplication.getContext().getString(R.string.twt_package))) {
                mContext.startActivity(shareIntent);
            } else {
                Toast.makeText(GlobalApplication.getContext(), GlobalApplication.getContext().getString(R.string.twt_not_found), Toast.LENGTH_SHORT).show();
            }
        }
//        else if(result == CommonStaticClass.REQUEST_INSTA) {
//            Uri imgUri;
//            imgUri = Uri.parse(filePath);
//
//            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName() + "\n#" + GlobalApplication.getContext().getString(R.string.instaHashTag));
//
//            if (filterByPackageName(GlobalApplication.getContext(), shareIntent, GlobalApplication.getContext().getString(R.string.insta_package))) {
//                GlobalApplication.getContext().startActivity(shareIntent);
//            } else {
//                Toast.makeText(GlobalApplication.getContext(), GlobalApplication.getContext().getString(R.string.insta_not_found), Toast.LENGTH_SHORT).show();
//            }
//        }
//        else if(result == CommonStaticClass.REQUEST_PIN) {
//            Intent shareIntent;
//            shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType(GlobalApplication.getContext().getString(R.string.image_type));
//            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
//            shareIntent.putExtra(Intent.EXTRA_TEXT, GlobalApplication.getContext().getString(R.string.app_link) + GlobalApplication.getContext().getPackageName());
//            if(filterByPackageName(GlobalApplication.getContext(), shareIntent, GlobalApplication.getContext().getString(R.string.pinterest_package))) {
//                GlobalApplication.getContext().startActivity(shareIntent);
//            }
//            else {
//                Toast.makeText(GlobalApplication.getContext(), GlobalApplication.getContext().getString(R.string.pinterest_not_found), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private boolean filterByPackageName(Context context, Intent intent, String prefix) {
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)) {
                intent.setPackage(info.activityInfo.packageName);
                return true;
            }
        }
        return false;
    }


}

