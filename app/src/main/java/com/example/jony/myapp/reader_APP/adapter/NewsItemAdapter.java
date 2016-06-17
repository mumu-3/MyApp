/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.jony.myapp.reader_APP.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.support.WebViewUrlActivity;

import java.util.ArrayList;

/**
 *
 */
public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {



    private ArrayList<NewsBean> mList;
    private Context mContext;
    private boolean isCollection = false;

    public NewsItemAdapter(Context context, ArrayList list) {

        mList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewsBean newsBean = mList.get(position);
        holder.description.setText(newsBean.getDescription());
        holder.title.setText(newsBean.getTitle());
        holder.date.setText(newsBean.getPubTime());
        holder.position = position;
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewUrlActivity.class);
                intent.putExtra(mContext.getString(R.string.reader_id_url), mList.get(position).getLink());
                mContext.startActivity(intent);
            }
        });

        if(isCollection){
            holder.collect_cb.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(holder.parentView, R.string.reader_notify_remove_from_collection, Snackbar.LENGTH_SHORT).
                            setAction(mContext.getString(R.string.reader_text_ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   /* if (mItems.contains(newsBean) == false) {
                                        return;
                                    }
                                    mCache.execSQL(NewsTable.updateCollectionFlag(newsBean.getTitle(), 0));
                                    mCache.execSQL(NewsTable.deleteCollectionFlag(newsBean.getTitle()));
                                    mItems.remove(position);
                                    notifyDataSetChanged();*/
                                }
                            })
                            .show();
                }
            });
            return;
        }

        holder.collect_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*newsBean.setIs_collected(isChecked ? 1 : 0);
                mCache.execSQL(NewsTable.updateCollectionFlag(newsBean.getTitle(), isChecked ? 1 : 0));
                if (isChecked) {
                    mCache.addToCollection(newsBean);
                } else {
                    mCache.execSQL(NewsTable.deleteCollectionFlag(newsBean.getTitle()));
                }*/
            }
        });
        holder.collect_cb.setChecked(newsBean.getIs_collected() == 1 ? true:false);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View parentView;
        private TextView title;
        private TextView description;
        private TextView date;
        private CheckBox collect_cb;
        private TextView text;
        private int position;

        ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            collect_cb = (CheckBox) itemView.findViewById(R.id.collect_cb);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
