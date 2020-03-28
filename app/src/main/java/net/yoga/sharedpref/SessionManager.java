package net.yoga.sharedpref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import net.yoga.activities.Splash;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN="isLoggedIn";
    private static final String PREF_NAME="Arham_Session";

    public static final String KEY_USERNAME="username";
    public static final String KEY_ARHAM_SESSIONS="noOfSessions";
    public static final String KEY_REFERRAL_CODE="referralcode";
    public static final String KEY_REFERRAL_COUNT="referraljoined";

    public static final String KEY_CAMPAIGN_SESSION_ID = "campaignid";
    public static final String KEY_CAMPAIGN_SESSION_COUNT = "campaignsessioncount";
    public static final String KEY_CAMPAIGN_SESSION_NAME = "campaignname";
    public static final String KEY_CAMPAIGN_ENROLLED_STATUS = "campaignenrolled";

    int PRIVATE_MODE=0;

    public SessionManager(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void createSession(String username,int noOfSessions,String myReferralCode){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_USERNAME,username);
        editor.putInt(KEY_ARHAM_SESSIONS,noOfSessions);
        editor.putString(KEY_REFERRAL_CODE,myReferralCode);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void putNoOfSessions(int count){
        editor.putInt(KEY_ARHAM_SESSIONS,count);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME,"");
    }

    public int getArhamSessions(){
        return  sharedPreferences.getInt(KEY_ARHAM_SESSIONS,0);
    }

    public String getReferralCode() {
        return sharedPreferences.getString(KEY_REFERRAL_CODE,"");
    }

}
