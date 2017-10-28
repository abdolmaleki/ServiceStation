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

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("service/token")
    Call<MediaSessionCompat.Token> getToken(@Field("grant_type") String grant_type, @Field("username") String userName, @Field("password") String password);

    @GET("api/Allapi")
    Call<String> getData(@Query("key") String key, @Query("value") String value, @Query("type") String apiType);

    @GET("api/SaveClient")
    Call<String> saveClient(@Query("key") String key, @Query("value") String value);

    @GET("api/Suggest")
    Call<String> sendOffer(@Query("key") String key, @Query("value") String value);


}