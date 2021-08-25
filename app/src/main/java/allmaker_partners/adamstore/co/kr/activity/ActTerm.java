package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.NoticeAdapter;
import allmaker_partners.adamstore.co.kr.data.NoticeData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutNoticeBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutTermBinding;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActTerm extends AppCompatActivity implements View.OnClickListener {
    LayoutTermBinding binding;
    Activity act;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_term);
        setLayout();
    }

    public void setLayout() {
        act = this;
        type = getIntent().getStringExtra("type");  //term , personal
        if(type.equalsIgnoreCase("term")){
            binding.tvTitle.setText("이용약관 동의");
        }else{
            binding.tvTitle.setText("개인정보 수집 및 이용동의");
        }
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
        binding.ivBack.setOnClickListener(this);
        getTerm();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void getTerm() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getTerm");
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(type.equalsIgnoreCase("term")){
                                    if(joList.has("di_terms")){
                                        binding.tvTerm.setText(HoUtils.getStr(joList,"di_terms"));
                                    }else{
                                        binding.tvTerm.setText("이용약관 정보가 존재하지 않습니다.");
                                    }
                                }else{
                                    if(joList.has("di_personal")){
                                        binding.tvTerm.setText(HoUtils.getStr(joList,"di_personal"));
                                    }else{
                                        binding.tvTerm.setText("개인정보처리방침 정보가 존재하지 않습니다.");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                HoUtils.hideProgressDialog();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    HoUtils.hideProgressDialog();
                }
            }
        }).start();
    }

}
