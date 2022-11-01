package com.softwarealliance.listplix.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String token;
    String email;

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

    public String getEmail() {
        token = sharedPreferences.getString("EMAIL","");
        return email;
    }

    public void setEmail(String email) {
        editor.putString("EMAIL",token);
        editor.commit();
        this.email = email;
    }
}
