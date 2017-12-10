package net.yoga.lib;

import android.content.res.Resources;

/**
 * Created by bruce on 14-11-6.
 */
public final class Utils {

    public static final String TAG = "ARM";

    private Utils() {
    }
    
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static String convertToMMSSTimeString(int progress) {
        int min = progress / 60;
        int sec = progress % 60;
        return min + ":" + (sec<=9 ? "0": "") + sec;
    }


}
