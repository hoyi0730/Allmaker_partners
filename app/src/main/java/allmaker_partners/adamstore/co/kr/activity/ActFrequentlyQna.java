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
import allmaker_partners.adamstore.co.kr.adapter.FrequentlyQnaAdapter;
import allmaker_partners.adamstore.co.kr.data.QnaData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutFrequentlyQnaBinding;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActFrequentlyQna extends AppCompatActivity implements View.OnClickListener {
    LayoutFrequentlyQnaBinding binding;
    Activity act;
    ArrayList<QnaData> noticeList;
    FrequentlyQnaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_frequently_qna);
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
        binding.ivBack.setOnClickListener(this);
        noticeList = new ArrayList<>();
        getNotice();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void getNotice() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getPFaq");
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                noticeList = new ArrayList<>();
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONArray data = new JSONArray(HoUtils.getStr(joList,"data"));
                                    for (int i = 0;i<data.length();i++){
                                        JSONObject ob = data.getJSONObject(i);
                                        QnaData nData = new QnaData();
                                        nData.setTitle(HoUtils.getStr(ob,"b_title"));
                                        /*nData.setView_cnt(HoUtils.getStr(ob,"b_hits"));
                                        nData.setDate(HoUtils.converLimitTime2(HoUtils.getStr(ob,"b_regdate"),"yyyy.MM.dd"));*/
                                        nData.setIdx(HoUtils.getStr(ob,"b_idx"));
                                        nData.setSub(HoUtils.getStr(ob,"b_contents"));
                                        noticeList.add(nData);
                                    }
                                    adapter = new FrequentlyQnaAdapter(getApplicationContext(),R.layout.frequently_qna_top,R.layout.frequently_qna_bottom,noticeList);
                                    binding.exListView.setAdapter(adapter);
                                    binding.llNoList.setVisibility(View.GONE);
                                    binding.exListView.setVisibility(View.VISIBLE);
                                }else{
                                    binding.llNoList.setVisibility(View.VISIBLE);
                                    binding.exListView.setVisibility(View.GONE);
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
