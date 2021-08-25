package allmaker_partners.adamstore.co.kr.util;

import android.content.Context;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import allmaker_partners.adamstore.co.kr.R;

public class CustomTransformer implements ViewPager2.PageTransformer {

    public static final float MAX_SCALE = 1.0f;
    public static final float MIN_SCALE = 0.9f;
    Context ctx;

    public CustomTransformer (Context context){
        this.ctx = context;
    }

    @Override
    public void transformPage(View page, float position) {
        float pageMarginPx = ctx.getResources().getDimensionPixelOffset(R.dimen.dp20);
        float offsetPx = ctx.getResources().getDimensionPixelOffset(R.dimen.dp36);
        float offset = position * -(2 * offsetPx + pageMarginPx);

        if (ViewCompat.getLayoutDirection(page) == ViewCompat.LAYOUT_DIRECTION_RTL) {
            page.setTranslationX(-offset);
        } else {
            page.setTranslationX(offset);
        }

    }
}

