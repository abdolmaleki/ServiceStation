package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;
import com.technotapp.servicestation.connection.restapi.dto.BillPaymentDto;
import com.technotapp.servicestation.connection.restapi.dto.ChargeServiceDto;
import com.technotapp.servicestation.connection.restapi.dto.TerminalTransactionDto;
import com.technotapp.servicestation.entity.TransactionService;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.fragment.PaymentListFragment;

public class TransactionHelper {

    public static String getResponseMessage(String code) {
        switch (code) {
            case "-1":
                return "خطای سرور";
            case "00":
                return "عملیات با موفقیت انجام شد";
            case "03":
                return "ترمینال وجود ندارد یا فعال نیست";
            case "12":
                return "این تراکنش قبلا ثبت شده است";
            case "14":
                return "شماره کارت وجود ندارد";
            case "30":
                return "خطا در سرور";
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

    public static void startServiceTransaction(Context context, int serviceType, BaseDto dto, PaymentListFragment.PaymentResultListener resultListener) {

        TransactionService.resetValues();

        TransactionService.dto = dto;
        TransactionService.serviceType = serviceType;

        PaymentListFragment paymentListFragment = null;
        switch (serviceType) {
            case ServiceType.CHARGE:
                paymentListFragment = PaymentListFragment.newInstance(((ChargeServiceDto) dto).chargeModel.amount);
                break;

            case ServiceType.BILL:
                paymentListFragment = PaymentListFragment.newInstance(((BillPaymentDto) dto).billModel.amount);
                break;

            case ServiceType.BUY_PRODUCT:
                paymentListFragment = PaymentListFragment.newInstance(((TerminalTransactionDto) dto).transactionModel.amountOfTransaction, true);
                break;

            case ServiceType.TRANSACTION_BALANCE:
                paymentListFragment = PaymentListFragment.newInstance(((TerminalTransactionDto) dto).transactionModel.amountOfTransaction);
                break;

            case ServiceType.TRANSACTION_BUY:
                paymentListFragment = PaymentListFragment.newInstance();
                break;

            default:
                paymentListFragment = PaymentListFragment.newInstance();

        }
        paymentListFragment.show(((FragmentActivity) context), resultListener);
    }

}
