package com.example.jony.myapp.reader_APP.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.jony.myapp.main.BaseApplication;
import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.ui.fragment.AboutFragment;
import com.example.jony.myapp.reader_APP.ui.fragment.CollectionFragment;
import com.example.jony.myapp.reader_APP.ui.fragment.DailyFragment;
import com.example.jony.myapp.reader_APP.ui.fragment.NewsFragment;
import com.example.jony.myapp.reader_APP.ui.fragment.ReadingFragment;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.example.jony.myapp.reader_APP.utils.Utils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class ReaderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CheeseListFragment.MyListener, View.OnClickListener {

    private ImageView mTab1;
    private ImageView mTab2;
    private ImageView mTab3;
    private ImageView mTab4;

    private int mCurrentFragment;
    private SmartTabLayout mTabTitle;
    private Menu menu;
    private FragmentTransaction mFragmentTransaction;
    private DailyFragment mDailyFragment;
    private NewsFragment mNewsFragment;
    private ReadingFragment mReadingFragment;
    private AboutFragment mAboutFragment;
    private android.app.FragmentTransaction mFragmentTransaction1;
    private SmartTabLayout mTabLayout;

    private Settings mSettings = Settings.getInstance();

    private boolean isShake = false;
    private long lastPressTime = 0;
    private int mLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Language
        mLang = Utils.getCurrentLanguage();
        if (mLang > -1) {
            Utils.changeLanguage(this, mLang);
        }

        //Settings
        Settings.isShakeMode = mSettings.getBoolean(Settings.SHAKE_TO_RETURN, false);
        Settings.searchID = mSettings.getInt(Settings.SEARCH, 0);
        Settings.swipeID = mSettings.getInt(Settings.SWIPE_BACK,0);
        Settings.isAutoRefresh = mSettings.getBoolean(Settings.AUTO_REFRESH, false);
        Settings.isExitConfirm = mSettings.getBoolean(Settings.EXIT_CONFIRM, true);
        Settings.isNightMode = mSettings.getBoolean(Settings.NIGHT_MODE, false);
        Settings.noPicMode = mSettings.getBoolean(Settings.NO_PIC_MODE, false);


        // change Brightness
        if(mSettings.isNightMode && Utils.getSysScreenBrightness() > CONSTANT.NIGHT_BRIGHTNESS){
            Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
        }else if(mSettings.isNightMode == false && Utils.getSysScreenBrightness() == CONSTANT.NIGHT_BRIGHTNESS){
            Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
        }

        if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
        }else{
            this.setTheme(R.style.DayTheme);
        }


        setContentView(R.layout.activity_reader2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.reader_app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        DrawerLayout drawer =  (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**设置MenuItem的字体颜色**/
        if(Settings.isNightMode){
            Resources resource = (Resources)getBaseContext().getResources();
            ColorStateList csl = (ColorStateList)resource.getColorStateList(R.color.navigation_menu_item_color_dark);
            navigationView.setItemTextColor(csl);
        }else{
            Resources resource = (Resources)getBaseContext().getResources();
            ColorStateList csl = (ColorStateList)resource.getColorStateList(R.color.navigation_menu_item_color);
            navigationView.setItemTextColor(csl);
        }


        navigationView.getMenu().getItem(0).setChecked(true);
        mTabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);

        initBottomTab();


    }

    private void initBottomTab() {

        findViewById(R.id.ll_tab_1).setOnClickListener(this);
        findViewById(R.id.ll_tab_2).setOnClickListener(this);
        findViewById(R.id.ll_tab_3).setOnClickListener(this);
        findViewById(R.id.ll_tab_4).setOnClickListener(this);

        mTab1 = (ImageView) findViewById(R.id.tab_1);
        mTab2 = (ImageView) findViewById(R.id.tab_2);
        mTab3 = (ImageView) findViewById(R.id.tab_3);
        mTab4 = (ImageView) findViewById(R.id.tab_4);

        mCurrentFragment = 0;
        initTabImage(mCurrentFragment);
        initFragment(mCurrentFragment);

    }

    private void initTabImage(int t) {
        ImageView[] imageViews = new ImageView[]{mTab1, mTab2, mTab3, mTab4};

        int[] images = new int[]{R.drawable.tab1, R.drawable.tab2, R.drawable.tab3, R.drawable.tab4};
        int[] imagesSelected = new int[]{R.drawable.tab1_selected, R.drawable.tab2_selected, R.drawable.tab3_selected, R.drawable.tab4_selected};


        for (int i = 0; i < imageViews.length; i++) {

            imageViews[i].setImageResource(images[i]);

        }
        if (t > -1 && t < imageViews.length) {

            imageViews[t].setImageResource(imagesSelected[t]);
        }

    }

    private void initFragment(int currentFragment) {


        switch (currentFragment) {
            case 0:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DailyFragment()).commit();
                getSupportActionBar().setTitle(R.string.reader_daily);
                if (menu != null) {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_daily, menu);
                }

                break;
            case 1:


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new NewsFragment()).commit();
                getSupportActionBar().setTitle(R.string.reader_news);
                if (menu != null) {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_daily, menu);
                }

                break;
            case 2:


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ReadingFragment()).commit();
                getSupportActionBar().setTitle(R.string.reader_reading);
                if (menu != null) {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_reading, menu);
                }

                break;
            case 3:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new CollectionFragment()).commit();
                getSupportActionBar().setTitle(R.string.reader_collection);
                if (menu != null) {
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_daily, menu);
                }

                break;
        }

        mCurrentFragment = currentFragment;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isShake == false && canExit()){
            super.onBackPressed();
        }
            isShake = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_daily, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_search) {
            Intent intent = new Intent(this,SearchBooksActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onHideBar() {

    }

    @Override
    public void onShowBar() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_tab_1:
                if (mCurrentFragment != 0) {
                    initTabImage(0);
                    initFragment(0);
                }
                break;
            case R.id.ll_tab_2:
                if (mCurrentFragment != 1) {
                    initTabImage(1);
                    initFragment(1);
                }
                break;
            case R.id.ll_tab_3:
                if (mCurrentFragment != 2) {
                    initTabImage(2);
                    initFragment(2);
                }
                break;
            case R.id.ll_tab_4:
                if (mCurrentFragment != 3) {
                    initTabImage(3);
                    initFragment(3);
                }
                break;
        }
    }


    private boolean canExit(){
        if(Settings.isExitConfirm){
            if(System.currentTimeMillis() - lastPressTime > CONSTANT.exitConfirmTime){
                lastPressTime = System.currentTimeMillis();
                Snackbar.make(getCurrentFocus(), R.string.reader_notify_exit_confirm,Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    /**
     * 在设置 里 更改设置之后，返回时 会调用 onResume 然后执行 recreate
     * 会重新根据Setting的选项创建主题及设置
     *
     * */
    @Override
    protected void onResume() {
        super.onResume();

        if(Settings.needRecreate) {
            Settings.needRecreate = false;
            this.recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getRefWatcher(this).watch(this);
    }
}
