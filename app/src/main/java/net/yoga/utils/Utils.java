package net.yoga.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.security.SecureRandom;

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

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String generateReferralCode(){
        char[] corpus = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int generated = 0;
        int desired=6;
        char[] result= new char[desired];

        while(generated<desired){
            byte[] ran = SecureRandom.getSeed(desired);
            for(byte b: ran){
                if(b>=0&&b<corpus.length){
                    result[generated] = corpus[b];
                    generated+=1;
                    if(generated==desired) break;
                }
            }
        }
        return new String(result);
    }

}
