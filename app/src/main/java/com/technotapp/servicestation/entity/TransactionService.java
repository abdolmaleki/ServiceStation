package com.technotapp.servicestation.entity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;
import com.technotapp.servicestation.connection.restapi.dto.BillPaymentDto;
import com.technotapp.servicestation.connection.restapi.dto.BuyInternetPackageDto;
import com.technotapp.servicestation.connection.restapi.dto.ChargeServiceDto;
import com.technotapp.servicestation.connection.restapi.dto.GetCustomerAccountsDto;
import com.technotapp.servicestation.connection.restapi.dto.GetCustomerAccountsVerificationDto;
import com.technotapp.servicestation.connection.restapi.dto.TerminalTransactionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.connection.restapi.sto.BillPaymentSto;
import com.technotapp.servicestation.connection.restapi.sto.BuyInternetPackaeResultSto;
import com.technotapp.servicestation.connection.restapi.sto.CustomerAccountSto;
import com.technotapp.servicestation.connection.restapi.sto.TransactionChargeResultSto;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.fragment.PaymentListFragment;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

public class TransactionService {

    public static long deviceTransactionId;
    public static Date transactionDateTime;
    public static String cardNumber;
    public static long accountNumber;
    public static boolean isUseScore;
    public static String accountPin;
    public static String paymentType;
    public static int serviceType;
    public static BaseDto dto;
    public static long amount;
    public static long idMerchant;
    public static String terminalId;
    public static String tokenId;
    public static String codeTerminalType;
    public static Session session;
    public static String hashId;
    public static boolean hasDiscount;

    //
    //  ____                  _            _____                   _   _                 _ _
    // / ___|  ___ _ ____   _(_) ___ ___  |_   _|   _ _ __   ___  | | | | __ _ _ __   __| | | ___
    // \___ \ / _ \ '__\ \ / / |/ __/ _ \   | || | | | '_ \ / _ \ | |_| |/ _` | '_ \ / _` | |/ _ \
    //  ___) |  __/ |   \ V /| | (_|  __/   | || |_| | |_) |  __/ |  _  | (_| | | | | (_| | |  __/
    // |____/ \___|_|    \_/ |_|\___\___|   |_| \__, | .__/ \___| |_| |_|\__,_|_| |_|\__,_|_|\___|
    //                                          |___/|_|
    public static void callPaymentService(Context context, PaymentListFragment.PaymentResultListener resultListener) {

        initialize(context);

        if (validation(context)) {
            switch (serviceType) {
                case ServiceType.BILL:
                    callBillPayment(context, resultListener);
                    break;

                case ServiceType.CHARGE:
                    callChargePayment(context, resultListener);
                    break;

                case ServiceType.TRANSACTION_BUY:
                    callTransactionBuy(context, resultListener);
                    break;

                case ServiceType.BUY_PRODUCT:
                    callTransactionBuy(context, resultListener);
                    break;

                case ServiceType.TRANSACTION_BALANCE:
                    callTransactionBalance(context, resultListener);
                    break;

                case ServiceType.BUY_ITERNET_PACKAGE:
                    callBuyInternetPackage(context, resultListener);
                    break;
            }
        } else {
            resultListener.onFailedPayment("در حال حاضر امکان ثبت تراکنش وجود ندارد", null);
        }
    }

    private static void initialize(Context context) {
        session = Session.getInstance(context);
    }

    private static boolean validation(Context context) {
        deviceTransactionId = Calendar.getInstance().getTimeInMillis();
        transactionDateTime = Calendar.getInstance().getTime();
        terminalId = session.getTerminalId();
        tokenId = session.getTokenId();
        idMerchant = Long.parseLong(session.getMerchantId());
        codeTerminalType = Constant.Pax.TYPE_POS;

        return true;
    }

