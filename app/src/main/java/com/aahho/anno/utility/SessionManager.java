package com.aahho.anno.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikdas on 28/10/17.
 */

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;
    private String PREFERENCE_NAME = "AnnoSharedPrefUser";
    private Context context;
    private final String isLoggedIn = "isLoggedIn";
    private final String userId = "userId";
    private final String username = "username";
    private final String token = "fcmToken";

//    public SessionManager(){
//        sharedPreferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
//        editor = sharedPreferences.edit();
//    }

    public SessionManager(final Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createRegisterSession(final String id, final String name){
        editor.putString(userId, id);
        editor.putString(username, name);
        editor.commit();
    }

    public void createLoginSession(final String id, final String name){
        editor.putString(userId, id);
        editor.putString(username, name);
        editor.putBoolean(isLoggedIn, true);
        editor.commit();
    }

    public void storeFcmToken(String ftoken){
        editor.putString(token, ftoken);
        editor.commit();
    }

    public Map<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put(userId, sharedPreferences.getString(userId, null));
        userDetails.put(username, sharedPreferences.getString(username, null));
        return userDetails;
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(isLoggedIn, false);
    }

    public String hasUserId(){
        return sharedPreferences.getString(userId, null);
    }

    public String getFcmToken(){
        return sharedPreferences.getString(token, null);
    }

    public  void logout(){
        editor.putBoolean(isLoggedIn, false);
        editor.commit();
    }


}
