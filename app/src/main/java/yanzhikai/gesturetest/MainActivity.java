package yanzhikai.gesturetest;

import android.app.Service;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements VideoGestureRelativeLayout.VideoGestureListener {
    private final String TAG = "gesturetestm";
    private VideoGestureRelativeLayout ly_VG;
    private ShowChangeLayout scl;
    private AudioManager mAudioManager;
    private int maxVolume = 0;
    private int oldVolume = 0;
    private int newProgress = 0, oldProgress = 0;
    private BrightnessHelper mBrightnessHelper;
    private float brightness = 1;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ly_VG = (VideoGestureRelativeLayout) findViewById(R.id.ly_VG);
        ly_VG.setVideoGestureListener(this);

        scl = (ShowChangeLayout) findViewById(R.id.scl);

        //初始化获取音量属性
        mAudioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //初始化亮度调节
        mBrightnessHelper = new BrightnessHelper(this);

        //下面这是设置当前APP亮度的方法配置
        mWindow = getWindow();
        mLayoutParams = mWindow.getAttributes();
        brightness = mLayoutParams.screenBrightness;
    }



    @Override
    public void onDown(MotionEvent e) {
        //每次按下的时候更新当前亮度和音量，还有进度
        oldProgress = newProgress;
        oldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        brightness = mLayoutParams.screenBrightness;
        if (brightness == -1){
            //一开始是默认亮度的时候，获取系统亮度，计算比例值
            brightness = mBrightnessHelper.getBrightness() / 255f;
        }
    }

    @Override
    public void onEndFF_REW(MotionEvent e) {
        makeToast("设置进度为" + newProgress);
    }

    @Override
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        Log.d(TAG, "onVolumeGesture: oldVolume " + oldVolume);
        int value = ly_VG.getHeight()/maxVolume ;
        int newVolume = (int) ((e1.getY() - e2.getY())/value + oldVolume);

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,newVolume,AudioManager.FLAG_PLAY_SOUND);


//        int newVolume = oldVolume;

        Log.d(TAG, "onVolumeGesture: value" + value);

        //另外一种调音量的方法，感觉体验不好，就没采用
//        if (distanceY > value){
//            newVolume = 1 + oldVolume;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        }else if (distanceY < -value){
//            newVolume = oldVolume - 1;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
//        }
        Log.d(TAG, "onVolumeGesture: newVolume "+ newVolume);

        //要强行转Float类型才能算出小数点，不然结果一直为0
        int volumeProgress = (int) (newVolume/Float.valueOf(maxVolume) *100);
        if (volumeProgress >= 50){
            scl.setImageResource(R.drawable.volume_higher_w);
        }else if (volumeProgress > 0){
            scl.setImageResource(R.drawable.volume_lower_w);
        }else {
            scl.setImageResource(R.drawable.volume_off_w);
        }
        scl.setProgress(volumeProgress);
        scl.show();
    }

    @Override
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //这是直接设置系统亮度的方法
//        if (Math.abs(distanceY) > ly_VG.getHeight()/255){
//            if (distanceY > 0){
//                setBrightness(4);
//            }else {
//                setBrightness(-4);
//            }
//        }

        //下面这是设置当前APP亮度的方法
        Log.d(TAG, "onBrightnessGesture: old" + brightness);
        float newBrightness = (e1.getY() - e2.getY()) / ly_VG.getHeight() ;
        newBrightness += brightness;

        Log.d(TAG, "onBrightnessGesture: new" + newBrightness);
        if (newBrightness < 0){
            newBrightness = 0;
        }else if (newBrightness > 1){
            newBrightness = 1;
        }
        mLayoutParams.screenBrightness = newBrightness;
        mWindow.setAttributes(mLayoutParams);
        scl.setProgress((int) (newBrightness * 100));
        scl.setImageResource(R.drawable.brightness_w);
        scl.show();
    }

    //这是直接设置系统亮度的方法
    private void setBrightness(int brightness) {
        //要是有自动调节亮度，把它关掉
        mBrightnessHelper.offAutoBrightness();

        int oldBrightness = mBrightnessHelper.getBrightness();
        Log.d(TAG, "onBrightnessGesture: oldBrightness: " + oldBrightness);
        int newBrightness = oldBrightness + brightness;
        Log.d(TAG, "onBrightnessGesture: newBrightness: " + newBrightness);
        //设置亮度
        mBrightnessHelper.setSystemBrightness(newBrightness);
        //设置显示
        scl.setProgress((int) (Float.valueOf(newBrightness)/mBrightnessHelper.getMaxBrightness() * 100));
        scl.setImageResource(R.drawable.brightness_w);
        scl.show();

    }


    @Override
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float offset = e2.getX() - e1.getX();
        Log.d(TAG, "onFF_REWGesture: offset " + offset);
        Log.d(TAG, "onFF_REWGesture: ly_VG.getWidth()" + ly_VG.getWidth());
        //根据移动的正负决定快进还是快退
        if (offset > 0) {
            scl.setImageResource(R.drawable.ff);
            newProgress = (int) (oldProgress + offset/ly_VG.getWidth() * 100);
            if (newProgress > 100){
                newProgress = 100;
            }
        }else {
            scl.setImageResource(R.drawable.fr);
            newProgress = (int) (oldProgress + offset/ly_VG.getWidth() * 100);
            if (newProgress < 0){
                newProgress = 0;
            }
        }

        scl.setProgress(newProgress);
        scl.show();
    }

    @Override
    public void onSingleTapGesture(MotionEvent e) {
        Log.d(TAG, "onSingleTapGesture: ");
        makeToast("SingleTap");
    }

    @Override
    public void onDoubleTapGesture(MotionEvent e) {
        Log.d(TAG, "onDoubleTapGesture: ");
        makeToast("DoubleTap");
    }

    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
