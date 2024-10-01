package com.example.fashionapp.Retrofit;

import com.example.fashionapp.Model.Message;
import com.example.fashionapp.Model.Notification;
import com.example.fashionapp.Model.ProductUpdateRequest;
import com.example.fashionapp.Model.ResponePost;
import com.example.fashionapp.Model.ResponseUpDatePass;
import com.example.fashionapp.Model.ResponseUpDateUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getSignIn/readGetUser.php")
    Call<GetUserResponse> getListUser();

    @POST("getUser/dangky.php")
    @FormUrlEncoded
    Call<SignUpResponse> dangky(
            @Field("username") String username,
            @Field("gender") String gender,
            @Field("phoneNumber") String phonenumber,
            @Field("address") String address,
            @Field("city") String city,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Call<ResponePost> createOder(
            @Field("user_id") int user_id,
            @Field("soluong") int soluong,
            @Field("total_money") int total_money,
            @Field("status") String status,
            @Field("payment_methods") String payment_methods,
            @Field("note") String note,
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("diachi") String diachi,
            @Field("created_date") String created_date,
            @Field("chitiet") String chitiet
    );

    @POST("getUser/update_user.php")
    @FormUrlEncoded
    Call<ResponseUpDateUser> updateUser(
            @Field("user_id") String user_id,
            @Field("username") String username,
            @Field("gender") String gender,
            @Field("phoneNumber") String phonenumber,
            @Field("address") String address,
            @Field("city") String city,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("getUser/update_pass.php")
    @FormUrlEncoded
    Call<ResponseUpDatePass> updatePass(
            @Field("user_id") String user_id,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password
    );


    @FormUrlEncoded
    @POST("send_otp.php")
    Call<ResponePost> sendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("check_otp.php")
    Call<ResponePost> checkOTP(
            @Field("email") String email,
            @Field("otp") String otp
    );

    @POST("deleteHoaDon.php")
    @FormUrlEncoded
    Call<ResponePost> deleteBill(
            @Field("order_id") int order_id
    );

    @POST("getUser/forgot_password.php")
    @FormUrlEncoded
    Call<ResponePost> forgotPassword(
            @Field("email") String email,
            @Field("password") String password
    );

//    @POST("postChat.php")
//    @FormUrlEncoded
//    Call<ResponePost> sendMessage(
//            @Field("user_id") int user_id,
//            @Field("message_text") String message_text,
//            @Field("sent_by") String sent_by
//    );
    @POST("postChat.php")
    @FormUrlEncoded
    Call<ResponePost> sendMessage(
            @Field("user_id") String user_id,
            @Field("message") String message_text,
            @Field("sent_by") String sent_by
    );
    @GET("getChat.php")
    Call<List<Message>> getMessages(
            @Query("user_id") String user_id
    );

//    @GET("getNotifications.php") // Địa chỉ endpoint API để lấy thông báo từ cơ sở dữ liệu
//    Call<List<Notification>> getNotifications();

    @GET("getNotification.php")
    Call<List<Notification>> getNotifications();
    @FormUrlEncoded
    @POST("getUser/login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    // Định nghĩa phương thức cập nhật quantity của sản phẩm
    @FormUrlEncoded
    @POST("update_product_quantity.php")
    Call<ResponePost> updateProductQuantity(
            @Field("product_id") int productId,
            @Field("quantity") int quantity
    );


    @FormUrlEncoded
    @POST("updateProductQuantityOnDelete.php")
    Call<ResponePost> updateProductQuantityOnDelete(
            @Field("product_id") int productId,
            @Field("quantity") int quantity
    );

}
