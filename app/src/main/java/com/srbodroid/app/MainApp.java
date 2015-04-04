package com.srbodroid.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;

import com.android.volley.VolleyLog;
import com.android.volley.cache.DiskLruBasedCache;
import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.data.MemCache;
import com.srbodroid.app.model.DaoMaster;
import com.srbodroid.app.model.DaoSession;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.ExceptionHandler;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by pedja on 10/8/13 10.15.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 *
 * Main Application class
 * Created when application is first started
 * It stays in memory as long as app is alive
 *
 * @author Predrag Čokulov
 */
public class MainApp extends Application
{
    private static  MainApp mainApp = null;
    private static Context context;

    private Activity currentActivity = null;
    private DaoSession daoSession;

    public DiskLruBasedCache.ImageCacheParams cacheParams;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.srbodroid.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.srbodroid";
    //sync interval in seconds
    public static final long SYNC_INTERVAL = 60 * 10;//every 10 minutes
    // The account name
    public static final String ACCOUNT = "Sync Account";
    Account mAccount;

    public synchronized static MainApp getInstance()
    {
        if(mainApp == null)
        {
            mainApp = new MainApp();
        }
        return mainApp;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Crashlytics.start(this);//setup crashlytics
        context = this.getApplicationContext();
        mainApp = this;

        if (Constants.STRICT_MODE)//strict mode prints all kind of debug messages/warnings/errors(i/o on main thread, closable objects not closed ...)
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(Constants.CRASH_REPORTS_FOLDER.getAbsolutePath()));

        initImageLoader();

        mAccount = createSyncAccount(this);
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        getDaoSession();//make database initiate(update if needed) before anything else happens

    }

    /**
     * Get dao session instance
     * If daoSession instance is null new will be created, else it will just return existing instance*/
    public DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_TABLE_NAME, null);
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_TABLE_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            if (BuildConfig.DEBUG)
            {
                QueryBuilder.LOG_SQL = true;
                QueryBuilder.LOG_VALUES = true;
            }
        }
        return daoSession;
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context)
    {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        accountManager.addAccountExplicitly(newAccount, null, null);
        return newAccount;
    }

    public void initImageLoader()
    {
        cacheParams = new DiskLruBasedCache.ImageCacheParams(getContext().getApplicationContext(), Constants.EXTERNAL_CACHE_DIR.getAbsolutePath());
        cacheParams.setMemCacheSizePercent(0.2f);
        cacheParams.diskCacheSize = 1024 * 1024 * 200;//200MB, is it ot much?
        cacheParams.diskCacheEnabled = true;
        cacheParams.memoryCacheEnabled = false;
        VolleyLog.DEBUG = false;
    }



    public static Context getContext()
    {
        return context;
    }

    public Activity getCurrentActivity()
    {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity)
    {
        this.currentActivity = currentActivity;
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        MemCache.getInstance().flushCache();
    }
}
