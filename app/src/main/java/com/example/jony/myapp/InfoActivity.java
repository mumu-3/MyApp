package com.example.jony.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.jony.myapp.reader_APP.ui.ReaderActivity;

public class InfoActivity extends AppCompatActivity {

    private String mString;
   /* private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(InfoActivity.this,ReaderActivity.class));
        }
    };*/

    public static Intent getStartIntent(Context context, Category category) {
        Intent starter = new Intent(context, InfoActivity.class);
        starter.putExtra(Category.TAG, category);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Category category = getIntent().getParcelableExtra(Category.TAG);


        toolbar.setBackgroundColor(getResources().getColor(category.getTheme().getPrimaryColor()));
        mString = getResources().getString(category.getName());
        toolbar.setTitle(mString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this,ReaderActivity.class));
            }
        });

        ImageView iv =  (ImageView) findViewById(R.id.iv);
        iv.setBackgroundColor(getResources().getColor(category.getTheme().getAccentColor()));

        //// TODO: 2016/6/14 动画效果

       // mHandler.sendEmptyMessageDelayed(0,3000);

    }

}
