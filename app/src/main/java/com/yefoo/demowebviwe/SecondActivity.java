package com.yefoo.demowebviwe;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

    private boolean isFull = true;
    private boolean isBlackText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Utils.setFullScreen(this, true, true, false);
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
//                showDialog();
                TestDialogActivity.show(SecondActivity.this);
            }
        });
    }

    private void showDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("32").create().show();
        TestDialog testDialog = new TestDialog(this);
        testDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.setFullScreen(SecondActivity.this, true, true, true);
            }
        });
        testDialog.show();
    }
}
