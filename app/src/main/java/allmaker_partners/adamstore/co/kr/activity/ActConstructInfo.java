package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
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
import allmaker_partners.adamstore.co.kr.adapter.ConstructMylistAdapter;
import allmaker_partners.adamstore.co.kr.adapter.VaginalAdapter;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.data.VaginalData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConsructInfoBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutLawSearchBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgConstructFilter;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.ItemDecorationAlbumColumns;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActConstructInfo extends AppCompatActivity implements View.OnClickListener {
    LayoutConsructInfoBinding binding;
    Activity act;
    ArrayList<ConstructData> constructList;
    GridLayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_consruct_info);
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
        type = "design";
        binding.llWrite.setOnClickListener(this);
        constructList = new ArrayList<>();
        mAdapter = new ConstructAdapter(constructList, act);
        mManager = new GridLayoutManager(act, 2);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
        binding.rcvList.addItemDecoration(new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen.dp10), 2));
        binding.tvLanguage.setOnClickListener(this);
        binding.tvNews.setOnClickListener(this);
        binding.flTab1.setOnClickListener(this);
        binding.flTab2.setOnClickListener(this);
        binding.flTab3.setOnClickListener(this);
        binding.tvNews.setOnClickListener(this);
        binding.tvLanguage.setOnClickListener(this);
        binding.llRefresh.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        getCommercialList(0);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_language:
                intent = new Intent(act, ActLawLanguageList.class);
                startActivity(intent);
                break;
            case R.id.tv_news:
                intent = new Intent(act,ActNews.class);
                startActivity(intent);
                break;
            case R.id.fl_tab_1:
                binding.tvTab1.setTextColor(getResources().getColor(R.color.color_09a9f4));
                binding.tvTab2.setTextColor(getResources().getColor(R.color.color_333333));
                binding.tvTab3.setTextColor(getResources().getColor(R.color.color_333333));
                binding.ivLine1.setVisibility(View.VISIBLE);
                binding.ivLine2.setVisibility(View.GONE);
                binding.ivLine3.setVisibility(View.GONE);
                intent = new Intent(act, DlgConstructFilter.class);
                intent.putExtra("type","design");
                startActivityForResult(intent,10);
                break;
            case R.id.fl_tab_2:
                binding.tvTab1.setTextColor(getResources().getColor(R.color.color_333333));
                binding.tvTab2.setTextColor(getResources().getColor(R.color.color_09a9f4));
                binding.tvTab3.setTextColor(getResources().getColor(R.color.color_333333));
                binding.ivLine1.setVisibility(View.GONE);
                binding.ivLine2.setVisibility(View.VISIBLE);
                binding.ivLine3.setVisibility(View.GONE);
                intent = new Intent(act, DlgConstructFilter.class);
                intent.putExtra("type","construction");
                startActivityForResult(intent,11);
                break;
            case R.id.fl_tab_3:
                binding.tvTab1.setTextColor(getResources().getColor(R.color.color_333333));
                binding.tvTab2.setTextColor(getResources().getColor(R.color.color_333333));
                binding.tvTab3.setTextColor(getResources().getColor(R.color.color_09a9f4));
                binding.ivLine1.setVisibility(View.GONE);
                binding.ivLine2.setVisibility(View.GONE);
                binding.ivLine3.setVisibility(View.VISIBLE);
                intent = new Intent(act, DlgConstructFilter.class);
                intent.putExtra("type","material");
                startActivityForResult(intent,12);
                break;
            /*case R.id.ll_refresh:
                binding.tvSub1.setText("전체");
                getCommercialList(0);
                break;*/
                //공인중개 - broker
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    type = "design";
                    binding.tvSub1.setText(data.getStringExtra("result"));
                    getCommercialList(0);
                    //Log.d(HoUtils.TAG,"결과 : "+data.getStringExtra("result"));
                }
                break;
            case 11:
                if(resultCode==RESULT_OK){
                    type = "construction";
                    binding.tvSub2.setText(data.getStringExtra("result"));
                    getCommercialList(1);
                    //Log.d(HoUtils.TAG,"결과 : "+data.getStringExtra("result"));
                }
                break;
            case 12:
                if(resultCode==RESULT_OK){
                    type = "material";
                    binding.tvSub3.setText(data.getStringExtra("result"));
                    getCommercialList(2);
                    //Log.d(HoUtils.TAG,"결과 : "+data.getStringExtra("result"));
                }
                break;
        }
    }


    public void getCommercialList(int posi) {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getCommercialList");
                    mParam.put("cb_type", type);
                    if(posi==0){
                        mParam.put("cb_type_sub1", binding.tvSub1.getText().toString().replaceAll("전체",""));
                    }else if(posi==1){
                        mParam.put("cb_type_sub2", binding.tvSub2.getText().toString().replaceAll("전체",""));
                    }else if(posi==2){
                        mParam.put("cb_type_sub3", binding.tvSub3.getText().toString().replaceAll("전체",""));
                    }
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONArray data = new JSONArray(HoUtils.getStr(joList,"data"));
                                    constructList = new ArrayList<>();
                                    for (int i = 0;i<data.length();i++){
                                        ConstructData cData = new ConstructData();
                                        JSONObject jo = data.getJSONObject(i);
                                        cData.setIdx(HoUtils.getStr(jo,"cb_idx"));
                                        cData.setTitle(HoUtils.getStr(jo,"cb_title"));
                                        cData.setSub(HoUtils.getStr(jo,"cb_sub"));
                                        cData.setImg(JsonUrl.DEFAULT_HTTP_ADDRESS+HoUtils.getStr(jo,"cb_file"));
                                        cData.setType(HoUtils.getStr(jo,"cb_type"));
                                        constructList.add(cData);
                                    }
                                    ((ConstructAdapter) mAdapter).setItems(constructList);
                                    if(constructList.size()!=0){
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
