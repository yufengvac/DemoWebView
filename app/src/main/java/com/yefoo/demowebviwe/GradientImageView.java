package com.yefoo.demowebviwe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/12/29-0029.
 * 自定义渐变图片
 */

public class GradientImageView extends AppCompatImageView {

    private Bitmap bitmap;
    private Paint paint;
    private Bitmap gradientBitmap;
    private boolean hasInitGraBitmap = false;

    public GradientImageView(Context context) {
        this(context, null);
    }

    public GradientImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GradientImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    private void intiBitmap() {
        if (bitmap == null) {
            bitmap = ((BitmapDrawable) (getDrawable())).getBitmap();
        }
        if (bitmap == null) {
            return;
        }
        try {
            Palette.Builder builder = Palette.from(bitmap);
            Palette palette = builder.generate();

            final int color = Color.parseColor("#545A65");
            final int darkMutedColor = palette.getDarkMutedColor(color);
            final int red = Color.red(darkMutedColor);
            final int green = Color.green(darkMutedColor);
            final int blue = Color.blue(darkMutedColor);
            int resultColor = Color.argb(243, red, green, blue);
            if (green < 25 && blue < 25 && red < 25) {
                resultColor = color;
            }
            createLinearGradient(resultColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLinearGradient(int darkColor) {
        int bgColors[] = new int[2];
        bgColors[0] = colorBurn(darkColor);
        bgColors[1] = Color.TRANSPARENT;
        gradientBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight() / 2, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(gradientBitmap);
        LinearGradient gradient = new LinearGradient(0, 0, 0, gradientBitmap.getHeight(), bgColors[1], bgColors[0], Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        RectF rectF = new RectF(0, 0, gradientBitmap.getWidth(), gradientBitmap.getHeight());
        canvas.drawRect(rectF, paint);

        invalidate();
        hasInitGraBitmap = true;
    }

    /**
     * 在此颜色基础上获取更深一点的颜色
     *
     * @param color 当前颜色
     * @return 深色
     */
    private int colorBurn(int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gradientBitmap != null) {
            canvas.drawBitmap(gradientBitmap, 0, getMeasuredHeight() / 2, paint);
        } else if (!hasInitGraBitmap) {
            intiBitmap();
        }
    }
}
