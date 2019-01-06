package com.yefoo.demowebviwe;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.yefoo.demowebviwe.barlibrary.FitsKeyboard;
import com.yefoo.demowebviwe.barlibrary.FlymeOSStatusBarFontUtils;
import com.yefoo.demowebviwe.barlibrary.OSUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yufeng on 2019/1/5.
 * 状态栏工具类
 */

public class StatusBar {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String MIUI_FORCE_FSG_NAV_BAR = "force_fsg_nav_bar";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";

    private static final int IMMERSION_STATUS_BAR_VIEW = R.id.immersion_status_bar_view;
    private static final int IMMERSION_NAVIGATION_BAR_VIEW = R.id.immersion_navigation_bar_view;
    private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";
    private static final String MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    private static final String MIUI_NAVIGATION_BAR_DARK = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE";


    private Activity mActivity;
    private Fragment mFragment;
    private Window mWindow;
    private ViewGroup mDecorView;
    private ViewGroup mContentView;

    public enum BarHide {
        /**
         * 隐藏状态栏
         * Flag hide status bar bar hide.
         */
        FLAG_HIDE_STATUS_BAR,
        /**
         * 隐藏导航栏
         * Flag hide navigation bar bar hide.
         */
        FLAG_HIDE_NAVIGATION_BAR,
        /**
         * 隐藏状态栏和导航栏
         * Flag hide bar bar hide.
         */
        FLAG_HIDE_BAR,
        /**
         * 显示状态栏和导航栏
         * Flag show bar bar hide.
         */
        FLAG_SHOW_BAR
    }


    /**
     * 沉浸式名字
     */
    private String mImmersionBarName;
    /**
     * 导航栏的高度，适配Emui3系统有用
     */
    private int mNavigationBarHeight = 0;
    /**
     * 导航栏的宽度，适配Emui3系统有用
     */
    private int mNavigationBarWidth = 0;
    /**
     * 是否是在Activity使用的沉浸式
     */
    private boolean mIsFragment = false;
    /**
     * Emui系统导航栏监听器
     */
    private ContentObserver mNavigationObserver = null;
    private FitsKeyboard mFitsKeyboard = null;
    /**
     * 是否适配过布局与导航栏重叠了
     */
    private boolean mIsFitsLayoutOverlap = false;

    /**
     * 是否已经获取到当前导航栏颜色了
     */
    private boolean mHasNavigationBarColor = false;


    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;

    private int statusBarColor = Color.TRANSPARENT;
    private int navigationBarColor = Color.BLACK;
    private float statusBarAlpha = 0f;
    private float navigationBarAlpha = 0f;
    private boolean fullScreen;
    private boolean statusBarDarkFont = false;
    private boolean navigationBarDarkIcon = false;
    private BarHide barHide = BarHide.FLAG_SHOW_BAR;

    private boolean hideNavigationBar = false;
    int defaultNavigationBarColor = Color.BLACK;
    /**
     * flymeOS状态栏字体变色
     * The Flyme os status bar font color.
     */
    @ColorInt
    private int flymeOSStatusBarFontColor;

