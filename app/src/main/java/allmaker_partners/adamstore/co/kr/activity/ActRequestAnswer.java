package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.RequestChkAdapter;
import allmaker_partners.adamstore.co.kr.data.RequestChkData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutRequestAnswerBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutSettingBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActRequestAnswer extends AppCompatActivity implements View.OnClickListener {
    LayoutRequestAnswerBinding binding;
    Activity act;
    String idx, yIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_request_answer);
        setLayout();
    }

    public void setLayout() {
        act = this;
        binding.ivBack.setOnClickListener(this);
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
        binding.tvOk.setOnClickListener(this);
        if(getIntent().getStringExtra("status").equalsIgnoreCase("Y")){
            binding.tvTitle.setText("문의답변수정하기");
            binding.tvOk.setText("답변하기");
        }else{
            binding.tvTitle.setText("문의답변하기");
            binding.tvOk.setText("저장하기");
        }

        binding.tvName.setText(getIntent().getStringExtra("name"));
        binding.tvDate.setText(getIntent().getStringExtra("date").substring(0,11));
        binding.tvQuestion.setText(getIntent().getStringExtra("request"));
        binding.etAnswer.setText(getIntent().getStringExtra("answer"));

        idx = getIntent().getStringExtra("idx");
        yIdx = getIntent().getStringExtra("yIdx");

        /*intent.putExtra("status",requestChkList.get(position).getStatus());
        intent.putExtra("request",requestChkList.get(position).getRequest());
        intent.putExtra("name",requestChkList.get(position).getName());
        intent.putExtra("date",requestChkList.get(position).getDate());
        intent.putExtra("idx",requestChkList.get(position).getIdx());
        intent.putExtra("answer",requestChkList.get(position).getAnswer());*/

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_ok:
                if(HoUtils.isNull(binding.etAnswer.getText().toString())){
                    Toast.makeText(act, "답변 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if(binding.tvTitle.getText().toString().contains("수정")){
                        modiPtoUQnaReply();
                    }else{
                        setPtoUQnaReply();
                    }
                }
                break;

        }
    }

    public void setPtoUQnaReply() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setPtoUQnaReply");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    mParam.put("cq_idx", idx);
                    mParam.put("cq_reply", binding.etAnswer.getText().toString());
                    mParam.put("y_idx", yIdx);

                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
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

    public void modiPtoUQnaReply() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "modiPtoUQnaReply");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    mParam.put("cq_idx", idx);
                    mParam.put("cq_reply", binding.etAnswer.getText().toString());
                    mParam.put("y_idx", yIdx);
                    mParam.put("type", "notify");

                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
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
