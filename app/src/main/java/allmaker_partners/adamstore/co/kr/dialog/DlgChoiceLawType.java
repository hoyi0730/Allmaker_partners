package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.DlgChoiceLawTypeBinding;
import allmaker_partners.adamstore.co.kr.util.HoUtils;

public class DlgChoiceLawType extends Activity implements View.OnClickListener {
    DlgChoiceLawTypeBinding binding;
    Activity act;
    String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 아래에 원하는 투명도를 넣으면 된다.
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_choice_law_type);
        setLayout();
    }

    private void setLayout(){
        act=this;
        type = "";
        binding.ivClose.setOnClickListener(this);
        binding.llTab1.setOnClickListener(this);
        binding.llTab2.setOnClickListener(this);
        binding.tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.ll_tab_1:
                binding.iv1.setImageResource(R.drawable.btn_choice_on);
                binding.iv2.setImageResource(R.drawable.btn_choice_off);
                type = "자음별";
                break;
            case R.id.ll_tab_2:
                binding.iv1.setImageResource(R.drawable.btn_choice_off);
                binding.iv2.setImageResource(R.drawable.btn_choice_on);
                type = "분류별";
                break;
            case R.id.tv_ok:
                if(HoUtils.isNull(type)){
                    Toast.makeText(act, "검색형태를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("result",type);
                    setResult(RESULT_OK,intent);
                    onBackPressed();
                }
                break;
        }
    }
}