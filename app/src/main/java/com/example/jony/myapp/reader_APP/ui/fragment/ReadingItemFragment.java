package com.example.jony.myapp.reader_APP.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.adapter.NewsItemAdapter;
import com.example.jony.myapp.reader_APP.adapter.ReadingItemAdapter;
import com.example.jony.myapp.reader_APP.api.ReadingApi;
import com.example.jony.myapp.reader_APP.db.cache.NewsCache;
import com.example.jony.myapp.reader_APP.db.cache.ReadingCache;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.model.reading.BookBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

/**
 * Created by Jony on 2016/6/16.
 */
public class ReadingItemFragment extends Fragment {


    private RecyclerView mRv;
    private ProgressBar mPb;

    private PullToRefreshView refreshView;

    private ReadingItemAdapter adapter;

    private ReadingCache mReadingCache;

    private ArrayList<BookBean> mList = new ArrayList<>();

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
    private String mUrl;
    private String mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mName = getArguments().getString(getString(R.string.reader_id_category));

        mUrl = ReadingApi.searchByTag + mName;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_list, container, false);

        mRv = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPb = (ProgressBar) view.findViewById(R.id.progressbar);

        initData();


        return view;
    }

    public void initData() {

        mReadingCache = new ReadingCache(mHandler);
        mReadingCache.load(mUrl, mList);

    }


    private void initView() {

        if (mList.size() > 0) {

            adapter = new ReadingItemAdapter(getActivity(), mList);
            mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRv.setAdapter(adapter);
            mPb.setVisibility(View.GONE);

        }

    }
}
