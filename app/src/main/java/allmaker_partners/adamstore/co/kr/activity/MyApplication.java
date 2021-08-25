package allmaker_partners.adamstore.co.kr.activity;

import android.app.Application;
import android.os.Build;

import com.google.firebase.FirebaseApp;

import allmaker_partners.adamstore.co.kr.util.HonotificationManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            HonotificationManager.createChannel(this);
        }
    }
}
