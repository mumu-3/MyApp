package com.example.jony.myapp.reader_APP.db.cache;

import android.os.Handler;

import com.example.jony.myapp.DebugUtils;
import com.example.jony.myapp.reader_APP.api.DailyApi;
import com.example.jony.myapp.reader_APP.model.daily.DailyBean;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
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
import java.util.List;

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

    public NewsCache(Handler handler) {
        mHandler = handler;
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
