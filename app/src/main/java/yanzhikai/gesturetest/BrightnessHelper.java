package yanzhikai.gesturetest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * Author: yanzhikai
 * Description: 用于辅助调节亮度的类
 * Email: yanzhikai_yjk@qq.com
 */

public class BrightnessHelper {
    private ContentResolver resolver;
    private int maxBrightness = 255;

    public BrightnessHelper(Context context){
        resolver = context.getContentResolver();
    }

    /*
     * 调整亮度范围
     */
    private int adjustBrightnessNumber(int brightness){
        if (brightness < 0) {
            brightness = 0;
        } else if (brightness > 255) {
            brightness = 255;
        }
        return brightness;
    }

    /*
     * 关闭自动调节亮度
     */
    public void offAutoBrightness(){
        try {
            if(Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            {
                Settings.System.putInt(resolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取系统亮度
     */
    public int getBrightness(){
        return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 255);
    }

    /*
     * 设置系统亮度，如果有设置了自动调节，请先调用offAutoBrightness()方法关闭自动调节，否则会设置失败
     */
    public void setSystemBrightness(int newBrightness){
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS
                ,adjustBrightnessNumber(newBrightness));
    }

    public int getMaxBrightness() {
        return maxBrightness;
    }

    //设置当前APP的亮度
    public void setAppBrightness(float brightnessPercent, Activity activity){
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightnessPercent;
        window.setAttributes(layoutParams);
    }
}
