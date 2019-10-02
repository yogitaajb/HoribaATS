package com.horibacare.ats;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "horiba-ats";
    private static final String isLogin = "isLogin";
    private static final String token = "token";
    private static final String userId = "userId";
    private static final String userType = "userType";  //horiba, customer
    private static final String imei = "imei";
    private static final String shopName = "shopName";
    private static final String name = "name";
    private static final String balance = "balance";
    private static final String isSignUp = "isSignUp";
    private static final String isRegister = "isRegister";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

    public boolean checkSharedPrefs(String key) {
        if (pref.contains(key)) {
            return true;
        }
        return false;
    }

    public void setUserId(String value) {
        editor.putString(userId, value);
        editor.commit();
    }
    public String getUserId() {
        return pref.getString(userId, "");
    }

    public String getUserType() {
        return pref.getString(userType, "");
    }

    public void setUserType(String value) {
        editor.putString(userType, value);
        editor.commit();
    }

    public String getImei() {
        return pref.getString(imei, "");
    }
    public void setImei(String value) {
        editor.putString(imei, value);
        editor.commit();
    }

    public String getShopName() {
        return pref.getString(shopName, "");
    }
    public void setShopName(String value) {
        editor.putString(shopName, value);
        editor.commit();
    }

    public String getName() {
        return pref.getString(name, "");
    }
    public void setName(String value) {
        editor.putString(name, value);
        editor.commit();
    }

    public float getBalance() {
        return pref.getFloat(balance, 0);
    }
    public void setBalance(float value) {
        editor.putFloat(balance, value);
        editor.commit();
    }

    public void setIsLogin(boolean value) {
        editor.putBoolean(isLogin, value);
        editor.commit();
    }
    public boolean getIsLogin() {
        return pref.getBoolean(isLogin, false);
    }

    public void setToken(String value) {
        editor.putString(token, value);
        editor.commit();
    }
    public String getToken() {
        return pref.getString(token, "");
    }

}
