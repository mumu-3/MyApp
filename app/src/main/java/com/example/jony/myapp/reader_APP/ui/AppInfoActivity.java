package com.example.jony.myapp.reader_APP.ui;

import com.example.jony.myapp.reader_APP.support.WebViewUrlActivity;

/**
 * Created by Jony on 2016/6/17.
 */
public class AppInfoActivity extends WebViewUrlActivity {

    @Override
    protected void loadData() {
        webView.loadUrl("file:///android_asset/LeisureIntroduction.html");
    }
}
