package com.liilab.textimage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Mazid on 18-Jan-18.
 */

public class StickerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> ids;
    private int mItemHeight = 0;
    private int mImgHeight = 0;
    private int mPadding = 0;

    public StickerAdapter(Context c, ArrayList<Integer> ids) {
        mContext = c;
        this.ids = ids;
    }

    public int getCount() {
        return ids.size();
    }

    public Object getItem(int position) {
        return ids.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mItemHeight, mItemHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(mPadding, mPadding, mPadding, mPadding);
        } else {
            imageView = (ImageView) convertView;
            imageView.setLayoutParams(new GridView.LayoutParams(mItemHeight, mItemHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(mPadding, mPadding, mPadding, mPadding);
        }
        imageView.setBackgroundResource(ids.get(position));

        return imageView;
    }

    public void setHeight(int height, int padding) {
        mItemHeight = height;
        mImgHeight = mItemHeight - padding;
        mPadding = padding;
        notifyDataSetChanged();
    }


}
