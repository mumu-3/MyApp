package com.example.jony.myapp.reader_APP.ui;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/2/17 0017.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 10;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //可见的时候，上滑（dy>0) 隐藏，
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        }
        //不可见的时候，下滑，出现
        else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }
        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }

    }


    public abstract void onHide();

    public abstract void onShow();
}