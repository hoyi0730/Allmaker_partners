package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConstructModifyBinding;
import allmaker_partners.adamstore.co.kr.databinding.LayoutConstructWriteBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;
import allmaker_partners.adamstore.co.kr.util.MultipartUtility;
import allmaker_partners.adamstore.co.kr.util.PhotoUtils;

public class ActConstructModify extends AppCompatActivity implements View.OnClickListener {
    LayoutConstructModifyBinding binding;
    Activity act;
    String filePath,type, typeChoice, idx;
    private Uri photoURI;
    File imgFile;
    final int GALLEY = 2;
    final int CROP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_construct_modify);
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
        binding.llFile.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvOk.setOnClickListener(this);
        binding.tvType.setOnClickListener(this);
        filePath = "";
        typeChoice = "";
        type = getIntent().getStringExtra("type");
        idx = getIntent().getStringExtra("idx");
        getCommercialDetail();
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); //???????????? ????????????
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
                        //Log.d(HoUtils.TAG, "?????? : " + width + "?????? : " + height);
                        resize = Bitmap.createScaledBitmap(bm, width, height, true);
                        resize.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    File size = new File(full_path);
                    if (size.length() > 10000000) {
                        Toast.makeText(act, "?????? ????????? 10MB??? ????????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        filePath = full_path;
                        //binding.tvFileName.setText(full_path);
                        binding.clBanner.setVisibility(View.VISIBLE);
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                        if (act.isFinishing()) {
                            return;
                        }
                        Glide.with(act)
                                .load(new File(filePath))
                                .apply(requestOptions)
                                .error(R.drawable.simbol_logoin)
                                .into(binding.ivBanner);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_cancel:
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_file:
                selectGallery();
                break;
            case R.id.tv_ok:
                if(HoUtils.isNull(typeChoice)){
                    Toast.makeText(act, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etTitle.getText().toString())){
                    Toast.makeText(act, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else if(HoUtils.isNull(binding.etSub.getText().toString())){
                    Toast.makeText(act, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }else{
                    setCommercial();
                }
                break;
            case R.id.tv_type:
                final String[] gender;
                if(type.equalsIgnoreCase("design")){
                    gender = new String[]{"????????????", "????????????/?????????", "??????", "??????/??????"};
                }else if(type.equalsIgnoreCase("construction")){
                    gender = new String[]{"????????????", "????????????/?????????", "??????", "??????/??????"};
                }else if(type.equalsIgnoreCase("material")){
                    gender = new String[]{"?????????", "???????????????", "???????????????", "??????"};
                }else{
                    //???????????? broker
                    gender = new String[]{"??????", "??????", "???", "???"};
                }
                new MaterialAlertDialogBuilder(act)
                        .setTitle("??????????????? ??????")
                        //.setMessage("????????? ???????????? ?????????.")
                        .setItems(gender, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.tvType.setText(gender[which]);
                                typeChoice = binding.tvType.getText().toString();
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    public void setCommercial() {
        HoUtils.showProgressDialog(act);
        final String charset = "UTF-8";
        final String mUrl = JsonUrl.CONTROL;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MultipartUtility multipart = new MultipartUtility(mUrl, charset);
                    multipart.addFormField("dbControl", "modiCommercial");
                    //multipart.addFormField("m_idx", AppUserData.getData(act, "idx"));
                    multipart.addFormField("cb_type", type);
                    if(type.equalsIgnoreCase("design")){
                        multipart.addFormField("cb_type_sub1", typeChoice);
                    }else if(type.equalsIgnoreCase("construction")){
                        multipart.addFormField("cb_type_sub2", typeChoice);
                    }else if(type.equalsIgnoreCase("material")){
                        multipart.addFormField("cb_type_sub3", typeChoice);
                    }else{
                        multipart.addFormField("cb_type_sub4", typeChoice);
                    }
                    multipart.addFormField("cb_title", binding.etTitle.getText().toString());
                    multipart.addFormField("cb_sub", binding.etSub.getText().toString());
                    multipart.addFormField("m_idx", AppUserData.getData(act,"idx"));
                    multipart.addFormField("cb_idx", idx);

                    if (!HoUtils.isNull(filePath)) {
                        multipart.addFilePart("cb_file", new File(filePath));
                    }

                    final String response = multipart.finish();
                    Log.d(HoUtils.TAG, "response > " + response);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HoUtils.hideProgressDialog();
                            try {
                                JSONObject jo = new JSONObject(response);
                                if (HoUtils.getStr(jo, "result").equalsIgnoreCase("Y")) {
                                    Toast.makeText(act, HoUtils.getStr(jo, "msg"), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(act, HoUtils.getStr(jo, "msg"), Toast.LENGTH_SHORT).show();
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


    public void getCommercialDetail() {
        HoUtils.showProgressDialog(act);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "getCommercialDetail");
                    mParam.put("cb_idx", idx);
                    result = conn.sendPost(mUrl, mParam);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    JSONObject data = new JSONObject(HoUtils.getStr(joList,"data"));
                                    if(type.equalsIgnoreCase("design")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub1"));
                                    }else if(type.equalsIgnoreCase("construction")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub2"));
                                    }else if(type.equalsIgnoreCase("material")){
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub3"));
                                    }else{
                                        binding.tvType.setText(HoUtils.getStr(data,"cb_type_sub4"));
                                    }
                                    binding.etTitle.setText(HoUtils.getStr(data,"cb_title"));
                                    binding.etSub.setText(HoUtils.getStr(data,"cb_sub"));
                                }else{
                                    Toast.makeText(act, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
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
