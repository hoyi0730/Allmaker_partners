package allmaker_partners.adamstore.co.kr.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;


public class HoUtils {
    public static final boolean DEBUG = true;
    public static final String TAG = "LOG====>";
    static CustomDialog progressBar;
//    public static final String VERSION = "Google";
    public static final String VERSION = "Google";
    public static final String ONSTOREKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDtSy+9E/OlQRkGAurXMB6V1CVD6PKQ9v8VGwK+SQIz+P+Xc1a8+p9Tw3kP72VHzVVUzk7HsuKZZLLyFvNpLFD+fsDRD3IBHaQoIHMPZ05t4muwPoKzmVMlFddepiPvmIFi1EG35FL8kPYRv4i1SSxTDnUnCKI4aZzJrbTsCZC4rQIDAQAB";
    public static String getDeviceName() {
        String strDeviceName = Build.MANUFACTURER + " " + Build.PRODUCT;

        return strDeviceName;
    }

    public static void scrollToView(View view, final NestedScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 200);
        }
    }

    public static String getDeviceId(Context ctx) {
//	   Log.i("TEST", "random > " + UUID.randomUUID().toString()); //ba123804-4454-48ea-b076-526fef515df1
        String id = Preferences.getPref(ctx, Preferences._DEVICEID);
        if (isNull(id)) {
            @SuppressLint("MissingPermission")
            String newId = ((TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            if (isNull(newId)) {
                newId = "35" +
                        Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                        Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                        Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                        Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                        Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                        Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                        Build.USER.length() % 10;
                if (isNull(newId)) {
                    newId = UUID.randomUUID().toString();
                }
            }
            Preferences.setPref(ctx, Preferences._DEVICEID, newId);
            id = newId;
        }
        return id;
    }

    public static String getDeivceIMEI(Context context) {
        //TelephonyManager 초기화
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();
    }

    public static String getDevicesUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public static int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now));

        return id;
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(colorId);
        }
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String PhoneNum = telManager.getLine1Number();
        if (HoUtils.isNull(PhoneNum)) {
            PhoneNum = "00000000000";
        }
        if (PhoneNum.startsWith("+82")) {
            PhoneNum = PhoneNum.replace("+82", "0");
        }

        return PhoneNum;

    }

    //시간변환하기
    public static String converLimitTime2(String original, String type) {
        String time1 = original;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = dateFormat1.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat(type, Locale.getDefault());
        String time2 = dateFormat2.format(date1);
        return time2;
    }

    public static String converTime(String original, String type) {
        String time1 = original;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = dateFormat1.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat(type, Locale.getDefault());
        String time2 = dateFormat2.format(date1);
        return time2;
    }

    public static long DateToMill(String date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date trans_date = null;
        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return trans_date.getTime();
    }


    public static String getPlusMoney(String a) {
        String res = "";

        int r = Integer.parseInt(a.replaceAll("원", "").replaceAll(" ", "").replaceAll(",", ""));
        r = r + r / 10;

        res = Integer.toString(r);

        return HoUtils.currentpoint(res);

    }

    public static String getTong(Context ctx){
        TelephonyManager tm = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String tong = "";
        String so = tm.getSimOperator();
        if(so!=null && !so.trim().equals("")){
            switch (Integer.parseInt(so)) {
                case 45005:
                case 45011:
                    tong = "SK";   // SKT
                    break;
                case 45002:
                case 45004:
                case 45008:
                    tong = "KT";   // KTF
                    break;
                case 45003:
                case 45006:
                    tong = "LG";   // LG U+
                    break;
            }
        }
        return tong;
    }


    public static void showProgressDialog(Activity act) {
        if (act.isFinishing()) {
            return;
        }
        progressBar = new CustomDialog(act);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.show();
    }

    public static void hideProgressDialog() {
        //progressBar = new DlgProgressBar(context);
        if(progressBar!=null){
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }

    public static boolean isNull(String s) {
        if (s == null || s.equals("") || s.equalsIgnoreCase("null"))
            return true;

        return false;
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //값이 이상할 경우 다른 값으로 대체 하는 함수
    public static String fixNull(String s1, String s2) {
        if (isNull(s1))
            return s2;

        return s1.trim();
    }

    public static String getStr(JSONObject jo, String key) {
        String s = null;
        try {
            if (jo.has(key)) {
                s = jo.getString(key);
            } else {
                s = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }


    public static String currentpoint(String result) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        DecimalFormat df = new DecimalFormat("###,###,###,###");
        df.setDecimalFormatSymbols(dfs);

        try {
            if (!HoUtils.isNull(result)) {
                result = result.replaceAll(",", "");
                double inputNum = Double.parseDouble(result);
                result = df.format(inputNum).toString();
            } else {
                result = "";
            }
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }

        return result;
    }

    //이메일 정규식
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // 이메일 검사
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //비밀번호 정규식
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[a-zA-Z0-9]{6,12}$");

    // 6자리 ~ 12자리까지 가능 숫자문자 혼합
    // 비밀번호 검사
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        //Log.d(HoUtils.TAG,"결과1 : "+matcher.matches());
        return matcher.matches();
    }

    //숫자만 입력했는지
    public static final Pattern VALID_ONLY_NUM = Pattern.compile("^[0-9]*$");

    public static boolean validateNumber(String pwStr) {
        Matcher matcher = VALID_ONLY_NUM.matcher(pwStr);
        return matcher.matches();
    }

    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        String imei = tm.getDeviceId();

        return HoUtils.fixNull(imei, "0");
    }


    /*public static void setGlidePhone(Activity context, String url, ImageView view){
        if (context.isFinishing()) {
            return;
        }
        Glide.with(context)
                .load(JsonUrl.DEFAULT_HTTP_ADDRESS+url)
                .error(R.drawable.img_noprofile).bitmapTransform(new CropCircleTransformation(context))
                .into(view);
    }*/

 /*   public static void setGlideBrand(Activity context, String url, ImageView view){
        if (context.isFinishing()) {
            return;
        }
        Glide.with(context)
                .load(JsonUrl.DEFAULT_HTTP_ADDRESS+url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .error(
                        Glide.with(context).load(R.drawable.img_noimg)
                )
                .into(view);
    }*/

}
