package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import com.pax.gl.pack.impl.PaxGLPacker;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.socket.ISocketCallback;
import com.technotapp.servicestation.connection.socket.SocketEngine;
import com.technotapp.servicestation.pax.printer.PrintFactory;
import com.technotapp.servicestation.pax.printer.Printable;
import com.technotapp.servicestation.pax.printer.PrinterHelper;

import java.util.Random;
import java.util.logging.Handler;

public class TransactionHelper {


    public static byte[] getPacker(Context mContext, TransactionDataModel transactionDataModel, int mode, String amount) {
        //TODO remove fakes
        Random fakeRandom = new Random();
        long number = 10000000 + ((long) (fakeRandom.nextDouble() * (99999999 - 10000000)));
        String fakeTransactionCode = number + "";
        String fakeMAC = "12345678";
        String fakeMerchantID = "23801101741";
        String fakePan = "6037997293714508";


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

    public static void sendRequest(final Context ctx, final int mode, TransactionDataModel transactionDataModel, String amount) {

        try {

            SocketEngine socketEngine = new SocketEngine(Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
            socketEngine.sendData(TransactionHelper.getPacker(ctx, transactionDataModel, mode, amount), new ISocketCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onReceiveData(TransactionDataModel dataModel) {
                    AppMonitor.Log(dataModel.getPanNumber());

                    //TODO set parameters to tvAmount
//                    Log.i("aa -------->", dataModel.getPanNumber() + "\n" +
//                            dataModel.getBackTransactionID() + "\n" +
//                            dataModel.getAmount() + "\n" +
//                            dataModel.getDateTimeShaparak() + "\n" +
//                            dataModel.getTerminalID() + "\n" +
//                            dataModel.getResponseCode() + "\n" +
//                            dataModel.getMAC() + "\n");

                    PrinterHelper printerHelper = PrinterHelper.getInstance();
                    Printable printable;

                    switch (mode) {


                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        /////// DEPOSIT
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        case Constant.RequestMode.DEPOSIT:

                            printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                            if (printable != null) {
                                printerHelper.startPrint(printable.getContent(ctx, "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                            }
                            break;

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        /////// BUY
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        case Constant.RequestMode.BUY:

                            final String backTransactionID = dataModel.getBackTransactionID();
                            final String amount = dataModel.getAmount();
                            final String terminalID = dataModel.getTerminalID();
                            final String panNumber = dataModel.getPanNumber();

                            printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
                            if (printable != null) {
                                printerHelper.startPrint(printable.getContent(ctx, "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                            }

                            new android.os.Handler(Looper.getMainLooper()) {
                            }.post(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ctx).setMessage("آیا نیاز به رسید فروشنده می باشد؟").setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Printable printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                                            PrinterHelper printerHelper = PrinterHelper.getInstance();
                                            if (printable != null) {
                                                printerHelper.startPrint(printable.getContent(ctx, "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", backTransactionID, terminalID, panNumber, amount));
                                            }

                                        }
                                    }).setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();

                                }
                            });

                            break;


                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        /////// BALANCE
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        case Constant.RequestMode.BALANCE:
                            printable = PrintFactory.getPrintContent(Printable.BALANCE);
                            if (printable != null) {
                                printerHelper.startPrint(printable.getContent(ctx, "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                            }
                            break;
                    }


                }
            });

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositFragment", "sendRequest");
        }
    }

    public static String getRsponseMessage(String code) {
        switch (code) {
            case "12":
                return "این تراکنش قبلا ثبت شده است";
            case "03":
                return "رمینال وجود ندارد یا فعال نیست";
            case "14":
                return "شماره کارت وجود ندارد";
            case "39":
                return "حساب وجود ندارد یا غیر فعال است";
            case "51":
                return "مبلغ درخواستی از حداقل موجودی حساب بیشتر است";
            case "00":
                return "عملیات با موفقیت انجام شد";
            case "-1":
                return "ERROR_MESSAGE()";

            default:
                return null;

        }
    }

}
