package com.example.fashionapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPageAdapterSearch extends FragmentStatePagerAdapter {


    public ViewPageAdapterSearch(@NonNull FragmentManager fm, int behavior) {
        super( fm, behavior );
    }

    private List<Fragment> listFm = new ArrayList<>();

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if ( position < getCount()){
            return listFm.get( position );
        }
        return new Fragment();
    }
    public void addFragment(Fragment fragment){
        listFm.add( fragment );
    }

    @Override
    public int getCount() {
        return listFm.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MEN";
            case 1:
                return "WOMEN";

            default:return "MEN";
        }

    }
}