    /**
     * 在Activit里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    public StatusBar(Activity activity) {

        mActivity = activity;
        mWindow = mActivity.getWindow();

        mImmersionBarName = mActivity.toString();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    public StatusBar(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private StatusBar(Activity activity, Fragment fragment) {
        mActivity = activity;
        mFragment = fragment;
        if (mActivity == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }

        mIsFragment = true;
        mWindow = mActivity.getWindow();

        mImmersionBarName = activity.toString() + fragment.toString();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }


    /**
     * 判断手机支不支持状态栏字体变色
     * Is support status bar dark font boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportStatusBarDarkFont() {
        return OSUtils.isMIUI6Later() || OSUtils.isFlymeOS4Later()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * 透明状态栏，默认透明
     *
     * @return the immersion bar
     */
    public StatusBar transparentStatusBar() {
        statusBarColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * 透明导航栏，默认黑色
     *
     * @return the immersion bar
     */
    public StatusBar transparentNavigationBar() {
        navigationBarColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * 透明状态栏和导航栏
     *
     * @return the immersion bar
     */
    public StatusBar transparentBar() {
        statusBarColor = Color.TRANSPARENT;
        navigationBarColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public StatusBar statusBarColor(@ColorRes int statusBarColor) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor));
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public StatusBar statusBarColor(@ColorRes int statusBarColor, @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor), alpha);
    }

    /**
     * 状态栏颜色
     * Status bar color int immersion bar.
     *
     * @param statusBarColor the status bar color
     * @return the immersion bar
     */
    public StatusBar statusBarColor(String statusBarColor) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor));
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public StatusBar statusBarColor(String statusBarColor,
                                    @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor), alpha);
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public StatusBar statusBarColorInt(@ColorInt int statusBarColor) {
        this.statusBarColor = statusBarColor;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public StatusBar statusBarColorInt(@ColorInt int statusBarColor, @FloatRange(from = 0f, to = 1f) float alpha) {
        this.statusBarColor = statusBarColor;
        this.statusBarAlpha = alpha;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public StatusBar navigationBarColor(@ColorRes int navigationBarColor) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor));
    }


    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public StatusBar navigationBarColorInt(@ColorInt int navigationBarColor) {
        this.navigationBarColor = navigationBarColor;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public StatusBar barColor(@ColorRes int barColor) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor));
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public StatusBar barColor(String barColor) {
        return this.barColorInt(Color.parseColor(barColor));
    }


    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public StatusBar barColorInt(@ColorInt int barColor) {
        statusBarColor = barColor;
        navigationBarColor = barColor;
        return this;
    }


    /**
     * 有导航栏的情况下，Activity是否全屏显示
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    public StatusBar fullScreen(boolean isFullScreen) {
        fullScreen = isFullScreen;
        return this;
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    public StatusBar statusBarDarkFont(boolean isDarkFont) {
        return statusBarDarkFont(isDarkFont, 0f);
    }

    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha 如果不支持状态栏字体变色可以使用statusAlpha来指定状态栏透明度，比如白色状态栏的时候可以用到
     * @return the immersion bar
     */
    public StatusBar statusBarDarkFont(boolean isDarkFont, @FloatRange(from = 0f, to = 1f) float statusAlpha) {
        statusBarDarkFont = isDarkFont;
        if (!isDarkFont) {
            flymeOSStatusBarFontColor = 0;
        }
        if (isSupportStatusBarDarkFont()) {
            statusBarAlpha = 0;
        } else {
            statusBarAlpha = statusAlpha;
        }
        return this;
    }

    /**
     * 导航栏图标深色或亮色，只支持android o以上版本
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon the is dark icon
     * @return the immersion bar
     */
    public StatusBar navigationBarDarkIcon(boolean isDarkIcon) {
        return navigationBarDarkIcon(isDarkIcon, 0f);
    }

    /**
     * 导航栏图标深色或亮色，只支持android o以上版本，判断设备支不支持导航栏图标变色来设置导航栏透明度
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon      the is dark icon
     * @param navigationAlpha the navigation alpha 如果不支持导航栏图标变色可以使用navigationAlpha来指定导航栏透明度，比如白色导航栏的时候可以用到
     * @return the immersion bar
     */
    public StatusBar navigationBarDarkIcon(boolean isDarkIcon, @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        navigationBarDarkIcon = isDarkIcon;
        if (isSupportNavigationIconDark()) {
            navigationBarAlpha = 0;
        } else {
            navigationBarAlpha = navigationAlpha;
        }
        return this;
    }

    /**
     * 隐藏导航栏或状态栏
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    public StatusBar hideBar(BarHide barHide) {
        this.barHide = barHide;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_1()) {
            if ((this.barHide == BarHide.FLAG_HIDE_NAVIGATION_BAR) ||
                    (this.barHide == BarHide.FLAG_HIDE_BAR)) {
                hideNavigationBar = true;
            } else {
                hideNavigationBar = false;
            }
        }
        return this;
    }

    /**
     * 通过上面配置后初始化后方可成功调用
     */
    public void setup() {
        //设置沉浸式
        setBar();
    }


    /**
     * 初始化状态栏和导航栏
     */
    private void setBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //防止系统栏隐藏时内容区域大小发生变化
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
                //适配刘海屏
                fitsNotchScreen();
                //初始化5.0以上，包含5.0
                uiFlags = initBarAboveLOLLIPOP(uiFlags);
                //android 6.0以上设置状态栏字体为暗色
                uiFlags = setStatusBarDarkFont(uiFlags);
                //android 8.0以上设置导航栏图标为暗色
                uiFlags = setNavigationIconDark(uiFlags);
            } else {
                //初始化5.0以下，4.4以上沉浸式
                initBarBelowLOLLIPOP();
            }
            //隐藏状态栏或者导航栏
            uiFlags = hideBar(uiFlags);
            //修正界面显示
            fitsWindows();
            mDecorView.setSystemUiVisibility(uiFlags);
        }
        if (OSUtils.isMIUI6Later()) {
            //修改miui状态栏字体颜色
            setMIUIBarDark(mWindow, MIUI_STATUS_BAR_DARK, statusBarDarkFont);
            //修改miui导航栏图标为黑色
            setMIUIBarDark(mWindow, MIUI_NAVIGATION_BAR_DARK, navigationBarDarkIcon);
        }
        // 修改Flyme OS状态栏字体颜色
        if (OSUtils.isFlymeOS4Later()) {
            if (flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, flymeOSStatusBarFontColor);
            } else {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, statusBarDarkFont);
            }
        }
    }

    /**
     * 适配刘海屏
     * Fits notch screen.
     */
    private void fitsNotchScreen() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !mIsFitsNotch) {
//            WindowManager.LayoutParams lp = mWindow.getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            mWindow.setAttributes(lp);
//            mIsFitsNotch = true;
//        }
    }

    /**
     * 初始化android 5.0以上状态栏和导航栏
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int initBarAboveLOLLIPOP(int uiFlags) {
        //获得默认导航栏颜色
        if (!mHasNavigationBarColor) {
            defaultNavigationBarColor = mWindow.getNavigationBarColor();
            mHasNavigationBarColor = true;
        }
        //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (fullScreen) {
            //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //判断是否存在导航栏
        if (getNavigationBarHeight(mActivity) > 0) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //需要设置这个才能设置状态栏和导航栏颜色
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        mWindow.setStatusBarColor(statusBarColor);

        //设置导航栏颜色
        mWindow.setNavigationBarColor(navigationBarColor);

        return uiFlags;
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private void initBarBelowLOLLIPOP() {
        //透明状态栏
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //创建一个假的状态栏
        setupStatusBarView();
        //判断是否存在导航栏，是否禁止设置导航栏
        if (getNavigationBarHeight(mActivity) > 0 || OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) {
            //透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            if (mNavigationBarHeight == 0) {
                mNavigationBarHeight = getNavigationBarHeight(mActivity);
            }
            if (mNavigationBarWidth == 0) {
                mNavigationBarWidth = getNavigationBarWidth(mActivity);
            }
            //创建一个假的导航栏
            setupNavBarView();
        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
        View statusBarView = mDecorView.findViewById(IMMERSION_STATUS_BAR_VIEW);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(mActivity));
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(IMMERSION_STATUS_BAR_VIEW);
            mDecorView.addView(statusBarView);
        }
        statusBarView.setBackgroundColor(statusBarColor);

    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private void setupNavBarView() {
        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW);
        if (navigationBarView == null) {
            navigationBarView = new View(mActivity);
            navigationBarView.setId(IMMERSION_NAVIGATION_BAR_VIEW);
            mDecorView.addView(navigationBarView);
        }

        FrameLayout.LayoutParams params;
        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getNavigationBarHeight(mActivity));
        params.gravity = Gravity.BOTTOM;

        navigationBarView.setLayoutParams(params);
        navigationBarView.setBackgroundColor(navigationBarColor);

        navigationBarView.setVisibility(View.VISIBLE);

    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    public int hideBar(int uiFlags) {
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
    @RequiresApi(api = 16)
    private void fitsWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
            //android 5.0以上解决状态栏和布局重叠问题
            fitsWindowsAboveLOLLIPOP();
        } else {
            //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
            fitsWindowsBelowLOLLIPOP();
            //解决华为emui3.1或者3.0导航栏手动隐藏的问题
            if (!mIsFragment && OSUtils.isEMUI3_x()) {
                fitsWindowsEMUI();
            }
        }
    }

    /**
     * android 5.0以上解决状态栏和布局重叠问题
     * Fits windows above lollipop.
     */
    @RequiresApi(api = 16)
    private void fitsWindowsAboveLOLLIPOP() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            return;
        }
        int top = getStatusBarHeight(mActivity);

        if (mContentView != null) {
            mContentView.setPadding(mContentView.getLeft(), top, mContentView.getRight(), mContentView.getBottom());
        }
    }

    /**
     * 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
     * Fits windows below lollipop.
     */
    @RequiresApi(api = 16)
    private void fitsWindowsBelowLOLLIPOP() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            return;
        }
        int top = getStatusBarHeight(mActivity);

        if (mContentView != null) {
            mContentView.setPadding(mContentView.getLeft(), top, mContentView.getRight(), mContentView.getBottom());
        }
    }

    /**
     * 检查布局根节点是否使用了android:fitsSystemWindows="true"属性
     * Check fits system windows boolean.
     *
     * @param view the view
     * @return the boolean
     */
    @RequiresApi(api = 16)
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

    /**
     * 注册emui3.x导航栏监听函数
     * Register emui 3 x.
     */
    @RequiresApi(api = 16)
    private void fitsWindowsEMUI() {
        final View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW);
        if (navigationBarView != null && mNavigationObserver == null) {
            mNavigationObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    int bottom = mContentView.getPaddingBottom(), right = mContentView.getPaddingRight();
                    if (mActivity != null && mActivity.getContentResolver() != null) {
                        int navigationBarIsMin = Settings.System.getInt(mActivity.getContentResolver(),
                                NAVIGATIONBAR_IS_MIN, 0);
                        if (navigationBarIsMin == 1) {
                            //导航键隐藏了
                            navigationBarView.setVisibility(View.GONE);
                            bottom = 0;
                            right = 0;
                        } else {
                            //导航键显示了
                            navigationBarView.setVisibility(View.VISIBLE);
                            if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
                                bottom = 0;
                                right = 0;
                            } else {
                                if (mNavigationBarHeight == 0) {
                                    mNavigationBarHeight = getNavigationBarHeight(mActivity);
                                }
                                if (mNavigationBarWidth == 0) {
                                    mNavigationBarWidth = getNavigationBarWidth(mActivity);
                                }
                                if (!hideNavigationBar) {
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) navigationBarView.getLayoutParams();

                                    params.gravity = Gravity.BOTTOM;
                                    params.height = mNavigationBarHeight;
                                    bottom = !fullScreen ? mNavigationBarHeight : 0;
                                    right = 0;

                                    navigationBarView.setLayoutParams(params);
                                }
                            }
                        }
                    }
                    if (mContentView != null) {
                        mContentView.setPadding(0, mContentView.getPaddingTop(), right, bottom);
                    }

                }
            };
            if (mActivity != null && mActivity.getContentResolver() != null && mNavigationObserver != null) {
                mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor
                        (NAVIGATIONBAR_IS_MIN), true, mNavigationObserver);
            }
        }

    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private int setStatusBarDarkFont(int uiFlags) {
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
    private int setNavigationIconDark(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && navigationBarDarkIcon) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            return uiFlags;
        }
    }

    @SuppressLint("PrivateApi")
    private void setMIUIBarDark(Window window, String key, boolean dark) {
        if (window != null) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField(key);
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception ignored) {

            }
        }
    }

    private static boolean isSupportNavigationIconDark() {
        return OSUtils.isMIUI6Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 解决顶部与布局重叠问题
     * Sets fits system windows.
     *
     * @param activity the activity
     */
    public static void setFitsSystemWindows(Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (childView instanceof DrawerLayout) {
                    continue;
                }
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }


    /**
     * 隐藏状态栏
     * Hide status bar.
     *
     * @param window the window
     */
    public static void hideStatusBar(@NonNull Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @TargetApi(14)
    private int getNavigationBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    key = NAV_BAR_HEIGHT_RES_NAME;
                } else {
                    key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarWidth(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                return getInternalDimensionSize(context, NAV_BAR_WIDTH_RES_NAME);
            }
        }
        return result;
    }

    private int getStatusBarHeight(Context context) {
        return getInternalDimensionSize(context, STATUS_BAR_HEIGHT_RES_NAME);
    }

    @TargetApi(14)
    private boolean hasNavBar(Activity activity) {
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

    private int getInternalDimensionSize(Context context, String key) {
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

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
