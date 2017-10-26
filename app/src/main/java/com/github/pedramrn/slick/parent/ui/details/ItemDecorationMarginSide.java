package com.github.pedramrn.slick.parent.ui.details;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.pedramrn.slick.parent.R;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-19
 */

public class ItemDecorationMarginSide extends RecyclerView.ItemDecoration {

    private int halfSpace;

    public ItemDecorationMarginSide(int space) {
        this.halfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        /*if (parent.getPaddingLeft() != halfSpace) {
            parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
            parent.setClipToPadding(false);
        }*/

        int margin = view.getResources().getDimensionPixelSize(R.dimen.margin_2dp);
        outRect.top = margin;
        outRect.bottom = margin;
        outRect.left = halfSpace;
        outRect.right = halfSpace;
    }
}