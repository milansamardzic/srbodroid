package com.srbodroid.app.utility;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by pedja on 11/18/13 10.13.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 * @author Predrag Čokulov
 */
public class  ExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private final Thread.UncaughtExceptionHandler defaultUEH;
    private final String localPath;

    public ExceptionHandler(String localPath)
    {
        this.localPath = localPath;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        String timestamp = Constants.LOG_TAG + System.currentTimeMillis() + "";
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        if(BuildConfig.DEBUG)e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".stacktrace";

        if (localPath != null)
        {
            File file = new File(localPath);
            if(!file.exists())file.mkdirs();
            writeToFile(stacktrace, filename);
        }

        //don't force app to stop(show fc dialog) if we are not on main thread
        //not sure if this is smart or not
        //that thread will remain dead unless started manually again
        //for example if GcmIntentService crashes only way to receive push again is to restart app completely
        //
        //if(t == Looper.getMainLooper().getThread())
        //{
            defaultUEH.uncaughtException(t, e);
        //}
        //else
        //{
        //    Crashlytics.setBool("main_thread", false);
        //    Crashlytics.logException(e);
        //}
    }

    /**
     * Write exception stacktrace to file
     * */
    private void writeToFile(String stacktrace, String filename)
    {
        try
        {
            BufferedWriter bos = new BufferedWriter(new FileWriter(localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        }
        catch (Exception e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
            Crashlytics.logException(e);
        }
    }


}
