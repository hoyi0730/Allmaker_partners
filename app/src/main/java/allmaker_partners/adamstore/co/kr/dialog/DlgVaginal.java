package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.DlgVaginalBinding;

public class DlgVaginal extends Activity implements View.OnClickListener {
    DlgVaginalBinding binding;
    Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_vaginal);
        setLayout();
    }

    private void setLayout(){
        act=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding.ivClose.setOnClickListener(this);
        //binding.ivBookMark.setOnClickListener(this);
        binding.tvQuestion.setText(getIntent().getStringExtra("question"));
        binding.tvAnswer.setText(getIntent().getStringExtra("answer"));
        binding.tvReference.setText(getIntent().getStringExtra("reference"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            /*case R.id.iv_book_mark:
                break;*/
        }
    }
}