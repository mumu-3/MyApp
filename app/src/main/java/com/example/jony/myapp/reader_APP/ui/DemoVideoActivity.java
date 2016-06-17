package com.example.jony.myapp.reader_APP.ui;

import com.example.jony.myapp.reader_APP.support.WebViewUrlActivity;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;

/**
 * Created by Jony on 2016/6/17.
 */
public class DemoVideoActivity extends WebViewUrlActivity {

    @Override
    protected void loadData() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(CONSTANT.DEMO_VIDEO_URL);
            }
        });
    }
}
