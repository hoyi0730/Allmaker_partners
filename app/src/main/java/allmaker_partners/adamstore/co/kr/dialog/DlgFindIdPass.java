package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.DlgFindIdPassBinding;


public class DlgFindIdPass extends Activity implements View.OnClickListener {
    DlgFindIdPassBinding binding;
    Activity act;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_find_id_pass);
        setLayout();
    }

    private void setLayout(){
        act=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding.ivClose.setOnClickListener(this);
        if(getIntent().getStringExtra("type").equalsIgnoreCase("id")){
            binding.tvType.setText("아이디");
            binding.llId.setVisibility(View.VISIBLE);
            binding.llPassword.setVisibility(View.GONE);
            binding.tvId.setText(getIntent().getStringExtra("data"));
        }else{
            binding.tvType.setText("패스워드");
            binding.llId.setVisibility(View.GONE);
            binding.llPassword.setVisibility(View.VISIBLE);
            binding.tvPass.setText(getIntent().getStringExtra("data"));
        }
        binding.tvCancel.setOnClickListener(this);
        binding.tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
            case R.id.tv_ok:
            case R.id.iv_close:
                finish();
                break;
        }
    }
}