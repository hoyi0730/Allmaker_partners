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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.LayoutFindIdPassBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutSettingBinding;
import allmaker_partners.adamstore.co.kr.fragment.FragFindId;
import allmaker_partners.adamstore.co.kr.fragment.FragFindPass;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.HoUtils;

public class ActSetting extends AppCompatActivity implements View.OnClickListener {
    LayoutSettingBinding binding;
    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_setting);
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
        binding.llPush.setOnClickListener(this);
        binding.tvTerm.setOnClickListener(this);
        binding.tvTermPersonal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_push:
                if(HoUtils.isNull(AppUserData.getData(act,"push"))){
                    AppUserData.setData(act,"push","off");
                    binding.ivPush.setImageResource(R.drawable.input_check_off);
                }else{
                    if(AppUserData.getData(act,"push").equalsIgnoreCase("on")){
                        AppUserData.setData(act,"push","off");
                        binding.ivPush.setImageResource(R.drawable.input_check_off);
                    }else{
                        AppUserData.setData(act,"push","on");
                        binding.ivPush.setImageResource(R.drawable.input_check_on);
                    }
                }
                break;
            case R.id.tv_term:
                intent = new Intent(act, ActTerm.class);
                intent.putExtra("type","term");
                startActivity(intent);
                break;
            case R.id.tv_term_personal:
                intent = new Intent(act, ActTerm.class);
                intent.putExtra("type","personal");
                startActivity(intent);
                break;
        }
    }

}
