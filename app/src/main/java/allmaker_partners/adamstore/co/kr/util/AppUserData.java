package allmaker_partners.adamstore.co.kr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by POINBEE-Android2 on 2017-02-27.
 */

public class AppUserData {

    public static SharedPreferences mShared;

    public static void setData(Context context, String dataKey, String data) {
        //SharedPreferences.Editor prefs = context.getSharedPreferences("pref", MODE_PRIVATE).edit();
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefs.putString(dataKey, data);
        prefs.commit();
    }

    // dataKey에 해당되는 데이터를 반환(String)
    public static String getData(Context context, String dataKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(dataKey, "");
    }

    public static void setListData(Context context, String dataKey, ArrayList<String> values) {
        //SharedPreferences.Editor prefs = context.getSharedPreferences("pref", MODE_PRIVATE).edit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(dataKey, a.toString());
        } else {
            editor.putString(dataKey, null);
        }
        editor.apply();

    }

    // dataKey에 해당되는 데이터를 반환(String)
    public static ArrayList<String> getListData(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static ArrayList<String> setDeleteList(Context context, String key, int position) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(urls.size()!=0){
            urls.remove(position);
            setListData(context,key,urls);
        }
        return urls;
    }

}
