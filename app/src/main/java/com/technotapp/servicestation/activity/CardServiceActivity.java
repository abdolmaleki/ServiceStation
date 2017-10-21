package com.technotapp.servicestation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pax.gl.pack.impl.PaxGLPacker;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.socket.ISocketCallback;
import com.technotapp.servicestation.connection.socket.SocketEngine;

public class CardServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_service);

        loadSetting();

        loadData();

        bindView();

    }

    private void bindView() {
    }

    private void loadData() {

    }

    private void loadSetting() {

    }

    private byte[] getPacker() {
        byte[] result = new byte[0];
        PaxGLPacker pa = PaxGLPacker.getInstance(this);
        try {
            //transaction 0200
            pa.getIso8583().getEntity().resetAllFieldsValue();
            pa.getIso8583().getEntity().resetAll();//singletone
            pa.getIso8583().getEntity().loadTemplate("packer200.xml");
            pa.getIso8583().getEntity().setFieldValue("h", "008360000480B5");
            pa.getIso8583().getEntity().setFieldValue("m", "0200");
            pa.getIso8583().getEntity().setFieldValue("2", "6037997293714508");
            pa.getIso8583().getEntity().setFieldValue("3", "500000");
            pa.getIso8583().getEntity().setFieldValue("4", "0");
            pa.getIso8583().getEntity().setFieldValue("7", "1112223045");
            pa.getIso8583().getEntity().setFieldValue("11", "123456");
            pa.getIso8583().getEntity().setFieldValue("12", "654321");
            pa.getIso8583().getEntity().setFieldValue("13", "1012");
            pa.getIso8583().getEntity().setFieldValue("25", "14");
            pa.getIso8583().getEntity().setFieldValue("32", "23801101741");
            pa.getIso8583().getEntity().setFieldValue("37", "123456789123");
            pa.getIso8583().getEntity().setFieldValue("41", "12345678");
            pa.getIso8583().getEntity().setFieldValue("49", "001");
            pa.getIso8583().getEntity().setFieldValue("62", "2212345671");
            pa.getIso8583().getEntity().setFieldValue("64", "12345678");
            result = pa.getIso8583().pack();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceActivity", "getPacker");
        }

        return result;
    }



}
