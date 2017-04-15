package com.github.pedramrn.slick.parent.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ViewPager extends android.support.v4.view.ViewPager {
    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
