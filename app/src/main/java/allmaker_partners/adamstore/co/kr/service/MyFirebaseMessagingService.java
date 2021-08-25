package allmaker_partners.adamstore.co.kr.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActConstructInfoDetail;
import allmaker_partners.adamstore.co.kr.activity.ActMain;
import allmaker_partners.adamstore.co.kr.activity.ActNotice;
import allmaker_partners.adamstore.co.kr.activity.ActRequestChkList;
import allmaker_partners.adamstore.co.kr.activity.ActRequestList;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.HonotificationManager;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;


/**
 * Created by Administrator on 2018-01-10.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String NOTIFICATION_CHANNEL_ID = "meshop";
    String type, adImg, yIdx;

    //UserData uData;
    String roomIdx, tName, tAge, tGender, tArea, tAreaDetail, tPhoto, tJob, tKm, notiTitle, notiSub, tIdx;
    String s_distance, s_gender, s_age, s_idx, s_msg, s_name;
    String sIdx, spIdx, sprIdx, smName, sPhoto, sName, spMoney;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(HoUtils.TAG, "새로운 토큰 : " + s);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //{type=GONGSI} 푸쉬 로그
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (!HoUtils.isNull(AppUserData.getData(getApplication(), "idx"))) {
            if(HoUtils.fixNull(AppUserData.getData(getApplicationContext(),"push"),"on").equalsIgnoreCase("on")){
                type = remoteMessage.getData().get("type");
                if(type.equalsIgnoreCase("userqna_partner")){
                    //Log.d(HoUtils.TAG,"type2 : "+type);
                    notiTitle = "문의내역도착";
                    notiSub = "문의내역을 확인하여주세요";
                    sendNotification();
                }else if(type.equalsIgnoreCase("construct_reply")){
                    notiTitle = "댓글확인";
                    notiSub = "게시글에 댓글을 확인해주세요.";
                    roomIdx = remoteMessage.getData().get("idx");
                    sendNotification();
                }else if(type.equalsIgnoreCase("construct_reply_re")){
                    notiTitle = "댓글확인";
                    notiSub = "댓글에 답변을 확인해주세요.";
                    roomIdx = remoteMessage.getData().get("idx");
                    sendNotification();
                }else if(type.equalsIgnoreCase("notice")){
                    notiTitle = "공지사항도착";
                    notiSub = remoteMessage.getData().get("b_title");
                    sendNotification();
                }else if(type.equalsIgnoreCase("reply")){
                    notiTitle = "다짓자";
                    notiSub = "1:1문의 답변이 도착하였습니다.";
                    sendNotification();
                }
            }
        }
    }


    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        if (appProcessInfos == null) {
            return false;
        }
        final String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcessInfo.processName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now));

        return id;
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            //System.out.println("Date in milli :: " + timeInMilliseconds + " 날짜 : " + date);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }


    private void sendNotification() {
        Intent targetIntent = null;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        PendingIntent pIntent;
        if (HoUtils.isNull(type)) {
            targetIntent = new Intent(this, ActMain.class);
        } else {
            if(type.equalsIgnoreCase("userqna_partner")){
                targetIntent = new Intent(this, ActRequestChkList.class);
                stackBuilder.addParentStack(ActMain.class);
                stackBuilder.addNextIntentWithParentStack(targetIntent);
            } else if(type.equalsIgnoreCase("construct_reply")||type.equalsIgnoreCase("construct_reply_re")){
                targetIntent = new Intent(this, ActConstructInfoDetail.class);
                targetIntent.putExtra("idx",roomIdx);
                stackBuilder.addParentStack(ActMain.class);
                stackBuilder.addNextIntentWithParentStack(targetIntent);
            } else if (type.equalsIgnoreCase("notice")) {
                targetIntent = new Intent(this, ActNotice.class);
                stackBuilder.addParentStack(ActMain.class);
                stackBuilder.addNextIntentWithParentStack(targetIntent);
            } else if (type.equalsIgnoreCase("reply")) {
                targetIntent = new Intent(this, ActRequestList.class);
                stackBuilder.addParentStack(ActMain.class);
                stackBuilder.addNextIntentWithParentStack(targetIntent);
            } else {
                targetIntent = new Intent(this, ActMain.class);
                stackBuilder.addParentStack(ActMain.class);
                stackBuilder.addNextIntentWithParentStack(targetIntent);
            }

        }
        pIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(type.equalsIgnoreCase("userqna_partner")){
                HonotificationManager.sendTextNotification(this, 1, HonotificationManager.Channel.NOTIFICATION_TXT, notiTitle, notiSub, pIntent);
            }else if(type.equalsIgnoreCase("construct_reply")||type.equalsIgnoreCase("construct_reply_re")){
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_TXT, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("chatting")) {
                HonotificationManager.sendTextNotification(this, Integer.parseInt(roomIdx), HonotificationManager.Channel.NOTIFICATION_CHATTING, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("notice")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_NOTICE, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("reserve_noti")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_RESERVE, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("reserve_success")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_RESERVE_SUCCESS, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("story_reply_reply")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_STORY_REPLY_REPLY, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("community_reply")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_COMMUNITY, notiTitle, notiSub, pIntent);
            } else if (type.equalsIgnoreCase("reply")) {
                HonotificationManager.sendTextNotification(this, createID(), HonotificationManager.Channel.NOTIFICATION_COMMUNITY, notiTitle, notiSub, pIntent);
            }
        } else {
            if (Build.VERSION.SDK_INT > 15) {
                //신버전(Notification.Builder) 사용 (API16부터 사용 가능)
                Notification.Builder mNoti = new Notification.Builder(this);
                mNoti.setTicker(notiSub);   //상단 상태표시줄에 잠깐 나오는 메세지
                mNoti.setSmallIcon(R.mipmap.ic_launcher);
                mNoti.setContentTitle(notiTitle);   //알림창 제목
                mNoti.setContentText(notiSub);  //알림 창 내용
                mNoti.setStyle(new Notification.BigTextStyle().bigText(notiSub));
                mNoti.setAutoCancel(true);
                mNoti.setContentIntent(pIntent);
                if (!HoUtils.isNull(adImg)) {
                    mNoti.setLargeIcon(getBitmap(JsonUrl.DEFAULT_HTTP_ADDRESS + adImg));
                }
                notificationManager.notify(createID(), mNoti.build());
            } else {
                //구버전(Notification 사용)
                Notification.Builder builder = new Notification.Builder(this)
                        .setContentIntent(pIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notiTitle)
                        .setContentText(notiSub)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                if (!HoUtils.isNull(adImg)) {
                    builder.setLargeIcon(getBitmap(JsonUrl.DEFAULT_HTTP_ADDRESS + adImg));
                }
                Notification notification = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    notification = builder.build();
                }
                notificationManager.notify(createID(), notification);
            }
        }
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            Bitmap tmp = BitmapFactory.decodeStream(is);
            retBitmap = Bitmap.createScaledBitmap(tmp, tmp.getWidth(), tmp.getHeight(), true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }

}