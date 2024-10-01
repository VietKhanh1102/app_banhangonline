package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Adapter.MessageAdapter;
import com.example.fashionapp.Fragment.ProfileFragment;
import com.example.fashionapp.Model.JsonResponse;
import com.example.fashionapp.Model.Message;
import com.example.fashionapp.Model.ResponePost;
import com.example.fashionapp.Model.ResponseUpDateUser;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText messageEditText;
    private ImageButton sendButton;
    private Gson gson;
    private OkHttpClient client;
    private ImageView ivBackChat;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AnhXa();
        String id = UserManager.getInstance().getCurrentUser().getUser_id();
        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        ivBackChat = findViewById(R.id.iv_back_chat);

        gson = new Gson();
        client = new OkHttpClient();

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        ivBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText, Message.SENT_BY_ME, id);
                    messageEditText.setText("");
                }
            }
        });


        loadMessages(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = UserManager.getInstance().getCurrentUser().getUser_id();
        loadMessages(id);
    }

    private void sendMessage(String messageText, String sentBy, String userId) {
        Message message = new Message(messageText, sentBy, userId);
        messageList.add(message);
        messageAdapter.notifyDataSetChanged();
        try {
            testluu(message.getSentBy(), message.getUser_id(), message.getMessage());

        } catch (Exception e) {
            System.out.print(e);
        }
        // Send message to API
        sendMessageToApi(message);
    }

    private void sendMessageToApi(Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, gson.toJson(message));
                    Request request = new Request.Builder()
                            .url(Utils.BASE_URL_CHAT + "/khanhdo/chat")
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JsonResponse jsonResponse = gson.fromJson(responseData, JsonResponse.class);
                        if (jsonResponse.getData() != null) {
                            String id = UserManager.getInstance().getCurrentUser().getUser_id();
                            Message botMessage = new Message(jsonResponse.getData(), Message.SENT_BY_BOT, id);
                            messageList.add(botMessage);

                            testluu(botMessage.getSentBy(), botMessage.getUser_id(), botMessage.getMessage());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messageAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messageList.size() - 1);
//                                    saveMessagesToPreferences();

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void testluu(String sent_by, String user_id, String message_text) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.187/uniquilo/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
            apiInterface.sendMessage(user_id, message_text, sent_by)
                    .enqueue(new Callback<ResponePost>() {
                        @Override
                        public void onResponse(Call<ResponePost> call, retrofit2.Response<ResponePost> response) {
                            if (response.isSuccessful()) {

                                if (response.body() != null) {
                                    String status = String.valueOf(response.body().getMessage());
                                    Log.d("AAA", "aa" + status);
                                }
                                Toast.makeText(ChatActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(ChatActivity.this, "Cập nhật thất ", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponePost> call, Throwable t) {
                            //Toast.makeText(ChatActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    private void loadMessages(String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.187/uniquilo/")  // Đặt đúng địa chỉ của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
        apiInterface.getMessages(userId)
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, retrofit2.Response<List<Message>> response) {
                        if (response.isSuccessful()) {
                            List<Message> newMessages = response.body();
                            if (newMessages != null) {
                                // Thêm tin nhắn mới vào đầu danh sách
                                messageList.addAll(0, newMessages);
                                messageAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(0); // Cuộn đến đầu danh sách
                            }
                        } else {
                            Toast.makeText(ChatActivity.this, "Không thể tải tin nhắn", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Message>> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }
    private void restoreMessagesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("messageList", "");
        Type type = new TypeToken<ArrayList<Message>>() {}.getType();
        messageList = gson.fromJson(json, type);

        if (messageList == null) {
            messageList = new ArrayList<>();
        }

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
    }



    private void AnhXa() {
        // Không có gì cần thiết ở đây nếu bạn không có mã khởi tạo view
    }
}

