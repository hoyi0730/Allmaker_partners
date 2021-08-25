package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.LayoutLoginBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActLogin extends AppCompatActivity implements View.OnClickListener {
    LayoutLoginBinding binding;
    Activity act;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_login);
        setLayout();
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), getString(R.string.app_finish), Toast.LENGTH_SHORT).show();
        }
    }


    public void setLayout() {
        act = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_EFF3F9));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (true) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(0);
            }
        }

        binding.tvJoin.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.llFindPass.setOnClickListener(this);
        if(!HoUtils.isNull(getIntent().getStringExtra("type"))){
            if(getIntent().getStringExtra("type").equalsIgnoreCase("join")){
                binding.etId.setText(AppUserData.getData(act,"id"));
                binding.etPass.setText(AppUserData.getData(act,"pass"));
                setLogin();
            }
        }

        if(!HoUtils.isNull(AppUserData.getData(act,"auto"))){
            if(AppUserData.getData(act,"auto").equalsIgnoreCase("on")){
                binding.etId.setText(AppUserData.getData(act,"id"));
                binding.etPass.setText(AppUserData.getData(act,"pass"));
                setLogin();
            }
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_find_pass:
                intent = new Intent(act,ActFindIdPass.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                if(HoUtils.isNull(binding.etId.getText().toString())){
                    Toast.makeText(act, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etPass.getText().toString())){
                    Toast.makeText(act, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    setLogin();
                }
                break;
            case R.id.tv_join:
                intent = new Intent(act,ActJoin.class);
                startActivity(intent);
                break;
        }
    }


    public void setLogin() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setPLoginCk");
                    mParam.put("c_id", binding.etId.getText().toString());
                    mParam.put("c_pass", binding.etPass.getText().toString());
                    mParam.put("c_fcm", FirebaseInstanceId.getInstance().getToken());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HoUtils.hideProgressDialog();
                            try {
                                if (!HoUtils.isNull(result)) {
                                    JSONObject jo = new JSONObject(result);
                                    if (HoUtils.getStr(jo, "result").equalsIgnoreCase("Y")) {
                                        /*intent = new Intent(act,ActMain.class);
                                          startActivity(intent);*/
                                        if(HoUtils.isNull(AppUserData.getData(act,"auto"))){
                                            AppUserData.setData(act,"auto","on");
                                        }
                                        AppUserData.setData(act,"id",binding.etId.getText().toString());
                                        AppUserData.setData(act,"pass",binding.etPass.getText().toString());
                                        AppUserData.setData(act,"idx",HoUtils.getStr(jo,"message"));
                                        Intent intent = new Intent(act,ActMain.class);
                                        startActivity(intent);
                                        finish();
                                    } else if(HoUtils.getStr(jo, "result").equalsIgnoreCase("P")){
                                        if(HoUtils.isNull(AppUserData.getData(act,"auto"))){
                                           AppUserData.setData(act,"auto","on");
                                        }
                                        AppUserData.setData(act,"id",binding.etId.getText().toString());
                                        AppUserData.setData(act,"pass",binding.etPass.getText().toString());
                                        Intent intent = new Intent(act,ActMain.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(act, HoUtils.getStr(jo,"message"), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(act, "잠시 후 다시 시도 부탁드립니다.", Toast.LENGTH_SHORT).show();
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
