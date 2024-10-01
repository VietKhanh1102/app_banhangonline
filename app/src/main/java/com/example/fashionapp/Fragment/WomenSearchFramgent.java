package com.example.fashionapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionapp.Activity.WomenListProductActivity;
import com.example.fashionapp.Adapter.ItemAdapter;
import com.example.fashionapp.Interface.ClickItemMenSearch;
import com.example.fashionapp.Model.Item;
import com.example.fashionapp.R;

import java.util.ArrayList;
import java.util.List;

public class WomenSearchFramgent extends Fragment {
    private RecyclerView mRecyclerView;
    private ItemAdapter itemAdapter;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate( R.layout.fragment_women_search, container, false );
        mRecyclerView = view.findViewById( R.id.rcv_item_Wonmen);
        itemAdapter = new ItemAdapter( getActivity() );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL,false );
        mRecyclerView.setLayoutManager( linearLayoutManager );
        itemAdapter.setData( getListItem(), new ClickItemMenSearch() {
            @Override
            public void onItemClick(Item item , int pos) {
                onClickGoToWomenListItem(item,pos);
            }
        } );
        mRecyclerView.setAdapter( itemAdapter );
        //phân ngang giữa các item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    private void onClickGoToWomenListItem(Item item, int pos) {
        Intent intent = new Intent(getActivity(), WomenListProductActivity.class );
        Bundle bundle = new Bundle();
        bundle.putSerializable( "item",item );
        bundle.putInt( "ps",pos );
        intent.putExtras( bundle );
        startActivity( intent );
    }


    @NonNull
    private List<Item> getListItem(){
        List<Item> list = new ArrayList<>();
        list.add( new Item("ÁO"));
        list.add( new Item("ĐỒ MẶC NGOÀI"));
        list.add( new Item("QUẦN"));
        list.add( new Item("VÁY & ĐẦM"));
        list.add( new Item("ĐỒ BẦU"));
        list.add( new Item("ĐỒ MẶC TRONG & ĐỒ LÓT"));
        list.add( new Item("ĐỒ MẶC NHÀ"));
        return list;
    }
}