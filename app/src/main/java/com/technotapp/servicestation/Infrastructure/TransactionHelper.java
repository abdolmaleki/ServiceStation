package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.pax.gl.pack.impl.PaxGLPacker;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.socket.ISocketCallback;
import com.technotapp.servicestation.connection.socket.SocketEngine;
import com.technotapp.servicestation.fragment.TransactionResponseDialogFragment;
import com.technotapp.servicestation.pax.printer.PrintFactory;
import com.technotapp.servicestation.pax.printer.Printable;
import com.technotapp.servicestation.pax.printer.PrinterHelper;
import com.technotapp.servicestation.setting.Session;

import java.util.Random;

public class TransactionHelper {

    private static Session mSession;

    public static void sendRequest(final Context ctx, final int mode, final TransactionDataModel transactionDataModel, String amount, TransactionResultListener transactionResultListener) throws Exception {
        Helper.progressBar.showDialog(ctx, "در حال ارتباط با بانک");
        mSession = new Session();
        SocketEngine socketEngine = new SocketEngine(ctx, Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
        socketEngine.sendData(TransactionHelper.getPacker(ctx, transactionDataModel, mode, amount), new ISocketCallback() {

            @Override
            public void onFail() {
                Helper.progressBar.hideDialog();
                //todo handle fail transaction
                transactionResultListener.onFailTransaction("مشکل ارتباط با سوییچ بانکی");

            }

            @Override
            public void onReceiveData(TransactionDataModel dataModel) {
                Helper.progressBar.hideDialog();
                AppMonitor.Log(dataModel.getPanNumber());
                //todo handle fail transaction

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                /////// Failed transaction
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (!(dataModel.getResponseCode().equals(Constant.TransactionResponseCode.SUCCESS))) {
                    transactionResultListener.onFailTransaction(getResponseMessage(dataModel.getResponseCode()));
                    return;
                }

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                /////// Successful  transaction
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                transactionResultListener.onSuccessfullTransaction(dataModel);

            }
        });
    }

    private static byte[] getPacker(Context mContext, TransactionDataModel transactionDataModel, int mode, String amount) {
        //TODO remove fakes
        Random fakeRandom = new Random();
        long number = 10000000 + ((long) (fakeRandom.nextDouble() * (99999999 - 10000000)));
        String fakeTransactionCode = number + "";
        String fakeMAC = "12345678";
        //String fakeMerchantID = "23801101741";
        String fakeMerchantID = mSession.getMerchantId();
        String panNumber = transactionDataModel.getPanNumber();


        String transactionType = null;

        switch (mode) {
            case Constant.RequestMode.BALANCE:
                transactionType = "22";
                break;
            case Constant.RequestMode.DEPOSIT:
                transactionType = "a0";
                break;
            case Constant.RequestMode.BUY:
                transactionType = "00";
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
            pa.getIso8583().getEntity().setFieldValue("2", panNumber);
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
            AppMonitor.reportBug(e, "TransactionHelper", "getPacker");
        }

        return result;
    }

    public static String getResponseMessage(String code) {
        switch (code) {
            case "-1":
                return "ERROR_MESSAGE()";
            case "00":
                return "عملیات با موفقیت انجام شد";
            case "03":
                return "ترمینال وجود ندارد یا فعال نیست";
            case "12":
                return "این تراکنش قبلا ثبت شده است";
            case "14":
                return "شماره کارت وجود ندارد";
            case "30":
                return "خطای سرور";
            case "39":
                return "حساب وجود ندارد یا غیر فعال است";
            case "51":
                return "مبلغ درخواستی از حداقل موجودی حساب بیشتر است";

            default:

                return "خطای نامشخص";

        }
    }

    public interface TransactionResultListener {
        void onSuccessfullTransaction(TransactionDataModel transactionDataModel);

        void onFailTransaction(String message);
    }

}
