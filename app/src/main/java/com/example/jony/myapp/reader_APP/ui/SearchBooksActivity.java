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

package com.example.jony.myapp.reader_APP.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.api.ReadingApi;
import com.example.jony.myapp.reader_APP.ui.fragment.ReadingItemFragment;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.example.jony.myapp.reader_APP.utils.Utils;


public class SearchBooksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String url;
    private int mLang = -1;

    private boolean isShakeMode = false;
    private Bundle mBundle;
    private SearchView mSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.reader_activity_search);
        initData();



    }
    private void initData(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.reader_text_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSv = (SearchView) findViewById(R.id.sv);
        mSv.setIconified(false);
        mSv.setQueryHint(getString(R.string.reader_text_search_books));



    }



    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_go, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_search_go) {

            CharSequence query = mSv.getQuery();
            if (TextUtils.isEmpty(query)){
                Snackbar.make(mSv,"请输入查询的内容",Snackbar.LENGTH_SHORT).show();
                return true;
            }

            Utils.closeKeyBoard(SearchBooksActivity.this);
            ReadingItemFragment fragment = new ReadingItemFragment();
            mBundle = new Bundle();
            mBundle.putString(getString(R.string.reader_id_category), String.valueOf(query));
            fragment.setArguments(mBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout,fragment);
            transaction.commit();

        }
        return super.onOptionsItemSelected(item);
    }


}
