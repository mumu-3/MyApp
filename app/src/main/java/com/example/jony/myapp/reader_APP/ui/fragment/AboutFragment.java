package com.example.jony.myapp.reader_APP.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.example.jony.myapp.reader_APP.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Jony on 2016/6/16.
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private Preference mAppIntro;
    private Preference mDemoVideo;
    private Preference mCheckUpdate;
    private Preference mShare;

    private Preference mLanguage;
    private Preference mSearch;
    private Preference mSwipeBack;
    private CheckBoxPreference mAutoRefresh;
    private CheckBoxPreference mNightMode;
    private CheckBoxPreference mShakeToReturn;
    private CheckBoxPreference mNoPicMode;
    private CheckBoxPreference mExitConfirm;
    private Preference mClearCache;

    private final String APP_INTRO = "app_intro";
    private final String DEMO_VIDEO = "demo_video";
    private final String CHECK_UPDATE = "check_update";
    private final String SHARE = "share";


    private ProgressBar progressBar;
    private Settings mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        mSettings = Settings.getInstance();

        mAppIntro = findPreference(APP_INTRO);
        mDemoVideo = findPreference(DEMO_VIDEO);
        mCheckUpdate = findPreference(CHECK_UPDATE);
        mShare = findPreference(SHARE);

        mLanguage = findPreference(Settings.LANGUAGE);
        mSearch = findPreference(Settings.SEARCH);
        mSwipeBack = findPreference(Settings.SWIPE_BACK);

        mAutoRefresh = (CheckBoxPreference) findPreference(Settings.AUTO_REFRESH);
        mNightMode = (CheckBoxPreference) findPreference(Settings.NIGHT_MODE);
        mShakeToReturn = (CheckBoxPreference) findPreference(Settings.SHAKE_TO_RETURN);
        mNoPicMode = (CheckBoxPreference) findPreference(Settings.NO_PIC_MODE);
        mExitConfirm = (CheckBoxPreference) findPreference(Settings.EXIT_CONFIRM);
        mClearCache = findPreference(Settings.CLEAR_CACHE);

        mLanguage.setSummary(this.getResources().getStringArray(R.array.langs)[Utils.getCurrentLanguage()]);
        mSearch.setSummary(this.getResources().getStringArray(R.array.search)[Settings.searchID]);
        mSwipeBack.setSummary(this.getResources().getStringArray(R.array.swipe_back)[Settings.swipeID]);

        mAutoRefresh.setChecked(Settings.isAutoRefresh);
        mNightMode.setChecked(Settings.isNightMode);
        mShakeToReturn.setChecked(Settings.isShakeMode);
        mExitConfirm.setChecked(Settings.isExitConfirm);
        mNoPicMode.setChecked(Settings.noPicMode);

        mAutoRefresh.setOnPreferenceChangeListener(this);
        mNightMode.setOnPreferenceChangeListener(this);
        mShakeToReturn.setOnPreferenceChangeListener(this);
        mExitConfirm.setOnPreferenceChangeListener(this);
        mNoPicMode.setOnPreferenceChangeListener(this);


        mLanguage.setOnPreferenceClickListener(this);
        mSearch.setOnPreferenceClickListener(this);
        mSwipeBack.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);


        mAppIntro.setOnPreferenceClickListener(this);
        mDemoVideo.setOnPreferenceClickListener(this);
        mCheckUpdate.setOnPreferenceClickListener(this);
        mShare.setOnPreferenceClickListener(this);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (mAppIntro == preference) {
            /*Intent toIntro = new Intent(getActivity(), AppIntroActivity.class);
            startActivity(toIntro);*/
        } else if (mDemoVideo == preference) {
            /*Intent toVideo = new Intent(getActivity(), DemoVideoActivity.class);
            startActivity(toVideo);*/
        } else if (mCheckUpdate == preference) {
            progressBar.setVisibility(View.VISIBLE);

            Request.Builder builder = new Request.Builder();
            builder.url(CONSTANT.VERSION_URL);
            Request request = builder.build();
            HttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Snackbar.make(getView(), R.string.reader_hint_fail_check_update, Snackbar.LENGTH_SHORT).show();
                    handle.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String latestVersion = response.body().string();
                    if (CONSTANT.CURRENT_VERSION.equals(latestVersion.trim())) {
                        Snackbar.make(getView(), getString(R.string.reader_notify_current_is_latest), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getView(), getString(R.string.reader_notify_find_new_version) + latestVersion, Snackbar.LENGTH_SHORT).show();
                    }
                    handle.sendEmptyMessage(1);
                }
            });

        }else if (mShare == preference) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.reader_text_share_info));
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.reader_text_share_leisure)));

        } else if(preference == mLanguage){
            showLangDialog();
        }else if(preference == mClearCache){
           /* Utils.clearCache();
            Settings.needRecreate = true;
            Snackbar.make(getView(), R.string.reader_text_clear_cache_successful,Snackbar.LENGTH_SHORT).show();*/
        }else if(preference == mSearch){
            ShowSearchSettingDialog();
        }else if(preference == mSwipeBack){
            showSwipeSettingsDialog();
        }
        return false;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference == mAutoRefresh){
            Settings.isAutoRefresh = Boolean.valueOf(newValue.toString());
            mSettings.putBoolean(mSettings.AUTO_REFRESH, Settings.isAutoRefresh);
            return true;
        }else if(preference == mNightMode){
            Settings.isNightMode = Boolean.valueOf(newValue.toString());
            Settings.needRecreate = true;
            mSettings.putBoolean(mSettings.NIGHT_MODE, Settings.isNightMode);

            if(mSettings.isNightMode && Utils.getSysScreenBrightness() > CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
            }else if(mSettings.isNightMode == false && Utils.getSysScreenBrightness() == CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
            }
            getActivity().recreate();
            return true;
        }else if(preference == mShakeToReturn){
            Settings.isShakeMode = Boolean.valueOf(newValue.toString());
            mSettings.putBoolean(mSettings.SHAKE_TO_RETURN,mSettings.isShakeMode);
            return true;
        }else if(preference == mExitConfirm){
            Settings.isExitConfirm = Boolean.valueOf(newValue.toString());
            mSettings.putBoolean(mSettings.EXIT_CONFIRM, Settings.isExitConfirm);
            return true;
        }else if(preference == mNoPicMode){
            Settings.noPicMode = Boolean.valueOf(newValue.toString());
            Settings.needRecreate = true;
            mSettings.putBoolean(mSettings.NO_PIC_MODE, Settings.noPicMode);
            return true;
        }
        return false;
    }

    private Handler handle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            return false;
        }
    });

    private void showLangDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.reader_text_language))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.langs), Utils.getCurrentLanguage(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which != Utils.getCurrentLanguage()) {
                                    mSettings.putInt(Settings.LANGUAGE, which);
                                    Settings.needRecreate = true;
                                }
                                dialog.dismiss();
                                if (Settings.needRecreate) {
                                    getActivity().recreate();
                                }
                            }
                        }
                ).show();

    }

    private void ShowSearchSettingDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.reader_text_search))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.search), Settings.searchID,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.searchID = which;
                                mSettings.putInt(Settings.SEARCH,which);
                                mSearch.setSummary(getResources().getStringArray(R.array.search)[Settings.searchID]);
                                dialog.dismiss();
                            }
                        }
                ).show();
    }

    private void showSwipeSettingsDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.reader_text_swipe_to_return))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.swipe_back), Settings.swipeID,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("SearchID",Settings.swipeID+"");
                                Log.d("which",which+"");
                                dialog.dismiss();
                                if(which != Settings.swipeID){
                                    Settings.swipeID = which;
                                    mSettings.putInt(Settings.SWIPE_BACK,which);
                                    mSearch.setSummary(getResources().getStringArray(R.array.swipe_back)[Settings.swipeID]);
                                    getActivity().recreate();
                                }
                            }
                        }

                ).show();
    }
}
