package com.softwarealliance.listplix.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String token;
    String email;
    Boolean isLoggedIn;
    int userID;
    int projectID;

    public LocalStorage(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("STORAGE_LOGIN_API",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getToken() {
        token = sharedPreferences.getString("TOKEN","");
        return token;
    }

    public void setToken(String token) {
        editor.putString("TOKEN",token);
        editor.commit();
        this.token = token;
    }

    public String fetchAuthToken() {
        token = sharedPreferences.getString("TOKEN","");
        return token;
    }

    public String getEmail() {
        token = sharedPreferences.getString("EMAIL","");
        return email;
    }

    public void setEmail(String email) {
        editor.putString("EMAIL",token);
        editor.commit();
        this.email = email;
    }



    public Boolean getLoggedIn() {
        isLoggedIn = sharedPreferences.getBoolean("LOGGEDIN",false);
        return isLoggedIn;
    }

    public int getUserID() {
        userID = sharedPreferences.getInt("USERID",0);
        return userID;
    }

    public void setUserID(int userID) {
        editor.putInt("USERID",userID);
        editor.commit();
        this.userID = userID;
    }

    public int getProjectID() {
        projectID = sharedPreferences.getInt("PROJECTID",0);
        return projectID;
    }

    public void setProjectID(int projectID) {
        editor.putInt("PROJECTID",projectID);
        editor.commit();
        this.projectID = projectID;
    }



    public void setLoggedIn(Boolean loggedIn) {
        editor.putBoolean("LOGGEDIN",loggedIn);
        editor.commit();
        isLoggedIn = loggedIn;
    }
}
