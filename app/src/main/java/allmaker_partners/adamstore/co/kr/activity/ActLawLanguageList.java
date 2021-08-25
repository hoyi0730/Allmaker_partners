package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.LawAdapter;
import allmaker_partners.adamstore.co.kr.data.LawData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutLawLanguagelistBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgChoiceLawType;
import allmaker_partners.adamstore.co.kr.dialog.DlgChoiceLawTypeSub;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActLawLanguageList extends AppCompatActivity implements View.OnClickListener {
    LayoutLawLanguagelistBinding binding;
    Activity act;
    ArrayList<LawData> lawList;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_law_languagelist);
        setLayout();
    }

    public void setLayout() {
        act = this;
        lawList = new ArrayList<>();
        mAdapter = new LawAdapter(lawList, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
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
        binding.tvType.setOnClickListener(this);
        binding.tvTypeSub.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_type:
                intent = new Intent(act, DlgChoiceLawType.class);
                startActivityForResult(intent,10);
                break;
            case R.id.tv_type_sub:
                if(binding.tvType.getText().toString().equalsIgnoreCase("분류별")){
                    intent = new Intent(act, DlgChoiceLawTypeSub.class);
                    startActivityForResult(intent,11);
                }else{
                    Toast.makeText(act, "검색형태가 분류별일 경우만 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_search:
                HoUtils.hideKeyboard(act,binding.etWord1);
                if(HoUtils.isNull(binding.etWord1.getText().toString())){
                    Toast.makeText(act, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    getLawLanguage();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_top:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    binding.tvType.setText(data.getStringExtra("result"));
                    if(data.getStringExtra("result").equalsIgnoreCase("분류별")){
                        /*Toast.makeText(act, "검색대상 선택 부탁드리겠습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(act, DlgChoiceLawTypeSub.class);
                        startActivityForResult(intent,11);*/
                        binding.tvTypeSub.setText("용어");
                    }else{
                        binding.tvTypeSub.setText("-");
                    }
                }
                break;
            case 11:
                if(resultCode==RESULT_OK){
                    binding.tvTypeSub.setText(data.getStringExtra("result"));
                }
                break;
        }
    }

    public void getLawLanguage() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "uselanddictionaylist");
                    if(binding.tvType.getText().toString().equalsIgnoreCase("분류별")){
                        mParam.put("type", "kind");
                        if(binding.tvTypeSub.getText().toString().equalsIgnoreCase("해설")){
                            mParam.put("type_kind", "sub");
                        }else{
                            mParam.put("type_kind", "title");
                        }
                    }else{
                        mParam.put("type", "word");
                    }
                    mParam.put("key", binding.etWord1.getText().toString());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                lawList = new ArrayList<>();
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONArray data = new JSONArray(HoUtils.getStr(joList,"data"));
                                    for (int i = 0;i<data.length();i++){
                                        JSONObject ob = data.getJSONObject(i);
                                        LawData nData = new LawData();
                                        nData.setIdx(HoUtils.getStr(ob,"ud_idx"));
                                        nData.setSub(HoUtils.getStr(ob,"ud_contents"));
                                        nData.setTitle(HoUtils.getStr(ob,"ud_title"));
                                        nData.setDate(HoUtils.getStr(ob,"ud_regdate"));
                                        lawList.add(nData);
                                    }
                                    binding.rcvList.setVisibility(View.VISIBLE);
                                    binding.llNoList.setVisibility(View.GONE);
                                    ((LawAdapter)mAdapter).setItems(lawList);
                                }else{
                                    binding.rcvList.setVisibility(View.GONE);
                                    binding.llNoList.setVisibility(View.VISIBLE);
                                    binding.tvNoti.setText("검색결과가 존재하지 않습니다.");
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
