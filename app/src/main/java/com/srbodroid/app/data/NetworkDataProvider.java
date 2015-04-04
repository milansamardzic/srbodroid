package com.srbodroid.app.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainApp;
import com.srbodroid.app.R;
import com.srbodroid.app.network.Internet;
import com.srbodroid.app.network.Network;
import com.srbodroid.app.network.XMLUtility;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.Utility;


/**
 * Created by pedja on 7.11.14. 09.34.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public class NetworkDataProvider<T> implements DataProvider<T>
{
    String url;
    T resultData;
    int requestCode;
    Handler handler;
    Object[] optParams;

    public NetworkDataProvider(String url, int requestCode, Object... optParams)
    {
        if(requestCode <= 0)
        {
            throw new IllegalArgumentException("invalid request code");
        }
        this.optParams = optParams;
        this.requestCode = requestCode;
        this.url = url;
        handler = new Handler(Looper.getMainLooper());//main thread handler for displaying toasts
    }

    @Override
    public boolean load()
    {
        if(!Network.isNetworkAvailable(MainApp.getContext()))
        {
            return false;
        }
        else
        {
            if(BuildConfig.DEBUG) Log.d(Constants.LOG_TAG, String.format("NetworkDataProvider::load()[requestCode=%s]", requestCode));
            final XMLUtility jsonUtility = new XMLUtility(Internet.httpGet(url));
            switch (requestCode)
            {
                case REQUEST_CODE_NEWS_RSS:
                    jsonUtility.parseNewsRss();
                    break;
            }
            if (jsonUtility.getParseObject() != null)
            {
                resultData = jsonUtility.getParseObject();
                return true;
            }
            else
            {
                final Internet.Response response = jsonUtility.getServerResponse();
                if(response != null)
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Utility.showToast(MainApp.getContext(),
                                    response.responseMessage != null && !response.isResponseOk() ? response.responseMessage : MainApp.getContext().getString(R.string.unknown_error));

                        }
                    });
                }
                return false;
            }
        }
    }

    @Override
    public T getResult()
    {
        return resultData;
    }
}
