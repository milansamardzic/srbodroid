package com.srbodroid.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.ui.NetworkImageViewPlus;
import com.srbodroid.app.model.News;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.DisplayManager;
import com.srbodroid.app.utility.Utility;
import com.srbodroid.app.view.NotifyingScrollView;

import java.text.ParseException;

/**
 * Created by pedja on 12.12.14. 09.22.
 * This class is part of the Srbodroid
 * Copyright © 2014 ${OWNER}
 */
public class NewsDetailsActivity extends ActionBarActivity implements Drawable.Callback, View.OnClickListener
{
    public static final String INTENT_EXTRA_NEWS_ID = "news_id";
    SimpleImageLoader mLoader;

    private Drawable mActionBarBackgroundDrawable;
    int mActionBarAlpha = 0;

    float mImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mImageHeight = getResources().getDimension(R.dimen.news_details_default_image_height);

        mLoader = new SimpleImageLoader(this, MainApp.getInstance().cacheParams);
        mLoader.setMaxImageSize(new DisplayManager().screenWidth);

        News ni = MainApp.getInstance().getDaoSession().getNewsDao().load(getIntent().getLongExtra(INTENT_EXTRA_NEWS_ID, -1));

        ScrollView sv = (ScrollView)findViewById(R.id.svContent);
        final LinearLayout llContent = (LinearLayout)findViewById(R.id.llContent);

        final NetworkImageViewPlus ivImage = (NetworkImageViewPlus)findViewById(R.id.ivImage);
        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        TextView tvText = (TextView)findViewById(R.id.tvText);
        TextView tvTime = (TextView)findViewById(R.id.tvTime);

        ivImage.setDefaultImageResId(R.drawable.no_image);
        ivImage.setImageUrl(ni.getMainImage().getUrl(), mLoader);
        ivImage.setImageListener(new Response.Listener<BitmapDrawable>()
        {
            @Override
            public void onResponse(BitmapDrawable bitmapDrawable)
            {
                ivImage.post(new Runnable()//is this needed ?
                {
                    @Override
                    public void run()
                    {
                        mImageHeight = ivImage.getHeight();
                        llContent.setPadding(0, (int) mImageHeight, 0, 0);
                    }
                });
            }
        });

        tvTitle.setText(ni.getTitle());
        tvText.setText(Html.fromHtml(ni.getContent()));
        tvText.setMovementMethod(LinkMovementMethod.getInstance());

        tvTime.setText(Utility.formatDateString(Constants.NEWS_TIME_FORMAT_OUT, ni.getPubDate()));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            mActionBarBackgroundDrawable.setCallback(this);
        }

        mActionBarBackgroundDrawable = new ColorDrawable(getResources().getColor(R.color.primary)).mutate();
        mActionBarBackgroundDrawable.setAlpha(mActionBarAlpha);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ni.getTitle());
        getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);

        ((NotifyingScrollView)sv).setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt)
            {
                final int headerHeight = ivImage.getHeight()/* - llHeader.getHeight()*/;
                final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
                final int newAlpha = (int) (ratio * 255);
                mActionBarAlpha = newAlpha;
                mActionBarBackgroundDrawable.setAlpha(newAlpha);

                float imageOffset = -(ratio * (mImageHeight / 3f));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
                params.setMargins(params.leftMargin, (int) imageOffset, params.rightMargin, params.bottomMargin);
                ivImage.setLayoutParams(params);
            }
        });

    }

    @Override
    public void invalidateDrawable(Drawable who)
    {
        getSupportActionBar().setBackgroundDrawable(who);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when)
    {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what)
    {

    }

    public static void start(Activity activity, long newsId)
    {
        activity.startActivity(new Intent(activity, NewsDetailsActivity.class).putExtra(INTENT_EXTRA_NEWS_ID, newsId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {

        }
    }
}
