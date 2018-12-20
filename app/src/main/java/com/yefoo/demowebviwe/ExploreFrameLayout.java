package com.yefoo.demowebviwe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by yufeng on 2018/12/20.
 * 的
 */

public class ExploreFrameLayout extends FrameLayout {
    private static final String TAG = ExploreFrameLayout.class.getSimpleName();
    private int limitHeight = 0;
    private float scrollY = 0;
    private float beginY = 0;
    private BaseWebView baseWebView;
    private float firstY;

    public ExploreFrameLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ExploreFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExploreFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void init(Context context) {
        limitHeight = dip2px(context, 200);
        baseWebView = getBaseWebView();
    }

    private BaseWebView getBaseWebView() {
        int childCount = getChildCount();
        if (childCount >= 2) {
            View view = getChildAt(1);
            return (BaseWebView) view;
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent - >" + event.getAction() + ", scrollY=" + scrollY + ", event.getY=" + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (beginY == 0) {
                    beginY = event.getY();
                }
                float curY = event.getY();
                if (curY - beginY > 0) {
                } else if (curY - beginY < 0) {

                }

                scrollY += (curY - beginY);
                beginY = curY;

                if (scrollY > -limitHeight) {
                    scrollTo(0, (int) -scrollY);
                } else if (scrollY <= -limitHeight && scrollY < 0) {
                    scrollY = -limitHeight;
                    scrollTo(0, (int) -scrollY);
                    break;
                } else if (scrollY >= 0) {
                    scrollY = 0;
                    scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                beginY = 0;
                performClick();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent - >" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent - >" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
//        if (baseWebView == null) {
//            baseWebView = getBaseWebView();
//        }
//        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
//            firstY = 0;
//        }
//
//        boolean isPullDown = false;
//        if (firstY == 0) {
//            firstY = ev.getY();
//        } else {
//            float curY = ev.getY();
//            isPullDown = curY - firstY > 0;
//            firstY = curY;
//        }
//        Log.e(TAG, "scrollY=" + scrollY);
//        if (baseWebView != null && baseWebView.isTop() && isPullDown) {
//            Log.e(TAG, "1111111111111111");
//            return ev.getAction() != MotionEvent.ACTION_DOWN;
//        }
//        if (scrollY <= -limitHeight || (scrollY >= 0 && isPullDown)) {
//            Log.e(TAG, "222222222222222");
//            return super.onInterceptTouchEvent(ev);
//        }
//        Log.e(TAG, "333333333333333");
//        return ev.getAction() != MotionEvent.ACTION_DOWN;
    }

}
