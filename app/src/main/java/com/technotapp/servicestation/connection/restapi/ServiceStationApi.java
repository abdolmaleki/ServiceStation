package com.technotapp.servicestation.connection.restapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<String> terminalLoginModel(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Applications/GetTerminalInfo")
    Call<String> getTerminalInfo(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Logs/InsertLogItem")
    Call<String> sendLogInfo(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Logs/InsertSuggestionItem")
    Call<String> submitSuggestion(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/shop/InsertUpdateProductItem")
    Call<String> addProduct(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/shop/ReturnProducts")
    Call<String> serachProduct(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/shop/InsertFactor")
    Call<String> insertFactor(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/InternalCharge/Rechagre")
    Call<String> buyCharge(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Applications/GetTerminalVersionMenu")
    Call<String> getVersion(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Shop/ReturnTransactions")
    Call<String> searchTransaction(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Shop/ReturnFactors")
    Call<String> searchFactor(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);

    @POST("api/Applications/UpdateShopInfo")
    Call<String> editShopInfo(@Query("key") String key, @Query("value") String value, @Query("deviceInfo") String deviceInfo);


}