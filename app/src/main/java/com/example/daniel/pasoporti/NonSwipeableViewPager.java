package com.example.daniel.pasoporti;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Daniel on 9/23/2017.
 */

public class NonSwipeableViewPager extends ViewPager {
    private boolean isPagingEnabled;
    public Context context;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
        this.context = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isPagingEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPagingEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void setPagingEnabled(boolean b) {
        isPagingEnabled = b;
    }

    public boolean getPagingEnabled(){ return isPagingEnabled;}


}
