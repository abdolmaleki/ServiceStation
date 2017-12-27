package com.technotapp.servicestation.pax.printer;


import android.content.Context;

import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.setting.Session;

public class PrintMaker {
    private static Printable printable;
    private static Session mSession;

    public static void startPrint(Context context, int requestType, TransactionDataModel dataModel) {

        mSession = Session.getInstance(context);

        switch (requestType) {
            case Constant.RequestMode.DEPOSIT:
                printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                if (printable != null) {
                    PrinterHelper.getInstance().startPrint(context, printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                }
                break;

            case Constant.RequestMode.BUY:
                printable = PrintFactory.getPrintContent(Printable.BUY_CUSTOMER);
                if (printable != null) {
                    PrinterHelper.getInstance().startPrint(context, printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                }
                break;

            case Constant.RequestMode.BALANCE:
                printable = PrintFactory.getPrintContent(Printable.BALANCE);
                if (printable != null) {
                    PrinterHelper.getInstance().startPrint(context, printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
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
                    PrinterHelper.getInstance().startPrint(context, printable.getContent(context, mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(amount)));
                }
                break;
        }


    }
}
