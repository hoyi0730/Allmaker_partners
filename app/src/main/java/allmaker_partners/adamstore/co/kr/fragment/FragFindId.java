package allmaker_partners.adamstore.co.kr.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.FragFindIdBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgFindIdPass;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;


public class FragFindId extends Fragment implements View.OnClickListener {
    FragFindIdBinding binding;
    Activity act;
    long START_TIME_IN_MILLIS = 180000;
    CountDownTimer mCountDownTimer;
    boolean mTimerRunning = false;
    long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    String certChk;

    public static FragFindId newInstance() {

        Bundle args = new Bundle();
        FragFindId fragment = new FragFindId();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_find_id, container, false);
        act = getActivity();
        binding.tvSendCertNum.setOnClickListener(this);
        binding.tvCert.setOnClickListener(this);
        binding.tvFindId.setOnClickListener(this);
        certChk = "0";
        return binding.getRoot();
    }

    public void updateCountDownText(){
        int minutes = (int)(mTimeLeftInMillis/1000) / 60;
        int seconds = (int)(mTimeLeftInMillis/1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d??? %02d???", minutes, seconds);
        binding.tvCountDown.setText("???????????? ???????????? "+timeLeftFormatted);
    }

    public void countDown(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mCountDownTimer.cancel();
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
            }
        }.start();
    }

    public void setHideKeyBoard(){
        HoUtils.hideKeyboard(act,binding.etCertNum);
        HoUtils.hideKeyboard(act,binding.etName);
        HoUtils.hideKeyboard(act,binding.etPhone);
    }

    @Override
    public void onClick(View v) {
        setHideKeyBoard();
        switch (v.getId()){
            case R.id.tv_find_id:
                if(HoUtils.isNull(binding.etName.getText().toString())){
                    Toast.makeText(act, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "????????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etCertNum.getText().toString())){
                    Toast.makeText(act, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(!certChk.equalsIgnoreCase("1")){
                    Toast.makeText(act, "???????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }else{
                    setFindId();
                }
                break;
            case R.id.tv_send_cert_num:
                if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else{
                    setSendCertNum();
                }
                break;
            case R.id.tv_cert:
                if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "????????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etCertNum.getText().toString())){
                    Toast.makeText(act, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(certChk.equalsIgnoreCase("1")){
                    Toast.makeText(act, "?????? ????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                }else{
                    setChkCertNum();
                }
                break;

        }
    }

    public void setSendCertNum() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setSmsSend");
                    mParam.put("m_hp", binding.etPhone.getText().toString());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                Toast.makeText(act, HoUtils.getStr(joList,"message"), Toast.LENGTH_SHORT).show();
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    countDown();
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

    public void setChkCertNum() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "chkSms");
                    mParam.put("m_hp", binding.etPhone.getText().toString());
                    mParam.put("certnum", binding.etCertNum.getText().toString());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                Toast.makeText(act, HoUtils.getStr(joList,"message"), Toast.LENGTH_SHORT).show();
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    mTimerRunning = false;
                                    mCountDownTimer.cancel();
                                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                                    updateCountDownText();
                                    binding.etPhone.setFocusable(false);
                                    binding.etPhone.setClickable(false);
                                    binding.etPhone.setTextColor(getResources().getColor(R.color.color_aaaaaa));
                                    binding.etCertNum.setFocusable(false);
                                    binding.etCertNum.setClickable(false);
                                    binding.etCertNum.setTextColor(getResources().getColor(R.color.color_aaaaaa));
                                    binding.tvCountDown.setVisibility(View.GONE);
                                    certChk = "1";
                                }else{
                                    certChk = "0";
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

    public void setFindId() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "findIdCompany");
                    mParam.put("m_company", binding.etName.getText().toString());
                    mParam.put("m_tel", binding.etPhone.getText().toString());
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                Toast.makeText(act, HoUtils.getStr(joList,"message"), Toast.LENGTH_SHORT).show();
                                binding.etPhone.setFocusable(true);
                                binding.etPhone.setClickable(true);
                                binding.etPhone.setTextColor(getResources().getColor(R.color.color_555555));
                                binding.etCertNum.setFocusable(true);
                                binding.etCertNum.setClickable(true);
                                binding.etCertNum.setTextColor(getResources().getColor(R.color.color_555555));
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    Intent intent = new Intent(act, DlgFindIdPass.class);
                                    intent.putExtra("type","id");
                                    intent.putExtra("data",HoUtils.getStr(joList,"data"));
                                    startActivity(intent);
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