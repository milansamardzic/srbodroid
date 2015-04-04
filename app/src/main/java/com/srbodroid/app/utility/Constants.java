package com.srbodroid.app.utility;

import android.os.Environment;

import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Predrag Čokulov*/

public class Constants
{

    // **************************************************
    // Misc
    // **************************************************
    /**
     * Debugging log tag
     * */
    public static final String LOG_TAG = "srbodroid";

    /**
     * HTTP connection timeout
     * */
    public static final int CONN_TIMEOUT = 2 * 60 * 1000;

    /**
     * URL encoding
     * */
    public static final String ENCODING = "UTF-8";
    public static final boolean LOGGING = BuildConfig.DEBUG;
    public static final boolean STRICT_MODE = LOGGING && false;


    private static final String CACHE_FOLDER_NAME = ".cache";
    public static final File INTERNAL_CACHE_DIR = new File(MainApp.getContext().getFilesDir(), CACHE_FOLDER_NAME);
    public static final File EXTERNAL_CACHE_DIR = new File(MainApp.getContext().getExternalCacheDir(), CACHE_FOLDER_NAME);
    public static final File CRASH_REPORTS_FOLDER = new File(EXTERNAL_CACHE_DIR, ".crashes");
    public static final String DB_TABLE_NAME = "srbodroid";

    static
    {
        CRASH_REPORTS_FOLDER.mkdirs();
        INTERNAL_CACHE_DIR.mkdirs();
        EXTERNAL_CACHE_DIR.mkdirs();
    }

    public static final SimpleDateFormat NEWS_TIME_FORMAT_IN = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.UK);
    public static final SimpleDateFormat NEWS_TIME_FORMAT_OUT = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm", new Locale("sr"));

    public static final String NEWS_RSS_URL = "http://feeds.feedburner.com/Srbodroid?format=xml";
}