    public static void resetValues() {
        deviceTransactionId = -1;
        transactionDateTime = null;
        cardNumber = null;
        accountNumber = -1;
        accountPin = null;
        paymentType = null;
        serviceType = -1;
        dto = null;
        amount = -1;
        idMerchant = -1;
        terminalId = null;
        tokenId = null;
        codeTerminalType = null;
        session = null;
        hasDiscount = false;
        isUseScore = false;
        hashId = null;

    }


//
    //
    //      _    ____ ___    ____      _ _ _
    //     / \  |  _ \_ _|  / ___|__ _| | (_)_ __   __ _
    //    / _ \ | |_) | |  | |   / _` | | | | '_ \ / _` |
    //   / ___ \|  __/| |  | |__| (_| | | | | | | | (_| |
    //  /_/   \_\_|  |___|  \____\__,_|_|_|_|_| |_|\__, |
    //                                             |___/

    private static void callChargePayment(Context context, PaymentListFragment.PaymentResultListener resultListener) {
        try {
            ChargeServiceDto chargeDto = (ChargeServiceDto) dto;
            chargeDto.transactionModel.accountNumber = accountNumber;
            chargeDto.transactionModel.cardNumber = cardNumber;
            chargeDto.transactionModel.deviceTransactionId = deviceTransactionId;
            chargeDto.transactionModel.transactionDateTime = transactionDateTime;
            chargeDto.transactionModel.idMerchant = idMerchant;
            chargeDto.transactionModel.useSourceScoreForTransaction = isUseScore;
            chargeDto.transactionModel.useShopDiscount = hasDiscount;
            chargeDto.tokenId = tokenId;
            chargeDto.terminalCode = terminalId;
            session = Session.getInstance(context);

            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            SecretKey AES_Key_For_Pin = Encryptor.generateAESKeyByArray(session.getPinKey());
            String encryptedPinKey = Encryptor.encriptAES(accountPin, AES_Key_For_Pin);
            chargeDto.transactionModel.accountPin = Base64.decode(encryptedPinKey, Base64.DEFAULT);


            new ApiCaller(Constant.Api.Type.BYE_CHARGE).call(context, chargeDto, AESsecretKey, "در حال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        TransactionChargeResultSto chargeSto = gson.fromJson(jsonResult, TransactionChargeResultSto.class);

                        if (chargeSto != null) {
                            Session.getInstance(context).setPinKey(chargeSto.newPinKey);
                            if (chargeSto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || chargeSto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                callGetCusomrtAccountAndVerification(context, chargeSto.nidTransaction);
                                if (chargeSto.chargeResult.resultCode.equals(Constant.Api.ResultCode.Successfull)) {

                                    chargeSto.amount = String.valueOf(chargeDto.chargeModel.amount);
                                    if (chargeDto.transactionModel.cardNumber != null && !TextUtils.isEmpty(chargeDto.transactionModel.cardNumber)) {
                                        chargeSto.cardNumber = chargeDto.transactionModel.cardNumber; // have carNumber
                                    } else {
                                        chargeSto.cardNumber = String.valueOf(chargeDto.transactionModel.accountNumber); // have HashId

                                    }

                                    resultListener.onSuccessfullPayment(chargeSto.chargeResult.note, chargeSto);
                                } else {
                                    resultListener.onFailedPayment(chargeSto.chargeResult.note, chargeSto);
                                }

                            } else {
                                resultListener.onFailedPayment(chargeSto.errorString, null);
                            }

                        } else {
                            resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(context, e, "TransactionService", "callChargePayment-OnResponse");
                        resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                    }
                }

                @Override
                public void onFail(String message) {
                    resultListener.onFailedPayment(message, null);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "ChargeFragment", "callByeCharge");
        } finally {
        }
    }

