package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.MainActivity;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.LogDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class AppMonitor {
    private static boolean isDebugMode = true;
    private static boolean isNeedUpdatingMenu = false;
    private static Session mSession;


    public static void reportBug(Context context, Exception ex, String className, String methodName) {
        mSession = Session.getInstance(context);
        AppMonitor.Log("Ex: " + ex.getMessage() + " class: " + className + " method: " + methodName);
        LogDto dto = createErrorLogDto(className, methodName, ex.getMessage());
        //callSendErrorLog(context, dto);

    }

    public static void Log(String logStr) {
        if (isDebugMode) {
            Log.e("ServiceStation ----->", logStr);
        }
    }

    public static boolean isNeedMenuUpdate() {
        return isNeedUpdatingMenu;
    }

    public static void setMenuNeedingUpdate(boolean isNeedUpdating) {
        isNeedUpdatingMenu = isNeedUpdating;
    }

    public static void Toast(Context ctx, String text, int lenght) {
        Toast.makeText(ctx, text, lenght).show();
    }

    private static void callSendErrorLog(Context context, LogDto dto) {
        try {
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.LOG_INFO).call(context, dto, AESsecretKey, null, new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<BaseSto>>() {
                        }.getType();
                        List<BaseSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                UpdateHelper.checkNeedingUpdate(context);
                            } else {
                                Log.e("AppMonitor,CallLog --->", context.getString(R.string.api_data_download_error));
                            }
                        } else {
                            Log.e("AppMonitor,CallLog --->", context.getString(R.string.api_data_download_error));

                        }
                    } catch (Exception e) {
                        Log.e("AppMonitor,CallLog --->", e.getMessage());

                    }
                }

                @Override
                public void onFail() {
                }

                @Override
                public void onNetworkProblem(String message) {

                }
            });
        } catch (Exception e) {
            Log.e("AppMonitor,CallLog --->", e.getMessage());
        }
    }

    private static LogDto createErrorLogDto(String className, String methodName, String message) {

        LogDto dto = new LogDto();
        dto.Description = "(" + DateHelper.getShamsiDate() + ")----> " + message;
        dto.Title = className + "/" + methodName;
        dto.UserDeviceInfo = "My Pos Info";
        dto.LogTypeId = 3;
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }
}
