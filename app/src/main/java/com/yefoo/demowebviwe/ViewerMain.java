package com.yefoo.demowebviwe;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by yufeng on 2019/1/5.
 * 基础菜单
 */

public class ViewerMain extends BaseDialogFragment {

    private LinearLayout topLayout;
    private LinearLayout bottomLayout;
    private OnMenuClickListener onMenuClickListener;

    public void show(FragmentManager manager) {
        super.show(manager, ViewerMain.class.getSimpleName());
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_dialog;
    }

    @Override
    protected void initView() {
        topLayout = (LinearLayout) findViewById(R.id.top_layout);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        topLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.setBackgroundColor(Color.WHITE);
        topLayout.startAnimation(loadTopShowAnim());
        bottomLayout.startAnimation(loadBottomShowAnim());
        findViewById(R.id.middle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimissNow();
                if (onMenuClickListener != null) {
                    onMenuClickListener.onFontClick();
                }
            }
        });
    }

    @Override
    protected boolean hideStatusBar() {
        return false;
    }

    @Override
    protected int getTopViewId() {
        return R.id.top_layout;
    }

    @Override
    public void dismiss() {
        topLayout.startAnimation(loadTopHideAnim());
        bottomLayout.startAnimation(loadBottomHideAnim());
        bottomLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewerMain.super.dismiss();
            }
        }, DURATION);
    }

    public interface OnMenuClickListener {
        void onFontClick();
    }
}
