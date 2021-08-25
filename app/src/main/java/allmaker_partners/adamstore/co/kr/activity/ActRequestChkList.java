package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.ConstructAdapter;
import allmaker_partners.adamstore.co.kr.adapter.RequestChkAdapter;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.data.RequestChkData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutRequestChkListBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutSettingBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActRequestChkList extends AppCompatActivity implements View.OnClickListener {
    LayoutRequestChkListBinding binding;
    Activity act;
    ArrayList<RequestChkData> requestChkData;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_request_chk_list);
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

        requestChkData = new ArrayList<>();
        mAdapter = new RequestChkAdapter(requestChkData, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommercialList();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }


    public void getCommercialList() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getPtoUQna");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONArray data = new JSONArray(HoUtils.getStr(joList,"data"));
                                    requestChkData = new ArrayList<>();
                                    for (int i = 0;i<data.length();i++){
                                        RequestChkData cData = new RequestChkData();
                                        JSONObject jo = data.getJSONObject(i);
                                        cData.setAnswer(HoUtils.fixNull(HoUtils.getStr(jo,"cq_reply"),""));
                                        cData.setDate(HoUtils.getStr(jo,"cq_regdate"));
                                        cData.setStatus(HoUtils.getStr(jo,"cq_replychk"));
                                        cData.setIdx(HoUtils.getStr(jo,"cq_idx"));
                                        cData.setRequest(HoUtils.getStr(jo,"cq_contents"));
                                        if(jo.has("uinfo")){
                                            if(!HoUtils.isNull(HoUtils.getStr(jo,"uinfo"))){
                                                JSONObject user = new JSONObject(HoUtils.getStr(jo,"uinfo"));
                                                cData.setName(HoUtils.getStr(user,"m_nick"));
                                                cData.setCYidx(HoUtils.getStr(user,"m_idx"));
                                                requestChkData.add(cData);
                                            }
                                        }
                                    }
                                    ((RequestChkAdapter) mAdapter).setItems(requestChkData);
                                    if(requestChkData.size()!=0){
                                        binding.llNoList.setVisibility(View.GONE);
                                        binding.rcvList.setVisibility(View.VISIBLE);
                                    }else{
                                        binding.llNoList.setVisibility(View.VISIBLE);
                                        binding.rcvList.setVisibility(View.GONE);
                                    }
                                }else{
                                    binding.llNoList.setVisibility(View.VISIBLE);
                                    binding.rcvList.setVisibility(View.GONE);
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
