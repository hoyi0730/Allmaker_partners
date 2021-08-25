package allmaker_partners.adamstore.co.kr.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActTerm;
import allmaker_partners.adamstore.co.kr.databinding.FragJoinStep1Binding;

public class FragJoinStep1 extends Fragment implements View.OnClickListener {
    FragJoinStep1Binding binding;
    Activity act;
    String chkAll, chk1, chk2;

    public static FragJoinStep1 newInstance() {

        Bundle args = new Bundle();
        FragJoinStep1 fragment = new FragJoinStep1();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_join_step_1, container, false);
        act = getActivity();
        binding.tvNext.setOnClickListener(this);
        binding.llChkAll.setOnClickListener(this);
        binding.llChk1.setOnClickListener(this);
        binding.llChk2.setOnClickListener(this);
        chkAll = "0";
        chk1 = "0";
        chk2 = "0";
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ll_chk_all:
                if(chkAll.equalsIgnoreCase("0")) {
                    chkAll = "1";
                    chk1 = "1";
                    chk2 = "1";
                    binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_on);
                    binding.ivChk1.setImageResource(R.drawable.btn_check_mb_on);
                    binding.ivChk2.setImageResource(R.drawable.btn_check_mb_on);
                }else{
                    chkAll = "0";
                    chk1 = "0";
                    chk2 = "0";
                    binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_off);
                    binding.ivChk1.setImageResource(R.drawable.btn_check_mb_off);
                    binding.ivChk2.setImageResource(R.drawable.btn_check_mb_off);
                }
                break;
            case R.id.ll_chk_1:
                if(chk1.equalsIgnoreCase("0")){
                    intent = new Intent(act, ActTerm.class);
                    intent.putExtra("type","term");
                    startActivity(intent);
                    binding.ivChk1.setImageResource(R.drawable.btn_check_mb_on);
                    chk1 = "1";
                    if(chk2.equalsIgnoreCase("0")){
                        chkAll = "0";
                        binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_off);
                    }else{
                        chkAll = "1";
                        binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_on);
                    }
                }else{
                    chk1 = "0";
                    binding.ivChk1.setImageResource(R.drawable.btn_check_mb_off);
                    chkAll = "0";
                    binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_off);
                }
                break;
            case R.id.ll_chk_2:
                if(chk2.equalsIgnoreCase("0")){
                    intent = new Intent(act, ActTerm.class);
                    intent.putExtra("type","personal");
                    startActivity(intent);
                    binding.ivChk2.setImageResource(R.drawable.btn_check_mb_on);
                    chk2 = "1";
                    if(chk1.equalsIgnoreCase("1")){
                        chkAll = "1";
                        binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_on);
                    }else{
                        chkAll = "0";
                        binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_off);
                    }
                }else{
                    chk2 = "0";
                    binding.ivChk2.setImageResource(R.drawable.btn_check_mb_off);
                    chkAll = "0";
                    binding.ivChkAll.setImageResource(R.drawable.btn_check_mb_off);
                }
                break;
            case R.id.tv_next:
                if(chk1.equalsIgnoreCase("0")||chk2.equalsIgnoreCase("0")){
                    Toast.makeText(act, "약관을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentTransaction ft = ((AppCompatActivity)act).getSupportFragmentManager().beginTransaction();
                    FragJoinStep2 np = new FragJoinStep2();
                    ft.addToBackStack(null);
                    ft.replace(R.id.fl_ground,np);
                    ft.commit();
                }
                break;
        }
    }
}