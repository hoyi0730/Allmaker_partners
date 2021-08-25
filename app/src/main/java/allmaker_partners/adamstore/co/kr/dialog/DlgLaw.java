package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.DlgLawBinding;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class DlgLaw extends Activity implements View.OnClickListener {
    DlgLawBinding binding;
    Activity act;
    String idx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_law);
        setLayout();
    }

    private void setLayout(){
        act=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding.ivClose.setOnClickListener(this);
        idx = getIntent().getStringExtra("idx");
        if(HoUtils.isNull(idx)){
            Toast.makeText(act, "잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            getLawDetail();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_book_mark:
                break;
        }
    }

    public void getLawDetail() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getuselanddictionary");
                    mParam.put("ud_idx", idx);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONObject data = new JSONObject(HoUtils.getStr(joList,"data"));
                                    binding.tvQuestion.setText(HoUtils.getStr(data,"ud_title"));
                                    binding.tvAnswer.setText(HoUtils.getStr(data,"ud_contents"));
                                }else{
                                    Toast.makeText(act, "잠시 후 다시 시도 부탁드립니다.", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
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