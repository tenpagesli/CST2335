package com.cst2335.hung;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private ArrayList<News> news;
    private Context context;
    private LayoutInflater inflater;

    public NewsAdapter(ArrayList<News> news, Context context) {
        this.news = news;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.activity_news_list_item, parent, false);
        }

        //TextView nameTextView = (TextView) view.findViewById(R.id.dessert_name);
        News currentItem = (News) getItem(position);
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.news_title);
        textViewItemName.setText(currentItem.getTitle());
      //  ListView messageText = view.findViewById(R.id.news_feed_list);
      //  messageText.setText(news.get(position).getTitle());
        return convertView;
    }
}


