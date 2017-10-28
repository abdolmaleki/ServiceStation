package com.technotapp.servicestation.connection.restapi;

import android.util.Base64;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.Encryptor;
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

    public void call(BaseDto dto, SecretKey AESsecretKey, Callback<String> callback) {

        // Client For Retrofit
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
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

        final Call<String> token = apiService.getData(RsaEncryptedkey, AesEncryptedValue, dto.type);
        token.enqueue(callback);
    }


}
