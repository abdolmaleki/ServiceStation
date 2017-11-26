package com.technotapp.servicestation.connection.restapi;

import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceStationApi {

    @GET("service/Account")
    Call<List<String>> listRoles(@Query("username") String username);

//    @GET("service/IndividualPersonMobile")
//    Call<Person> getPersonInfo(@Header("Authorization") String bearer);

//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/x-www-form-urlencoded"
//    })

    @POST("api/Applications/LoginTerminal")
    Call<String> terminalLoginModel(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String apiType);

    @POST("api/Applications/GetTerminalInfo")
    Call<String> getTerminalInfo(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String apiType);

}