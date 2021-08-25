package allmaker_partners.adamstore.co.kr.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.LayoutSplashBinding;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.O;

public class ActSplash extends AppCompatActivity {
    LayoutSplashBinding binding;
    Activity act;
    String device_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_splash);
        setLayout();
    }

    public void setLayout() {
        act = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (true) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(0);
            }
        }
        try {
            device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Glide.with(act)
                .asGif()
                .load(R.drawable.splash_720)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        ((GifDrawable) resource).setLoopCount(1);
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            /*@Override
                            public void onAnimationStart(Drawable drawable) {
                                super.onAnimationStart(drawable);
                                Log.d(HoUtils.TAG,"시작");
                            }*/
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                //Log.d(HoUtils.TAG, "끝");
                                versionChk();
                            }
                        });
                        return false;
                    }
                })
                .into(binding.ivSplash);

    }

    public void chkPermission() {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new MaterialAlertDialogBuilder(act).setTitle("권한허용")
                    .setMessage("다짓자 파트너스 이용을 위해 권한을 허용해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(HoUtils.TAG, "권한 3");
                            ActivityCompat.requestPermissions(act,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        }
                    }).setCancelable(false).create().show();
        } else {
            Log.d(HoUtils.TAG, "권한 5");
            setNextStep();
        }
    }

    public void setNextStep(){
        Intent intent = new Intent(act,ActLogin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0) {
                    int count = 0;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == -1) {
                            count++;
                        }
                    }
                    if (count != 0) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    } else {
                        setNextStep();
                    }
                }
                return;
            }

        }

    }

    public void versionChk() {
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    String device_version = "1.0.0";
                    try {
                        device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    mParam.put("dbControl", "partnersversion");
                    mParam.put("thisVer", device_version);
                    result = conn.sendPost(mUrl, mParam);
                    String finalDevice_version = device_version;
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!HoUtils.isNull(result)) {
                                    JSONObject jo = new JSONObject(result);
                                    if (HoUtils.getStr(jo, "result").equalsIgnoreCase("Y")) {
                                        int ver = Integer.valueOf(HoUtils.getStr(jo, "MEMCODE").replaceAll("\\.",""));
                                        int appver = Integer.valueOf(finalDevice_version.replaceAll("\\.",""));
                                        if (ver > appver){
                                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                                    new MaterialAlertDialogBuilder(new ContextThemeWrapper(act, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));
                                            materialAlertDialogBuilder.setTitle("업데이트");
                                            materialAlertDialogBuilder.setMessage("새로운 버전이 있습니다.\n업데이트 진행이 안될경우 Google Play 캐시를 삭제후 다시 시도 부탁드리겠습니다.")
                                                    .setPositiveButton("업데이트 바로가기", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=allmaker_partners.adamstore.co.kr"));
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog alertDialog = materialAlertDialogBuilder.create();
                                            alertDialog.setCanceledOnTouchOutside(false);
                                            alertDialog.show();
                                        }else{
                                            chkPermission();
                                        }
                                    } else {
                                        chkPermission();
                                    }
                                } else {
                                    chkPermission();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