    private static void callBillPayment(Context context, PaymentListFragment.PaymentResultListener resultListener) {
        try {
            BillPaymentDto billPaymentDto = (BillPaymentDto) dto;
            billPaymentDto.transactionModel.accountNumber = accountNumber;
            billPaymentDto.transactionModel.cardNumber = cardNumber;
            billPaymentDto.transactionModel.deviceTransactionId = deviceTransactionId;
            billPaymentDto.transactionModel.transactionDateTime = transactionDateTime;
            billPaymentDto.transactionModel.useSourceScoreForTransaction = isUseScore;
            billPaymentDto.transactionModel.idMerchant = idMerchant;
            billPaymentDto.transactionModel.useShopDiscount = hasDiscount;
            billPaymentDto.tokenId = tokenId;
            billPaymentDto.terminalCode = terminalId;

            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            SecretKey AES_Key_For_Pin = Encryptor.generateAESKeyByArray(session.getPinKey());
            String encryptedPinKey = Encryptor.encriptAES(accountPin, AES_Key_For_Pin);
            billPaymentDto.transactionModel.accountPin = Base64.decode(encryptedPinKey, Base64.DEFAULT);


            new ApiCaller(Constant.Api.Type.BILL_PAYMENT).call(context, billPaymentDto, AESsecretKey, "در حال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        BillPaymentSto billPaymentSto = gson.fromJson(jsonResult, BillPaymentSto.class);

                        if (billPaymentSto != null) {
                            Session.getInstance(context).setPinKey(billPaymentSto.newPinKey);
                            billPaymentSto.amount = String.valueOf(billPaymentDto.billModel.amount);
                            if (billPaymentDto.transactionModel.cardNumber != null && !TextUtils.isEmpty(billPaymentDto.transactionModel.cardNumber)) {
                                billPaymentSto.cardNumber = billPaymentDto.transactionModel.cardNumber; // have carNumber
                            } else {
                                billPaymentSto.cardNumber = String.valueOf(billPaymentDto.transactionModel.accountNumber); // have HashId

                            }

                            if (billPaymentSto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || billPaymentSto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                callGetCusomrtAccountAndVerification(context, billPaymentSto.nidTransaction);

                                if (billPaymentSto.billResult.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) {

                                    resultListener.onSuccessfullPayment(billPaymentSto.billResult.errorString, billPaymentSto);
                                } else {
                                    resultListener.onFailedPayment(billPaymentSto.billResult.errorString, billPaymentSto);
                                }

                            } else {
                                resultListener.onFailedPayment(billPaymentSto.errorString, null);

                            }

                        } else {
                            resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(context, e, "TransactionService", "callBillPayment-OnResponse");
                        resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                    }
                }

                @Override
                public void onFail(String message) {
                    resultListener.onFailedPayment(message, null);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "TransactionService", "callBillPayment");
        } finally {
        }
    }

    private static void callTransactionBuy(Context context, PaymentListFragment.PaymentResultListener resultListener) {
        try {
            TerminalTransactionDto buyDto = (TerminalTransactionDto) dto;
            buyDto.paymentType = paymentType;
            buyDto.transactionModel.accountNumber = accountNumber;
            buyDto.transactionModel.cardNumber = cardNumber;
            buyDto.transactionModel.deviceTransactionId = deviceTransactionId;
            buyDto.transactionModel.transactionDateTime = transactionDateTime;
            buyDto.transactionModel.useSourceScoreForTransaction = isUseScore;
            buyDto.transactionModel.idMerchant = idMerchant;
            buyDto.transactionModel.codeTerminalType = codeTerminalType;
            buyDto.transactionModel.amountOfTransaction = amount;
            buyDto.transactionModel.useShopDiscount = hasDiscount;
            buyDto.tokenId = tokenId;
            buyDto.terminalCode = terminalId;
            buyDto.transactionModel.codeTransactionType = Constant.TransactionType.BUY;

            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            SecretKey AES_Key_For_Pin = Encryptor.generateAESKeyByArray(session.getPinKey());
            String encryptedPinKey = Encryptor.encriptAES(accountPin, AES_Key_For_Pin);
            buyDto.transactionModel.accountPin = Base64.decode(encryptedPinKey, Base64.DEFAULT);

            new ApiCaller(Constant.Api.Type.INSERT_TRANSACTION).call(context, buyDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        BaseTransactionSto transactionResultSto = gson.fromJson(jsonResult, BaseTransactionSto.class);

                        if (transactionResultSto != null) {
                            Session.getInstance(context).setPinKey(transactionResultSto.newPinKey);
                            transactionResultSto.paymentType = buyDto.paymentType;
                            transactionResultSto.amount = String.valueOf(buyDto.transactionModel.amountOfTransaction);
                            if (buyDto.transactionModel.cardNumber != null && !TextUtils.isEmpty(buyDto.transactionModel.cardNumber)) {
                                transactionResultSto.cardNumber = buyDto.transactionModel.cardNumber; // have carNumber
                            } else {
                                transactionResultSto.cardNumber = String.valueOf(buyDto.transactionModel.accountNumber); // have HashId

                            }
                            Session.getInstance(context).setPinKey(transactionResultSto.newPinKey);
                            if (transactionResultSto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || transactionResultSto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                callGetCusomrtAccountAndVerification(context, transactionResultSto.nidTransaction);
                                resultListener.onSuccessfullPayment(transactionResultSto.errorString, transactionResultSto);
                            } else {
                                resultListener.onFailedPayment(transactionResultSto.errorString, null);
                            }

                        } else {
                            resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(context, e, "TransactionService", "callTransactionBuy-OnResponse");
                        resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                    }
                }

                @Override
                public void onFail(String message) {
                    resultListener.onFailedPayment(message, null);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "ChargeFragment", "callByeCharge");
        } finally {
        }
    }

