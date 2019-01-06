package com.yefoo.demowebviwe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yefoo.demowebviwe.barlibrary.FlymeOSStatusBarFontUtils;
import com.yefoo.demowebviwe.barlibrary.OSUtils;

/**
 * Created by yufeng on 2019/1/5.
 */

public class StatusBar2 {

    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String MIUI_FORCE_FSG_NAV_BAR = "force_fsg_nav_bar";

    /**
     * 隐藏状态栏
     * Flag hide status bar bar hide.
     */
    public static final int FLAG_HIDE_STATUS_BAR = 0;
    /**
     * 隐藏导航栏
     * Flag hide navigation bar bar hide.
     */
    private static final int FLAG_HIDE_NAVIGATION_BAR = 1;
    /**
     * 隐藏状态栏和导航栏
     * Flag hide bar bar hide.
     */
    private static final int FLAG_HIDE_BAR = 2;
    /**
     * 显示状态栏和导航栏
     * Flag show bar bar hide.
     */
    public static final int FLAG_SHOW_BAR = 3;

    /**
     * 初始化状态栏和导航栏
     */
    public static void setBar(Activity activity, int statusBarColor, int navigationColor, boolean isDarkStatus, int hideBar) {
        Window window = activity.getWindow();
        View mDecorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //防止系统栏隐藏时内容区域大小发生变化
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
                //初始化5.0以上，包含5.0
                uiFlags = initBarAboveLOLLIPOP(activity, window, uiFlags, statusBarColor, navigationColor);
                //android 6.0以上设置状态栏字体为暗色
                uiFlags = setStatusBarDarkFont(uiFlags, isDarkStatus);
                //android 8.0以上设置导航栏图标为暗色
                uiFlags = setNavigationIconDark(uiFlags, isDarkStatus);
            } else {
                //初始化5.0以下，4.4以上沉浸式
                initBarBelowLOLLIPOP();
            }
            //隐藏状态栏或者导航栏
            uiFlags = hideBar(uiFlags, hideBar);
            //修正界面显示
            fitsWindows(activity, mDecorView);
            mDecorView.setSystemUiVisibility(uiFlags);
        }
