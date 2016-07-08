package com.example.jony.myapp.reader_APP.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.ui.fragment.AboutFragment;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.example.jony.myapp.reader_APP.utils.Utils;

/**
 * Created by Jony on 2016/6/17.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int mLang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Language
        mLang = Utils.getCurrentLanguage();
        if (mLang > -1) {
            Utils.changeLanguage(this, mLang);
        }

        //set Theme
        if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
        }else{
            this.setTheme(R.style.DayTheme);
        }

        setContentView(R.layout.activity_reader_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.reader_about));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.framelayout,new AboutFragment()).commit();
    }
}
