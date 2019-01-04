package com.yefoo.demowebviwe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView shadomIv;
    private ImageView bottomIV;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WebView webView = findViewById(R.id.webView);
//        webView.loadUrl("http://www.baidu.com");
//
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) webView.getLayoutParams();
//        layoutParams.height = getResources().getDisplayMetrics().heightPixels - getStateBar();
//        webView.setLayoutParams(layoutParams);

        final GradientImageView gradientImageView = findViewById(R.id.gradientImageView);

        shadomIv = findViewById(R.id.shadow_bg);
        bottomIV = findViewById(R.id.shadow_bottom);
        iv = findViewById(R.id.iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        intiBitmap(gradientImageView);
    }

    private void intiBitmap(ImageView imageView) {

        Bitmap bitmap =  ((BitmapDrawable)(imageView.getDrawable())).getBitmap();
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                final int color = Color.parseColor("#545A65");
                final int darkMutedColor = palette.getDarkMutedColor(color);
                final int red = Color.red(darkMutedColor);
                final int green = Color.green(darkMutedColor);
                final int blue = Color.blue(darkMutedColor);
                int resultColor = Color.argb(243, red, green, blue);
                if (green < 25 && blue < 25 && red < 25) {
                    resultColor = color;
                }

                int bgColors[] = new int[2];
                bgColors[0] = colorBurn(resultColor);
                bgColors[1] = Color.TRANSPARENT;

                int[] colors = new int[]{bgColors[0], bgColors[1]};
                GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
                shadomIv.setBackgroundDrawable(drawable);

                bottomIV.setBackgroundColor(bgColors[0]);
                iv.setBackgroundColor(bgColors[0]);
            }
        });

    }

    private int colorlight(int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.argb(0, red, green, blue);
    }

    private int colorBurn(int color) {
        int alpha = color >> 24;
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.argb(255,red, green, blue);
    }

    private int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDaoHangHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }
}
