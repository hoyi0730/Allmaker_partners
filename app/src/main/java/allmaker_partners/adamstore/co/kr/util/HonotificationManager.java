package allmaker_partners.adamstore.co.kr.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import allmaker_partners.adamstore.co.kr.R;


public class HonotificationManager {

    private static final String GROUP_TED_PARK = "CottonCandy";

    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel(Context context) {
        NotificationChannelGroup group = new NotificationChannelGroup(GROUP_TED_PARK, GROUP_TED_PARK);
        getManager(context).createNotificationChannelGroup(group);

        NotificationChannel txtNoti = new NotificationChannel(Channel.NOTIFICATION_TXT,
                "텍스트알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        txtNoti.setDescription(context.getString(R.string.app_name));
        txtNoti.setGroup(GROUP_TED_PARK);
        txtNoti.setLightColor(Color.GREEN);
        txtNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(txtNoti);



        NotificationChannelGroup group1 = new NotificationChannelGroup(GROUP_TED_PARK, GROUP_TED_PARK);
        getManager(context).createNotificationChannelGroup(group1);

        NotificationChannel noticeNoti = new NotificationChannel(Channel.NOTIFICATION_NOTICE,
                "공지사항", android.app.NotificationManager.IMPORTANCE_HIGH);
        noticeNoti.setDescription(context.getString(R.string.app_name));
        noticeNoti.setGroup(GROUP_TED_PARK);
        noticeNoti.setLightColor(Color.GREEN);
        noticeNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(noticeNoti);


        NotificationChannel replyNoti = new NotificationChannel(Channel.NOTIFICATION_REPLY,
                "댓글알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        replyNoti.setDescription(context.getString(R.string.app_name));
        replyNoti.setGroup(GROUP_TED_PARK);
        replyNoti.setLightColor(Color.GREEN);
        replyNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(replyNoti);

        NotificationChannel topNoti = new NotificationChannel(Channel.NOTIFICATION_CHATTING,
                "채팅알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        topNoti.setDescription(context.getString(R.string.app_name));
        topNoti.setGroup(GROUP_TED_PARK);
        topNoti.setLightColor(Color.GREEN);
        topNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(topNoti);


        NotificationChannel reserveNoti = new NotificationChannel(Channel.NOTIFICATION_RESERVE,
                "예약알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        reserveNoti.setDescription(context.getString(R.string.app_name));
        reserveNoti.setGroup(GROUP_TED_PARK);
        reserveNoti.setLightColor(Color.GREEN);
        reserveNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(reserveNoti);


        NotificationChannel storyReplyNoti = new NotificationChannel(Channel.NOTIFICATION_RESERVE,
                "스토리댓글알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        storyReplyNoti.setDescription(context.getString(R.string.app_name));
        storyReplyNoti.setGroup(GROUP_TED_PARK);
        storyReplyNoti.setLightColor(Color.GREEN);
        storyReplyNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(storyReplyNoti);


        NotificationChannel resuccessNoti = new NotificationChannel(Channel.NOTIFICATION_RESERVE_SUCCESS,
                "리뷰작성알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        resuccessNoti.setDescription(context.getString(R.string.app_name));
        resuccessNoti.setGroup(GROUP_TED_PARK);
        resuccessNoti.setLightColor(Color.GREEN);
        resuccessNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(resuccessNoti);

        NotificationChannel cmNoti = new NotificationChannel(Channel.NOTIFICATION_COMMUNITY,
                "커뮤니티댓글알림", android.app.NotificationManager.IMPORTANCE_HIGH);
        cmNoti.setDescription(context.getString(R.string.app_name));
        cmNoti.setGroup(GROUP_TED_PARK);
        cmNoti.setLightColor(Color.GREEN);
        cmNoti.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(cmNoti);




        NotificationChannel requestChat = new NotificationChannel(Channel.NOTIFICATION_CHAT_REQUEST,
                "일반채팅신청", android.app.NotificationManager.IMPORTANCE_HIGH);
        requestChat.setDescription(context.getString(R.string.app_name));
        requestChat.setGroup(GROUP_TED_PARK);
        requestChat.setLightColor(Color.GREEN);
        requestChat.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(requestChat);

        NotificationChannel requestFaceChat = new NotificationChannel(Channel.NOTIFICATION_CHAT_FACE_REQUEST,
                "영상채팅신청", android.app.NotificationManager.IMPORTANCE_HIGH);
        requestFaceChat.setDescription(context.getString(R.string.app_name));
        requestFaceChat.setGroup(GROUP_TED_PARK);
        requestFaceChat.setLightColor(Color.GREEN);
        requestFaceChat.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(requestFaceChat);

        NotificationChannel chatAccept = new NotificationChannel(Channel.NOTIFICATION_CHAT_ARRIVE,
                "채팅승락유무", android.app.NotificationManager.IMPORTANCE_HIGH);
        chatAccept.setDescription(context.getString(R.string.app_name));
        chatAccept.setGroup(GROUP_TED_PARK);
        chatAccept.setLightColor(Color.GREEN);
        chatAccept.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(chatAccept);

        NotificationChannel faceChatAccept = new NotificationChannel(Channel.NOTIFICATION_CHAT_FACE_ARRIVE,
                "영상채팅승락유무", android.app.NotificationManager.IMPORTANCE_HIGH);
        faceChatAccept.setDescription(context.getString(R.string.app_name));
        faceChatAccept.setGroup(GROUP_TED_PARK);
        faceChatAccept.setLightColor(Color.GREEN);
        faceChatAccept.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(faceChatAccept);

        NotificationChannel starGift = new NotificationChannel(Channel.NOTIFICATION_STAR_GIFT,
                "별풍선", android.app.NotificationManager.IMPORTANCE_HIGH);
        starGift.setDescription(context.getString(R.string.app_name));
        starGift.setGroup(GROUP_TED_PARK);
        starGift.setLightColor(Color.GREEN);
        starGift.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(starGift);

        NotificationChannel acceptchat = new NotificationChannel(Channel.NOTIFICATION_CHAT_ACCEPT,
                "채팅신청수락", android.app.NotificationManager.IMPORTANCE_HIGH);
        acceptchat.setDescription(context.getString(R.string.app_name));
        acceptchat.setGroup(GROUP_TED_PARK);
        acceptchat.setLightColor(Color.GREEN);
        acceptchat.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(acceptchat);

        NotificationChannel profilelike = new NotificationChannel(Channel.NOTIFICATION_LIKE_PROFILE,
                "관심회원등록", android.app.NotificationManager.IMPORTANCE_HIGH);
        profilelike.setDescription(context.getString(R.string.app_name));
        profilelike.setGroup(GROUP_TED_PARK);
        profilelike.setLightColor(Color.GREEN);
        profilelike.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(profilelike);

    }

    private static android.app.NotificationManager getManager(Context context) {
        return (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void deleteChannel(Context context, @Channel String channel) {
        getManager(context).deleteNotificationChannel(channel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendImgNotification(Context context, int id, @Channel String channel, String title, String body, PendingIntent intent, String img) {
        Notification.Builder builder = new Notification.Builder(context, channel)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setContentIntent(intent)
                .setLargeIcon(getBitmap(JsonUrl.DEFAULT_HTTP_ADDRESS+img))
                .setAutoCancel(true);
        getManager(context).notify(id, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendTextNotification(Context context, int id, @Channel String channel, String title, String body, PendingIntent intent) {
        Notification.Builder builder = new Notification.Builder(context, channel)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setContentIntent(intent)
                .setAutoCancel(true);
        getManager(context).notify(id, builder.build());
    }

    private static Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpsURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpsURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            Bitmap tmp = BitmapFactory.decodeStream(is);
            retBitmap = Bitmap.createScaledBitmap(tmp, tmp.getWidth(), tmp.getHeight(),true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }

    private static int getSmallIcon() {
        return R.drawable.icon_512;
        //return R.drawable.icon_140;
        //return R.drawable.icon512;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Channel.NOTIFICATION_TXT,


            Channel.NOTIFICATION_NOTICE,
            Channel.NOTIFICATION_REPLY,


            Channel.NOTIFICATION_CHATTING,
            Channel.NOTIFICATION_RESERVE,
            Channel.NOTIFICATION_STORY_REPLY_REPLY,
            Channel.NOTIFICATION_RESERVE_SUCCESS,
            Channel.NOTIFICATION_COMMUNITY,

            Channel.NOTIFICATION_CHAT_REQUEST,
            Channel.NOTIFICATION_CHAT_FACE_REQUEST,
            Channel.NOTIFICATION_CHAT_ARRIVE,
            Channel.NOTIFICATION_CHAT_FACE_ARRIVE,
            Channel.NOTIFICATION_STAR_GIFT,
            Channel.NOTIFICATION_CHAT_ACCEPT,
            Channel.NOTIFICATION_LIKE_PROFILE


    })
    public @interface Channel {
        String NOTIFICATION_TXT = "텍스트알림";

        String NOTIFICATION_NOTICE = "공지사항";
        String NOTIFICATION_REPLY = "댓글작성알림";
        String NOTIFICATION_CHATTING = "채팅알림";
        String NOTIFICATION_RESERVE = "예약알림";
        String NOTIFICATION_STORY_REPLY_REPLY = "스토리댓글";
        String NOTIFICATION_RESERVE_SUCCESS = "예약성공";
        String NOTIFICATION_COMMUNITY = "커뮤니티알림";
        String NOTIFICATION_CHAT_REQUEST = "일반채팅신청";
        String NOTIFICATION_CHAT_FACE_REQUEST = "영상채팅신청";
        String NOTIFICATION_CHAT_ARRIVE = "채팅 승락유무도착";
        String NOTIFICATION_CHAT_FACE_ARRIVE = "영상채팅 승락유무도착";
        String NOTIFICATION_STAR_GIFT = "별풍선선물";
        String NOTIFICATION_CHAT_ACCEPT = "채팅신청수락";
        String NOTIFICATION_LIKE_PROFILE = "관심친구등록";

    }

}
