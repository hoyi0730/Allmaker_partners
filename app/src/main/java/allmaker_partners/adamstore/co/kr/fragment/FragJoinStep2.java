package allmaker_partners.adamstore.co.kr.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActFindAddress;
import allmaker_partners.adamstore.co.kr.databinding.FragJoinStep2Binding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;
import allmaker_partners.adamstore.co.kr.util.MultipartUtility;
import allmaker_partners.adamstore.co.kr.util.PhotoUtils;

import static android.app.Activity.RESULT_OK;

public class FragJoinStep2 extends Fragment implements View.OnClickListener {
    FragJoinStep2Binding binding;
    Activity act;
    String profilePath, filePath, choiceType, type, certChk;
    private Uri photoURI;
    File imgFile;
    final int GALLEY = 2;
    final int CROP = 3;
    ImageView ivList[];
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> chkItems = new ArrayList<>();
    long START_TIME_IN_MILLIS = 180000;
    CountDownTimer mCountDownTimer;
    boolean mTimerRunning = false;
    long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public static FragJoinStep2 newInstance() {

        Bundle args = new Bundle();
        FragJoinStep2 fragment = new FragJoinStep2();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_join_step_2, container, false);
        act = getActivity();
        certChk = "0";
        binding.flImg.setOnClickListener(this);
        binding.llFile.setOnClickListener(this);
        binding.ivCancelFile.setOnClickListener(this);
        binding.tvSearchAddress.setOnClickListener(this);
        binding.llTab1.setOnClickListener(this);
        binding.llTab2.setOnClickListener(this);
        binding.llTab3.setOnClickListener(this);
        binding.llTab4.setOnClickListener(this);
        binding.tvArea.setOnClickListener(this);
        binding.tvSendCertNum.setOnClickListener(this);
        binding.tvOk.setOnClickListener(this);
        binding.tvCertOk.setOnClickListener(this);
        filePath = "";
        profilePath = "";
        ivList = new ImageView[]{binding.ivTab1, binding.ivTab2, binding.ivTab3, binding.ivTab4};
        binding.tvEmailType.setOnClickListener(this);
        return binding.getRoot();
    }

    public void setImageTab(int posi) {
        for (int i = 0; i < ivList.length; i++) {
            if (i == posi) {
                ivList[i].setImageResource(R.drawable.btn_check_on_3_110302);
            } else {
                ivList[i].setImageResource(R.drawable.btn_check_off_4_110302);
            }
        }
        if (posi == 0) {
            type = "design";
        } else if (posi == 1) {
            type = "construction";
        } else if (posi == 2) {
            type = "material";
        } else {
            type = "broker";
        }
    }

    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d분 %02d초", minutes, seconds);
        binding.tvCountDown.setText("인증번호 유효시간 " + timeLeftFormatted);
    }

    public void countDown() {
        binding.tvCountDown.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
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
                binding.tvCountDown.setVisibility(View.GONE);
            }
        }.start();
    }

    public void setHideKeyBoard(){
        HoUtils.hideKeyboard(act,binding.etAddressSub);
        HoUtils.hideKeyboard(act,binding.etCertNum);
        HoUtils.hideKeyboard(act,binding.etCompanyName);
        HoUtils.hideKeyboard(act,binding.etCompanyTel);
        HoUtils.hideKeyboard(act,binding.etId);
        HoUtils.hideKeyboard(act,binding.etInfo);
        HoUtils.hideKeyboard(act,binding.etMail);
        HoUtils.hideKeyboard(act,binding.etOrnerName);
        HoUtils.hideKeyboard(act,binding.etPass);
        HoUtils.hideKeyboard(act,binding.etPassChk);
        HoUtils.hideKeyboard(act,binding.etPhone);
        HoUtils.hideKeyboard(act,binding.etProfessional);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        setHideKeyBoard();
        switch (v.getId()) {
            case R.id.tv_cert_ok:
                if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etCertNum.getText().toString())){
                    Toast.makeText(act, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(certChk.equalsIgnoreCase("1")){
                    Toast.makeText(act, "이미 인증을 완료 하였습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    setChkCertNum();
                }
                break;
            case R.id.tv_ok:
                if (HoUtils.isNull(binding.etId.getText().toString())) {
                    Toast.makeText(act, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etPass.getText().toString())){
                    Toast.makeText(act, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etPassChk.getText().toString())){
                    Toast.makeText(act, "패스워드를 한번 더 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!binding.etPass.getText().toString().equalsIgnoreCase(binding.etPassChk.getText().toString())){
                    Toast.makeText(act, "패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if(HoUtils.isNull(binding.etCompanyName.getText().toString())){
                    Toast.makeText(act, "업체명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etCompanyTel.getText().toString())){
                    Toast.makeText(act, "업체 연락처를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.tvAddress.getText().toString())){
                    Toast.makeText(act, "주소를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etAddressSub.getText().toString())){
                    Toast.makeText(act, "상세주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etOrnerName.getText().toString())){
                    Toast.makeText(act, "대표자명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etMail.getText().toString())){
                    Toast.makeText(act, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(binding.tvEmailType.getText().toString().equalsIgnoreCase("선택")){
                    Toast.makeText(act, "이메일 형식을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "휴대폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etCertNum.getText().toString())){
                    Toast.makeText(act, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!certChk.equalsIgnoreCase("1")){
                    Toast.makeText(act, "휴대폰번호 인증을 진행해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(type)){
                    Toast.makeText(act, "업종을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etProfessional.getText().toString())){
                    Toast.makeText(act, "전문분야를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(binding.tvArea.getText().toString().equalsIgnoreCase("지역선택")){
                    Toast.makeText(act, "서비스가능지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etInfo.getText().toString())){
                    Toast.makeText(act, "회사 소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(filePath)){
                    Toast.makeText(act, "사업자등록증을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(profilePath)){
                    Toast.makeText(act, "업체로고사진을 등록해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    setJoinMember();
                }
                break;
            case R.id.tv_send_cert_num:
                if(HoUtils.isNull(binding.etPhone.getText().toString())){
                    Toast.makeText(act, "대표자 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    setSendCertNum();
                }
                break;
            case R.id.tv_area:
                listItems = getResources().getStringArray(R.array.area_lit);
                checkedItems = new boolean[listItems.length];
                new MaterialAlertDialogBuilder(act)
                        .setTitle("서비스가능지역")
                        .setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                /*if (isChecked) {
                                    if (!chkItems.contains(which)) {
                                        chkItems.add(which);
                                    }
                                } else if (chkItems.contains(which)) {
                                    chkItems.remove(which);
                                }*/
                                if (isChecked) {
                                    if (!chkItems.contains(which)) {
                                        chkItems.add(which);
                                    }
                                } else {
                                    for (int i = 0; i < chkItems.size(); i++) {
                                        if (chkItems.get(i)==which) {
                                            chkItems.remove(i);
                                        }
                                    }
                                }
                            }
                        }).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < chkItems.size(); i++) {
                            item = item + listItems[chkItems.get(i)];
                            if (i != chkItems.size() - 1) {
                                item = item + ", ";
                            }
                            binding.tvArea.setText(item);
                        }
                    }
                }).show();
                break;
            case R.id.tv_email_type:
                final String[] gender = new String[]{"naver.com", "yahoo.co.kr", "hanmail.net", "empal.com", "paran.com", "hotmail.com", "nate.com", "korea.com", "gmail.com"};
                new MaterialAlertDialogBuilder(act)
                        .setTitle("이메일형식 선택")
                        //.setMessage("아이디 검색결과 입니다.")
                        .setItems(gender, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.tvEmailType.setText(gender[which]);
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.ll_tab_1:
                setImageTab(0);
                break;
            case R.id.ll_tab_2:
                setImageTab(1);
                break;
            case R.id.ll_tab_3:
                setImageTab(2);
                break;
            case R.id.ll_tab_4:
                setImageTab(3);
                break;
            case R.id.tv_search_address:
                intent = new Intent(act, ActFindAddress.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.iv_cancel_file:
                choiceType = "";
                filePath = "";
                binding.tvFileName.setText("");
                binding.llFileName.setVisibility(View.GONE);
                break;
            case R.id.fl_img:
                choiceType = "1";
                selectGallery();
                break;
            case R.id.ll_file:
                choiceType = "2";
                selectGallery();
                break;
        }
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); //이미지만 불러오기
        startActivityForResult(intent, GALLEY);
    }

    private void cropImg() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(act, "allmaker_partners.adamstore.co.kr", imgFile);
            intent.setDataAndType(contentUri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("output", photoURI);
        } else {
            intent.setDataAndType(photoURI, "image/*");
            intent.putExtra("output", photoURI);
        }
        startActivityForResult(intent, CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    binding.tvAddress.setText(data.getStringExtra("result"));
                }
                break;
            case GALLEY:
                if (resultCode == RESULT_OK) {
                    photoURI = data.getData();
                    File origin = PhotoUtils.getImageFile(act, photoURI);
                    imgFile = new File(PhotoUtils.getSaveFolder(), PhotoUtils.getNewImageFileName());
                    photoURI = Uri.fromFile(imgFile);
                    File clone = new File(photoURI.getPath());
                    PhotoUtils.copyFile(origin, clone);
                    cropImg();
                }
                break;
            case CROP:
                if (resultCode == RESULT_OK) {
                    String full_path = photoURI.getPath();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(full_path, options);
                    Bitmap resize = null;
                    try {
                        File file = new File(full_path);
                        FileOutputStream out = new FileOutputStream(file);
                        int width = bm.getWidth();
                        int height = bm.getHeight();
                        //Log.d(HoUtils.TAG, "가로 : " + width + "세로 : " + height);
                        resize = Bitmap.createScaledBitmap(bm, width, height, true);
                        resize.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    File size = new File(full_path);
                    if (size.length() > 10000000) {
                        Toast.makeText(act, "파일 용량이 10MB가 넘습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (choiceType.equalsIgnoreCase("1")) {
                            profilePath = full_path;
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                            if (((Activity) act).isFinishing()) {
                                return;
                            }
                            Glide.with(act)
                                    .load(new File(profilePath))
                                    .circleCrop()
                                    .apply(requestOptions)
                                    .error(R.drawable.simbol_logoin)
                                    .into(binding.ivImg);
                        } else {
                            binding.llFileName.setVisibility(View.VISIBLE);
                            filePath = full_path;
                            binding.tvFileName.setText(filePath);
                        }
                        //binding.tvFileName.setText(full_path);
                    }
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


    public void setJoinMember() {
        HoUtils.showProgressDialog(act);
        final String charset = "UTF-8";
        final String mUrl = JsonUrl.CONTROL;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MultipartUtility multipart = new MultipartUtility(mUrl, charset);
                    multipart.addFormField("dbControl", "setPUserMember");
                    multipart.addFilePart("m_company_logo", new File(profilePath));
                    multipart.addFormField("m_id", binding.etId.getText().toString());
                    multipart.addFormField("m_pass", binding.etPass.getText().toString());
                    multipart.addFormField("m_company", binding.etCompanyName.getText().toString());
                    multipart.addFormField("m_tel", binding.etCompanyTel.getText().toString());
                    multipart.addFormField("m_addr1", binding.tvAddress.getText().toString());
                    multipart.addFormField("m_addr2", binding.etAddressSub.getText().toString());
                    multipart.addFormField("m_company_ceo", binding.etOrnerName.getText().toString());
                    String mail = binding.etMail.getText().toString()+"@"+binding.tvEmailType.getText().toString();
                    multipart.addFormField("m_company_email", mail);
                    multipart.addFormField("m_company_tel", binding.etPhone.getText().toString());
                    multipart.addFilePart("m_ein_file", new File(filePath));
                    multipart.addFormField("m_company_type", type);
                    multipart.addFormField("m_company_field", binding.etProfessional.getText().toString());
                    multipart.addFormField("m_company_area", binding.tvArea.getText().toString());
                    multipart.addFormField("m_company_info", binding.etInfo.getText().toString());
                    multipart.addFormField("fcm", FirebaseInstanceId.getInstance().getToken());
                    final String response = multipart.finish();
                    Log.d(HoUtils.TAG, "response > " + response);
                    //Toast.makeText(act, "저장로그 : "+response, Toast.LENGTH_SHORT).show();
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HoUtils.hideProgressDialog();
                            try {
                                JSONObject jo = new JSONObject(response);
                                if (HoUtils.getStr(jo, "result").equalsIgnoreCase("Y")) {
                                    Toast.makeText(act, HoUtils.getStr(jo, "message"), Toast.LENGTH_SHORT).show();
                                    AppUserData.setData(act,"id",binding.etId.getText().toString());
                                    AppUserData.setData(act,"pass",binding.etPass.getText().toString());
                                    AppUserData.setData(act,"idx",HoUtils.getStr(jo,"data"));
                                    FragmentTransaction ft = ((AppCompatActivity) act).getSupportFragmentManager().beginTransaction();
                                    FragJoinStep3 np = new FragJoinStep3();
                                    ft.addToBackStack(null);
                                    ft.replace(R.id.fl_ground, np);
                                    ft.commit();
                                } else {
                                    Toast.makeText(act, HoUtils.getStr(jo, "message"), Toast.LENGTH_SHORT).show();
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