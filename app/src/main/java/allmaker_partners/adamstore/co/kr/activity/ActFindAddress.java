package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
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
import allmaker_partners.adamstore.co.kr.adapter.AddressAdapter;
import allmaker_partners.adamstore.co.kr.databinding.LayoutFindAddressBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActFindAddress extends AppCompatActivity implements View.OnClickListener {
    LayoutFindAddressBinding binding;
    Activity act;
    ArrayList<String> addressDataList;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_find_address);
        setLayout();
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    HoUtils.hideKeyboard(act,binding.etSearch);
                    if(HoUtils.isNull(binding.etSearch.getText().toString())){
                        Toast.makeText(act, "검색 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        /*ArrayList<String> list = new ArrayList<String>();
                        list = AppUserData.getListData(act,"searchList");
                        list.add(binding.etSearch.getText().toString());
                        AppUserData.setListData(act,"searchList",list);*/
                        getAddress();
                    }
                    handled = true;
                }
                return handled;
            }
        });

    }

    public void setResult(String res){
        Intent intent = new Intent();
        intent.putExtra("result",res);
        setResult(RESULT_OK,intent);
        onBackPressed();
    }

    public void setLayout(){
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
        binding.ivSearch.setOnClickListener(this);
        addressDataList = new ArrayList<>();
        mAdapter = new AddressAdapter(addressDataList, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvListAddress.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvListAddress.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search:
                HoUtils.hideKeyboard(act,binding.etSearch);
                if(HoUtils.isNull(binding.etSearch.getText().toString())){
                    Toast.makeText(act, "검색 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    getAddress();
                }
                break;
        }
    }

    public void getAddress()
    {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final String result;
                try
                {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl","searchMapAddress");
                    mParam.put("address",binding.etSearch.getText().toString());
                    mParam.put("size","");
                    mParam.put("page","");
                    // Log.d(CommonUtil.TAG,"유니크 : "+CommonUtil.getDeviceId(act)+"모델 : "+CommonUtil.getDeviceName());
                    result = conn.sendPost(mUrl, mParam);

                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                HoUtils.hideProgressDialog();
                                addressDataList = new ArrayList<>();
                                JSONObject jo = new JSONObject(result);
                                if(HoUtils.getStr(jo,"result").equalsIgnoreCase("Y")){
                                    JSONArray array = new JSONArray(HoUtils.getStr(jo,"data"));
                                    for (int i = 0;i<array.length();i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        if(!HoUtils.isNull(HoUtils.getStr(ob,"address"))){
                                            JSONObject adObject = new JSONObject(HoUtils.getStr(ob,"address"));
                                            if(!HoUtils.isNull(HoUtils.getStr(adObject,"bldnmdc"))){
                                                addressDataList.add(HoUtils.getStr(adObject,"road")+" "+HoUtils.getStr(adObject,"bldnmdc"));
                                            }else{
                                                addressDataList.add(HoUtils.getStr(adObject,"road"));
                                            }
                                        }
                                    }
                                    ((AddressAdapter) mAdapter).setItems(addressDataList);
                                    binding.llSearchResult.setVisibility(View.VISIBLE);
                                    binding.llNoListAddress.setVisibility(View.GONE);
                                }else{
                                    binding.llSearchResult.setVisibility(View.GONE);
                                    binding.llNoListAddress.setVisibility(View.VISIBLE);
                                }
                                binding.llInfo.setVisibility(View.GONE);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
