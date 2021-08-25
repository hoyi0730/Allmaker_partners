package allmaker_partners.adamstore.co.kr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.ViewPagerAdapter;
import allmaker_partners.adamstore.co.kr.data.CommercialData;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.databinding.LayoutMainBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgFindIdPass;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.CustomTransformer;
import allmaker_partners.adamstore.co.kr.util.ExpandingViewPager2Transformer;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.HorizontalMarginItemDecoration;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

import static java.lang.Math.abs;

public class ActMain extends AppCompatActivity implements View.OnClickListener{
    LayoutMainBinding binding;
    Activity act;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main);
        setLayout();
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), getString(R.string.app_finish), Toast.LENGTH_SHORT).show();
        }
    }

    public void setLayout(){
        act = this;
        //AppUserData.setData(act,"idx","6");
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
        binding.ivSlide.setOnClickListener(this);
        binding.ivSetting.setOnClickListener(this);
        binding.llNotice.setOnClickListener(this);
        binding.llFaq.setOnClickListener(this);
        binding.llRequestChk.setOnClickListener(this);
        binding.llConstruct.setOnClickListener(this);
        binding.llLaw.setOnClickListener(this);
        binding.llWrite.setOnClickListener(this);
        binding.slideMenu.ivClose.setOnClickListener(this);
        binding.slideMenu.tvLogout.setOnClickListener(this);
        binding.slideMenu.tvNotice.setOnClickListener(this);
        binding.slideMenu.tvRequest.setOnClickListener(this);
        binding.slideMenu.tvQna.setOnClickListener(this);
        binding.slideMenu.tvSetting.setOnClickListener(this);
        binding.slideMenu.tvSlideConstruct.setOnClickListener(this);
        binding.slideMenu.tvSlideLaw.setOnClickListener(this);
        binding.slideMenu.tvSlideRequestChk.setOnClickListener(this);
        binding.slideMenu.tvSlideWrite.setOnClickListener(this);
        binding.slideMenu.tvModify.setOnClickListener(this);
        binding.tvModifyMain.setOnClickListener(this);
        binding.tvCommercial.setOnClickListener(this);

        binding.viewpager.setOffscreenPageLimit(1);
        float nextItemVisiblePx = getResources().getDimension(R.dimen.dp24);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.dp42);
        float pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        ViewPager2.PageTransformer pageTransformer = new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setTranslationX(- pageTranslationX * position);
                //page.setScaleY(1 - (0.25f * abs(position)));  //세로길이 줄이려면 이렇게하기
                // Next line scales the item's height. You can remove it if you don't want this effect
            }
        };
        /*float nextItemVisiblePx = getResources().getDimension(R.dimen.dp26);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.dp42);*/
        binding.viewpager.setPageTransformer(pageTransformer);
        binding.viewpager.addItemDecoration(new HorizontalMarginItemDecoration(getResources().getDimension(R.dimen.dp42)));

        /*CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new ExpandingViewPager2Transformer());
        compositePageTransformer.addTransformer(new CustomTransformer(act));
        binding.viewpager.setPageTransformer(compositePageTransformer);*/

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_commercial:
                intent = new Intent(act, ActConstructWrite.class);
                intent.putExtra("type","material");
                startActivity(intent);
                break;
            case R.id.tv_modify_main:
            case R.id.tv_modify:
                intent = new Intent(act,ActModify.class);
                startActivity(intent);
                break;
            case R.id.tv_request:
                intent = new Intent(act,ActRequestList.class);
                startActivity(intent);
                break;
            case R.id.tv_logout:
                new MaterialAlertDialogBuilder(act).setTitle("로그아웃")
                        .setMessage("다짓자 파트너스 에서 로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).create().show();
                break;
            case R.id.tv_slide_construct:
            case R.id.ll_construct:
                intent = new Intent(act,ActConstructInfo.class);
                startActivity(intent);
                break;
            case R.id.tv_slide_law:
            case R.id.ll_law:
                intent = new Intent(act,ActLawSearch.class);
                startActivity(intent);
                break;
            case R.id.tv_slide_write:
            case R.id.ll_write:
                intent = new Intent(act,ActConstructMyList.class);
                startActivity(intent);
                break;
            case R.id.tv_slide_request_chk:
            case R.id.ll_request_chk:
                intent = new Intent(act,ActRequestChkList.class);
                startActivity(intent);
                break;
            case R.id.tv_qna:
            case R.id.ll_faq:
                intent = new Intent(act,ActFrequentlyQna.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
            case R.id.ll_notice:
                intent = new Intent(act,ActNotice.class);
                startActivity(intent);
                break;
            case R.id.iv_close:
            case R.id.iv_slide:
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)==true){
                    binding.drawerLayout.closeDrawers();
                }else{
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.tv_setting:
            case R.id.iv_setting:
                intent = new Intent(act, ActSetting.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMemberInfo();
        getCommercial();
    }

    public void getMemberInfo() {
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getPMemberInfo");
                    mParam.put("m_idx", AppUserData.getData(act,"idx"));
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONObject data = new JSONObject(HoUtils.getStr(joList,"data"));
                                    binding.tvCompanyName.setText(HoUtils.getStr(data,"m_company")+"님");
                                    binding.slideMenu.tvName.setText(HoUtils.getStr(data,"m_company")+"님");
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                                    if (act.isFinishing()) {
                                        return;
                                    }
                                    Glide.with(act)
                                            .load(JsonUrl.DEFAULT_HTTP_ADDRESS+HoUtils.getStr(data,"m_company_logo"))
                                            .circleCrop()
                                            .apply(requestOptions)
                                            .error(R.drawable.icon_512)
                                            .into(binding.ivLogo);

                                    Glide.with(act)
                                            .load(JsonUrl.DEFAULT_HTTP_ADDRESS+HoUtils.getStr(data,"m_company_logo"))
                                            .circleCrop()
                                            .apply(requestOptions)
                                            .error(R.drawable.icon_512)
                                            .into(binding.slideMenu.ivLog);
                                    AppUserData.setData(act,"m_company_type",HoUtils.getStr(data,"m_company_type"));
                                }else{
                                    Toast.makeText(act, "회원정보를 불러올 수 없습니다. \n\n ※관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                                    finish();
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
                                    ArrayList<ConstructData> bList = new ArrayList<>();
                                    for (int i = 0;i<data.length();i++){
                                        ConstructData cData = new ConstructData();
                                        JSONObject jo = data.getJSONObject(i);
                                        cData.setIdx(HoUtils.getStr(jo,"cb_idx"));
                                        cData.setTitle(HoUtils.getStr(jo,"cb_title"));
                                        cData.setSub(HoUtils.getStr(jo,"cb_sub"));
                                        cData.setImg(JsonUrl.DEFAULT_HTTP_ADDRESS+HoUtils.getStr(jo,"cb_file"));
                                        bList.add(cData);
                                    }

                                    if(bList.size()!=0){
                                        binding.llNoList.setVisibility(View.GONE);
                                        binding.viewpager.setVisibility(View.VISIBLE);
                                        viewPagerAdapter = new ViewPagerAdapter(bList,act);
                                        binding.viewpager.setAdapter(viewPagerAdapter);
                                    }else{
                                        binding.llNoList.setVisibility(View.VISIBLE);
                                        binding.viewpager.setVisibility(View.GONE);
                                    }
                                }else{
                                    binding.llNoList.setVisibility(View.VISIBLE);
                                    binding.viewpager.setVisibility(View.GONE);
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