package allmaker_partners.adamstore.co.kr.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration{

    private int horizontalMarginInPx;

    public HorizontalMarginItemDecoration(float hoPx){
        horizontalMarginInPx = (int) hoPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = horizontalMarginInPx;
        outRect.left = horizontalMarginInPx;
    }
}
