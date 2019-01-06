package com.yefoo.demowebviwe;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by yufeng on 2019/1/5.
 * 基础菜单
 */

public class ViewerMainFont extends BaseDialogFragment {

    private LinearLayout bottomLayout;
    private OnMenuClickListener onMenuClickListener;

    public void show(FragmentManager manager) {
        super.show(manager, ViewerMainFont.class.getSimpleName());
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_dialog_font;
    }

    @Override
    protected void initView() {
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        bottomLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.startAnimation(loadBottomShowAnim());
        findViewById(R.id.middle_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        bottomLayout.startAnimation(loadBottomHideAnim());
        bottomLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewerMainFont.super.dismiss();
            }
        }, DURATION);
    }

    public interface OnMenuClickListener {
        void onFontClick();
    }
}
