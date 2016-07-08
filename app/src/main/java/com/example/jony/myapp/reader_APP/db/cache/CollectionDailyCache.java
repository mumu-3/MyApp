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

package com.example.jony.myapp.reader_APP.db.cache;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.example.jony.myapp.main.BaseApplication;
import com.example.jony.myapp.reader_APP.db.database.DatabaseHelper;
import com.example.jony.myapp.reader_APP.db.database.table.DailyTable;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;

import java.util.ArrayList;


/**
 * Created by mummyding on 15-12-4.
 */
public class CollectionDailyCache{


    private DailyTable table;

    private ArrayList<StoryBean> mList;

    private DatabaseHelper mHelper;
    private SQLiteDatabase db;

    private Handler mHandler;

    public CollectionDailyCache(Handler mHandler,ArrayList<StoryBean> list) {

        this.mHandler = mHandler;
        mList = list;
        mHelper = DatabaseHelper.instance(BaseApplication.AppContext);

    }

    public void execSQL(String sql) {
        db = mHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    private Cursor query(String sql){
        return mHelper.getReadableDatabase().rawQuery(sql,null);
    }

    public  void loadFromCache() {
        Cursor cursor = query(table.SELECT_ALL_FROM_COLLECTION);
        while (cursor.moveToNext()){
            StoryBean storyBean = new StoryBean();
            storyBean.setTitle(cursor.getString(table.ID_TITLE));
            storyBean.setId(cursor.getInt(table.ID_ID));
            storyBean.setImages(new String[]{cursor.getString(table.ID_IMAGE)});
            storyBean.setBody(cursor.getString(table.ID_BODY));
            storyBean.setLargepic(cursor.getString(table.ID_LARGEPIC));
            mList.add(storyBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
    }
}
