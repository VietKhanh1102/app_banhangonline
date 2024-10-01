package com.example.fashionapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Adapter.ViewPagerAdapter;
import com.example.fashionapp.Fragment.FavoriteFragment;
import com.example.fashionapp.Fragment.HomeFragment;
import com.example.fashionapp.Fragment.NotificationFragment;
import com.example.fashionapp.Fragment.ProfileFragment;
import com.example.fashionapp.Fragment.SearchFramgent;
import com.example.fashionapp.Interface.IntegerCallBack;
import com.example.fashionapp.MainViewModel;
import com.example.fashionapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IntegerCallBack {
    private ViewPager mViewPager;

    private MainViewModel mainViewModel;
    private BottomNavigationView mBottomNavigationView;

    private SearchFramgent searchFramgent;

    private ViewPagerAdapter adapter;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private FavoriteFragment favoriteFragment;
    private NotificationFragment notificationFragment;



    private long backTime;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        AnhXa();
        homeFragment= new HomeFragment(this);
        profileFragment= new ProfileFragment();
        searchFramgent = new SearchFramgent();
        notificationFragment= new NotificationFragment();
        favoriteFragment= new FavoriteFragment();

        //bắt sự kiện navigation
        adapter= new ViewPagerAdapter( getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment( homeFragment );
        adapter.addFragment( searchFramgent );
        adapter.addFragment( favoriteFragment);
        adapter.addFragment( notificationFragment );
        adapter.addFragment( profileFragment );
        mViewPager.setAdapter( adapter );
        mViewPager.setOffscreenPageLimit( 4 );

        //tắt chức năng vuốt băngf onTouch
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });



        //vuốt sang trái phải thanh navigation bên dưới
        mViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mBottomNavigationView.getMenu().findItem(R.id.toolbar_home).setChecked(true);
                } else if (position == 1) {
                    mBottomNavigationView.getMenu().findItem(R.id.toolbar_search).setChecked(true);
                } else if (position == 2) {
                    mBottomNavigationView.getMenu().findItem(R.id.toolbar_favorite).setChecked(true);
                } else if (position == 3) {
                    mBottomNavigationView.getMenu().findItem(R.id.toolbar_notification).setChecked(true);
                } else if (position == 4) {
                    mBottomNavigationView.getMenu().findItem(R.id.toolbar_profile).setChecked(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );
        //ấn nút thay vi vuốt sang ngang
        mBottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.toolbar_home) {
                    mViewPager.setCurrentItem(0);
                } else if (itemId == R.id.toolbar_search) {
                    mViewPager.setCurrentItem(1);
                } else if (itemId == R.id.toolbar_favorite) {
                    mViewPager.setCurrentItem(2);
                } else if (itemId == R.id.toolbar_notification) {
                    mViewPager.setCurrentItem(3);
                } else if (itemId == R.id.toolbar_profile) {
                    mViewPager.setCurrentItem(4);
                }
                return false;
            }

        } );




    }

    private void AnhXa() {
        mBottomNavigationView = findViewById(R.id.bottomNavView);
        mViewPager = findViewById(R.id.content_frame);
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        if (backTime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            mToast = Toast.makeText( MainActivity.this, "Chạm lại để thoát", Toast.LENGTH_SHORT );
            mToast.show();
        } backTime=System.currentTimeMillis();
    }

    @Override
    public void integerCallBack(int i) {
        mViewPager.setCurrentItem( 1 );
        mBottomNavigationView.setSelectedItemId( R.id.toolbar_search );
        if (searchFramgent != null){
            searchFramgent.setCurrentPage( i - 1);
        }
    }
}