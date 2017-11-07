package com.technotapp.servicestation.connection.restapi;

import android.util.Base64;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;

import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCaller {

    private int mApiType;

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


    public void call(BaseDto dto, SecretKey AESsecretKey, Callback<String> callback) {
        try {
            // Client For Retrofit
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
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
            }
            token.enqueue(callback);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ApiCaller", "call");
        }
    }


}
