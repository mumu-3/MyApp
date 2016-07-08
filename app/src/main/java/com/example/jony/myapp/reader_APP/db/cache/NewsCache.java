package com.example.jony.myapp.reader_APP.db.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.example.jony.myapp.main.BaseApplication;
import com.example.jony.myapp.reader_APP.db.database.DatabaseHelper;
import com.example.jony.myapp.reader_APP.db.database.table.NewsTable;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.SAXNewsParse;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Jony on 2016/6/15.
 * Daily Cache. function:<br>
 * <li>Get Daily data from ZhiHu api via net</li>
 * <li>Cache Daily data to database if it updates</li>
 * <li>Load Daily data from database</li>
 * <li>Notify to fragment/activity if work completed</li>
 */
public class NewsCache {

    private Handler mHandler;
    private Context mContext = BaseApplication.AppContext;
    private DatabaseHelper mHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private NewsTable table;


    public NewsCache(Handler handler) {
        mHandler = handler;
        mHelper = DatabaseHelper.instance(mContext);
        db = mHelper.getWritableDatabase();
    }

    public synchronized void cache(String mCategory,ArrayList<NewsBean> mList){
        db = mHelper.getWritableDatabase();
        db.beginTransaction();
        values = new ContentValues();
        putData(mCategory,mList);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public synchronized void execSQL(String sql){
        db = mHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public synchronized void addToCollection(NewsBean object){
        db = mHelper.getWritableDatabase();
        db.beginTransaction();
        values = new ContentValues();
        putData(object);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 将从网络获取的数据保存在数据库中
     * */
    protected void putData(String mCategory,ArrayList<NewsBean> mList) {
        db.execSQL(mHelper.DELETE_TABLE_DATA + table.NAME+" where " + table.CATEGORY+"=\'"+mCategory+"\'");
        for(int i=0;i<mList.size();i++){
            NewsBean newsBean =  mList.get(i);
            values.put(NewsTable.TITLE,newsBean.getTitle());
            values.put(NewsTable.DESCRIPTION,newsBean.getDescription());
            values.put(NewsTable.PUBTIME,newsBean.getPubTime());
            values.put(NewsTable.IS_COLLECTED,newsBean.getIs_collected());
            values.put(NewsTable.LINK,newsBean.getLink());
            values.put(NewsTable.CATEGORY,mCategory);
            db.insert(NewsTable.NAME,null,values);
        }
    }



    protected void putData(NewsBean newsBean) {
        values.put(NewsTable.TITLE,newsBean.getTitle());
        values.put(NewsTable.DESCRIPTION,newsBean.getDescription());
        values.put(NewsTable.PUBTIME,newsBean.getPubTime());
        values.put(NewsTable.LINK,newsBean.getLink());
        db.insert(NewsTable.COLLECTION_NAME, null, values);
    }

    public void load(String mUrl, final ArrayList<NewsBean> mList) {
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if (response.isSuccessful() == false) {
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                InputStream is =
                        new ByteArrayInputStream(response.body().string().getBytes(Charset.forName("UTF-8")));
                try {
                    ArrayList<String> collectionTitles = new ArrayList<String>();
                    for(int i = 0 ; i<mList.size() ; i++ ){
                        if(mList.get(i).getIs_collected() == 1){
                            collectionTitles.add(mList.get(i).getTitle());
                        }
                    }

                    mList.clear();
                    mList.addAll(SAXNewsParse.parse(is));
                    for(String title:collectionTitles){
                        for(int i=0 ; i<mList.size() ; i++){
                            if(title.equals(mList.get(i).getTitle())){
                                mList.get(i).setIs_collected(1);
                            }
                        }
                    }
                    is.close();
                    mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
