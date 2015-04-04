package com.srbodroid.app.data;

import android.util.Log;

import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.utility.Constants;


/**
 * Created by pedja on 7.11.14. 09.34.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class MemoryCacheDataProvider<T> implements DataProvider<T>
{
    String key;
    T resultData;

    public MemoryCacheDataProvider(String key)
    {
        this.key = key;
    }

    @Override
    public boolean load()
    {
        if (BuildConfig.DEBUG)
            Log.d(Constants.LOG_TAG, String.format("MemoryCacheDataProvider::load()[key=%s]", key));
        resultData = MemCache.getInstance().getFromCache(key);
        return resultData != null;
    }

    @Override
    public T getResult()
    {
        return resultData;
    }
}
