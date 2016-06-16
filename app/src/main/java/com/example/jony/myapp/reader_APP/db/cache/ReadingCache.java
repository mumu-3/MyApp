package com.example.jony.myapp.reader_APP.db.cache;

import android.os.Handler;

import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.model.reading.BookBean;
import com.example.jony.myapp.reader_APP.model.reading.ReadingBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.SAXNewsParse;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
public class ReadingCache {

    private Handler mHandler;

    public ReadingCache(Handler handler) {
        mHandler = handler;
    }


    public void load(String mUrl, final ArrayList<BookBean> mList) {
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);
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


                Gson gson = new Gson();
                BookBean[] bookBeans = gson.fromJson(response.body().string(), ReadingBean.class).getBooks();
                mList.clear();
                for (BookBean bookBean : bookBeans) {
                    mList.add(bookBean);
                }

                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);

            }
        });
    }

}

