package allmaker_partners.adamstore.co.kr.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhotoUtils {

    public static File getImageFile(Activity act, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = act.getContentResolver().query(uri, proj, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor == null || cursor.getCount() < 1) {
            return null;
        }

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(index);

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        return new File(path);
    }

    public static String getNewVideoFileName() {
        return System.currentTimeMillis() + ".mp4";
    }

    public static File getSaveFolder() {
        File dir;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/WareU/");
        }else{
            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WareU/");
        }*/
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AllMaker/");
        Log.d(HoUtils.TAG,"파일생성");
        if (!dir.exists()) {
            dir.mkdirs();
            Log.d(HoUtils.TAG,"파일생성!!");
        }
        return dir;
    }

    public static String getNewImageFileName() {
        return System.currentTimeMillis() + ".jpg";
    }

    public static void copyFile(File fromFile, File toFile) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(fromFile); // 복사할 파일
            os = new FileOutputStream(toFile); // 임시파일 경로
            FileChannel fcin = is.getChannel();
            FileChannel fcout = os.getChannel();

            long size = fcin.size();

            fcin.transferTo(0, size, fcout);

            fcout.close();
            fcin.close();
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(HoUtils.TAG,"이셉션");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(HoUtils.TAG,"이셉션2");
        }
    }

    public static String getVideoTime(String path) {
        Log.d(HoUtils.TAG,"비디오 패스 : "+path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14){
            retriever.setDataSource(path, new HashMap<String, String>());
        }else{
            retriever.setDataSource(path);
        }
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong(time);
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInmillisec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInmillisec)),
                TimeUnit.MILLISECONDS.toSeconds(timeInmillisec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInmillisec)));
        return hms;
    }

    public static String setThumbnail(String mVideoFilePath) {
        Bitmap tempThumb = null;
        if (mVideoFilePath == null)
            return null;

        try {
            // 썸네일 추출후 리사이즈해서 다시 비트맵 생성
            String root = Environment.getExternalStorageDirectory().toString();
//                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFilePath, MediaStore.Images.Thumbnails.MINI_KIND);
            Log.i("TEST_HOME", "rootPath: " + root + "/test/video_test.mp4");
            Bitmap thumb = ThumbnailUtils.extractThumbnail(bitmap, 360, 480);

            Log.i("TEST_HOME", "bitmap: " + bitmap);
            Log.i("TEST_HOME", "thumb: " + thumb);

            tempThumb = bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //썸네일 저장 후 경로가지고 있기
        String filename = "/rec" + System.currentTimeMillis() + ".png";

        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AllMaker/");
        if (!myDir.exists())
            myDir.mkdir();
//        mThumbFilePath = saveBitmapAsFile(tempThumb, myDir.getAbsolutePath() + filename);
        return saveBitmapAsFile(tempThumb, myDir.getAbsolutePath() + filename);
    }

    public static String saveBitmapAsFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);
        OutputStream os = null;

        try {
            file.createNewFile();
            os = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
}
