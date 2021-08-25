package allmaker_partners.adamstore.co.kr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by POINBEE-Android2 on 2017-02-16.
 */

public class Preferences {
    public static SharedPreferences m_cPref;
    private final static String TAG_PREF = "Pref";
    public final static String _REGID = "regId";
    public final static String _MIDX = "mIdx";
    public final static String _MAIL = "mMail";
    public final static String _PASS = "mPass";
    public final static String _NAME = "mName";
    public final static String _PHONE = "mPhone";
    public final static String _ISPAY = "mIsPay";

    public final static String _DEVICEID = "mDeviceId";

    public final static String _SETTING_ALARM_PUSH = "mPush";
    public final static String _SETTING_AUTO_LOGIN = "mLogin";
    public final static String _SETTING_ALWAYS_ON = "mAlwaysOn";
    public final static String _SETTING_OPEN_POPOUP = "mOpenPopup";

    public final static String _SETTING_ALARM_REPEAT = "mAlarmRepeat";
    public final static String _SETTING_ALARM_HOUR = "mAlarmHour";
    public final static String _SETTING_ALARM_MINUTE = "mAlarmMinute";
    private static boolean isPublicData(String key){
        return key.equals(_REGID)
                || key.equals(_MIDX)
                || key.equals(_MAIL)
                || key.equals(_PASS)
                || key.equals(_SETTING_ALARM_REPEAT)
                || key.equals(_SETTING_ALARM_HOUR)
                || key.equals(_SETTING_ALARM_MINUTE);
    }

    public static String getId(){
        return null;
    }
    public static void setId(){

    }

    public static String getPassword(){
        return null;
    }
    public static void setPassword(){

    }

    public static String getPasswordChk(Context ctx, String key){
        SharedPreferences pref = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        String midx = pref.getString(key, "on");
        return midx;
    }

    public static void setPasswordChk(Context ctx, String key, String value){
        SharedPreferences pref = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(Context ctx, String key){
        return getPref(ctx, key, "");
    }

    public static String getPref(Context ctx, String key, String defString) {
        if(ctx == null){
//    		Log.i("TEST", "getPref context null");
            return "";
        }else{
            String prefName;
            if(isPublicData(key)){
                prefName = TAG_PREF;
            }else{
                prefName = TAG_PREF+"_"+getMIdx(ctx);
            }
            SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            String str = pref.getString(key, defString);
            return str;
        }
    }

    public static String getMIdx(Context ctx) {
        if(ctx == null){
            Log.i("TEST", "getMIdx context null");
            return "";
        }else{
            SharedPreferences pref = ctx.getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
            String str = pref.getString(_MIDX, "");
            return str;
        }
    }

    public static void setPref(Context ctx, String key, String value) {
        if(ctx == null){
//    		Log.i("TEST", "setPref context null");
        }else{
            String prefName;
            if(isPublicData(key)){
                prefName = TAG_PREF;
            }else{
                prefName = TAG_PREF+"_"+getMIdx(ctx);
            }
            SharedPreferences pref = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }
}
