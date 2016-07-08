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

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.adapter.DailyAdapter;
import com.example.jony.myapp.reader_APP.adapter.NewsItemAdapter;
import com.example.jony.myapp.reader_APP.db.cache.DailyCache;
import com.example.jony.myapp.reader_APP.db.cache.NewsCache;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.example.jony.myapp.reader_APP.utils.Settings;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

/**
 * Created by Jony on 2016/6/16.
 */
public class NewsItemFragment extends Fragment {


    private RecyclerView mRv;
    private ProgressBar mPb;

    private PullToRefreshView refreshView;

    private NewsItemAdapter adapter;

    private NewsCache mNewsCache;

    private ArrayList<NewsBean> mList = new ArrayList<>();

    private boolean needCache;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.ID_FAILURE:
                    Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                    break;

                case CONSTANT.ID_SUCCESS:
                    initView();
                    if(needCache){
                        mNewsCache.cache(mCategory,mList);
                    }
                    break;
                case CONSTANT.ID_LOADMORE:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private String mUrl;
    private String mCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUrl = getArguments().getString(getString(R.string.reader_id_url));
        mCategory = getArguments().getString(getString(R.string.reader_id_category));

        needCache = Settings.needCache;

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

    private void initData() {

        mNewsCache = new NewsCache(mHandler);
        mNewsCache.load(mUrl,mList);

    }


    private void initView() {

        if (mList.size() > 0) {

            adapter = new NewsItemAdapter(getActivity(),mList);
            mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRv.setAdapter(adapter);
            mPb.setVisibility(View.GONE);

        }

    }
}
