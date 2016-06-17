/*
 *  *  Copyright (C) 2015 MummyDing
 *  *
 *  *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *  *
 *  *  Leisure is free software: you can redistribute it and/or modify
 *  *  it under the terms of the GNU General Public License as published by
 *  *  the Free Software Foundation, either version 3 of the License, or
 *  *  (at your option) any later version.
 *  *                             ｀
 *  *  Leisure is distributed in the hope that it will be useful,
 *  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *  GNU General Public License for more details.
 *  *
 *  *  You should have received a copy of the GNU General Public License
 *  *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.example.jony.myapp.reader_APP.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.example.jony.myapp.DebugUtils;
import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.api.DailyApi;
import com.example.jony.myapp.reader_APP.db.cache.DailyCache;
import com.example.jony.myapp.reader_APP.db.database.table.DailyTable;
import com.example.jony.myapp.reader_APP.model.daily.DailyDetailsBean;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.DisplayUtil;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class DailyDetailsActivity extends BaseDetailsActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private boolean isShakeMode = false;
    private DailyDetailsBean dailyDetailsBean;
    private String url;
    private int id;
    private String imageUrl;
    private String title;
    private String body;
    private DailyCache cache;
    private StoryBean storyBean;

    @Override
    protected void onDataRefresh() {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                DebugUtils.DLog(res);
                Gson gson = new Gson();
                dailyDetailsBean = gson.fromJson(res, DailyDetailsBean.class);

                cache.execSQL(DailyTable.updateBodyContent(DailyTable.NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getBody()));
                cache.execSQL(DailyTable.updateBodyContent(DailyTable.COLLECTION_NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getBody()));
                cache.execSQL(DailyTable.updateLargePic(DailyTable.NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getImage()));
                cache.execSQL(DailyTable.updateLargePic(DailyTable.COLLECTION_NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getImage()));


                imageUrl = dailyDetailsBean.getImage();
                body = dailyDetailsBean.getBody();

                handler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initView();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    private void getData(){
        storyBean = new StoryBean();
        url = getIntent().getStringExtra(getString(R.string.reader_id_url));
        id = getIntent().getIntExtra(getString(R.string.reader_id_id),0);
        body = getIntent().getStringExtra(getString(R.string.reader_id_body));
        title = getIntent().getStringExtra(getString(R.string.reader_id_title));
        imageUrl = getIntent().getStringExtra(getString(R.string.reader_id_imageurl));
        isCollected = getIntent().getBooleanExtra(getString(R.string.reader_id_collection),false);

        storyBean.setId(id);
        storyBean.setBody(body);
        storyBean.setTitle(title);
        storyBean.setLargepic(imageUrl);
        storyBean.setImages(new String[]{getIntent().getStringExtra(getString(R.string.reader_id_small_image))});

    }

    protected void initView(){
        super.initView();
        cache = new DailyCache(handler);
        if(body == "" || body == null||imageUrl == null || imageUrl == "") {
            onDataRefresh();
        }else{
            handler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        }
    }

    @Override
    protected void removeFromCollection() {
        cache.execSQL(DailyTable.updateCollectionFlag(storyBean.getTitle(),0));
        cache.execSQL(DailyTable.deleteCollectionFlag(storyBean.getTitle()));
    }

    @Override
    protected void addToCollection() {
        cache.execSQL(DailyTable.updateCollectionFlag(storyBean.getTitle(),1));
        cache.addToCollection(storyBean);
    }

    @Override
    protected String getShareInfo() {
        return "["+title+"]:"+ DailyApi.daily_story_base_url+id+" ("+getString(R.string.reader_text_share_from)+getString(R.string.app_name)+")";
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case CONSTANT.ID_FAILURE:
                    hideLoading();
                    displayNetworkError();
                    break;
                case CONSTANT.ID_SUCCESS:

                case CONSTANT.ID_FROM_CACHE:
                    // fix issue #13
                    if(HttpUtil.isWIFI == true || Settings.getInstance().getBoolean(Settings.NO_PIC_MODE, false) == false) {
                        setMainContentBg(imageUrl);
                    }
                    scrollView.setVisibility(View.VISIBLE);
                    scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            topImage.setTranslationY(Math.max(-scrollY / 2, -DisplayUtil.dip2px(getBaseContext(), 170)));
                        }
                    });
                    contentView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"dailycss.css\" />"+body, "text/html", "utf-8", null);
                    hideLoading();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        isShakeMode = Settings.getInstance().getBoolean(Settings.SHAKE_TO_RETURN,true);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(isShakeMode == false){
            return;
        }

        float value[] = event.values;
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if((Math.abs(value[0]) + Math.abs(value[1]) + Math.abs(value[2]))>CONSTANT.shakeValue){
                onBackPressed();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
