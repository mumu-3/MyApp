package com.example.jony.myapp.reader_APP.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jony.myapp.DebugUtils;
import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.adapter.DailyAdapter;
import com.example.jony.myapp.reader_APP.api.DailyApi;
import com.example.jony.myapp.reader_APP.db.cache.DailyCache;
import com.example.jony.myapp.reader_APP.model.daily.DailyBean;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.HttpUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jony on 2016/6/15.
 */
public class DailyFragment extends Fragment {


    private RecyclerView mRv;
    private ProgressBar mPb;
    private RecyclerView.LayoutManager mLayoutManager;

    protected ImageView placeHolder;
    private PullToRefreshView refreshView;

    private DailyAdapter adapter;

    private DailyCache mDailyCache;

    private ArrayList<StoryBean> mList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.ID_FAILURE:
                    Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                    break;

                case CONSTANT.ID_SUCCESS:
                    initView();
                    break;
                case CONSTANT.ID_LOADMORE:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_list, container, false);

        mRv = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPb = (ProgressBar) view.findViewById(R.id.progressbar);

        initData();
        return view;
    }

    private void initData() {

        mDailyCache = new DailyCache(mHandler);
        mDailyCache.load(mList);

    }


    private void initView() {

        if (mList.size() > 0) {

            adapter = new DailyAdapter(getActivity(),mList);
            mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRv.setAdapter(adapter);
            mPb.setVisibility(View.GONE);

        }

    }
}
