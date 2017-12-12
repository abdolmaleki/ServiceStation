package com.technotapp.servicestation.Infrastructure;

import android.app.ProgressDialog;
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

    private static Printable printable;
    private static Session mSession;

    private static byte[] getPacker(Context mContext, TransactionDataModel transactionDataModel, int mode, String amount) {
        //TODO remove fakes
        Random fakeRandom = new Random();
        long number = 10000000 + ((long) (fakeRandom.nextDouble() * (99999999 - 10000000)));
        String fakeTransactionCode = number + "";
        String fakeMAC = "12345678";
        String fakeMerchantID = "23801101741";
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

    public static void sendRequest(final Context ctx, final int mode, final TransactionDataModel transactionDataModel, String amount) {
        try {
            NetworkHelper.isConnectingToInternet(ctx, new NetworkHelper.CheckNetworkStateListener() {
                @Override
                public void onNetworkChecked(boolean isSuccess, String message) {
                    if (isSuccess) {
                        mSession = new Session();
                        final ProgressDialog transactionWaitingDialog;

                        transactionWaitingDialog = new ProgressDialog(ctx);
                        transactionWaitingDialog.setMessage(ctx.getString(R.string.TransactionHelper_pleaseWait));
                        transactionWaitingDialog.setCancelable(false);

                        transactionWaitingDialog.show();

                        SocketEngine socketEngine = new SocketEngine(ctx, Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
                        socketEngine.sendData(TransactionHelper.getPacker(ctx, transactionDataModel, mode, amount), new ISocketCallback() {
                            @Override
                            public void onFail() {
                                transactionWaitingDialog.dismiss();
                                //todo handle fail transaction
                                Helper.alert(ctx, "بروز مشکل در سرور", Constant.AlertType.Error);

                            }

                            @Override
                            public void onReceiveData(TransactionDataModel dataModel) {
                                transactionWaitingDialog.dismiss();
                                AppMonitor.Log(dataModel.getPanNumber());
                                //todo handle fail transaction

                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                /////// Failed transaction
                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                if (!(dataModel.getResponseCode().equals(Constant.ResponseCode.SUCCESS))) {
                                    transactionDialog(ctx, dataModel, mode, false);


                                    return;
                                }

                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                /////// Successful  transaction
                                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                transactionDialog(ctx, dataModel, mode, true);
                                switch (mode) {

                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    /////// DEPOSIT
                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                    case Constant.RequestMode.DEPOSIT:
                                        printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                                        break;

                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    /////// BUY
                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                    case Constant.RequestMode.BUY:
                                        printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
                                        break;

                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    /////// BALANCE
                                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                    case Constant.RequestMode.BALANCE:
                                        printable = PrintFactory.getPrintContent(Printable.BALANCE);
                                        break;

                                }
                                // Todo change print static content
                                if (printable != null) {
                                    PrinterHelper.getInstance().startPrint(ctx,printable.getContent(ctx, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                                }
                            }
                        });
                    } else {
                        //Todo create alert
                    }
                }
            });


        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionHelper", "sendRequest");
        }
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
            case "39":
                return "حساب وجود ندارد یا غیر فعال است";
            case "51":
                return "مبلغ درخواستی از حداقل موجودی حساب بیشتر است";
            default:

                return null;

        }
    }

    private static void transactionDialog(final Context ctx, final TransactionDataModel dataModel, int transactionMode, boolean isSuccess) {
        try {

            FragmentManager manager = ((FragmentActivity) ctx).getSupportFragmentManager();
            final TransactionResponseDialogFragment dialog = new TransactionResponseDialogFragment();
            dialog.setCancelable(false);
            Bundle bundle = new Bundle();
            if (isSuccess) {
                switch (transactionMode) {
                    case Constant.RequestMode.BALANCE:
                    case Constant.RequestMode.DEPOSIT:

                        bundle.putBoolean("isSuccess", true);
                        bundle.putBoolean("hasSellerReceipt", false);

                        dialog.onClickListener(new TransactionResponseDialogFragment.MyOnClickListener() {
                            @Override
                            public void onClick(View v) {

                                switch (v.getId()) {
                                    case R.id.fragment_dialog_transaction_response_btnPositive:
                                        dialog.dismiss();
                                        break;


                                }

                            }
                        });
                        break;


                    case Constant.RequestMode.BUY:
                        bundle.putBoolean("hasSellerReceipt", true);
                        bundle.putBoolean("isSuccess", true);
                        bundle.putString("extraMessage", "آیا نیاز به رسید فروشنده دارید؟");
                        dialog.onClickListener(new TransactionResponseDialogFragment.MyOnClickListener() {
                            @Override
                            public void onClick(View v) {

                                switch (v.getId()) {
                                    case R.id.fragment_dialog_transaction_response_btnPositive:
                                        Printable printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                                        PrinterHelper printerHelper = PrinterHelper.getInstance();
                                        if (printable != null) {
                                            printerHelper.startPrint(ctx,printable.getContent(ctx, "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));

                                        }
                                        dialog.dismiss();
                                        break;
                                    case R.id.fragment_dialog_transaction_response_btnNegative:
                                        dialog.dismiss();
                                        break;

                                }

                            }
                        });

                        break;

                }
            } else {
                bundle.putBoolean("isSuccess", false);
                bundle.putString("extraMessage", getResponseMessage(dataModel.getResponseCode()));

                dialog.onClickListener(new TransactionResponseDialogFragment.MyOnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {
                            case R.id.fragment_dialog_transaction_response_btnPositive:
                                dialog.dismiss();
                                break;

                        }

                    }
                });

            }
            dialog.setArguments(bundle);
            dialog.show(manager, "");

        } catch (Exception e) {
            AppMonitor.reportBug(e, "TransactionHelper", "transactionDialog");
        }

    }

}
