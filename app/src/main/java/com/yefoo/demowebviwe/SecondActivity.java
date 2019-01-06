package com.yefoo.demowebviwe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yefoo.demowebviwe.barlibrary.BarHide;
import com.yefoo.demowebviwe.barlibrary.ImmersionBar;

public class SecondActivity extends AppCompatActivity {

    private boolean isFull = true;
    private boolean isBlackText = true;

    private ViewerMain viewerMain;
    private ViewerMainFont viewerMainFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        Utils.setFullScreen(this, true, true, false);
        ImmersionBar.with(this).keyboardEnable(true).init();
        StatusBar2.setBar(this, Color.RED, ContextCompat.getColor(this, R.color.black53), true, StatusBar2.FLAG_SHOW_BAR);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isFull = !isFull;
                Utils.setFullScreen(SecondActivity.this, true, true, true);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isBlackText = !isBlackText;
                Utils.setFullScreen(SecondActivity.this, false, true, false);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
//                TestDialogActivity.show(SecondActivity.this);
            }
        });
    }

    private void showDialog() {

//        final TestDialog testDialog = new TestDialog(this);
//        testDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                Utils.setFullScreen(SecondActivity.this, true, true, true);
//            }
//        });
//        ImmersionBar.with(this, testDialog)
//                .barColor(R.color.colorPrimary)
//                .navigationBarColor(R.color.colorAccent)
//                .keyboardEnable(true)
//                .init();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                testDialog.show();
//            }
//        }, 100);
//        testDialog.show();
        if (viewerMain == null) {
            viewerMain = new ViewerMain();
            viewerMain.setOnMenuClickListener(new ViewerMain.OnMenuClickListener() {
                @Override
                public void onFontClick() {
                    showMenuFont();
                }
            });
            viewerMain.setOnDismissListener(new BaseDialogFragment.OnDismissListener() {
                @Override
                public void onDismiss() {
//                    Utils.setFullScreen(SecondActivity.this, true, true, true);
//                    StatusBar2.setBar(SecondActivity.this, Color.RED, ContextCompat.getColor(SecondActivity.this, R.color.black53), true, StatusBar2.FLAG_HIDE_STATUS_BAR);
                    ImmersionBar.with(SecondActivity.this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                }
            });
        }
        viewerMain.show(getSupportFragmentManager());
    }

    private void showMenuFont() {
        if (viewerMainFont == null) {
            viewerMainFont = new ViewerMainFont();
            viewerMainFont.setOnDismissListener(new BaseDialogFragment.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Utils.setFullScreen(SecondActivity.this, true, true, true);
                }
            });
        }
        viewerMainFont.show(getSupportFragmentManager());
    }

}
