package com.example.txtledbluetooth.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.txtledbluetooth.R;
import com.nostra13.universalimageloader.utils.L;

/**
 * Created by KomoriWu
 * on 2017-04-26.
 */


public class ColorPicker extends ImageView implements View.OnTouchListener {

    private Bitmap mBitmap;//色轮图片
    private int mWidth, mHeight;//色轮图片的尺寸
    private float x, y, radio;

    private OnColorSelectListener mOnColorSelectListener;
    private Paint mPaint;

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.circle);
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();
        setImageBitmap(mBitmap);
        setOnTouchListener(this);

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        setClickable(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float xx = event.getX();
        float yy = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!inCircle(xx, yy)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!inCircle(xx, yy)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!inCircle(xx, yy)) {
                    return false;
                }
                break;
        }
        x = xx;
        y = yy;
        invalidate();
        if (mOnColorSelectListener != null) {
            mOnColorSelectListener.onColorSelect(getColor(xx, yy),x,y);
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        x = getWidth() / 2;
        y = getHeight() / 2;
        radio = x > y ? y : x;

        invalidate();
    }

    public void setPaintPixel(float x, float y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, 20, mPaint);
    }


    private int getColor(float x, float y) {
        this.x = x;
        this.y = y;

        int  w = getWidth();
        int  h = getHeight();

        int dx = (int) ((x / w) * mWidth);
        int dy = (int) ((y / h) * mHeight);
        int color = Color.BLACK;
        try {
            color = mBitmap.getPixel(dx, dy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return color;
    }

    private boolean inCircle(float x, float y) {
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float d = (float) Math.abs(Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy)));
        return d <= radio;
    }

    public interface OnColorSelectListener {
        void onColorSelect(int color, float x, float y);
    }

    public OnColorSelectListener getOnColorSelectListener() {
        return mOnColorSelectListener;
    }

    public void setOnColorSelectListener(OnColorSelectListener mOnColorSelectListener) {
        this.mOnColorSelectListener = mOnColorSelectListener;
    }

    /**
     * 回收Bitmap内存
     */
    public void recycle() {
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            mBitmap = null;
        }
    }

    public boolean isRecycled() {
        return mBitmap == null || mBitmap.isRecycled();
    }

}
