package com.example.jony.myapp.main;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jony.myapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jony on 2016/6/13.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Activity mActivity;
    private List<Category> mDatas;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public MainAdapter(Activity activity) {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
        initData();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add(new Category(R.string.reader,R.mipmap.icon_category_knowledge_raster,Theme.topeka));
        mDatas.add(new Category(R.string.shop,R.mipmap.icon_category_entertainment_raster,Theme.purple));
        mDatas.add(new Category(R.string.news,R.mipmap.icon_category_food_raster,Theme.green));
        mDatas.add(new Category(R.string.video,R.mipmap.icon_category_geography_raster,Theme.red));
        mDatas.add(new Category(R.string.qq,R.mipmap.icon_category_history_raster,Theme.blue));
        mDatas.add(new Category(R.string.microReader,R.mipmap.icon_category_leaderboard_raster,Theme.yellow));
        mDatas.add(new Category(R.string.music,R.mipmap.icon_category_music_raster,Theme.topeka));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Category category = mDatas.get(position);
        Theme theme = category.getTheme();

        holder.icon.setBackground(mActivity.getResources().getDrawable(category.getResId()));
        holder.itemView.setBackgroundColor(getColor(theme.getWindowBackgroundColor()));
        holder.title.setText(category.getName());
        holder.title.setTextColor(getColor(theme.getTextPrimaryColor()));
        holder.title.setBackgroundColor(getColor(theme.getPrimaryColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, position);
            }
        });
    }

    public Category getItem(int position){
        return mDatas.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * Convenience method for color loading.
     *
     * @param colorRes The resource id of the color to load.
     * @return The loaded color.
     */
    private int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(mActivity, colorRes);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView icon;
        final TextView title;

        public ViewHolder(View container) {
            super(container);
            icon = (ImageView) container.findViewById(R.id.category_icon);
            title = (TextView) container.findViewById(R.id.category_title);
        }
    }
}
