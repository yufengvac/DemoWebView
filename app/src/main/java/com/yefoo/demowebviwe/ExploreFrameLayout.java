package com.yefoo.demowebviwe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by yufeng on 2018/12/20.
 * 的
 */

public class ExploreFrameLayout extends FrameLayout {
    private static final String TAG = ExploreFrameLayout.class.getSimpleName();
    private int limitHeight = 0;
    private BaseWebView baseWebView;

    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity, mMinimumVelocity;
    private Scroller mScroller;
    private int mTouchSlop;
    private boolean isInControl = false;
    private float lastY;
    private boolean isDragging;
    private boolean isTopHidden;


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

        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                lastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:

                float dy = y - lastY;
                if (!isDragging && Math.abs(dy) > mTouchSlop) {
                    isDragging = true;
                }
                if (isDragging) {
                    scrollBy(0, (int) -dy);
                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (getScrollY() == limitHeight && dy < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                        return super.onTouchEvent(event);
                    }
                }
                lastY = y;
                return true;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                performClick();
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
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
        int action = ev.getAction();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                float dy = y - lastY;

                BaseWebView baseWebView = getBaseWebView();
                if (baseWebView != null && baseWebView.isTop() && dy > 0 && !isInControl) {
                    isInControl = true;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    dispatchTouchEvent(ev);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, limitHeight);
        invalidate();
    }

    public void flingTop(int velocityY) {
        mScroller.fling(0, limitHeight, 0, velocityY, 0, 0, 0, limitHeight);
        invalidate();

    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > limitHeight) {
            y = limitHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
        isTopHidden = getScrollY() == limitHeight;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (baseWebView == null) {
            baseWebView = getBaseWebView();
        }
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - lastY;

                if (Math.abs(dy) > mTouchSlop) {
                    isDragging = true;
                    // 如果topView没有隐藏
                    // 或sc的scrollY = 0 && topView隐藏 && 下拉，则拦截
                    if (!isTopHidden || (baseWebView != null && baseWebView.isTop() && isTopHidden && dy > 0)) {
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        lastY = y;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                recycleVelocityTracker();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
}
