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
import com.example.jony.myapp.reader_APP.api.ReadingApi;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Jony on 2016/6/15.
 */
public class ReadingFragment extends Fragment {

    private View parentView;
    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private PagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = View.inflate(getContext(), R.layout.layout_top_navigation,null);
        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        smartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tab_layout);
        smartTabLayout.setVisibility(View.VISIBLE);
        initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        return parentView;
    }



    private PagerAdapter initPagerAdapter() {
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), ReadingApi.Tag_Titles) {
            @Override
            public Fragment getItem(int position) {
                ReadingItemFragment fragment = new ReadingItemFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.reader_id_pos),position);
                bundle.putString(getString(R.string.reader_id_category),ReadingApi.Tag_Titles[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null){
            getChildFragmentManager().getFragments().clear();
        }
    }
}
