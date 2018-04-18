package com.technotapp.servicestation.pax.printer;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ArchiveTransactionAdapterModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.entity.TransactionService;
import com.technotapp.servicestation.fragment.AlertDialogFragment;
import com.technotapp.servicestation.fragment.PrintDialogFragment;
import com.technotapp.servicestation.fragment.SellerPrintConfirmFragment;
import com.technotapp.servicestation.setting.AppSetting;
import com.technotapp.servicestation.setting.Session;

public class PrintMaker {
    private static Printable printable;
    private static Bitmap printBitmap;
    private static Session mSession;

    public static void startPrint(Context context, ArchiveTransactionAdapterModel model) {

        mSession = Session.getInstance(context);

        printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);

        if (printable != null) {
            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", model.time, model.date, String.valueOf(model.transactionId), mSession.getTerminalId(), model.cardNumber, String.valueOf(model.amount));

            PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                @Override
                public void onSuccessfulPrint() {

                }

                @Override
                public void failedPrint(String message) {
                    Helper.alert(context, message, Constant.AlertType.Error);
                }
            });
        }
    }

    public static void startPrint(Context context, int requestType, BaseTransactionSto sto) {

        mSession = Session.getInstance(context);
        boolean isTurnRateEnabled = AppSetting.getInstance(context).isTurningEnabled();
        boolean isSellerPrintEnabled = AppSetting.getInstance(context).isSellerPrintEnabled();
        boolean isCustomerPrintEnabled = AppSetting.getInstance(context).isCustomerPrintEnabled();


        switch (requestType) {
            case Constant.RequestMode.DEPOSIT:
                printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(TransactionService.deviceTransactionId), TransactionService.terminalId, TransactionService.cardNumber, String.valueOf(TransactionService.amount));
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
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(TransactionService.deviceTransactionId), TransactionService.terminalId, TransactionService.cardNumber, String.valueOf(TransactionService.amount));
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

                if (isCustomerPrintEnabled) {
                    if (isTurnRateEnabled) {
                        printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER_HAVE_RATE);

                    } else {
                        printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
                    }
                    if (printable != null) {
                        if (isTurnRateEnabled) {
                            long turnRate = PaxHelper.generateTurnRating(context);
                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, String.valueOf(TransactionService.amount), (turnRate + ""));

                        } else {
                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, String.valueOf(TransactionService.amount));

                        }
                        PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                            @Override
                            public void onSuccessfulPrint() {
                                if (isSellerPrintEnabled) {
                                    SellerPrintConfirmFragment dialog = SellerPrintConfirmFragment.newInstance();
                                    dialog.show(((Activity) context).getFragmentManager(), "seller.print");
                                    dialog.setOnDialogButtonClick(new SellerPrintConfirmFragment.OnSellerPrintDialogClick() {
                                        @Override
                                        public void onAccept() {
                                            printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(TransactionService.deviceTransactionId), mSession.getTerminalId(), TransactionService.cardNumber, String.valueOf(TransactionService.amount));
                                            PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                                                @Override
                                                public void onSuccessfulPrint() {

                                                }

                                                @Override
                                                public void failedPrint(String message) {

                                                }
                                            });
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void failedPrint(String message) {
                                AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                                alertDialogFragment.show((Activity) context);
                                alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER_NO_ICON);
                                        printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(TransactionService.deviceTransactionId), TransactionService.terminalId, TransactionService.cardNumber, String.valueOf(TransactionService.amount));
                                        PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                        printDialogFragment.show((Activity) context);
                                        alertDialogFragment.dismiss();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    if (isSellerPrintEnabled) {
                        printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                        printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(TransactionService.deviceTransactionId), TransactionService.terminalId, TransactionService.cardNumber, String.valueOf(TransactionService.amount));
                        PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                            @Override
                            public void onSuccessfulPrint() {

                            }

                            @Override
                            public void failedPrint(String message) {

                            }
                        });
                    }
                }
                break;

            case Constant.RequestMode.BALANCE:

                printable = PrintFactory.getPrintContent(Printable.BALANCE);
                if (printable != null) {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, String.valueOf(sto.accountAmount));
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
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, String.valueOf(sto.accountAmount));
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

    public static void startFactorPrint(Context context, BaseTransactionSto sto) {

        mSession = Session.getInstance(context);
        boolean isTurnRateEnabled = AppSetting.getInstance(context).isTurningEnabled();
        boolean isSellerPrintEnabled = AppSetting.getInstance(context).isSellerPrintEnabled();
        boolean isCustomerPrintEnabled = AppSetting.getInstance(context).isCustomerPrintEnabled();

        if (isCustomerPrintEnabled) {
            if (isTurnRateEnabled) {
                printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER_HAVE_RATE);

            } else {
                printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
            }
            if (printable != null) {
                if (isTurnRateEnabled) {
                    long turnRate = PaxHelper.generateTurnRating(context);
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount, (turnRate + ""));

                } else {
                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);

                }
                PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                    @Override
                    public void onSuccessfulPrint() {
                        if (isSellerPrintEnabled) {
                            SellerPrintConfirmFragment dialog = SellerPrintConfirmFragment.newInstance();
                            dialog.show(((Activity) context).getFragmentManager(), "seller.print");
                            dialog.setOnDialogButtonClick(new SellerPrintConfirmFragment.OnSellerPrintDialogClick() {
                                @Override
                                public void onAccept() {
                                    printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                                    printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);
                                    PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                                        @Override
                                        public void onSuccessfulPrint() {

                                        }

                                        @Override
                                        public void failedPrint(String message) {

                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void failedPrint(String message) {
                        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(Constant.AlertType.Error, message, context.getString(R.string.transaction_result_btn_view));
                        alertDialogFragment.show((Activity) context);
                        alertDialogFragment.setConfirmClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER_NO_ICON);
                                printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);
                                PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                                printDialogFragment.show((Activity) context);
                                alertDialogFragment.dismiss();
                            }
                        });
                    }
                });
            }
        } else {
            if (isSellerPrintEnabled) {
                printable = PrintFactory.getPrintContent(Printable.BUY_SELLER);
                printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);
                PrinterHelper.getInstance().startPrint(context, printBitmap, new PrinterHelper.PrinterListener() {
                    @Override
                    public void onSuccessfulPrint() {

                    }

                    @Override
                    public void failedPrint(String message) {

                    }
                });
            }
        }
    }

    public static void startPrint(Context context, int requestType, long amount) {

        boolean isTurnRateEnabled = AppSetting.getInstance(context).isTurningEnabled();
        boolean isCustomerPrintEnabled = AppSetting.getInstance(context).isCustomerPrintEnabled();

        mSession = Session.getInstance(context);

        switch (requestType) {
            case Constant.RequestMode.CASH_PAYMENT:
                if (isCustomerPrintEnabled) {
                    if (isTurnRateEnabled) {
                        printable = PrintFactory.getPrintContent(Printable.CASH_HAVE_RATE);
                    } else {
                        printable = PrintFactory.getPrintContent(Printable.CASH);
                    }
                    if (printable != null) {

                        if (isTurnRateEnabled) {
                            long turnRate = PaxHelper.generateTurnRating(context);
                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(amount), ("" + turnRate));
                        } else {
                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(amount));
                        }
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
                }
                break;
        }


    }

    public static void failOperationPrint(Context context, BaseTransactionSto sto) {
        printable = PrintFactory.getPrintContent(Printable.FAILED_OPERATION);
        mSession = Session.getInstance(context);


        if (printable != null) {

            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);

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
                            printBitmap = printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(sto.deviceTransactionID), mSession.getTerminalId(), sto.cardNumber, sto.amount);
                            PrintDialogFragment printDialogFragment = PrintDialogFragment.newInstance(printBitmap);
                            printDialogFragment.show((Activity) context);
                            alertDialogFragment.dismiss();
                        }
                    });
                }
            });
        }

    }
}
