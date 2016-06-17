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
import com.example.jony.myapp.reader_APP.adapter.ReadingItemAdapter;
import com.example.jony.myapp.reader_APP.db.cache.CollectionDailyCache;
import com.example.jony.myapp.reader_APP.db.cache.CollectionNewsCache;
import com.example.jony.myapp.reader_APP.db.cache.CollectionReadingCache;
import com.example.jony.myapp.reader_APP.model.daily.StoryBean;
import com.example.jony.myapp.reader_APP.model.news.NewsBean;
import com.example.jony.myapp.reader_APP.model.reading.BookBean;
import com.example.jony.myapp.reader_APP.utils.CONSTANT;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

/**
 * Created by Jony on 2016/6/17.
 */
public class CollectionItemFragment extends Fragment {

    private RecyclerView mRv;
    private ProgressBar mPb;

    private PullToRefreshView refreshView;

    private RecyclerView.Adapter adapter;


    private ArrayList mList;


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
                case CONSTANT.ID_FROM_CACHE:
                    initView();
                    break;
            }
        }
    };

    private int position;
    private ImageView mImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        position = getArguments().getInt(getString(R.string.reader_id_pos));


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collection_list, container, false);

        mRv = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPb = (ProgressBar) view.findViewById(R.id.progressbar);
        mImageView = (ImageView) view.findViewById(R.id.sad_face);

        initData();
        return view;
    }

    private void initData() {

       switch (position){
           case 0:
               mList = new ArrayList<StoryBean>();
               CollectionDailyCache collectionDailyCache = new CollectionDailyCache(mHandler, mList);
               collectionDailyCache.loadFromCache();
               adapter = new DailyAdapter(getActivity(),mList);

               break;
           case 2:
               mList = new ArrayList<BookBean>();
               CollectionReadingCache cache = new CollectionReadingCache(mHandler, mList);
               cache.loadFromCache();
               adapter = new ReadingItemAdapter(getActivity(),mList);
               break;
           case 1:
               mList = new ArrayList<NewsBean>();
               CollectionNewsCache collectionNewsCache = new CollectionNewsCache(mHandler, mList);
               collectionNewsCache.loadFromCache();
               adapter = new NewsItemAdapter(getActivity(),mList);
               break;
       }

    }


    private void initView() {

        if (mList.size() > 0) {

            mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRv.setAdapter(adapter);
            mPb.setVisibility(View.GONE);

        }else if (mList.size() == 0){


            mRv.setVisibility(View.GONE);
            mPb.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }

    }
}