//        if (OSUtils.isMIUI6Later()) {
//            //修改miui状态栏字体颜色
//            setMIUIBarDark(mWindow, MIUI_STATUS_BAR_DARK, mBarParams.statusBarDarkFont);
//            //修改miui导航栏图标为黑色
//            if (mBarParams.navigationBarEnable) {
//                setMIUIBarDark(mWindow, MIUI_NAVIGATION_BAR_DARK, mBarParams.navigationBarDarkIcon);
//            }
//        }
//        // 修改Flyme OS状态栏字体颜色
//        if (OSUtils.isFlymeOS4Later()) {
//            if (mBarParams.flymeOSStatusBarFontColor != 0) {
//                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
//            } else {
//                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.statusBarDarkFont);
//            }
//        }
    }

    /**
     * 初始化android 5.0以上状态栏和导航栏
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static int initBarAboveLOLLIPOP(Context context, Window mWindow, int uiFlags, int statusBarColor, int navigationBarColor) {
        //获得默认导航栏颜色
//        if (!mHasNavigationBarColor) {
//            mBarParams.defaultNavigationBarColor = mWindow.getNavigationBarColor();
//            mHasNavigationBarColor = true;
//        }
        //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
        //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
//        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
//        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //判断是否存在导航栏
        if (hasNavBar(context)) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //需要设置这个才能设置状态栏和导航栏颜色
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        mWindow.setStatusBarColor(statusBarColor);

        mWindow.setNavigationBarColor(navigationBarColor);
        return uiFlags;
    }

    @TargetApi(14)
    private static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar1((Activity) context)) {
                String key;
                if ((context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
                    key = NAV_BAR_HEIGHT_RES_NAME;
                } else {
                    key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }

    private static int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                int sizeOne = context.getResources().getDimensionPixelSize(resourceId);
                int sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId);

                if (sizeTwo >= sizeOne) {
                    return sizeTwo;
                } else {
                    float densityOne = context.getResources().getDisplayMetrics().density;
                    float densityTwo = Resources.getSystem().getDisplayMetrics().density;
                    return Math.round(sizeOne * densityTwo / densityOne);
                }
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }

    private static boolean hasNavBar(Context context) {
        return getNavigationBarHeight(context) > 0;
    }

    @TargetApi(14)
    private static boolean hasNavBar1(Activity activity) {
        //判断小米手机是否开启了全面屏,开启了，直接返回false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(activity.getContentResolver(), MIUI_FORCE_FSG_NAV_BAR, 0) != 0) {
                return false;
            }
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private static int setStatusBarDarkFont(int uiFlags, boolean statusBarDarkFont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && statusBarDarkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 设置导航栏图标亮色与暗色
     * Sets dark navigation icon.
     */
    private static int setNavigationIconDark(int uiFlags, boolean navigationBarDarkIcon) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && navigationBarDarkIcon) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private static void initBarBelowLOLLIPOP() {
        //透明状态栏
//        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //创建一个假的状态栏
//        setupStatusBarView();
//        //判断是否存在导航栏，是否禁止设置导航栏
//        if (mBarConfig.hasNavigationBar() || OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) {
//            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
//                //透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
//                mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            } else {
//                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            }
//            if (mNavigationBarHeight == 0) {
//                mNavigationBarHeight = mBarConfig.getNavigationBarHeight();
//            }
//            if (mNavigationBarWidth == 0) {
//                mNavigationBarWidth = mBarConfig.getNavigationBarWidth();
//            }
//            //创建一个假的导航栏
//            setupNavBarView();
//        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
//        View statusBarView = mDecorView.findViewById(IMMERSION_STATUS_BAR_VIEW);
//        if (statusBarView == null) {
//            statusBarView = new View(mActivity);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                    mBarConfig.getStatusBarHeight());
//            params.gravity = Gravity.TOP;
//            statusBarView.setLayoutParams(params);
//            statusBarView.setVisibility(View.VISIBLE);
//            statusBarView.setId(IMMERSION_STATUS_BAR_VIEW);
//            mDecorView.addView(statusBarView);
//        }
//        if (mBarParams.statusBarColorEnabled) {
//            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
//                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
//        } else {
//            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
//                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
//        }
    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private void setupNavBarView() {
//        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW);
//        if (navigationBarView == null) {
//            navigationBarView = new View(mActivity);
//            navigationBarView.setId(IMMERSION_NAVIGATION_BAR_VIEW);
//            mDecorView.addView(navigationBarView);
//        }
//
//        FrameLayout.LayoutParams params;
//        if (mBarConfig.isNavigationAtBottom()) {
//            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBarConfig.getNavigationBarHeight());
//            params.gravity = Gravity.BOTTOM;
//        } else {
//            params = new FrameLayout.LayoutParams(mBarConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
//            params.gravity = Gravity.END;
//        }
//        navigationBarView.setLayoutParams(params);
//        navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
//                mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));
//
//        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable && !mBarParams.hideNavigationBar) {
//            navigationBarView.setVisibility(View.VISIBLE);
//        } else {
//            navigationBarView.setVisibility(View.GONE);
//        }
    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    private static int hideBar(int uiFlags, int barHide) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (barHide) {
                case FLAG_HIDE_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.INVISIBLE;
                    break;
                case FLAG_HIDE_STATUS_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.INVISIBLE;
                    break;
                case FLAG_HIDE_NAVIGATION_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    break;
                case FLAG_SHOW_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
                    break;
                default:
                    break;
            }
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * 修正界面显示
     */
    private static void fitsWindows(Activity activity, View mDecorView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
            //android 5.0以上解决状态栏和布局重叠问题
            fitsWindowsAboveLOLLIPOP(activity, mDecorView);
        } else {
            //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
//            fitsWindowsBelowLOLLIPOP();
//            //解决华为emui3.1或者3.0导航栏手动隐藏的问题
//            if (!mIsFragment && OSUtils.isEMUI3_x()) {
//                fitsWindowsEMUI();
//            }
        }
    }

    /**
     * android 5.0以上解决状态栏和布局重叠问题
     * Fits windows above lollipop.
     */
    @RequiresApi(value = 16)
    private static void fitsWindowsAboveLOLLIPOP(Activity activity, View mDecorView) {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            return;
        }
        int top  = getStatusBarHeight(activity);

        ViewGroup mContentView = mDecorView.findViewById(android.R.id.content);
        mContentView.setPadding(0, top, 0, 0);
    }

    public static int getStatusBarHeight(Activity activity){
        return getInternalDimensionSize(activity, STATUS_BAR_HEIGHT_RES_NAME);
    }

    /**
     * 检查布局根节点是否使用了android:fitsSystemWindows="true"属性
     * Check fits system windows boolean.
     *
     * @param view the view
     * @return the boolean
     */
    @RequiresApi(value = 16)
    public static boolean checkFitsSystemWindows(View view) {
        if (view.getFitsSystemWindows()) {
            return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, count = viewGroup.getChildCount(); i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof DrawerLayout) {
                    if (checkFitsSystemWindows(childView)) {
                        return true;
                    }
                }
                if (childView.getFitsSystemWindows()) {
                    return true;
                }
            }
        }
        return false;
    }
}
