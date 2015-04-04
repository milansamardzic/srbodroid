package com.srbodroid.app.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainApp;
import com.srbodroid.app.R;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pedja on 10/9/13 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 * @author Predrag Čokulov
 */
public class Utility
{
    /**
     * This class can not be instantiated, it will throw exception if you try to instantiate it
     *
     * @throws IllegalStateException
     */
    public Utility()
    {
        throw new IllegalStateException("Class " + this.getClass().getName() + " is not instantiable!");
    }


    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, String message,
                                                     String title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title != null ? Html.fromHtml(title) : null);
        builder.setMessage(message != null ? Html.fromHtml(message) : null);
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, int message,
                                                     int title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title > 0)builder.setTitle(Html.fromHtml(context.getString(title)));
        builder.setMessage(Html.fromHtml(context.getString(message)));
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * No Network dialog
     */
    public static AlertDialog buildNoNetworkDialog(Context context, int message,
                                                   int title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, listener);
        builder.setCancelable(false);
        return builder.create();
    }

    /**
     * General Purpose Toast
     */
    public static void showToast(Context context, String message)
    {
        Toast.makeText(context, message != null ? Html.fromHtml(message) : null, Toast.LENGTH_LONG).show();
    }

    /**
     * General Purpose Toast
     */
    public static void showToast(Context context, int resId)
    {
        Toast.makeText(context, Html.fromHtml(context.getString(resId)), Toast.LENGTH_LONG).show();
    }

    /**
     * Converts Density-independent pixel (dp) into pixels
     *
     * @param dp input value
     * @param context Context for retrieving resources
     */
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * Check if app is installed on the device
     * @param uri package name of the application
     * */
    public static boolean isAppInstalled(Context context, String uri)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    /**
     * Check if email is in valid format(eg. test@gmail.com)
     * @param email email to validate
     * */
    public static boolean isEmailValid(String email)
    {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }





    /**
     * Tries to parse String as Integer
     * if error occurs default value will be returned
     * @param value String to parse as int
     * @param mDefault default value to return in case of value is not integer
     * @return parsed int or default value
     * */
    public static int parseInt(String value, int mDefault)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            String log = "Utility.parseInt >> failed to parse " + value + " as integer";
            if(BuildConfig.DEBUG)Log.w(Constants.LOG_TAG, log);
            Crashlytics.log(log);
        }
        return mDefault;
    }

    /**
     * Tries to parse String as Double
     * if error occurs default value will be returned
     * @param value String to parse as double
     * @param mDefault default value to return in case of value is not double
     * @return parsed double or default value
     * */
    public static double parseDouble(String value, double mDefault)
    {
        try
        {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e)
        {
            String log = "Utility.parseDouble >> failed to parse " + value + " as double";
            if(BuildConfig.DEBUG)Log.w(Constants.LOG_TAG, log);
            Crashlytics.log(log);
        }
        return mDefault;
    }

    /**
     * Tries to parse String as Long
     * if error occurs default value will be returned
     * @param value String to parse as long
     * @param mDefault default value to return in case of value is not long
     * @return parsed long or default value
     * */
    public static long parseLong(String value, long mDefault)
    {
        try
        {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e)
        {
            String log = "Utility.parseInt >> failed to parse " + value + " as integer";
            if(BuildConfig.DEBUG)Log.w(Constants.LOG_TAG, log);
            Crashlytics.log(log);
        }
        return mDefault;
    }

    /**
     * Generate md5 sum from string
     * */
    public static String md5(final String s)
    {
        final String MD5 = "MD5";
        try
        {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
            {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e)
        {
            String log = "Utility.md5 >> error: " + e.getMessage();
            if(BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, log);
            Crashlytics.log(log);
        }
        return "";
    }



    /**
     * Encode string as URL UTF-8
     * */
    @SuppressWarnings("deprecation")
    public static String encodeString(String string)
    {
        return URLEncoder.encode(string);
    }

    /**
     * Read file from /res/raw to string
     * @param rawResId of the file
     * @return Content of the file as string
     * */
    public static String readRawFile(int rawResId) throws IOException
    {
        InputStream is = MainApp.getContext().getResources().openRawResource(rawResId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String read;
        StringBuilder sb = new StringBuilder();
        while((read = br.readLine()) != null)
        {
            sb.append(read);
        }
        return sb.toString();
    }

    public static String decodeString(String input)
    {
        try
        {
            return URLDecoder.decode(input, Constants.ENCODING);
        }
        catch (Exception e)
        {
            //if(BuildConfig.DEBUG)e.printStackTrace();
            //Crashlytics.logException(e);
            return input;
        }
    }


    public static String toUpperCase(String text)
    {
        return text == null ? null : text.toUpperCase();
    }

    public static String sanitizeUrl(String url)
    {
        if(url == null)return null;
        url = url.replaceAll("\\\\", "/");
        url = url.replaceAll("(?<!http:)//", "/");
        url = Uri.encode(url, "@#&=*+-_.,:!?()/~'%");
        return url;
    }

    public static void openLink(Activity activity, String url)
    {
        if(!url.startsWith("http://") && !url.startsWith("https://"))url = "http://" + url;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent = Intent.createChooser(intent, null);
        activity.startActivity(intent);
    }

    public static boolean isStringValid(String value)
    {
        return !TextUtils.isEmpty(value) && !"null".equals(value);
    }

    public static String formatDateString(@NotNull SimpleDateFormat inFormat, @NotNull SimpleDateFormat outFormat, String date)
    {
        try
        {
            return outFormat.format(inFormat.parse(date));
        }
        catch (ParseException e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if current thread (thread that this method is called from) is applications main thread*/
    public static boolean isMainThread()
    {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Date parseDateTime(@NotNull SimpleDateFormat format, String dateTime)
    {
        try
        {
            return format.parse(dateTime);
        }
        catch (ParseException e)
        {
            Crashlytics.logException(e);
            if(BuildConfig.DEBUG)e.printStackTrace();
            return new Date(0);
        }
    }

    public static String formatDateString(@NotNull SimpleDateFormat outFormat, long date)
    {
        return outFormat.format(date);
    }
}
