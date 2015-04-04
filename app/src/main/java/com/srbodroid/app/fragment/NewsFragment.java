package com.srbodroid.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.srbodroid.app.BuildConfig;
import com.srbodroid.app.NewsDetailsActivity;
import com.srbodroid.app.R;
import com.srbodroid.app.adapter.NewsAdapter;
import com.srbodroid.app.data.DataLoader;
import com.srbodroid.app.data.DataProvider;
import com.srbodroid.app.data.MemoryCacheDataProvider;
import com.srbodroid.app.data.NetworkDataProvider;
import com.srbodroid.app.model.News;
import com.srbodroid.app.utility.Constants;
import com.srbodroid.app.utility.SettingsManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedja on 11.12.14. 16.03.
 * This class is part of the Srbodroid
 * Copyright © 2014 ${OWNER}
 */
public class NewsFragment extends Fragment implements DataLoader.LoadListener<List<News>>, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener
{
    DataLoader<List<News>> dataLoader;

    NetworkDataProvider<List<News>> networkProvider;
    MemoryCacheDataProvider<List<News>> memProvider;

    ProgressBar pbLoading;
    NewsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView tvEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_news, container, false);

        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvEmptyView = (TextView)view.findViewById(R.id.tvEmptyView);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this);

        ListView lvNews = (ListView)view.findViewById(R.id.lvNews);
        adapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        View header = inflater.inflate(R.layout.layout_news_header, lvNews, false);

        lvNews.addHeaderView(header, null, false);

        lvNews.setAdapter(adapter);

        lvNews.setOnItemClickListener(this);

        dataLoader = new DataLoader<>();
        dataLoader.setListener(this);

        List<DataProvider<List<News>>> providers = new ArrayList<>();
        networkProvider = new NetworkDataProvider<>(Constants.NEWS_RSS_URL, DataProvider.REQUEST_CODE_NEWS_RSS);
        memProvider = new MemoryCacheDataProvider<>(NavigationDrawerFragment.NDItem.Id.news.toString());
        providers.add(memProvider);
        providers.add(networkProvider);

        dataLoader.setProviders(providers);
        dataLoader.loadData();

        return view;
    }

    @Override
    public void onLoadingFinished(DataLoader.Result<List<News>> result)
    {
        pbLoading.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        if(result.status == DataLoader.Result.STATUS_OK && result.data != null)
        {
            adapter.clear();
            adapter.addAll(new ArrayList<>(result.data));
            adapter.notifyDataSetChanged();
            if(!result.data.isEmpty())
            {
                /*try
                {
                    SettingsManager.setLastLocalNewsDate(Constants.NEWS_TIME_FORMAT_IN.parse(result.data.get(0).pubDate).getTime());
                }
                catch (ParseException e)
                {
                    if(BuildConfig.DEBUG)e.printStackTrace();
                    Crashlytics.logException(e);
                }*/
            }
        }

        tvEmptyView.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoadStarted()
    {

    }

    @Override
    public void onRefresh()
    {
        List<DataProvider<List<News>>> providers = new ArrayList<>();
        providers.add(networkProvider);
        dataLoader.setProviders(providers);
        dataLoader.loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(position == 0)return;//first is header
        NewsDetailsActivity.start(getActivity(), adapter.getItem(position - 1).getId());
    }
}
