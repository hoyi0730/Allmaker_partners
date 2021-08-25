package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
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
import allmaker_partners.adamstore.co.kr.fragment.FragFindId;
import allmaker_partners.adamstore.co.kr.fragment.FragFindPass;

public class ActFindIdPass extends AppCompatActivity implements View.OnClickListener {
    LayoutFindIdPassBinding binding;
    Activity act;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_find_id_pass);
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
        transaction = fragmentManager.beginTransaction();
        FragFindId menu1Fragment = new FragFindId();
        transaction.replace(R.id.fl_ground, menu1Fragment).commitAllowingStateLoss();
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        binding.llTab1.setOnClickListener(this);
        binding.llTab2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_tab_1:
                binding.tvTab1.setTextColor(getResources().getColor(R.color.color_444444));
                binding.tvTab2.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                binding.ivLine1.setVisibility(View.VISIBLE);
                binding.ivLine2.setVisibility(View.INVISIBLE);
                transaction = fragmentManager.beginTransaction();
                FragFindId fragFindId = new FragFindId();
                transaction.replace(R.id.fl_ground, fragFindId).commitAllowingStateLoss();
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.ll_tab_2:
                binding.tvTab1.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                binding.tvTab2.setTextColor(getResources().getColor(R.color.color_444444));
                binding.ivLine1.setVisibility(View.INVISIBLE);
                binding.ivLine2.setVisibility(View.VISIBLE);
                transaction = fragmentManager.beginTransaction();
                FragFindPass fragFindPass = new FragFindPass();
                transaction.replace(R.id.fl_ground, fragFindPass).commitAllowingStateLoss();
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

}
