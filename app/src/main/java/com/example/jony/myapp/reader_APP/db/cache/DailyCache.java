package com.example.jony.myapp.reader_APP.db.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.example.jony.myapp.main.BaseApplication;
import com.example.jony.myapp.main.DebugUtils;
import com.example.jony.myapp.reader_APP.api.DailyApi;
import com.example.jony.myapp.reader_APP.db.database.DatabaseHelper;
import com.example.jony.myapp.reader_APP.db.database.table.DailyTable;
import com.example.jony.myapp.reader_APP.model.daily.DailyBean;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jony on 2016/6/15.
 * Daily Cache. function:<br>
 * <li>Get Daily data from ZhiHu api via net</li>
 * <li>Cache Daily data to database if it updates</li>
 * <li>Load Daily data from database</li>
 * <li>Notify to fragment/activity if work completed</li>
 */
public class DailyCache {

    private  Context mContext = BaseApplication.AppContext;
    private DatabaseHelper mHelper;
    private SQLiteDatabase db;

    private Handler mHandler;
    private ContentValues values;

    public DailyCache(Handler handler) {
        mHandler = handler;

        mHelper = DatabaseHelper.instance(mContext);
        db = mHelper.getWritableDatabase();

    }


    // load today's data
    public void load(final ArrayList<StoryBean> list){
        Request.Builder builder = new Request.Builder();
        builder.url(DailyApi.daily_url);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful() == false) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                String res = response.body().string();
                DebugUtils.DLog(res);

                Gson gson = new Gson();
                DailyBean dailyBean = gson.fromJson(res, DailyBean.class);
                StoryBean[] storyBeans = dailyBean.getStories();
                for (StoryBean storyBeen : storyBeans) {
                    list.add(storyBeen);
                }

                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                loadOld(dailyBean.getDate(),list);
            }
        });

    }

    // load yesterday's data
    private  void loadOld(String date, final List<StoryBean> newList) {
        Request.Builder builder = new Request.Builder();
        builder.url(DailyApi.daily_old_url + date);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() == false) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                String res = response.body().string();

                DebugUtils.DLog(res);
                Gson gson = new Gson();
                StoryBean[] storyBeans = (gson.fromJson(res, DailyBean.class)).getStories();
                for (StoryBean storyBeen : storyBeans) {
                    newList.add(storyBeen);
                }

                // notify
                mHandler.sendEmptyMessage(CONSTANT.ID_LOADMORE);
            }
        });
    }

    public synchronized void addToCollection(StoryBean storyBean){
        db = mHelper.getWritableDatabase();
        db.beginTransaction();
        values = new ContentValues();
        putData(storyBean);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    protected void putData(StoryBean storyBean) {
        values.put(DailyTable.TITLE,storyBean.getTitle());
        values.put(DailyTable.ID, storyBean.getId());
        values.put(DailyTable.IMAGE, storyBean.getImages()[0]);
        values.put(DailyTable.BODY, storyBean.getBody() == null ? "":storyBean.getBody());
        values.put(DailyTable.LARGEPIC, storyBean.getLargepic());
        db.insert(DailyTable.COLLECTION_NAME, null, values);
    }

    public synchronized void execSQL(String sql){
        db.execSQL(sql);
    }
}
