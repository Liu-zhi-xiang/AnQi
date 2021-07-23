package com.motorbike.anqi.util;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 控制ViewPager是否可以滑动
 * Created by liujing on 2016/6/22.
 */
public class ScrollViewPager extends ViewPager {
    /**默认为true表示可以滑动，false表示不滑动*/
    private boolean canScroll;

    public ScrollViewPager(Context context) {
        this(context, null);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        canScroll = true;
    }

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
}
