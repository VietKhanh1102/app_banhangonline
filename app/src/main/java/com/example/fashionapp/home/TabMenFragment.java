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
import android.widget.TableLayout;

import com.example.fashionapp.Activity.ChiTietProductActivity;
import com.example.fashionapp.Adapter.ImageAdapter;
import com.example.fashionapp.Adapter.PhotoViewPager2Adapter;
import com.example.fashionapp.Adapter.SanPhamNoiBatAdapter;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Interface.IntegerCallBack;
import com.example.fashionapp.Model.DataProduct;
import com.example.fashionapp.Model.Image;
import com.example.fashionapp.Model.Photo;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabMenFragment extends Fragment {

    private CircleImageView imgMacNgoai,
            imgSoMi,imgThun,imgQuanDai, imgQuanShort,
            imgPolo, imgDoMacNha,imgDoLot;
    private View view;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mIndicator3;
    private List<Image> imageList;

    private RecyclerView mRecyclerView, saleRecyclerView;
    private List<Product> mList;
    private SanPhamNoiBatAdapter adapter;



    private IntegerCallBack integerCallBack;

    private TableLayout tableLayout;

    public TabMenFragment(IntegerCallBack listener) {
        this.integerCallBack = listener;
    }

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

    public TabMenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_tab_men, container, false );
        AnhXa();
        onClick();
        loadURLImg();
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
        return view;
    }

    private void AnhXa() {
        imgMacNgoai=view.findViewById( R.id.img_mac_ngoai );
        imgSoMi=view.findViewById( R.id.img_so_mi );
        imgThun=view.findViewById( R.id.img_ao_thun );
        imgQuanDai=view.findViewById( R.id.img_quan_dai );
        imgQuanShort=view.findViewById( R.id.img_quan_short );
        imgPolo=view.findViewById( R.id.img_polo );
        imgDoMacNha=view.findViewById( R.id.img_do_mac_nha );
        imgDoLot=view.findViewById( R.id.img_do_lot );
        mViewPager2= view.findViewById( R.id.view_page_top );
        mIndicator3= view.findViewById( R.id.circleIndicator3 );
        tableLayout = view.findViewById( R.id.tb_featured_category );
        adapter = new SanPhamNoiBatAdapter( getActivity() );
        mRecyclerView =view.findViewById( R.id.recyc_product_sale );

    }

    private void onClick() {
        tableLayout.setOnClickListener( v -> {
            integerCallBack.integerCallBack( 1 );
        } );
    }

    private void loadURLImg() {
        String imageUrl1 = "https://im.uniqlo.com/global-cms/spa/res4834fdd5cdf58a93a28010d6a3757696fr.jpg";
        String imageUrl2 = "https://im.uniqlo.com/global-cms/spa/res9fc25ae2875affafa76990a605cefb5dfr.jpg";
        String imageUrl3 = "https://im.uniqlo.com/global-cms/spa/resf3d9c854951fe4ec9add3b2f545cd410fr.jpg";
        String imageUrl4 = "https://im.uniqlo.com/global-cms/spa/res0f532481e1253864af9d6c30abe3f707fr.jpg";
        String imageUrl5 = "https://im.uniqlo.com/global-cms/spa/res6fb8419fb0b905a1659bf52fff173450fr.jpg";
        String imageUrl6 = "https://im.uniqlo.com/global-cms/spa/res387115447eca49c744f29e9d6d268987fr.jpg";
        String imageUrl7 = "https://im.uniqlo.com/global-cms/spa/resb8e1122fad5f4cf2b749214c1d7d81fbfr.jpg";
        String imageUrl8 = "https://im.uniqlo.com/global-cms/spa/res54eec6ab75c5c0fdfd418f11a4c46363fr.jpg";

        Picasso.get().load(imageUrl1).into(imgMacNgoai);
        Picasso.get().load(imageUrl2).into(imgSoMi);
        Picasso.get().load(imageUrl3).into(imgThun);
        Picasso.get().load(imageUrl4).into(imgQuanDai);
        Picasso.get().load(imageUrl5).into(imgQuanShort);
        Picasso.get().load(imageUrl6).into(imgPolo);
        Picasso.get().load(imageUrl7).into(imgDoMacNha);
        Picasso.get().load(imageUrl8).into(imgDoLot);
    }
    private List<Image> getListImage() {
        List<Image> list =new ArrayList<>();
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/rese8b304dae4f49f367231e604109cea41fr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/resc2fb77a858c9f854acf4d68866a4918efr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/res8ef91c7e92a95a35ca273664177a42c1fr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/resb387f558b363883b8a196e9593313730fr.jpg") );
        list.add( new Image("https://im.uniqlo.com/global-cms/spa/resf46418d98dcb9ff1ad47b614a696f3b9fr.jpg") );
        return list;
    }

    private List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();

        callApiAoWomen();
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
        APIService.apiServiceKids.getSanPhamMoiNam().enqueue(new Callback<DataProduct>() {
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
}