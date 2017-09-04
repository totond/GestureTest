package yanzhikai.gesturetest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by yany on 2017/7/4.
 */

public class ShowChangeLayout extends RelativeLayout {
    private static final String TAG = "gesturetest";
    private ImageView iv_center;
    private ProgressBar pb;
    private HideRunnable mHideRunnable;
    private int duration = 1000;

    public ShowChangeLayout(Context context) {
        super(context);
        init(context);
    }

    public ShowChangeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.show_change_layout,this);
        iv_center = (ImageView) findViewById(R.id.iv_center);
        pb = (ProgressBar) findViewById(R.id.pb);

        mHideRunnable = new HideRunnable();
        ShowChangeLayout.this.setVisibility(GONE);
    }

    public void show(){
        setVisibility(VISIBLE);
        removeCallbacks(mHideRunnable);
        postDelayed(mHideRunnable,duration);
    }

    //设置进度
    public void setProgress(int progress){
        pb.setProgress(progress);
        Log.d(TAG, "setProgress: " +progress);
    }

    //设置持续时间
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setImageResource(int resource){
        iv_center.setImageResource(resource);
    }

    private class HideRunnable implements Runnable{
        @Override
        public void run() {
            ShowChangeLayout.this.setVisibility(GONE);
        }
    }
}
