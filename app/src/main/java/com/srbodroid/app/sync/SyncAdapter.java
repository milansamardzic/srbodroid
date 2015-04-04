package com.srbodroid.app.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.MainActivity;
import com.srbodroid.app.R;
import com.srbodroid.app.data.DataLoader;
import com.srbodroid.app.data.DataProvider;
import com.srbodroid.app.data.NetworkDataProvider;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.SettingsManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedja on 26.1.15. 11.21.
 * This class is part of the Srbodroid
 * Copyright © 2015 ${OWNER}
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    public static final long DATE_2013_MILLIS = 1356994800000l;
    private static final int NOTIFICATION_ID = 2012;

    public SyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        /*DataLoader<List<NewsItem>> dataLoader = new DataLoader<>();

        List<DataProvider<List<NewsItem>>> providers = new ArrayList<>();
        NetworkDataProvider<List<NewsItem>> networkProvider = new NetworkDataProvider<>(Constants.NEWS_RSS_URL, DataProvider.REQUEST_CODE_NEWS_RSS);
        providers.add(networkProvider);

        dataLoader.setProviders(providers);
        DataLoader.Result<List<NewsItem>> result = dataLoader.loadData(true);
        //FIXME we hope that last/first news in list is latest, sort manually and get news with latest date
        if(result.data == null || result.data.isEmpty())return;
        NewsItem firstNews = result.data.get(0);
        try
        {
            long pubDate = Constants.NEWS_TIME_FORMAT_IN.parse(firstNews.pubDate).getTime();
            long lastLocalNewsDate = SettingsManager.getLastLocalNewsDate();
            System.out.println(":sync pubDate: " + pubDate);
            System.out.println(":sync lasLocalNewsDate: " + lastLocalNewsDate);
            if(lastLocalNewsDate <= DATE_2013_MILLIS)
            {
                SettingsManager.setLastLocalNewsDate(pubDate);
                return;//if last stored local news date is older than year 2013, its probably a bug
            }
            if(pubDate > lastLocalNewsDate)
            {
                //post notification
                postNotification(result.data);
                SettingsManager.setLastLocalNewsDate(pubDate);
            }
        }
        catch (ParseException e)
        {
            if(BuildConfig.DEBUG)e.printStackTrace();
            Crashlytics.logException(e);
        }*/
    }

    /*private void postNotification(List<NewsItem> list)
    {
        List<NewsItem> newNews = findNewsNewerThan(list, SettingsManager.getLastLocalNewsDate());
        if(newNews.isEmpty())return;

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent dataIntent = new Intent(getContext(), MainActivity.class);
        dataIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(dataIntent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());

        //TODO generate notification icon
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image));

        mBuilder.setSmallIcon(R.drawable.no_image);

        String title = generateNotificationTitle(newNews);
        String text = generateNotificationText(newNews);

        mBuilder.setContentTitle(title);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        mBuilder.setNumber(newNews.size());
        mBuilder.setContentText(text);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.FLAG_ONLY_ALERT_ONCE);

        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String generateNotificationTitle(List<NewsItem> newNews)
    {
        if(newNews.size() == 1)
        {
            return newNews.get(0).title;
        }
        else
        {
            return getContext().getString(R.string.app_name);
        }
    }

    private String generateNotificationText(List<NewsItem> newNews)
    {
        if(newNews.size() == 1)
        {
            return Html.fromHtml(newNews.get(0).description).toString();
        }
        else
        {

            return null;//return getContext().getString(R.string.new_news_notification_text, newNews.size(), getCountDescriptorForCount(newNews.size()));
        }
    }

    private String getCountDescriptorForCount(int size)
    {
        int lastDigit = size % 10;
        int[] fileStrings = new int[]{R.string.nova, R.string.nove, R.string.nove, R.string.nove, R.string.novih,
                R.string.novih, R.string.novih, R.string.novih, R.string.novih, R.string.novih,
                R.string.novih};
        return getContext().getString(fileStrings[lastDigit]);
        return null;
    }

    private List<NewsItem> findNewsNewerThan(List<NewsItem> newList, long date)
    {
        List<NewsItem> filtered = new ArrayList<>();
        for(NewsItem item : newList)
        {
            try
            {
                if(Constants.NEWS_TIME_FORMAT_IN.parse(item.pubDate).getTime() > date)
                {
                    filtered.add(item);
                }
            }
            catch (ParseException e)
            {
                if(BuildConfig.DEBUG)e.printStackTrace();
                Crashlytics.logException(e);
            }
        }
        return filtered;
    }*/
}
