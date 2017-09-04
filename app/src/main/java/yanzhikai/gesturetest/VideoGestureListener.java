package yanzhikai.gesturetest;

import android.view.MotionEvent;

/**
 * Created by yany on 2017/7/4.
 */

public interface VideoGestureListener {
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    public void onSingleTapGesture(MotionEvent e);
    public void onDoubleTapGesture(MotionEvent e);
    public void onDown(MotionEvent e);
}
