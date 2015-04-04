package com.srbodroid.app.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.srbodroid.app.MainApp;

/**
 * Created by pedja on 2/12/14.
 * Handles all reads and writes to SharedPreferences
 * @author Predrag Čokulov
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsManager
{
    private static final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());
    private static final SharedPreferences syncPrefs = MainApp.getContext().getSharedPreferences("sync_prefs", Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? Context.MODE_MULTI_PROCESS : 0);

    public enum Key
    {
        email, home_phone, cell_phone, name, last_local_news_date
    }

    public static void setEmail(String email)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.email.toString(), email);
        editor.apply();
    }

    public static void setHomePhone(String phone)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.home_phone.toString(), phone);
        editor.apply();
    }

    public static void setCellPhone(String phone)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.cell_phone.toString(), phone);
        editor.apply();
    }

    public static void setName(String name)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.name.toString(), name);
        editor.apply();
    }

    public static String getEmail()
    {
        return prefs.getString(Key.email.toString(), "");
    }

    public static String getHomePhone()
    {
        return prefs.getString(Key.home_phone.toString(), "");
    }

    public static String getCellPhone()
    {
        return prefs.getString(Key.cell_phone.toString(), "");
    }

    public static String getName()
    {
        return prefs.getString(Key.name.toString(), "");
    }

    public static long getLastLocalNewsDate()
    {
        return syncPrefs.getLong(Key.last_local_news_date.toString(), -1);
    }

    public static void setLastLocalNewsDate(long lastNewsDate)
    {
        SharedPreferences.Editor editor = syncPrefs.edit();
        editor.putLong(Key.last_local_news_date.toString(), lastNewsDate);
        editor.apply();
    }


    public static void clearAllPrefs()
    {
        SharedPreferences.Editor editor = prefs.edit();
        for(Key key : Key.values())
        {
            editor.remove(key.toString());
        }
        editor.apply();
    }
}
