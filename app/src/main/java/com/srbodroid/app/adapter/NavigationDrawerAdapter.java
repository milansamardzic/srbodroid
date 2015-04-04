package com.srbodroid.app.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.srbodroid.app.R;
import com.srbodroid.app.fragment.NavigationDrawerFragment;

import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerFragment.NDItem>
{
    ListView listView;
    public NavigationDrawerAdapter(ListView listView, List<NavigationDrawerFragment.NDItem> objects)
    {
        super(listView.getContext(), 0, objects);
        this.listView = listView;
    }

    /**
     * Custom viewholder for our planet views.
     */
    public class ViewHolder
    {
        public TextView mTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NavigationDrawerFragment.NDItem item = getItem(position);

        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater vi = LayoutInflater.from(parent.getContext());
            convertView = vi.inflate(R.layout.drawer_list_item, parent, false);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(item.title);

        holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0);

        if (listView.isItemChecked(position))
        {
            holder.mTextView.setTextColor(listView.getContext().getResources().getColor(R.color.primary));
            holder.mTextView.setTypeface(null, Typeface.BOLD);
        }
        else
        {
            holder.mTextView.setTextColor(listView.getContext().getResources().getColor(R.color.text_color_grey));
            holder.mTextView.setTypeface(null, Typeface.NORMAL);
        }

        return convertView;
    }
}