    private static void callTransactionBalance(Context context, PaymentListFragment.PaymentResultListener resultListener) {
        try {
            TerminalTransactionDto balanceDto = (TerminalTransactionDto) dto;
            balanceDto.transactionModel.accountNumber = accountNumber;
            balanceDto.transactionModel.cardNumber = cardNumber;
            balanceDto.transactionModel.deviceTransactionId = deviceTransactionId;
            balanceDto.transactionModel.transactionDateTime = transactionDateTime;
            balanceDto.transactionModel.useSourceScoreForTransaction = isUseScore;
            balanceDto.transactionModel.idMerchant = idMerchant;
            balanceDto.transactionModel.codeTerminalType = codeTerminalType;
            balanceDto.tokenId = tokenId;
            balanceDto.terminalCode = terminalId;
            balanceDto.transactionModel.useShopDiscount = hasDiscount;


            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            SecretKey AES_Key_For_Pin = Encryptor.generateAESKeyByArray(session.getPinKey());
            String encryptedPinKey = Encryptor.encriptAES(accountPin, AES_Key_For_Pin);
            balanceDto.transactionModel.accountPin = Base64.decode(encryptedPinKey, Base64.DEFAULT);

            new ApiCaller(Constant.Api.Type.INSERT_TRANSACTION).call(context, balanceDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        BaseTransactionSto transactionResultSto = gson.fromJson(jsonResult, BaseTransactionSto.class);

                        if (transactionResultSto != null) {
                            Session.getInstance(context).setPinKey(transactionResultSto.newPinKey);
                            transactionResultSto.paymentType = balanceDto.paymentType;
                            if (balanceDto.transactionModel.cardNumber != null && !TextUtils.isEmpty(balanceDto.transactionModel.cardNumber)) {
                                transactionResultSto.cardNumber = balanceDto.transactionModel.cardNumber; // have carNumber
                            } else {
                                transactionResultSto.cardNumber = String.valueOf(balanceDto.transactionModel.accountNumber); // have HashId

                            }
                            Session.getInstance(context).setPinKey(transactionResultSto.newPinKey);
                            if (transactionResultSto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || transactionResultSto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                callGetCusomrtAccountAndVerification(context, transactionResultSto.nidTransaction);
                                resultListener.onSuccessfullPayment(transactionResultSto.errorString, transactionResultSto);
                            } else {
                                resultListener.onFailedPayment(transactionResultSto.errorString, null);
                            }

                        } else {
                            resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(context, e, "TransactionService", "callTransactionBalance-OnResponse");
                        resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                    }
                }

                @Override
                public void onFail(String message) {
                    resultListener.onFailedPayment(message, null);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "TransactionService", "callTransactionBalance");
        } finally {
        }
    }

