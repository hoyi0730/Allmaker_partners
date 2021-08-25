package allmaker_partners.adamstore.co.kr.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActJoin;
import allmaker_partners.adamstore.co.kr.activity.ActLogin;
import allmaker_partners.adamstore.co.kr.databinding.FragJoinStep3Binding;

public class FragJoinStep3 extends Fragment implements View.OnClickListener {
    FragJoinStep3Binding binding;
    Activity act;

    public static FragJoinStep3 newInstance() {

        Bundle args = new Bundle();
        FragJoinStep3 fragment = new FragJoinStep3();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_join_step_3, container, false);
        act = getActivity();
        binding.tvOk.setOnClickListener(this);
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                Intent intent = new Intent(act, ActLogin.class);
                intent.putExtra("type","join");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ((ActJoin)act).finish();
                break;
        }
    }
}