package yanzhikai.gesturetest;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yany on 2017/7/4.
 */

public class VideoGestureRelativeLayout extends RelativeLayout {
    private static final String TAG = "gesturetest";
    private static final int NONE = 0, VOLUME = 1, BRIGHTNESS = 2, FF_REW = 3;
    private @ScrollMode int mScrollMode = NONE;

    @IntDef({NONE,VOLUME,BRIGHTNESS,FF_REW})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface ScrollMode {}

    private MyOnGestureListener mOnGestureListener;
    private GestureDetector mGestureDetector;
    private VideoGestureListener mVideoGestureListener;
    //横向偏移检测，让快进快退不那么敏感
    private int offsetX = 1;

    public VideoGestureRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public VideoGestureRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


    }

    private void init(Context context){
        mOnGestureListener = new MyOnGestureListener();
        mGestureDetector = new GestureDetector(context,mOnGestureListener);
//        mGestureDetector.setIsLongpressEnabled(false);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "onTouch: event:" + event.getAction());
                boolean handled = false;
                handled |= mGestureDetector.onTouchEvent(event);
                handled |= mGestureDetector.onGenericMotionEvent(event);
                return handled;

                //监听触摸事件
//                return mGestureDetector.onTouchEvent(event);
                //监听鼠标点击事件
//                return mGestureDetector.onGenericMotionEvent(event);
            }
        });
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll: e1:" + e1.getX() + "," + e1.getY());
            Log.d(TAG, "onScroll: e2:" + e2.getX() + "," + e2.getY());
            Log.d(TAG, "onScroll: X:" + distanceX + "  Y:" + distanceY);
            switch (mScrollMode){
                case NONE:
                    Log.d(TAG, "NONE: ");
                    if (Math.abs(distanceX) - Math.abs(distanceY) > offsetX){
                        mScrollMode = FF_REW;
                    }else {
                        if (e1.getX() < getWidth()/2){
                            mScrollMode = BRIGHTNESS;
                        }else {
                            mScrollMode = VOLUME;
                        }
                    }
                    break;
                case VOLUME:
                    if (mVideoGestureListener != null){
                        mVideoGestureListener.onVolumeGesture(e1,e2,distanceX,distanceY);
                    }
                    Log.d(TAG, "VOLUME: ");
                    break;
                case BRIGHTNESS:
                    if (mVideoGestureListener != null){
                        mVideoGestureListener.onBrightnessGesture(e1,e2,distanceX,distanceY);
                    }
                    Log.d(TAG, "BRIGHTNESS: ");
                    break;
                case FF_REW:
                    if (mVideoGestureListener != null){
                        mVideoGestureListener.onFF_REWGesture(e1,e2,distanceX,distanceY);
                    }
                    Log.d(TAG, "FF_REW: ");
                    break;
            }
            return true;
        }


        @Override
        public boolean onContextClick(MotionEvent e) {
            Log.d(TAG, "onContextClick: ");
            return super.onContextClick(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: ");
            if (mVideoGestureListener != null){
                mVideoGestureListener.onDoubleTapGesture(e);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress: ");
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d(TAG, "onDoubleTapEvent: ");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp: ");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling: ");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown: ");
            mScrollMode = NONE;
            if (mVideoGestureListener != null){
                mVideoGestureListener.onDown(e);
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress: ");
            super.onShowPress(e);
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "onSingleTapConfirmed: ");
            if (mVideoGestureListener != null){
                mVideoGestureListener.onSingleTapGesture(e);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public void setVideoGestureListener(VideoGestureListener videoGestureListener){
        mVideoGestureListener = videoGestureListener;
    }
}