    private static void callBuyInternetPackage(Context context, PaymentListFragment.PaymentResultListener resultListener) {
        try {
            BuyInternetPackageDto buyInternetPackageDto = (BuyInternetPackageDto) dto;
            buyInternetPackageDto.transactionModel.accountNumber = accountNumber;
            buyInternetPackageDto.transactionModel.cardNumber = cardNumber;
            buyInternetPackageDto.transactionModel.deviceTransactionId = deviceTransactionId;
            buyInternetPackageDto.transactionModel.transactionDateTime = transactionDateTime;
            buyInternetPackageDto.transactionModel.useSourceScoreForTransaction = isUseScore;
            buyInternetPackageDto.transactionModel.idMerchant = idMerchant;
            buyInternetPackageDto.transactionModel.useShopDiscount = hasDiscount;
            buyInternetPackageDto.tokenId = tokenId;
            buyInternetPackageDto.terminalCode = terminalId;
            session = Session.getInstance(context);

            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            SecretKey AES_Key_For_Pin = Encryptor.generateAESKeyByArray(session.getPinKey());
            String encryptedPinKey = Encryptor.encriptAES(accountPin, AES_Key_For_Pin);
            buyInternetPackageDto.transactionModel.accountPin = Base64.decode(encryptedPinKey, Base64.DEFAULT);


            new ApiCaller(Constant.Api.Type.BUY_INTERNET_PACKAGE).call(context, buyInternetPackageDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        BuyInternetPackaeResultSto buyInternetPackaeResultSto = gson.fromJson(jsonResult, BuyInternetPackaeResultSto.class);

                        if (buyInternetPackaeResultSto != null) {
                            Session.getInstance(context).setPinKey(buyInternetPackaeResultSto.newPinKey);
                            if (buyInternetPackaeResultSto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || buyInternetPackaeResultSto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                callGetCusomrtAccountAndVerification(context, buyInternetPackaeResultSto.nidTransaction);
                                if (buyInternetPackaeResultSto.internetPackResult.resultCode.equals(Constant.Api.ResultCode.Successfull)) {

                                    buyInternetPackaeResultSto.amount = String.valueOf(buyInternetPackageDto.internetPackModel.amount);
                                    if (buyInternetPackageDto.transactionModel.cardNumber != null && !TextUtils.isEmpty(buyInternetPackageDto.transactionModel.cardNumber)) {
                                        buyInternetPackaeResultSto.cardNumber = buyInternetPackageDto.transactionModel.cardNumber; // have carNumber
                                    } else {
                                        buyInternetPackaeResultSto.cardNumber = String.valueOf(buyInternetPackageDto.transactionModel.accountNumber); // have HashId

                                    }

                                    resultListener.onSuccessfullPayment(buyInternetPackaeResultSto.internetPackResult.note, buyInternetPackaeResultSto);
                                } else {
                                    resultListener.onFailedPayment(buyInternetPackaeResultSto.internetPackResult.note, buyInternetPackaeResultSto);
                                }

                            } else {
                                resultListener.onFailedPayment(buyInternetPackaeResultSto.errorString, null);
                            }

                        } else {
                            resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(context, e, "TransactionService", "callBuyInternetPackage-OnResponse");
                        resultListener.onFailedPayment(context.getString(R.string.api_data_download_error), null);
                    }
                }

                @Override
                public void onFail(String message) {
                    resultListener.onFailedPayment(message, null);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "ChargeFragment", "callBuyInternetPackage");
        } finally {
        }
    }

    private static void callGetCusomrtAccountAndVerification(Context context, long nidTransaction) {

        try {
            GetCustomerAccountsVerificationDto dto = new GetCustomerAccountsVerificationDto();
            dto.cardNumber = cardNumber;
            dto.terminalCode = terminalId;
            dto.tokenId = tokenId;
            dto.nidTransaction = nidTransaction;
            dto.idHashCustomer = hashId;
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.GET_CUSTOMER_ACCOUNTS_AND_VERIFICATION).call(context, dto, AESsecretKey, null, new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                }

                @Override
                public void onFail(String message) {
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "TransactionService", "callGetCusomrtAccountAndVerification");
        } finally {
            resetValues();
        }
    }
}
