package com.liilab.textimage.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.appsngames.textimage.R;
import com.liilab.textimage.multitouch.MultiTouchController;

import java.util.ArrayList;

/**
 * Created by Mazid on 08-Jan-18.
 */

public class ImageEditText extends android.support.v7.widget.AppCompatEditText
        implements MultiTouchController.MultiTouchObjectCanvas<ImageEditText.Img> {


    private static final String TAG = "JOIN_IMAGE_VIEW";

    private ArrayList<Img> mImages = new ArrayList<Img>();

    // -------------------------------------------------------------------------------------------------

    private MultiTouchController<Img> multiTouchController = new MultiTouchController<Img>(this);

    // -------------------------------------------------------------------------------------------------

    private MultiTouchController.PointInfo currTouchPoint = new MultiTouchController.PointInfo();
    private boolean mShowDebugInfo = false;
    private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;
    private int mUIMode = UI_MODE_ROTATE;

    // --

    private Paint mLinePaintTouchPointCircle = new Paint();

    // ---------------------------------------------------------------------------------------------------

    private Canvas comboCanvas;
    private Bitmap comboBitmap;
    private Context context;

//    private Bitmap sealBitmap;

    // ---------------------------------------------------------------------------------------------------

    private int screenWidth;
    private int screenHeight;
    private int[] location = new int[2];
    private Rect frameToDraw;
    private RectF whereToDraw;

    private float wRatio;
    private float hRatio;
    private float ratioMultiplier;
    private DisplayMetrics displayMetrics;

    // ---------------------------------------------------------------------------------------------------

    public ImageEditText(Context context) {
        //this(context, null);
        super(context);
        init(context);
    }

    public ImageEditText(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        super(context, attrs);
        init(context);
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void addImg(Resources res, Bitmap bitmap) {
        Img img = new Img(res, bitmap);
        mImages.add(img);
    }

    private void init(Context context) {
        this.context = context;
        getLocationOnScreen(location);
        mLinePaintTouchPointCircle.setColor(Color.YELLOW);
        mLinePaintTouchPointCircle.setStrokeWidth(5);
        mLinePaintTouchPointCircle.setStyle(Paint.Style.STROKE);
        mLinePaintTouchPointCircle.setAntiAlias(true);
        setBackgroundColor(Color.TRANSPARENT);

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        if(comboBitmap != null) comboBitmap.recycle();
        comboBitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
        comboCanvas = new Canvas(comboBitmap);
//        if(sealBitmap != null) sealBitmap.recycle();
//        sealBitmap = BitmapFactory.decodeResource(((Activity) getContext()).getResources(), R.drawable.seal);
    }

    // -------------------------------------------------------------------------------------------------------


    /** Called by activity's onResume() method to load the images */
    public void loadImages(Context context) {
        getLocationOnScreen(location);
        Resources res = context.getResources();
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).load(res);
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unloadImages() {
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).unload();
    }

    // ---------------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        screenWidth = canvas.getWidth();
//        screenHeight = canvas.getHeight();
//        width = 100;//CommonStaticClass.getInstance().getmSuit().getWidth();
//        height = 100;//CommonStaticClass.getInstance().getmSuit().getHeight();
//
//        wRatio = (float)screenWidth / (float)width;
//        hRatio = (float)screenHeight / (float)height;

//        ratioMultiplier = wRatio;

//        if (hRatio < wRatio) {
//            ratioMultiplier = hRatio;
//        }

        int n = mImages.size();
        for (int i = 0; i < n; i++) {
            mImages.get(i).draw(canvas);
        }

//        frameToDraw = new Rect(0, 0, width, height);
//        whereToDraw = new RectF(0, 0, width*ratioMultiplier, height*ratioMultiplier);
//        //canvas.drawBitmap(CommonStaticClass.getInstance().getmSuit(),frameToDraw,whereToDraw,null);
//        canvas.drawBitmap(sealBitmap,canvas.getWidth()-sealBitmap.getWidth(),canvas.getHeight()-sealBitmap.getHeight(),null);

        if (mShowDebugInfo)
            drawMultitouchDebugMarks(canvas);
    }

    // ---------------------------------------------------------------------------------------------------

    public void trackballClicked() {
        mUIMode = (mUIMode + 1) % 3;
        invalidate();
    }

    private void drawMultitouchDebugMarks(Canvas canvas) {
        if (currTouchPoint.isDown()) {
            float[] xs = currTouchPoint.getXs();
            float[] ys = currTouchPoint.getYs();
            float[] pressures = currTouchPoint.getPressures();
            int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
            for (int i = 0; i < numPoints; i++)
                canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80, mLinePaintTouchPointCircle);
            if (numPoints == 2)
                canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /** Pass touch events to the MT controller */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int n = mImages.size();
        for (int i = n - 1; i >= 0; i--) {
            Img im = mImages.get(i);
            if (im.containsPoint(x, y))
                return multiTouchController.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        screenWidth = xNew;
        screenHeight = yNew;
        getLocationOnScreen(location);
//        Log.d("XYZ", "location = "+location[0]+", "+location[1]);
    }


    /** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
    public Img getDraggableObjectAtPoint(MultiTouchController.PointInfo pt) {
        float x = pt.getX(), y = pt.getY();
        int n = mImages.size();
        for (int i = n - 1; i >= 0; i--) {
            Img im = mImages.get(i);
            if (im.containsPoint(x, y))
                return im;
        }
        return null;
    }

    /**
     * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
     * and a drag operation is starting. Called with null when drag op ends.
     */
    public void selectObject(Img img, MultiTouchController.PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        if (img != null) {
            // Move image to the top of the stack when selected
            mImages.remove(img);
            mImages.add(img);
        } else {
            // Called with img == null when drag stops.
        }
        invalidate();
    }

    /** Get the current position and scale of the selected image. Called whenever a drag starts or is reset. */
    public void getPositionAndScale(Img img, MultiTouchController.PositionAndScale objPosAndScaleOut) {
        // FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
        objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
                (img.getScaleX() + img.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(), img.getScaleY(),
                (mUIMode & UI_MODE_ROTATE) != 0, img.getAngle());
    }

    /** Set the position and scale of the dragged/stretched image. */
    public boolean setPositionAndScale(Img img, MultiTouchController.PositionAndScale newImgPosAndScale, MultiTouchController.PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        boolean ok = img.setPos(newImgPosAndScale);
        if (ok)
            invalidate();
        return ok;
    }

    // ----------------------------------------------------------------------------------------------


    public class Img {

        public Bitmap bitmap;
        public Drawable drawable;

        private boolean firstLoad;
        private int width, height, displayWidth, displayHeight;
        private float centerX, centerY, scaleX, scaleY, angle;
        private float minX, maxX, minY, maxY;

        private static final float SCREEN_MARGIN = 5;
        private boolean resize = false;

        public Img(Resources res, Bitmap bitmap, int type, String test) {
            this.bitmap = bitmap;
            this.firstLoad = true;
            getMetrics(res);
        }
        public Img(Resources res,Bitmap bitmap) {
            this.bitmap = bitmap;
            this.firstLoad = true;
            getMetrics(res);
        }
        public Img(Resources res,Bitmap bitmap, boolean resize) {
            this.bitmap = bitmap;
            this.firstLoad = true;
            this.resize = resize;
            getMetrics(res);
        }
        public Img(Bitmap bitmap, Resources res, int type, String test,
                   float cx, float cy, float sx, float sy, float ang, String text) {
            this.bitmap = bitmap;
            this.firstLoad = true;
            this.centerX = cx;
            this.centerY = cy;
            this.scaleX = sx;
            this.scaleY = sy;
            this.angle = ang;
            getMetrics(res);
        }

        private void getMetrics(Resources res) {
            DisplayMetrics metrics = res.getDisplayMetrics();
            // The DisplayMetrics don't seem to always be updated on screen rotate, so we hard code a portrait
            // screen orientation for the non-rotated screen here...
            // this.displayWidth = metrics.widthPixels;
            // this.displayHeight = metrics.heightPixels;
            this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                    metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
            this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                    metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
        }

        /** Called by activity's onResume() method to load the images */
        public void load(Resources res) {
            getMetrics(res);
            this.drawable = new BitmapDrawable(res, bitmap);
            this.width = drawable.getIntrinsicWidth();
            this.height = drawable.getIntrinsicHeight();

//            float wRatio = (float)screenWidth / (float)width;
//            float hRatio = (float)screenHeight / (float)height;
//
//            float ratioMultiplier = wRatio;
//
//            if (hRatio < wRatio) {
//                ratioMultiplier = hRatio;
//            }

//            Log.d("XYZ", "screenwidth="+screenWidth+" screenheight="+screenHeight);
//            Log.d("XYZ", "centerX="+(location[0]+(width))+" centerY="+(location[1]+(height)));
            if(firstLoad) {
                firstLoad = false;
                centerX = width;
                centerY = height;
                scaleX = 2;
                scaleY = 2;
            }
            setPos(centerX, centerY, scaleX, scaleY, angle);
        }

        /** Called by activity's onPause() method to free memory used for loading the images */
        public void unload() {
            this.drawable = null;
        }

        /** Set the position and scale of an image in screen coordinates */
        public boolean setPos(MultiTouchController.PositionAndScale newImgPosAndScale) {
            return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
                    .getScaleX() : newImgPosAndScale.getScale(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
                    : newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
            // FIXME: anisotropic scaling jumps when axis-snapping
            // FIXME: affine-ize
            // return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), newImgPosAndScale.getScaleAnisotropicX(),
            // newImgPosAndScale.getScaleAnisotropicY(), 0.0f);
        }

        /** Set the position and scale of an image in screen coordinates */
        private boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
            float ws = (width / 2) * scaleX, hs = (height / 2) * scaleY;
            float newMinX = centerX - ws, newMinY = centerY - hs, newMaxX = centerX + ws, newMaxY = centerY + hs;
//            if (newMinX > screenWidth - SCREEN_MARGIN || newMaxX < SCREEN_MARGIN || newMinY > screenHeight - SCREEN_MARGIN
//                    || newMaxY < SCREEN_MARGIN) {
//                Log.d("XYZ", "return false");
//                return false;
//            }
            this.centerX = centerX;
            this.centerY = centerY;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.angle = angle;
            this.minX = newMinX;
            this.minY = newMinY;
            this.maxX = newMaxX;
            this.maxY = newMaxY;
//            Log.d("XYZ", "return true");
//            Log.d("XYZ", "centerX="+centerX+" centerY="+centerY+" scaleX="+scaleX+" scaleY="+scaleY);
//            Log.d("XYZ", "minx="+minX+" miny="+minY+" maxx="+maxX+" maxy="+maxY);
            return true;
        }

        /** Return whether or not the given screen coords are inside this image */
        public boolean containsPoint(float scrnX, float scrnY) {
            // FIXME: need to correctly account for image rotation
//            Log.d("XYZ", "scrX="+scrnX+" scrY="+scrnY+" minX="+minX+" maxX="+maxX+" minY="+minY+" maxY="+maxY);
            return (scrnX >= minX && scrnX <= maxX && scrnY >= minY && scrnY <= maxY);
//            return true;
        }

        public void draw(Canvas canvas) {
            try {
                canvas.save();

                float dx = (maxX + minX) / 2;
                float dy = (maxY + minY) / 2;
                drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
                canvas.translate(dx, dy);
                canvas.rotate(angle * 180.0f / (float) Math.PI);
                canvas.translate(-dx, -dy);
                drawable.draw(canvas);
                canvas.restore();
            }catch (Exception e){}

        }

        public Drawable getDrawable() {
            return drawable;
        }

        public Bitmap getBitmap() {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public float getCenterX() {
            return centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public float getScaleX() {
            return scaleX;
        }

        public float getScaleY() {
            return scaleY;
        }

        public float getAngle() {
            return angle;
        }

        // FIXME: these need to be updated for rotation
        public float getMinX() {
            return minX;
        }

        public float getMaxX() {
            return maxX;
        }

        public float getMinY() {
            return minY;
        }

        public float getMaxY() {
            return maxY;
        }
    }
}
