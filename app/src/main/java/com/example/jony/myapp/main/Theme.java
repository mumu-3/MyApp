package com.example.jony.myapp.main;

import android.support.annotation.ColorRes;

import com.example.jony.myapp.R;

/**
 * Created by Jony on 2016/6/14.
 */
public enum  Theme {

    topeka(R.color.topeka_primary, R.color.topeka_primary_dark,
            R.color.theme_blue_background, R.color.theme_blue_text,
            R.color.topeka_accent),
    blue(R.color.theme_blue_primary, R.color.theme_blue_primary_dark,
            R.color.theme_blue_background, R.color.theme_blue_text,
            R.color.theme_blue_accent),
    green(R.color.theme_green_primary, R.color.theme_green_primary_dark,
            R.color.theme_green_background, R.color.theme_green_text,
            R.color.theme_green_accent),
    purple(R.color.theme_purple_primary, R.color.theme_purple_primary_dark,
            R.color.theme_purple_background, R.color.theme_purple_text,
            R.color.theme_purple_accent),
    red(R.color.theme_red_primary, R.color.theme_red_primary_dark,
            R.color.theme_red_background, R.color.theme_red_text,
            R.color.theme_red_accent),
    yellow(R.color.theme_yellow_primary, R.color.theme_yellow_primary_dark,
            R.color.theme_yellow_background, R.color.theme_yellow_text,
            R.color.theme_yellow_accent);

    private final int mColorPrimaryId;
    private final int mWindowBackgroundColorId;
    private final int mColorPrimaryDarkId;
    private final int mTextColorPrimaryId;
    private final int mAccentColorId;


    Theme(final int colorPrimaryId, final int colorPrimaryDarkId,
          final int windowBackgroundColorId, final int textColorPrimaryId,
          final int accentColorId) {
        mColorPrimaryId = colorPrimaryId;
        mWindowBackgroundColorId = windowBackgroundColorId;
        mColorPrimaryDarkId = colorPrimaryDarkId;
        mTextColorPrimaryId = textColorPrimaryId;
        mAccentColorId = accentColorId;
    }

    @ColorRes
    public int getTextPrimaryColor() {
        return mTextColorPrimaryId;
    }

    @ColorRes
    public int getWindowBackgroundColor() {
        return mWindowBackgroundColorId;
    }

    @ColorRes
    public int getPrimaryColor() {
        return mColorPrimaryId;
    }

    @ColorRes
    public int getAccentColor() {
        return mAccentColorId;
    }

    @ColorRes
    public int getPrimaryDarkColor() {
        return mColorPrimaryDarkId;
    }
}
