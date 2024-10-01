package com.example.fashionapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fashionapp.Activity.MenListItem;
import com.example.fashionapp.Adapter.ItemAdapter;
import com.example.fashionapp.Interface.ClickItemMenSearch;
import com.example.fashionapp.Model.Item;
import com.example.fashionapp.R;

import java.util.ArrayList;
import java.util.List;


public class MenSearchFragment extends Fragment {
    private RecyclerView rcvItemMen;
    private ItemAdapter itemAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_men_search, container, false);
        rcvItemMen = mView.findViewById(R.id.rcv_itemMen);

        itemAdapter = new ItemAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvItemMen.setLayoutManager(linearLayoutManager);
        rcvItemMen.addItemDecoration(new DividerItemDecoration(rcvItemMen.getContext(), linearLayoutManager.getOrientation()));

        itemAdapter.setData(getListItem(), new ClickItemMenSearch() {
            @Override
            public void onItemClick(Item item, int pos) {
                onClickGoToMenListItem(item, pos);
            }
        });
        rcvItemMen.setAdapter(itemAdapter);
        return mView;
    }
    private void onClickGoToMenListItem(Item item, int pos) {
        Intent intent = new Intent(getActivity(), MenListItem.class );
        Bundle bundle = new Bundle();
        bundle.putInt( "pos", pos );
        bundle.putSerializable( "object_item",item );
        intent.putExtras( bundle );
        startActivity( intent );
    }

    private List<Item> getListItem() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("ÁO"));
        list.add(new Item("ĐỒ MẶC NGOÀI"));
        list.add(new Item("QUẦN"));
        list.add(new Item("ĐỒ MẶC TRONG & ĐỒ LÓT"));
        list.add(new Item("ĐỒ MẶC NHÀ"));
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}