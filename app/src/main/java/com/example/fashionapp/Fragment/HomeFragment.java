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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fashionapp.Activity.GioHangActivity;
import com.example.fashionapp.Activity.SearchActivity;
import com.example.fashionapp.Adapter.HomeViewPagerAdapter;
import com.example.fashionapp.Interface.IntegerCallBack;
import com.example.fashionapp.R;
import com.example.fashionapp.home.TabHomeFragment;
import com.example.fashionapp.home.TabMenFragment;
import com.example.fashionapp.home.TabWomenFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    private TabLayout mTableLayout;
    private ViewPager mViewPager;
    private View mView;
    private ImageView imgGioHang;
    private LinearLayout linearLayoutSerch;

    private IntegerCallBack integerCallBack;

    public HomeFragment() {
    }

    public HomeFragment(IntegerCallBack listener){
        this.integerCallBack = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e( "Check","reload framgent home" );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        AnhXa();

        TabHomeFragment tabHomeFragment = new TabHomeFragment();
        TabMenFragment tabMenFragment= new TabMenFragment(integerCallBack);
        TabWomenFragment tabWomenFragment = new TabWomenFragment(integerCallBack);
        HomeViewPagerAdapter adapter=new HomeViewPagerAdapter( getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        adapter.addFragment( tabHomeFragment );
        adapter.addFragment( tabMenFragment );
        adapter.addFragment( tabWomenFragment );
        mViewPager.setAdapter( adapter );
        mTableLayout.setupWithViewPager( mViewPager );

        oncClickListner();
        return mView;
    }

    private void AnhXa() {
        mTableLayout = mView.findViewById( R.id.tab_top);
        mViewPager=mView.findViewById( R.id.view_home );
        imgGioHang=mView.findViewById( R.id.img_gio_hang );
        linearLayoutSerch=mView.findViewById( R.id.ll_search );
    }

    public void oncClickListner(){
        imgGioHang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), GioHangActivity.class);
                startActivity( intent );
            }
        } );
        linearLayoutSerch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SearchActivity.class );
                startActivity( intent );
            }
        } );

    }


}
