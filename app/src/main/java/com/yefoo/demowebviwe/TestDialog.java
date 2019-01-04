package com.yefoo.demowebviwe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2019/1/4-0004.
 * 测试Dialog
 */

public class TestDialog extends Dialog {
    private LinearLayout topLayout, bottomLayout;

    public TestDialog(@NonNull Context context) {
        super(context);
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
        Utils.setFullScreen(getWindow(), true, true, false);
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        findViewById(R.id.middle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
        Utils.setFullScreen(getWindow(), false, true, false);
        super.show();
//        topLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {

                topLayout.startAnimation(loadTopShowAnim());
                bottomLayout.startAnimation(loadBottomShowAnim());
//            }
//        }, 100);
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
        }, 200);
    }

    /**
     * 顶栏显示
     */
    protected final Animation loadTopShowAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(200);
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
        anim.setDuration(200);
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
        anim.setDuration(200);
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
        anim.setDuration(200);
        anim.setFillAfter(false);
        return anim;
    }
}
