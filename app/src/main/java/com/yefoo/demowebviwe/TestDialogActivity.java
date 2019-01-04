package com.yefoo.demowebviwe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2019/1/4-0004.
 * 自定义TestDialog
 */

public class TestDialogActivity extends AppCompatActivity {
    private LinearLayout topLayout, bottomLayout;
    private static final int DURATION = 200;

    public static void show(Context context) {
        context.startActivity(new Intent(context, TestDialogActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
//        Utils.setFullScreen(this, true, true, false);
        setupStatusBar(getWindow());
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        topLayout.startAnimation(loadTopShowAnim());
        bottomLayout.startAnimation(loadBottomShowAnim());
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

    private void setupStatusBar(Window window) {
        try {
//            window.requestFeature(Window.FEATURE_NO_TITLE);

            if (Build.VERSION.SDK_INT >= 19) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= 16) {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else if (Build.VERSION.SDK_INT >= 23) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

//            if (Build.VERSION.SDK_INT >= 21) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.WHITE);
//                window.setNavigationBarColor(ContextCompat.getColor(context, R.color.black54));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismiss() {
        topLayout.startAnimation(loadTopHideAnim());
        bottomLayout.startAnimation(loadBottomHideAnim());
        topLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                topLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
                finish();
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

    public interface OnDismissListener {
        void onDismiss();
    }

}
