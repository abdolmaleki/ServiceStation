package com.technotapp.servicestation.pax.printer;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.AlertDialogFragment;
import com.technotapp.servicestation.fragment.PrintDialogFragment;
import com.technotapp.servicestation.setting.Session;

public class PrintMaker {
    private static Printable printable;
    private static Bitmap printBitmap;
    private static Session mSession;

    public static void startPrint(Context context, int requestType, TransactionDataModel dataModel) {

        mSession = Session.getInstance(context);

        switch (requestType) {
            case Constant.RequestMode.DEPOSIT:
                printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                    PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                        @Override
                        public void onSuccessfulPrint() {

                        }

                        @Override
                        public void failedPrint(String message) {
                            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                            alertDialogFragment.show((Activity) context);
                            alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    printable = PrintFactory.getPrintContent(Printable.DEPOSIT_NO_ICON);
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                                    PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                    printDialogFragment.show((Activity) context);
                                    alertDialogFragment.dismiss();
                                }
                            });
                        }
                    });
                }
                break;

            case Constant.RequestMode.BUY:
                printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                    PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                        @Override
                        public void onSuccessfulPrint() {

                        }

                        @Override
                        public void failedPrint(String message) {
                            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                            alertDialogFragment.show((Activity) context);
                            alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER_NO_ICON);
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                                    PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                    printDialogFragment.show((Activity) context);
                                    alertDialogFragment.dismiss();
                                }
                            });
                        }
                    });
                }
                break;

            case Constant.RequestMode.BALANCE:
                printable = PrintFactory.getPrintContent(Printable.BALANCE);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                    PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                        @Override
                        public void onSuccessfulPrint() {

                        }

                        @Override
                        public void failedPrint(String message) {
                            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                            alertDialogFragment.show((Activity) context);
                            alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    printable = PrintFactory.getPrintContent(Printable.BALANCE_NO_ICON);
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount());
                                    PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                    printDialogFragment.show((Activity) context);
                                    alertDialogFragment.dismiss();
                                }
                            });

                        }
                    });
                }
                break;
        }
    }

    public static void startPrint(Context context, int requestType, long amount) {
        mSession = Session.getInstance(context);
        switch (requestType) {
            case Constant.RequestMode.CASH_PAYMENT:
                printable = PrintFactory.getPrintContent(Printable.CASH);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(amount));
                    PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                        @Override
                        public void onSuccessfulPrint() {

                        }

                        @Override
                        public void failedPrint(String message) {
                            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                            alertDialogFragment.show((Activity) context);
                            alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    printable = PrintFactory.getPrintContent(Printable.CASH_NO_ICON);
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(amount));
                                    PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                    printDialogFragment.show((Activity) context);
                                    alertDialogFragment.dismiss();
                                }
                            });
                        }
                    });
                }
                break;
        }


    }
}
