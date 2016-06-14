package com.example.jony.myapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jony on 2016/6/14.
 */
public class Category implements Parcelable {

    public static final String TAG = "Category";

    private int mName;
    private int mResId;
    private Theme mTheme;


    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };


    public Category(int string,int resId,Theme theme) {
        mName = string;
        mResId = resId;
        mTheme = theme;
    }

    public Category(Parcel in) {
        mName = in.readInt();
        mResId = in.readInt();
        mTheme = Theme.values()[in.readInt()];
    }

    public int getName() {
        return mName;
    }

    public void setName(int name) {
        mName = name;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public Theme getTheme() {
        return mTheme;
    }

    public void setTheme(Theme theme) {
        mTheme = theme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mName);
        dest.writeInt(mResId);
        dest.writeInt(mTheme.ordinal());
    }
}
