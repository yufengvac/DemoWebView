package com.yefoo.demowebviwe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yefoo.demowebviwe.barlibrary.ImmersionBar;

/**
 * Created by Administrator on 2019/1/4-0004.
 * 测试Dialog
 */

public class TestDialog extends Dialog {
    private LinearLayout topLayout, bottomLayout;
    private static final int DURATION = 2000;

    private Context mContext;

    public TestDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public TestDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public TestDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
//        Utils.setFullScreen(getWindow(), true, true, false);

//        if (Build.VERSION.SDK_INT >= 21) {
////            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(Color.YELLOW);
//            getWindow().setNavigationBarColor(Color.parseColor("#FF4081"));
//        }
//        hideSystemUI(getWindow().getDecorView());
//        fullscreen(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);


        findViewById(R.id.middle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        getWindow().getDecorView().setFitsSystemWindows(false);
    }

    private void fullscreen(boolean enable) {

        if (enable) { //显示状态栏

            WindowManager.LayoutParams lp = getWindow().getAttributes();

            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;

            getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else { //隐藏状态栏

            WindowManager.LayoutParams lp = getWindow().getAttributes();

            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().setAttributes(lp);


            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

    }

    //盖在状态栏上
    public void hideSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(layoutParams);
    }

    @Override
    public void show() {
//        Utils.setFullScreen(getWindow(), false, true, false);
        TestDialog.super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, DURATION);

        topLayout.startAnimation(loadTopShowAnim());
        bottomLayout.startAnimation(loadBottomShowAnim());
        getWindow().setGravity(Gravity.TOP);
        getWindow().setLayout(mContext.getResources().getDisplayMetrics().widthPixels, mContext.getResources().getDisplayMetrics().heightPixels);
//        ImmersionBar.with((Activity) mContext, this)
//                .titleBar(topLayout)
//                .barColor(R.color.colorPrimary)
//                .navigationBarColor(R.color.colorAccent)
//                .keyboardEnable(true)
//                .init();
    }

    @Override
    public void dismiss() {
        topLayout.startAnimation(loadTopHideAnim());
        bottomLayout.startAnimation(loadBottomHideAnim());
        topLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                TestDialog.super.dismiss();
            }
        }, DURATION);
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
}
