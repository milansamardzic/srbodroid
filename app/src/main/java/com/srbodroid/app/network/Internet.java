package com.srbodroid.app.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainApp;
import com.srbodroid.app.R;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.MyTimer;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * @author Predrag Čokulov
 */

public class Internet
{
    private static final boolean log = BuildConfig.DEBUG;

    private Internet()
    {

    }

    /**
     * Executes HTTP GET request and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     *
     * @param url requestUrl
     * @return server response as string
     */
    public static Response httpGet(String url)
    {
        MyTimer timer = new MyTimer();
        Response response = new Response();
        try
        {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(Constants.CONN_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();

            response.responseStream = conn.getInputStream();
            response.code = conn.getResponseCode();
            response.responseMessage = conn.getResponseMessage();
            if (log) Log.d(Constants.LOG_TAG, "httpGet[" + url + "]: " + response);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.network_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = url;
        }
        timer.log("Internet:httpGet>>time");

        return response;
    }

    public static class Response
    {
        public static final int RESPONSE_CODE_UNKNOWN_ERROR = -1;
        public static final int RESPONSE_CODE_UNSUPPORTED_ENCODING = -2;
        public static final int RESPONSE_CODE_MALFORMED_URL = -3;
        public static final int RESPONSE_CODE_IO_ERROR = -4;

        public int code = RESPONSE_CODE_UNKNOWN_ERROR;
        public String responseMessage;
        public InputStream responseStream;
        public String request;

        public boolean isResponseOk()
        {
            return code < 400 && code > 0;
        }

        @Override
        public String toString()
        {
            return "Response{" +
                    "request='" + request + '\'' +
                    ", responseMessage='" + responseMessage + '\'' +
                    ", code=" + code +
                    '}';
        }
    }
}