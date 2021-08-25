package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.DlgRequestCompleteBinding;


public class DlgRequestComplete extends Activity implements View.OnClickListener {
    DlgRequestCompleteBinding binding;
    Activity act;
    String photo,url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_request_complete);
        setLayout();
    }

    private void setLayout(){
        act=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding.ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }
}