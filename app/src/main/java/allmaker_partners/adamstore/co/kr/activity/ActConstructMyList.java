package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
import allmaker_partners.adamstore.co.kr.adapter.AddressAdapter;
import allmaker_partners.adamstore.co.kr.adapter.ConstructAdapter;
import allmaker_partners.adamstore.co.kr.adapter.ConstructMylistAdapter;
import allmaker_partners.adamstore.co.kr.adapter.ViewPagerAdapter;
import allmaker_partners.adamstore.co.kr.data.CommercialData;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConsructInfoBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConstructMyListBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgConstructFilter;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.ItemDecorationAlbumColumns;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActConstructMyList extends AppCompatActivity implements View.OnClickListener {
    LayoutConstructMyListBinding binding;
    Activity act;
    ArrayList<ConstructData> constructList;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_construct_my_list);
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

        constructList = new ArrayList<>();
        mAdapter = new ConstructMylistAdapter(constructList, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
        binding.ivBack.setOnClickListener(this);
        binding.llRegist.setOnClickListener(this);
        binding.tvRegist.setOnClickListener(this);
    }

    public void setList(){
        if(constructList.size()!=0){
            binding.rcvList.setVisibility(View.VISIBLE);
            binding.llNoList.setVisibility(View.GONE);
        }else{
            binding.rcvList.setVisibility(View.GONE);
            binding.llNoList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommercial();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_regist:
            case R.id.tv_regist:
                Intent intent = new Intent(act,ActConstructWrite.class);
                startActivity(intent);
                break;
        }
    }

    public void getCommercial() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getCommercial");
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
                                    ((ConstructMylistAdapter) mAdapter).setItems(constructList);
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
