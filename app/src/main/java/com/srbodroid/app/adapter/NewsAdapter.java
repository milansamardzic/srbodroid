package com.srbodroid.app.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.cache.plus.SimpleImageLoader;
import com.srbodroid.app.MainApp;
import com.srbodroid.app.R;
import com.srbodroid.app.model.News;
import com.srbodroid.app.utility.DisplayManager;
import com.srbodroid.app.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by pedja on 11.12.14. 16.33.
 * This class is part of the Srbodroid
 * Copyright © 2014 ${OWNER}
 */
public class NewsAdapter extends ArrayAdapter<News>
{
    LayoutInflater inflater;
    SimpleImageLoader mLoader;
    SimpleDateFormat formatMonthDay;
    SimpleDateFormat formatYear;
    public NewsAdapter(Context context, List<News> items)
    {
        super(context, 0, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoader = new SimpleImageLoader(context, MainApp.getInstance().cacheParams);
        mLoader.setDefaultDrawable(R.drawable.no_image);
        mLoader.setMaxImageSize((int) (new DisplayManager().screenWidth * 1.1f));
        formatMonthDay = new SimpleDateFormat("MM/dd");
        formatYear = new SimpleDateFormat("yyyy");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        News ni = getItem(position);
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_news, parent, false);
            holder = new ViewHolder();

            holder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
            holder.tvText = (TextView)convertView.findViewById(R.id.tvText);
            holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            holder.tvDate1 = (TextView)convertView.findViewById(R.id.tvDate1);
            holder.tvDate2 = (TextView)convertView.findViewById(R.id.tvDate2);
            holder.tvAuthor = (TextView)convertView.findViewById(R.id.tvAuthor);
            holder.tvCommentCount = (TextView)convertView.findViewById(R.id.tvCommentCount);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(ni.getTitle());
        holder.tvText.setText(ni.getDescription());
        holder.tvDate1.setText(Utility.formatDateString(formatMonthDay, ni.getPubDate()));
        holder.tvDate2.setText(Utility.formatDateString(formatYear, ni.getPubDate()));
        holder.tvCommentCount.setText(ni.getComment_count() + "");
        holder.tvAuthor.setText(Html.fromHtml(getContext().getResources().getString(R.string.author, ni.getAuthor(), ni.getCategory())));
        mLoader.get(ni.getMainImage().getUrl(), holder.ivImage);

        return convertView;
    }

    class ViewHolder
    {
        TextView tvTitle, tvDate1, tvDate2, tvText, tvAuthor, tvCommentCount;
        ImageView ivImage;
    }
}
