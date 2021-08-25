package allmaker_partners.adamstore.co.kr.activity;

import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.databinding.LayoutRequestBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;
import allmaker_partners.adamstore.co.kr.util.MultipartUtility;
import allmaker_partners.adamstore.co.kr.util.PhotoUtils;

public class ActRequest extends AppCompatActivity implements View.OnClickListener {
    LayoutRequestBinding binding;
    Activity act;
    String filePath;
    private Uri photoURI;
    File imgFile;
    final int GALLEY = 2;
    final int CROP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_request);
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
        binding.llFile.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvOk.setOnClickListener(this);
        filePath = "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_file:
                selectGallery();
                break;
            case R.id.tv_ok:
                if (HoUtils.isNull(AppUserData.getData(act, "idx"))) {
                    Toast.makeText(act, "로그인 후 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (HoUtils.isNull(binding.etTitle.getText().toString())) {
                    Toast.makeText(act, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (HoUtils.isNull(binding.etSub.getText().toString())) {
                    Toast.makeText(act, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    setRequest();
                }
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
                        filePath = full_path;
                        binding.tvFileName.setText(full_path);
                    }
                }
                break;
        }
    }


    public void setRequest() {
        HoUtils.showProgressDialog(act);
        final String charset = "UTF-8";
        final String mUrl = JsonUrl.CONTROL;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MultipartUtility multipart = new MultipartUtility(mUrl, charset);
                    multipart.addFormField("dbControl", "setPQna");
                    //multipart.addFormField("m_idx", AppUserData.getData(act, "idx"));
                    multipart.addFormField("m_idx", AppUserData.getData(act,"idx"));
                    multipart.addFormField("b_title", binding.etTitle.getText().toString());
                    multipart.addFormField("b_contents", binding.etSub.getText().toString());

                    if (!HoUtils.isNull(filePath)) {
                        multipart.addFilePart("m_photo", new File(filePath));
                    }

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

}
