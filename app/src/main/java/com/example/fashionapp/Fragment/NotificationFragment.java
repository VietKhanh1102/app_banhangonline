package com.example.fashionapp.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.fashionapp.API.RetrofitClient;
import com.example.fashionapp.Adapter.NotificationAdapter;
import com.example.fashionapp.Model.Notification;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_notifications);
        textEmptyView = view.findViewById(R.id.text_empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchNotifications();

        return view;
    }

    private void fetchNotifications() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Notification>> call = apiInterface.getNotifications();

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notifications = response.body();
                    if (notifications.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        textEmptyView.setVisibility(View.GONE);
                        NotificationAdapter adapter = new NotificationAdapter(notifications, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textEmptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                textEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}

