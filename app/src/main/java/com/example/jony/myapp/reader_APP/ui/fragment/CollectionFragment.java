package com.example.jony.myapp.reader_APP.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jony.myapp.R;
import com.example.jony.myapp.reader_APP.adapter.PagerAdapter;
import com.example.jony.myapp.reader_APP.api.NewsApi;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Jony on 2016/6/17.
 */
public class CollectionFragment extends Fragment {


    private String[] name;
    private String[] url;
    private View parentView;
    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private PagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_top_navigation, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.inner_viewpager);
        smartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        smartTabLayout.setVisibility(View.VISIBLE);
        initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        return view;
    }

    private PagerAdapter initPagerAdapter() {
        String [] tabTitles ={getContext().getString(R.string.reader_daily),getContext().getString(R.string.reader_reading)
                ,getContext().getString(R.string.reader_news)};
        pagerAdapter = new PagerAdapter(getChildFragmentManager(),tabTitles) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new CollectionItemFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.reader_id_pos),position);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }
}
