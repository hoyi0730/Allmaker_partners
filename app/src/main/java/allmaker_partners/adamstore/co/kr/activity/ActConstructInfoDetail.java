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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.ConstructAdapter;
import allmaker_partners.adamstore.co.kr.adapter.ReplyAdapter;
import allmaker_partners.adamstore.co.kr.adapter.VaginalAdapter;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.data.ReplyData;
import allmaker_partners.adamstore.co.kr.data.VaginalData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConsructInfoBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConstructInfoDetailBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.ItemDecorationAlbumColumns;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ActConstructInfoDetail extends AppCompatActivity implements View.OnClickListener {
    LayoutConstructInfoDetailBinding binding;
    Activity act;
    String idx;
    ArrayList<ReplyData> replyData;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    String replyType, replyUserIdx, replyUserName, replyIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_construct_info_detail);
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
        binding.tvReply.setOnClickListener(this);

        replyData = new ArrayList<>();
        mAdapter = new ReplyAdapter(act);
        mManager = new LinearLayoutManager(act);
        binding.rcvReply.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvReply.setAdapter(mAdapter);

        idx = getIntent().getStringExtra("idx");
        getCommercialDetail();
        replyType = "0";
        getReplyList();
        binding.llReplyName.setOnClickListener(this);
    }

    public void setReply(String name, String idx, String rIdx){
        HoUtils.scrollToView(binding.etReply, binding.scroll,0);
        replyType = "1";
        replyUserName = name;
        replyUserIdx = idx;
        replyIdx = rIdx;
        binding.tvReplyName.setText(replyUserName);
        binding.llReplyName.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_reply_name:
                binding.llReplyName.setVisibility(View.GONE);
                replyUserName = "";
                replyUserIdx = "";
                replyIdx = "";
                replyType = "0";
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_reply:
                if(HoUtils.isNull(binding.etReply.getText().toString())){
                    Toast.makeText(act, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if(replyType.equalsIgnoreCase("0")){
                        setCommercialComment();
                    }else{
                        setCommercialComment_comment();
                    }
                }
                break;

        }
    }

    public void getCommercialDetail() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getCommercialDetail");
                    mParam.put("cb_idx", idx);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONObject data = new JSONObject(HoUtils.getStr(joList,"data"));
                                    binding.tvTitle.setText(HoUtils.getStr(data,"cb_title"));
                                    binding.tvSub.setText(HoUtils.getStr(data,"cb_sub"));
                                    binding.tvViewCnt.setText("조회수:"+HoUtils.fixNull(HoUtils.getStr(data,"cb_hits"),"0"));
                                    binding.tvDate.setText(HoUtils.converLimitTime2(HoUtils.getStr(data,"cb_regdate"),"yyyy.MM.dd"));
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                                    if (act.isFinishing()) {
                                        return;
                                    }
                                    Glide.with(act)
                                            .load(JsonUrl.DEFAULT_HTTP_ADDRESS+HoUtils.getStr(data,"cb_file"))
                                            .apply(requestOptions)
                                            .centerCrop()
                                            .error(R.drawable.simbol_logoin)
                                            .into(binding.ivBanner);
                                    if(joList.has("writer")){
                                        JSONObject writer = new JSONObject(HoUtils.getStr(joList,"writer"));
                                        if(HoUtils.isNull(HoUtils.getStr(writer,"m_addr1"))){
                                            binding.tvAddress.setVisibility(View.GONE);
                                            binding.tvName.setText("작성자:"+HoUtils.getStr(writer,"m_nick"));
                                        }else{
                                            binding.tvName.setText("업체:"+HoUtils.getStr(writer,"m_company"));
                                            binding.tvAddress.setVisibility(View.VISIBLE);
                                            binding.tvAddress.setText("위치:"+HoUtils.getStr(writer,"m_addr1")+" "+HoUtils.getStr(writer,"m_addr2"));
                                        }
                                    }

                                    /*if(type.equalsIgnoreCase("design")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub1"));
                                    }else if(type.equalsIgnoreCase("construction")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub2"));
                                    }else if(type.equalsIgnoreCase("material")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub3"));
                                    }else{
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub4"));
                                    }
                                    binding.etTitle.setText(HoUtils.getStr(data,"cb_title"));
                                    binding.etSub.setText(HoUtils.getStr(data,"cb_sub"));*/
                                }else{
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
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

    public void getReplyList(){
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getCommercialDetail");
                    mParam.put("cb_idx", idx);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    replyData = new ArrayList<>();
                                    if(joList.has("comments")){
                                        if(!HoUtils.isNull(HoUtils.getStr(joList,"comments"))){
                                            JSONArray array = new JSONArray(HoUtils.getStr(joList,"comments"));
                                            for (int i=0;i<array.length();i++){
                                                JSONObject ob = array.getJSONObject(i);
                                                ReplyData rData = new ReplyData();
                                                rData.setIdx(HoUtils.getStr(ob,"cbc_idx"));
                                                rData.setDate(HoUtils.converLimitTime2(HoUtils.getStr(ob,"cbc_regdate"),"yyyy.MM.dd"));
                                                if(HoUtils.isNull(HoUtils.getStr(ob,"m_company"))){
                                                    rData.setNick(HoUtils.getStr(ob,"m_nick"));
                                                }else{
                                                    rData.setNick(HoUtils.getStr(ob,"m_company"));
                                                }
                                                rData.setReplyChk(HoUtils.getStr(ob,"cbc_reply"));
                                                rData.setSub(HoUtils.getStr(ob,"cbc_comment"));
                                                rData.setUserIdx(HoUtils.getStr(ob,"m_idx"));
                                                replyData.add(rData);
                                            }
                                            ((ReplyAdapter)mAdapter).addItems2(replyData);
                                        }
                                    }
                                }/*else{
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                }*/
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


    public void setCommercialComment() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setCommercialComment");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    mParam.put("cb_idx", idx);
                    mParam.put("cbc_comment", binding.etReply.getText().toString());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    getReplyList();
                                    binding.etReply.setText("");
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

    public void setCommercialComment_comment() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setCommercialComment_comment");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    mParam.put("cb_idx", idx);
                    mParam.put("cbc_comment", binding.etReply.getText().toString());
                    mParam.put("cbc_idx", replyIdx);
                    mParam.put("t_idx", replyUserIdx);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    getReplyList();
                                    binding.etReply.setText("");
                                    replyUserName = "";
                                    replyUserIdx = "";
                                    replyIdx = "";
                                    replyType = "0";
                                    binding.llReplyName.setVisibility(View.GONE);
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
