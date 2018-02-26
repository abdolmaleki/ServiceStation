package com.technotapp.servicestation.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

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

    public void setUserName(String userName) {
        prefs.edit().putString(Constant.Session.USER_NAME, userName).apply();
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

    public void setMenuCategory(String menuCategory) {
        prefs.edit().putString(Constant.Session.MENU_CATEGORY, menuCategory).apply();
    }

    public void setMerchantId(String merchantId) {
        prefs.edit().putString(Constant.Session.ENID_MERCHANT, merchantId).apply();
    }


    public void setShopName(String shopName) {
        prefs.edit().putString(Constant.Session.SHOP_NAME, shopName).apply();
    }

    public void setTokenId(String tokenId) {
        prefs.edit().putString(Constant.Session.TOKEN_ID, tokenId).apply();
    }

    public void setBaseUrl(String baseUrl) {
        prefs.edit().putString(Constant.Session.BASE_URL, baseUrl).apply();
    }

    public void setHashId(String hashId) {
        prefs.edit().putString(Constant.Session.HASH_ID, hashId).apply();
    }

    public void setAppVersion(int ver) {
        prefs.edit().putInt(Constant.Session.APP_VERSION, ver).apply();
    }

    public void setIsNewMenu(boolean isNewMenu) {
        prefs.edit().putBoolean(Constant.Session.IS_NEW_MENU, isNewMenu).apply();
    }

    public void setLastVersion(int ver) {
        prefs.edit().putInt(Constant.Session.LAST_VERSION, ver).apply();
    }

    public void setIsFirstRun(boolean isFirstRun) {
        prefs.edit().putBoolean(Constant.Session.IS_FIRST_RUN, isFirstRun).apply();
    }

    public void setDescription(String description) {
        prefs.edit().putString(Constant.Session.DESCRIPTION, description).apply();
    }

    public void setEconomicCode(String economicCode) {
        prefs.edit().putString(Constant.Session.ECONOMIC_CODE, economicCode).apply();
    }

    public void setManagerName(String managerName) {
        prefs.edit().putString(Constant.Session.MANAGERNAME, managerName).apply();
    }

    public void setRegisterDate(String registerDate) {
        prefs.edit().putString(Constant.Session.REGISTER_DATE, registerDate).apply();
    }

    public void setRegisterCode(String registerCode) {
        prefs.edit().putString(Constant.Session.REGISTER_ID, registerCode).apply();
    }

    public void setShopCategory(String shopCategory) {
        prefs.edit().putString(Constant.Session.SHPCATEGORY, shopCategory).apply();
    }

    public void setPinKey(String pinKey) {
        prefs.edit().putString(Constant.Session.PIN_KEY, pinKey).apply();
    }

    public void setTel(String tel) {
        prefs.edit().putString(Constant.Session.TEL, tel).apply();
    }

    public void setFax(String fax) {
        prefs.edit().putString(Constant.Session.FAX, fax).apply();
    }


    public boolean IsFirstRun() {
        boolean isFirstRun = prefs.getBoolean(Constant.Session.IS_FIRST_RUN, true);
        return isFirstRun;
    }

    public byte[] getPinKey() {
        String key = prefs.getString(Constant.Session.PIN_KEY, "");
        return Base64.decode(key, Base64.DEFAULT);
    }

    public String getTelephone() {


        String tel = prefs.getString(Constant.Session.TEL, "");
        return tel;
    }

    public String getMobile() {
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

    public String getUserName() {
        String userName = prefs.getString(Constant.Session.USER_NAME, "");
        return userName;
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

    public int getLastVersion() {
        int version = prefs.getInt(Constant.Session.LAST_VERSION, -1);
        return version;
    }

    public boolean IsNewMenu() {
        boolean isNewMenu = prefs.getBoolean(Constant.Session.IS_NEW_MENU, false);
        return isNewMenu;
    }

    public String getAddress() {

        String address = prefs.getString(Constant.Session.ADDRESS, "");
        return address;
    }

    public String getShopName() {

        String shopName = prefs.getString(Constant.Session.SHOP_NAME, "");
        return shopName;
    }

    public String getTerminalId() {

        String terminalId = prefs.getString(Constant.Session.TERMINAL_ID, "");
        return terminalId;
    }

    public String getMerchantId() {

        String merchantId = prefs.getString(Constant.Session.ENID_MERCHANT, "");
        return merchantId;
    }

    public String getMenuCategory() {

        String menuCategory = prefs.getString(Constant.Session.MENU_CATEGORY, "");
        return menuCategory;
    }

    public String getTokenId() {
        String tokenId = prefs.getString(Constant.Session.TOKEN_ID, "");
        return tokenId;
    }

    public String getHashId() {
        String hashId = prefs.getString(Constant.Session.HASH_ID, "");
        return hashId;
    }

    public String getEmail() {
        String email = prefs.getString(Constant.Session.EMAIL, "");
        return email;
    }

    public String getBaseUrl() {
        String url = prefs.getString(Constant.Session.BASE_URL, "");
        return url;
    }

    public void clear() {
//        prefs.edit().remove(Constant.Session.FirstName).apply();
        prefs.edit().clear();
        prefs.edit().commit();
    }

}
