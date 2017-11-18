package com.technotapp.servicestation.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.technotapp.servicestation.application.Constant;


public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setMobileNumber(String mobileNumber) {
        prefs.edit().putString(Constant.Session.MOBILE, mobileNumber).apply();
    }

    public void setIsCustomer(boolean isCustomer) {
        prefs.edit().putBoolean(Constant.Session.IS_CUSTOMER, isCustomer).apply();
    }

    public void setFirstName(String firstName) {
        prefs.edit().putString(Constant.Session.FIRST_NAME, firstName).apply();
    }

    public void setLastName(String lastName) {
        prefs.edit().putString(Constant.Session.LASTNAME, lastName).apply();
    }

    public void setGender(boolean gender) {
        prefs.edit().putBoolean(Constant.Session.GENDER, gender).apply();
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

    public void setTokenId(String tokenId) {
        prefs.edit().putString(Constant.Session.ADDRESS, tokenId).apply();
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

    public boolean getGender() {
        boolean gender = prefs.getBoolean(Constant.Session.GENDER, false);
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

    public String getAddress() {

        String score = prefs.getString(Constant.Session.SCORE, "");
        return score;
    }

    public String getTokenId() {
        String tokenId = prefs.getString(Constant.Session.TOKEN_ID, "");
        return tokenId;
    }

    public String getEmail() {
        String email = prefs.getString(Constant.Session.EMAIL, "");
        return email;
    }

    public boolean isCustomer() {
        boolean email = prefs.getBoolean(Constant.Session.IS_CUSTOMER, false);
        return email;
    }


    public void clear() {
//        prefs.edit().remove(Constant.Session.FirstName).apply();
        prefs.edit().clear();
        prefs.edit().commit();
    }

}
