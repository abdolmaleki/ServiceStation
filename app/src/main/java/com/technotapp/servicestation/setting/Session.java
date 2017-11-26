package com.technotapp.servicestation.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.technotapp.servicestation.application.Constant;


public class Session {

    private static SharedPreferences prefs;
    private static Session session;

    public static Session getInstance(Context cntx) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        }
        if (session == null) {
            session = new Session();
        }

        return session;
    }

    public void setMobileNumber(String mobileNumber) {
        prefs.edit().putString(Constant.Session.MOBILE, mobileNumber).apply();
    }

    public void setFirstName(String firstName) {
        prefs.edit().putString(Constant.Session.FIRST_NAME, firstName).apply();
    }

    public void setLastName(String lastName) {
        prefs.edit().putString(Constant.Session.LASTNAME, lastName).apply();
    }

    public void setGender(int gender) {
        prefs.edit().putInt(Constant.Session.GENDER, gender).apply();
    }

    public void setBirthDate(String birthDate) {
        prefs.edit().putString(Constant.Session.BIRTHDATE, birthDate).apply();
    }

    public void setEmail(String email) {
        prefs.edit().putString(Constant.Session.EMAIL, email).apply();
    }

    public void setScore(int score) {
        prefs.edit().putInt(Constant.Session.SCORE, score).apply();
    }

    public void setAddress(String address) {
        prefs.edit().putString(Constant.Session.ADDRESS, address).apply();
    }

    public void setTerminalId(String terminalId) {
        prefs.edit().putString(Constant.Session.TERMINAL_ID, terminalId).apply();
    }


    public void setShopName(String shopName) {
        prefs.edit().putString(Constant.Session.SHOP_NAME, shopName).apply();
    }

    public void setTokenId(String tokenId) {
        prefs.edit().putString(Constant.Session.TOKEN_ID, tokenId).apply();
    }

    public void setAppVersion(int ver) {
        prefs.edit().putInt(Constant.Session.APP_VERSION, ver).apply();
    }

    public void setIsFirstRun(boolean isFirstRun) {
        prefs.edit().putBoolean(Constant.Session.IS_FIRST_RUN, isFirstRun).apply();
    }


    public boolean IsFirstRun() {
        boolean isFirstRun = prefs.getBoolean(Constant.Session.IS_FIRST_RUN, true);
        return isFirstRun;
    }

    public String getMobileNumber() {
        String mobile = prefs.getString(Constant.Session.MOBILE, "");
        return mobile;
    }

    public String getFirstName() {
        String firstName = prefs.getString(Constant.Session.FIRST_NAME, "");
        return firstName;
    }

    public String getLastName() {
        String lastName = prefs.getString(Constant.Session.LASTNAME, "");
        return lastName;
    }

    public int getGender() {
        int gender = prefs.getInt(Constant.Session.GENDER, 0);
        return gender;
    }

    public String getBirthDate() {
        String birthDate = prefs.getString(Constant.Session.BIRTHDATE, "");
        return birthDate;
    }

    public int getScore() {
        int score = prefs.getInt(Constant.Session.SCORE, -1);
        return score;
    }

    public int getAppVersion() {
        int version = prefs.getInt(Constant.Session.APP_VERSION, -1);
        return version;
    }

    public String getAddress() {

        String address = prefs.getString(Constant.Session.ADDRESS, "");
        return address;
    }

    public String getTerminalId() {

        String terminalId = prefs.getString(Constant.Session.TERMINAL_ID, "");
        return terminalId;
    }

    public String getTokenId() {
        String tokenId = prefs.getString(Constant.Session.TOKEN_ID, "");
        return tokenId;
    }

    public String getEmail() {
        String email = prefs.getString(Constant.Session.EMAIL, "");
        return email;
    }

    public void clear() {
//        prefs.edit().remove(Constant.Session.FirstName).apply();
        prefs.edit().clear();
        prefs.edit().commit();
    }

}
