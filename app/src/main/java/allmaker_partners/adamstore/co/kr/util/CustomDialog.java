package allmaker_partners.adamstore.co.kr.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import allmaker_partners.adamstore.co.kr.R;


public class CustomDialog extends Dialog {

    public CustomDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // No Title
        setContentView(R.layout.custom_dialog);

    }
}