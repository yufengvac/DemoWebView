package com.yefoo.demowebviwe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.yefoo.demowebviwe.barlibrary.BarHide;
import com.yefoo.demowebviwe.barlibrary.ImmersionBar;

/**
 * DialogFragment 实现沉浸式的基类
 *
 * @author geyifeng
 * @date 2017 /8/26
 */
public abstract class BaseDialogFragment extends DialogFragment {
    protected OnDismissListener dismissListener;
    protected Activity mActivity;
    protected View mRootView;
    protected Window mWindow;
    protected static final int DURATION = 200;
    /**
     * 屏幕宽度
     */
    protected int mWidth;
    /**
     * 屏幕高度
     */
    protected int mHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    protected void dimissNow() {
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ImmersionBar.with(this).init();
//        StatusBar2.setBar(getActivity(), Color.WHITE, ContextCompat.getColor(getActivity(), R.color.black53), true, StatusBar2.FLAG_SHOW_BAR);
        if (hideStatusBar()) {
//            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).statusBarDarkFont(false).navigationBarColor(R.color.black53).keyboardEnable(true).init();
            StatusBar2.setBar(getActivity(), Color.TRANSPARENT, ContextCompat.getColor(getActivity(), R.color.black53), true, StatusBar2.FLAG_HIDE_STATUS_BAR);
        } else {
            ImmersionBar.with(this).statusBarDarkFont(true).statusBarColor(R.color.white).navigationBarColor(R.color.black53).keyboardEnable(true).init();
//            StatusBar2.setBar(getActivity(), ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.black53), true, StatusBar2.FLAG_SHOW_BAR);

        }
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected View findViewById(int resId) {
        return mRootView.findViewById(resId);
    }

    /**
     * 顶栏显示
     */
    protected final Animation loadTopShowAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.setFillAfter(false);
        return anim;
    }

    /**
     * 顶栏隐藏
     */
    protected final Animation loadTopHideAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.setFillAfter(false);
        return anim;
    }

    /**
     * 底栏显示
     */
    protected final Animation loadBottomShowAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.setFillAfter(false);
        return anim;
    }

    /**
     * 底栏隐藏
     */
    protected final Animation loadBottomHideAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.setFillAfter(false);
        return anim;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    protected abstract int getTopViewId();

    protected abstract void initView();

    protected abstract boolean hideStatusBar();

}
