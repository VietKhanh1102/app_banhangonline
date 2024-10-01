package com.example.fashionapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fashionapp.Activity.SearchActivity;
import com.example.fashionapp.Adapter.ViewPageAdapterSearch;
import com.example.fashionapp.R;
import com.google.android.material.tabs.TabLayout;

public class SearchFramgent extends Fragment {
    private TabLayout mTableLayout;
    private ViewPager mViewPager;
    private View mView;
    private LinearLayout linearLayoutSerch;
    @Override
    public void onResume() {
        super.onResume();
        Log.e( "Check","reload framgent search" );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate( R.layout.fragment_search_framgent,container,false );
        AnhXa();



        WomenSearchFramgent womenFragment = new WomenSearchFramgent();
        MenSearchFragment menFragment = new MenSearchFragment();
        ViewPageAdapterSearch adapterSearch= new ViewPageAdapterSearch(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT  );

        adapterSearch.addFragment( menFragment );
        adapterSearch.addFragment( womenFragment );
        mViewPager.setAdapter( adapterSearch );
        mTableLayout.setupWithViewPager( mViewPager );
        oncClickListner();

        return mView;
    }
    public void setCurrentPage(int i){
        mViewPager.setCurrentItem( i );
        mTableLayout.setScrollPosition( i, 0f, true );
    }
    private void AnhXa() {
        mTableLayout = mView.findViewById( R.id.tab_search);
        mViewPager=mView.findViewById( R.id.view_search );
        linearLayoutSerch=mView.findViewById( R.id.linea_search );

    }
    public void oncClickListner(){
        linearLayoutSerch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SearchActivity.class );
                startActivity( intent );
            }
        } );

    }
}