package com.example.fashionapp.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionapp.Activity.ChiTietProductActivity;
import com.example.fashionapp.Adapter.ImageAdapter;
import com.example.fashionapp.Adapter.SanPhamNoiBatAdapter;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Model.DataProduct;
import com.example.fashionapp.Model.Image;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHomeFragment extends Fragment {
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mIndicator3;
    private View view;
    private RecyclerView mRecyclerView, saleRecyclerView;
    private SanPhamNoiBatAdapter adapter, adapterSale;

    private List<Image> imageList;

    private List<Product> mList;

    private Handler mHandler= new Handler( Looper.getMainLooper());
    private Runnable mRunnable= new Runnable() {
        @Override
        public void run() {
            int troi =  mViewPager2.getCurrentItem();
            if (troi ==imageList.size()-1){
                mViewPager2.setCurrentItem( 0 );
            }else {
                mViewPager2.setCurrentItem( troi+1 );
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_tab_home, container, false );

        //setting img view_page
        mViewPager2= view.findViewById( R.id.view_page_top );
        mIndicator3= view.findViewById( R.id.circleIndicator3 );
        mViewPager2.setOffscreenPageLimit( 3 );
        mViewPager2.setClipToPadding( false );
        mViewPager2.setClipChildren( false );


        imageList=getListImage();
        ImageAdapter imageAdapter= new ImageAdapter(getActivity(), imageList );
        mViewPager2.setAdapter( imageAdapter );
        mIndicator3.setViewPager( mViewPager2 );
        mViewPager2.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected( position );
                mHandler.removeCallbacks( mRunnable );
                mHandler.postDelayed( mRunnable,3000 );
            }
        } );



        AnhXa();


        GridLayoutManager gridLayoutManager= new GridLayoutManager( getActivity(),3);
        mRecyclerView.setLayoutManager( gridLayoutManager );
        mRecyclerView.setNestedScrollingEnabled( false );
        adapter.setData( getListProduct( ), new ClickItemProduct() {
            @Override
            public void onItemProductClick(Product product) {
                onClickgotoChitiet(product);
            }

            @Override
            public void onClickFavoriteItem(int pos) {

            }
        } );
        mRecyclerView.setAdapter( adapter );
        //
        GridLayoutManager gridLayoutManager1= new GridLayoutManager( getActivity(),3);
        saleRecyclerView.setLayoutManager( gridLayoutManager1 );
        saleRecyclerView.setNestedScrollingEnabled( false );
        adapterSale.setData( getListProductSale( ), new ClickItemProduct() {
            @Override
            public void onItemProductClick(Product product) {
                onClickgotoChitiet(product);
            }

            @Override
            public void onClickFavoriteItem(int pos) {

            }
        } );
        saleRecyclerView.setAdapter( adapterSale );

        return view;


    }


    private List<Image> getListImage() {
        List<Image> list =new ArrayList<>();
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/res6b8ce45b8387ddcf7cec6a3e92848c23fr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/resd4df83327e0165e6f1b21207cdbbcdeafr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/res0883ec0339f52ea55c053dff593ecbbbfr.jpg") );
        list.add( new Image("https://media.istockphoto.com/id/694044976/vi/anh/t%C3%B4i-bi%E1%BA%BFt-t%C3%B4i-s%E1%BA%BD-t%C3%ACm-th%E1%BA%A5y-th%E1%BB%A9-g%C3%AC-%C4%91%C3%B3-t%C3%B4i-th%C3%ADch-%E1%BB%9F-%C4%91%C3%A2y.jpg?s=1024x1024&w=is&k=20&c=f_kEBtvKh6UpWLDfexc-mUxSOgwlgCthD0jNeUDnKiI=") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/reseef4ead8734e46647f914d68b13cc4dffr.jpg") );
        return list;
    }

    private List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();

        callApiAoWomen();
        return list;
    }
    private List<Product> getListProductSale() {
        List<Product> list = new ArrayList<>();

        callApiProductSale();
        return list;
    }

    private void onClickgotoChitiet(Product product) {
        Intent intent = new Intent( getActivity(), ChiTietProductActivity.class );
        Bundle bundle= new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void callApiAoWomen() {
        APIService.apiServiceKids.getSanPhamNoiBat().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );
            }
            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                //Toast.makeText( getActivity(), "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiProductSale() {
        APIService.apiServiceKids.getSanPhamSale().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapterSale.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                saleRecyclerView.setAdapter( adapterSale );
            }
            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();

            }
        } );
    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks( mRunnable );
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed( mRunnable,3000 );
    }
    private void AnhXa() {
        mRecyclerView =view.findViewById( R.id.recyc_product );
        saleRecyclerView=view.findViewById( R.id.recyc_product_sale );
        adapter = new SanPhamNoiBatAdapter( getActivity() );
        adapterSale=new SanPhamNoiBatAdapter( getActivity() );

    }

}