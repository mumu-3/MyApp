package com.example.jony.myapp.reader_APP.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * Created by Jony on 2016/6/16.
 */
public class TabImageView extends ImageView implements Checkable {

    private boolean mChecked;

    public TabImageView(Context context) {
        this(context,null);
    }

    public TabImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {

        setChecked(!mChecked);
    }
}
