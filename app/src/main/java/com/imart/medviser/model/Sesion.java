package com.imart.medviser.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by imartin on 9/06/16.
 */
public class Sesion {

    public static String user = "";
    public static String pass = "";

    public static void loadSavedLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        user = prefs.getString("user", "");
        pass = prefs.getString("pass", "");
    }
}
