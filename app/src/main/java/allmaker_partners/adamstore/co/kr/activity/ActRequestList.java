package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.RequestAdapter;
import allmaker_partners.adamstore.co.kr.data.RequestData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutRequestListBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActRequestList extends AppCompatActivity implements View.OnClickListener {
    LayoutRequestListBinding binding;
    Activity act;
    ArrayList<RequestData> requestData;
    RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_request_list);
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
        requestData = new ArrayList<>();
        binding.exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(requestData.get(groupPosition).getStatus().equalsIgnoreCase("N")){
                    Toast.makeText(act, "답변이 작성되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    return false;
                }
            }
        });

        binding.llWrite.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotice();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_write:
                intent = new Intent(act,ActRequest.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_top:
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
                    mParam.put("dbControl", "getPQna");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                requestData = new ArrayList<>();
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONArray data = new JSONArray(HoUtils.getStr(joList,"data"));
                                    for (int i = 0;i<data.length();i++){
                                        JSONObject ob = data.getJSONObject(i);
                                        RequestData nData = new RequestData();
                                        nData.setIdx(HoUtils.getStr(ob,"b_idx"));
                                        nData.setReplyDate(HoUtils.getStr(ob,"b_reply_date"));
                                        nData.setStatus(HoUtils.getStr(ob,"b_reply_is"));
                                        nData.setSub(HoUtils.getStr(ob,"b_contents"));
                                        nData.setTDate(HoUtils.getStr(ob,"b_regdate"));
                                        nData.setTitle(HoUtils.getStr(ob,"b_title"));
                                        nData.setViewCnt(HoUtils.getStr(ob,"b_hits"));
                                        requestData.add(nData);
                                    }
                                    adapter = new RequestAdapter(getApplicationContext(),R.layout.request_top,R.layout.request_bottom,requestData);
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
