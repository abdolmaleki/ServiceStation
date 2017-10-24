package com.technotapp.servicestation.Infrastructure;

import android.content.Context;

import com.pax.gl.pack.impl.PaxGLPacker;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;

import java.util.Random;

public class TransactionHelper {


    public static byte[] getPacker(TransactionDataModel transactionDataModel, int mode, Context mContext) {
        //TODO remove fakes
        Random fakeRandom = new Random();
        long number = 10000000 + ((long) (fakeRandom.nextDouble() * (99999999 - 10000000)));
        String fakeTransactionCode = number + "";
        String fakeMAC = "12345678";
        String fakeMerchantID = "23801101741";
        String fakePan = "6037997293714508";



        String transactionType = null;
        String amount = null;

        switch (mode) {
            case Constant.RequestMode.BALANCE:
                transactionType = "22";
                amount = "0";
                break;
            case Constant.RequestMode.DEPOSIT:
                transactionType = "00";
                amount = transactionDataModel.getAmount();
                break;
            case Constant.RequestMode.PURCHASE:
                transactionType = "a0";
                amount = "100000";
                break;
        }


        byte[] result = new byte[0];
        PaxGLPacker pa = PaxGLPacker.getInstance(mContext);
        try {
            //transaction 0200
            pa.getIso8583().getEntity().resetAllFieldsValue();
            pa.getIso8583().getEntity().resetAll();
            pa.getIso8583().getEntity().loadTemplate("packer200.xml");
            pa.getIso8583().getEntity().setFieldValue("h", "008360000480B5");
            pa.getIso8583().getEntity().setFieldValue("m", "0200");
//            pa.getIso8583().getEntity().setFieldValue("2", transactionDataModel.getPanNumber());
            pa.getIso8583().getEntity().setFieldValue("2", fakePan);
            pa.getIso8583().getEntity().setFieldValue("3", "500000");
            pa.getIso8583().getEntity().setFieldValue("4", amount);
//            pa.getIso8583().getEntity().setFieldValue("7", transactionDataModel.getDateTimeShaparak());
            pa.getIso8583().getEntity().setFieldValue("7", "1112223045");
            pa.getIso8583().getEntity().setFieldValue("11", "123456");
            pa.getIso8583().getEntity().setFieldValue("12", "654321");
            pa.getIso8583().getEntity().setFieldValue("13", "1012");
            pa.getIso8583().getEntity().setFieldValue("25", "14");
            pa.getIso8583().getEntity().setFieldValue("32", fakeMerchantID);
            pa.getIso8583().getEntity().setFieldValue("37", "123456789123");
            pa.getIso8583().getEntity().setFieldValue("41", "12345678");
            pa.getIso8583().getEntity().setFieldValue("49", "001");
            pa.getIso8583().getEntity().setFieldValue("62", transactionType + fakeTransactionCode);
            pa.getIso8583().getEntity().setFieldValue("64", fakeMAC);
            result = pa.getIso8583().pack();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceActivity", "getPacker");
        }

        return result;
    }

}
