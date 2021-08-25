package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.VaginalAdapter;
import allmaker_partners.adamstore.co.kr.data.VaginalData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutLawSearchBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutLoginBinding;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActLawSearch extends AppCompatActivity implements View.OnClickListener {
    LayoutLawSearchBinding binding;
    Activity act;
    ArrayList<VaginalData> vaginalData;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_law_search);
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

        vaginalData = new ArrayList<>();
        mAdapter = new VaginalAdapter(vaginalData, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
        binding.ivSearch1.setOnClickListener(this);
        binding.ivSearch2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search_1:
                //대지, 건축물, 건축설비, 지하층, 거실, 건축, 대수선, 리모델링, 도로, 건축주, 내화구조, 부속건축물, 부속용도, 발코니
                HoUtils.hideKeyboard(act,binding.etWord1);
                HoUtils.hideKeyboard(act,binding.etWord2);
                if(HoUtils.isNull(binding.etWord1.getText().toString())){
                    Toast.makeText(act, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    getSearchList("keyword",binding.etWord1.getText().toString());
                }
                break;
            case R.id.iv_search_2:
                //건축
                HoUtils.hideKeyboard(act,binding.etWord1);
                HoUtils.hideKeyboard(act,binding.etWord2);
                if(HoUtils.isNull(binding.etWord2.getText().toString())){
                    Toast.makeText(act, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    getSearchList("case",binding.etWord2.getText().toString());
                }
                break;
        }
    }

    public void getSearchList(final String type, final String word) {
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "searchLaw");
                    mParam.put("type", type);
                    mParam.put("word", word);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!HoUtils.isNull(result)) {
                                    JSONObject jo = new JSONObject(result);
                                    vaginalData = new ArrayList<>();
                                    if (HoUtils.getStr(jo, "result").equalsIgnoreCase("Y")) {
                                        JSONArray jsonArray = new JSONArray(HoUtils.getStr(jo,"data"));
                                        for (int i = 0; i<jsonArray.length();i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            VaginalData vData = new VaginalData();
                                            vData.setIdx(HoUtils.getStr(object,"bl_idx"));
                                            vData.setType(HoUtils.getStr(object,"bl_type"));
                                            vData.setKeyword(HoUtils.getStr(object,"bl_keyword"));
                                            vData.setExample(HoUtils.getStr(object,"bl_example"));
                                            vData.setBl_question(HoUtils.getStr(object,"bl_question"));
                                            vData.setBl_answer(HoUtils.getStr(object,"bl_answer"));
                                            vData.setBl_reference(HoUtils.getStr(object,"bl_reference"));
                                            vaginalData.add(vData);
                                        }
                                        ((VaginalAdapter) mAdapter).setItems(vaginalData);
                                        binding.llNoList.setVisibility(View.GONE);
                                        binding.rcvList.setVisibility(View.VISIBLE);
                                    } else {
                                        binding.rcvList.setVisibility(View.GONE);
                                        binding.llNoList.setVisibility(View.VISIBLE);
                                        binding.tvNoti.setText("검색된 결과가 존재하지 않습니다.");
                                    }
                                } else {
                                    Toast.makeText(act, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
