package com.technotapp.servicestation.entity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.MultiplePulse;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;
import com.technotapp.servicestation.R;

@DontObfuscate
public class

MyProgressDialog extends android.app.ProgressDialog {

    private ProgressBar progressBar;
    private TextView mTX_Message;

    public MyProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress_global);
        progressBar = findViewById(R.id.dialog_progress_loading_progress);
        mTX_Message = findViewById(R.id.dialog_progress_loading_txt_text);
        MultiplePulse multiplePulse = new MultiplePulse();
        progressBar.setIndeterminateDrawable(multiplePulse);
        setCancelable(false);
    }

    public void setMessageText(String message) {
        mTX_Message.setText(message);
    }
}
