package com.technotapp.servicestation.connection.restapi;

import android.content.Context;
import android.os.Handler;
import android.util.Base64;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.GsonHelper;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;

import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@DontObfuscate
public class ApiCaller {

    private int mApiType;
    private Handler apiHandler;

    public ApiCaller(int apiType) {
        this.mApiType = apiType;
    }

    //
    //     _    ____ ___     ____             __ _
    //    / \  |  _ \_ _|   / ___|___  _ __  / _(_) __ _
    //   / _ \ | |_) | |   | |   / _ \| '_ \| |_| |/ _` |
    //  / ___ \|  __/| |   | |__| (_) | | | |  _| | (_| |
    // /_/   \_\_|  |___|   \____\___/|_| |_|_| |_|\__, |
    //                                             |___/


    public void call(final Context ctx, BaseDto dto, final SecretKey AESsecretKey, String loadingMessage, final ApiCallback apiCallback) {

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////// show progressbar if needed ////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (loadingMessage != null) {
            Helper.progressBar.showDialog(ctx, loadingMessage);
        }

        try {
            // Client For Retrofit
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.Pax.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            ServiceStationApi apiService = retrofit.create(ServiceStationApi.class);

            // Gson gson = new Gson();
            String value = GsonHelper.customGson.toJson(dto);
            //String value = gson.toJson(dto);

            final String AesEncryptedValue = Encryptor.encriptAES(value, AESsecretKey);

            String AESKeyString = Base64.encodeToString(AESsecretKey.getEncoded(), Base64.DEFAULT);
            String RsaEncryptedkey = Encryptor.encriptRSA(AESKeyString);

            Call<String> token = null;


            //
            //     _    ____ ___   _____                   _   _                 _      _
            //    / \  |  _ \_ _| |_   _|   _ _ __   ___  | | | | __ _ _ __   __| | ___| |
            //   / _ \ | |_) | |    | || | | | '_ \ / _ \ | |_| |/ _` | '_ \ / _` |/ _ \ |
            //  / ___ \|  __/| |    | || |_| | |_) |  __/ |  _  | (_| | | | | (_| |  __/ |
            // /_/   \_\_|  |___|   |_| \__, | .__/ \___| |_| |_|\__,_|_| |_|\__,_|\___|_|
            //                          |___/|_|

            switch (mApiType) {
                case Constant.Api.Type.TERMINAL_LOGIN:
                    token = apiService.terminalLoginModel(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.TERMINAL_INFO:
                    token = apiService.getTerminalInfo(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.LOG_INFO:
                    token = apiService.sendLogInfo(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SUBMIT_SUGGESTION:
                    token = apiService.submitSuggestion(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.ADD_UPDATE_PRODUCT:
                    token = apiService.addProduct(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_PRODUCT:
                    token = apiService.serachProduct(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_PRODUCT_SUPPORT:
                    token = apiService.serachProductSupport(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SUBMIT_FACTOR:
                    token = apiService.insertFactor(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.GET_VERSION:
                    token = apiService.getVersion(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_TRANSACTION:
                    token = apiService.searchTransaction(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_TRANSACTION_SUPPORT:
                    token = apiService.searchTransactionSupport(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_Factor:
                    token = apiService.searchFactor(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.SEARCH_Factor_SUPPORT:
                    token = apiService.searchFactorSupport(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.EDIT_SHOP_INFO:
                    token = apiService.editShopInfo(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.GET_CUSTOMER_ACCOUNT:
                    token = apiService.getCustomerAccount(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.BYE_CHARGE:
                    token = apiService.buyCharge(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.BILL_PAYMENT:
                    token = apiService.billPayment(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.INSERT_TRANSACTION:
                    token = apiService.InsertTransaction(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.LOGIN_SETTING:

                    token = apiService.loginSetting(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.GET_INTERNET_PACKAGE:
                    token = apiService.getInternetPackages(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.BUY_INTERNET_PACKAGE:
                    token = apiService.buyInternetPackages(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;

                case Constant.Api.Type.GET_CUSTOMER_ACCOUNTS_AND_VERIFICATION:
                    token = apiService.getCustomerAccountsAndVerifyTransaction(RsaEncryptedkey, AesEncryptedValue, Helper.getDeviceInfo());
                    break;
            }

            token.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    Helper.progressBar.hideDialog();
                    String EncryptedResponse = response.body();
                    if (EncryptedResponse == null || EncryptedResponse.isEmpty()) {
                        apiCallback.onFail("خطای ارتباط با سرور مرکزی");
                    } else {

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //////// Decrypt Response Message and convert To Json and Format it
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        try {
                            Gson gson = Helper.getGson();
                            String AESsecretKeyString = Base64.encodeToString(AESsecretKey.getEncoded(), Base64.DEFAULT);
                            String decryptedResponseString = Encryptor.decriptAES(AESsecretKeyString, EncryptedResponse);
                            String formattedJsonString = gson.fromJson(decryptedResponseString, String.class);
                            if (formattedJsonString != null && !formattedJsonString.equals("")) {
                                apiCallback.onResponse(response.code(), formattedJsonString);

                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //////// Check if menu needing update do it
                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                //UpdateHelper.checkNeedingUpdate(ctx);

                            } else {
                                apiCallback.onFail("خطا در دریافت اطلاعات");
                            }
                        } catch (Exception e) {
                            apiCallback.onFail("خطا در دریافت اطلاعات");
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Helper.progressBar.hideDialog();
                    apiCallback.onFail("لطفا اتصال اینترنت خود را بررسی نمایید");
                }
            });

        } catch (Exception e) {
            Helper.progressBar.hideDialog();
            AppMonitor.reportBug(ctx, e, "Apicaller", "onSuccess");
        }
    }


    public interface ApiCallback {
        void onResponse(int responseCode, String jsonResult);

        void onFail(String message);

    }


}
