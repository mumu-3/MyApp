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
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.model.reading.BookBean;
import com.example.jony.myapp.reader_APP.ui.ReadingDetailsActivity;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.example.jony.myapp.reader_APP.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 *
 */
public class ReadingItemAdapter extends RecyclerView.Adapter<ReadingItemAdapter.ViewHolder> {



    private ArrayList<BookBean> mList;
    private Context mContext;
    private boolean isCollection = false;

    public ReadingItemAdapter(Context context, ArrayList list) {

        mList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reading, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final BookBean bookBean = mList.get(position);
        holder.title.setText(bookBean.getTitle());
        holder.info.setText(bookBean.getInfo());

        if(Settings.noPicMode && HttpUtil.isWIFI == false){
            holder.image.setImageURI(null);
        }else {
            holder.image.setImageURI(Uri.parse(bookBean.getImage()));
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(mContext, ReadingDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(mContext.getString(R.string.reader_id_book), bookBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        // set ebook
        if(Utils.hasString(bookBean.getEbook_url())) {
            holder.ebook.setVisibility(View.VISIBLE);
        }
        else {
            holder.ebook.setVisibility(View.GONE);
        }


        if(isCollection){
            holder.collect_cb.setVisibility(View.GONE);
            holder.text.setText(R.string.reader_text_remove);
            holder.text.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.text.setTextSize(18);
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(holder.parentView, R.string.reader_notify_remove_from_collection, Snackbar.LENGTH_SHORT).
                            setAction(mContext.getString(R.string.reader_text_ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    /*if (mItems.contains(bookBean) == false) {
                                        return;
                                    }
                                    mCache.execSQL(ReadingTable.updateCollectionFlag(bookBean.getTitle(), 0));
                                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bookBean.getTitle()));
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
                /*bookBean.setIs_collected(isChecked ? 1 : 0);
                mCache.execSQL(ReadingTable.updateCollectionFlag(bookBean.getTitle(), isChecked ? 1 : 0));
                if (isChecked) {
                    mCache.addToCollection(bookBean);
                } else {
                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bookBean.getTitle()));
                }*/
            }
        });
        holder.collect_cb.setChecked(bookBean.getIs_collected() == 1 ? true : false);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private View parentView;
        private SimpleDraweeView image;
        private TextView title;
        private TextView info;
        private CheckBox collect_cb;
        private TextView text;
        private ImageView ebook;
        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            image = (SimpleDraweeView) itemView.findViewById(R.id.bookImg);
            title = (TextView) itemView.findViewById(R.id.bookTitle);
            info = (TextView) itemView.findViewById(R.id.bookInfo);
            collect_cb = (CheckBox) itemView.findViewById(R.id.collect_cb);
            ebook = (ImageView) itemView.findViewById(R.id.ebook);
            if(isCollection) {
                text = (TextView) parentView.findViewById(R.id.text);
            }
        }
    }

}
