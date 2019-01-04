package com.yefoo.demowebviwe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/12/29-0029.
 * 工具类
 */

public class Utils {

    public static void setFullScreen(Activity activity, boolean transparentStatusBar, boolean blackStatusTextColor, boolean hideNavigationBar) {
        Window window = activity.getWindow();

        if (Build.VERSION.SDK_INT >= 19) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            if (Build.VERSION.SDK_INT >= 19) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (blackStatusTextColor) {
                        if (transparentStatusBar) {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                            } else {
//                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            }
                        } else {
                            if (!hideNavigationBar) {
//                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                                flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            } else {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            }
                        }

                    } else {
                        if (transparentStatusBar) {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        } else {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        }

                    }
                } else if (Build.VERSION.SDK_INT >= 19) {
                    if (blackStatusTextColor) {
                        if (transparentStatusBar) {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//黑色字体
                            } else {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            }
                        } else {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//黑色字体
                            }
                        }

                    } else {
                        if (transparentStatusBar) {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        } else {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        }

                    }
                }
            }

            window.getDecorView().setSystemUiVisibility(flag);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorAccent));
        }

//        if (Build.VERSION.SDK_INT >= 23) {
//            if (blackStatusTextColor) {
//                if (full) {
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
//                } else {
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//设置状态栏黑色字体
//                }
//            } else {
//                if (full) {
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//恢复状态栏白色字体
//                } else {
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                }
//            }
//        } else if (Build.VERSION.SDK_INT >= 21) {
//            if (blackStatusTextColor) {
//                window.setStatusBarColor(Color.parseColor("#66000000"));
//            } else {
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }
//
//        }
//
//        if (full) {
//            int flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            if (Build.VERSION.SDK_INT > 19) {
//                flag |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            }
//            window.getDecorView().setSystemUiVisibility(flag);
//        } else {
//            window.getDecorView().setSystemUiVisibility(View.VISIBLE);
//        }
    }

    public static void setFullScreen(Window window, boolean transparentStatusBar, boolean blackStatusTextColor, boolean hideNavigationBar) {

        if (Build.VERSION.SDK_INT >= 19) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            if (Build.VERSION.SDK_INT >= 19) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (blackStatusTextColor) {
                        if (transparentStatusBar) {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                            } else {
//                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            }
                        } else {
                            if (!hideNavigationBar) {
//                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//黑色字体
                                flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            } else {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            }
                        }

                    } else {
                        if (transparentStatusBar) {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        } else {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        }

                    }
                } else if (Build.VERSION.SDK_INT >= 19) {
                    if (blackStatusTextColor) {
                        if (transparentStatusBar) {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//黑色字体
                            } else {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            }
                        } else {
                            if (!hideNavigationBar) {
                                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//黑色字体
                            }
                        }

                    } else {
                        if (transparentStatusBar) {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        } else {
                            flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//白色字体
                        }

                    }
                }
            }

            window.getDecorView().setSystemUiVisibility(flag);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.parseColor("#FF4081"));
        }

    }
}
