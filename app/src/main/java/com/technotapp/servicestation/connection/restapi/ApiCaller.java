package com.technotapp.servicestation.connection.restapi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;

import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        NetworkHelper.isConnectingToInternet(ctx, new NetworkHelper.CheckNetworkStateListener() {
            @Override
            public void onNetworkChecked(boolean isSuccess, String message) {
                if (isSuccess) {
                    Message.obtain(apiHandler, 0, message).sendToTarget();
                } else {
                    Message.obtain(apiHandler, 1, message).sendToTarget();
                }
            }
        });

        apiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {


                if (msg.what == 0) {

                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
                    ////////// Network and Internet are ok
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

                    // Client For Retrofit
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.Pax.API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    ServiceStationApi apiService = retrofit.create(ServiceStationApi.class);

                    Gson gson = new Gson();
                    String value = gson.toJson(dto);

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
                    }

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    ////////// show progressbar if needed ////////////////////
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    if (loadingMessage != null) {
                        Helper.alert(ctx, loadingMessage,Constant.AlertType.Loading);
                    }

                    token.enqueue(new retrofit2.Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Helper.dismiss(Constant.AlertType.Loading);
                            String EncryptedResponse = response.body();
                            if (EncryptedResponse == null || EncryptedResponse.isEmpty()) {
                                Helper.alert(ctx, "خطا در دریافت اطلاعات", Constant.AlertType.Error);
                            } else {

                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //////// Decrypt Response Message and convert To Json and Format it
                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                Gson gson = Helper.getGson();
                                String AESsecretKeyString = Base64.encodeToString(AESsecretKey.getEncoded(), Base64.DEFAULT);
                                String decryptedResponseString = Encryptor.decriptAES(AESsecretKeyString, EncryptedResponse);
                                String formattedJsonString = gson.fromJson(decryptedResponseString, String.class);
                                if (formattedJsonString != null && !formattedJsonString.equals("")) {
                                    apiCallback.onResponse(response.code(), formattedJsonString);

                                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    //////// Check if menu needing update do it
                                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                    UpdateHelper.checkNeedingUpdate(ctx);

                                } else {
                                    Helper.alert(ctx, "خطا در دریافت اطلاعات", Constant.AlertType.Error);
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Helper.dismiss(Constant.AlertType.Loading);
                            apiCallback.onFail();
                        }
                    });

                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
                    ////////// Network Problem
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

                } else { // NetworkProblem

                }
            }
        };


        try {

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ApiCaller", "call");
        }
    }


    public interface ApiCallback {
        void onResponse(int responseCode, String jsonResult);

        void onFail();
    }


}
