package com.yefoo.demowebviwe;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

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

    private float initY, initScrollY;

    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity, mMinimumVelocity;
    private Scroller mScroller;
    private int mTouchSlop;
    private boolean isInControl = false;


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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
//                if (beginY == 0) {
//                    beginY = event.getY();
//                }
//                boolean isPullDown = false;
//                float curY = event.getY();
//                if (curY - beginY > 0) {
//                    isPullDown = true;
//                } else if (curY - beginY < 0) {
//                    isPullDown = false;
//                }

                float deltaY = event.getY() - initY - initScrollY;

//                scrollY += (curY - beginY);
//                beginY = curY;

                if (deltaY < 0) { //向上滚动

                    if (-deltaY < limitHeight) {
                        scrollTo(0, (int) -deltaY);
                        return true;
                    } else { //需要手动分发down事件
                        scrollTo(0, limitHeight);
                        initY = event.getY();
                        initScrollY = getScrollY();

                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                    }


                } else if (deltaY > 0) {//向下滚动

                    if (getScrollY() > 0) {
                        scrollTo(0, (int) (initScrollY - (event.getY() - initY)));
                        return true;
                    } else {
                        scrollTo(0, 0);
                        initY = event.getY();
                        initScrollY = getScrollY();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                beginY = 0;
                initY = event.getY();
                performClick();
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
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
                initY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - initY;

                BaseWebView baseWebView = getBaseWebView();
                if (baseWebView!= null && baseWebView.isTop() && dy > 0 && !isInControl){
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
//        Log.i(TAG, "onInterceptTouchEvent - >" + ev.getAction());
        if (baseWebView == null) {
            baseWebView = getBaseWebView();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                initY = ev.getY();
                initScrollY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                return isShouldIntercept(ev);
            case MotionEvent.ACTION_UP:
                initY = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isShouldIntercept(MotionEvent ev) {
        if (baseWebView == null) {
            baseWebView = getBaseWebView();
        }
        if (Math.abs(ev.getY() - initY) <= mTouchSlop) {
            return super.onInterceptTouchEvent(ev);
        }
        boolean isPullDown = false;
        if (ev.getY() - initY > 0) {
            isPullDown = true;
        } else if (ev.getY() - initY < 0) {
            isPullDown = false;
        }

        if ((!isPullDown && getScrollY() < limitHeight) || (isPullDown && getScrollY() > 0 && baseWebView != null && baseWebView.isTop())) {
//            if (isPullDown && getScrollY() > 0 && baseWebView != null && baseWebView.isTop()) {
                initY = ev.getY();
//            }
            Log.i(TAG, "onInterceptTouchEvent - >拦截");
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(ev);
            return true;
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


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, "onNestedFling , target->" + target + ",velocityY->" + velocityY + ",consumed->" + consumed);
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
