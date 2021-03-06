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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.api.DailyApi;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.ui.DailyDetailsActivity;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 *
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    private ArrayList<StoryBean> mList;
    private Context mContext;
    private boolean isCollection = false;


    public DailyAdapter(Context context,ArrayList<StoryBean> list) {

        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StoryBean storyBean = mList.get(position);
        holder.title.setText(storyBean.getTitle());

        if(Settings.noPicMode && HttpUtil.isWIFI == false){
            holder.image.setImageURI(null);
        }else {
            holder.image.setImageURI(Uri.parse(storyBean.getImages()[0]));
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(mContext.getString(R.string.reader_id_url), DailyApi.daily_details_url+storyBean.getId());
                bundle.putString(mContext.getString(R.string.reader_id_title),storyBean.getTitle());
                bundle.putString(mContext.getString(R.string.reader_id_body),storyBean.getBody());
                bundle.putString(mContext.getString(R.string.reader_id_imageurl),storyBean.getLargepic());
                bundle.putString(mContext.getString(R.string.reader_id_small_image),storyBean.getImages()[0]);
                bundle.putInt(mContext.getString(R.string.reader_id_id),storyBean.getId());
                if(isCollection){
                    bundle.putBoolean(mContext.getString(R.string.reader_id_collection),true);
                }else {
                    bundle.putBoolean(mContext.getString(R.string.reader_id_collection), storyBean.isCollected() == 1 ? true : false);
                }
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private View parentView;
        private SimpleDraweeView image;
        public ViewHolder(View container) {
            super(container);
            parentView = itemView;
            title = (TextView) container.findViewById(R.id.title);
            image = (SimpleDraweeView) container.findViewById(R.id.image);
        }
    }

}
