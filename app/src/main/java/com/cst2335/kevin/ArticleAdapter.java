/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/


package com.cst2335.kevin;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import com.cst2335.R;


public class ArticleAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Article> article;


    public ArticleAdapter(ArrayList<Article> news, Context context) {
        this.article = news;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return article.size();
    }

    @Override
    public Object getItem(int position) {
        return article.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.activity_article_search_list, parent, false);
        }
        Article currentItem = (Article) getItem(position);
        TextView textViewItemName = convertView.findViewById(R.id.article_id);
        textViewItemName.setText(currentItem.getTitle());

        return convertView;
    }

}
