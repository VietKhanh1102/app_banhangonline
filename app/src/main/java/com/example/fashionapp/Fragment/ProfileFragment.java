package com.example.fashionapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fashionapp.Activity.ChangePasswordActivity;
import com.example.fashionapp.Activity.ChatActivity;
import com.example.fashionapp.Activity.ChiTietDonHangActivity;
import com.example.fashionapp.Activity.LichSuDonHangActivity;
import com.example.fashionapp.Activity.LoginActivity;
import com.example.fashionapp.Activity.UpdateMyProfile;
import com.example.fashionapp.MainViewModel;
import com.example.fashionapp.Model.User;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;

import java.util.List;

public class ProfileFragment extends Fragment {
    private View mView;
    private Button btnDangXuat,btnChinhSuaHoSo, btnChangePassword,  btnLichSu, btnChat, btnDonHang;


    private TextView tvUserName,tvEmail;

    private List<User> mListUser;

    private OnChangeAddressClickListener changeAddressClickListener;

    private MainViewModel mainViewModel;



    @Override
    public void onResume() {
        super.onResume();
        Log.e( "Check","reload framgent Person" );
    }

    public interface OnChangeAddressClickListener {
        void onChangeAddressClick();
    }

    public void setOnChangeAddressClickListener(OnChangeAddressClickListener listener) {
        changeAddressClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate( R.layout.fragment_profile,container,false );
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        AnhXa();
        onClickListener();
//        showUserInformation();

        mListUser = mainViewModel.users;
        return mView;
    }

    private void AnhXa() {
        btnDangXuat=mView.findViewById( R.id.btn_dang_xuat );
        tvUserName=mView.findViewById( R.id.tv_show_user_name);
        tvEmail=mView.findViewById( R.id.tv_show_email );
        btnChinhSuaHoSo=mView.findViewById( R.id.btn_chinh_sua_profile );
        btnChangePassword=mView.findViewById(R.id.btn_change_password);
        btnLichSu=mView.findViewById( R.id.btn_lich_su_don_hang );
//        edtEmailSignIn = mView.findViewById(R.id.edt_email)
        btnChat = mView.findViewById(R.id.btn_chat);
        btnDonHang=mView.findViewById( R.id.btn_donhang );



        User currentUser = UserManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            tvUserName.setText(currentUser.getUsername());
            tvEmail.setText(currentUser.getEmail());
        } else {
            // Xử lý trường hợp currentUser là null
        }
    }
    private void onClickListener() {
//        btnDangXuat.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(getActivity(), LoginActivity.class );
//                startActivity(intent);
//                UserManager.getInstance().dangXuat();
//                getActivity().finish();
//            }
//        } );
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xác nhận Đăng xuất");
                builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");

                // Nút Đồng ý
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý đăng xuất ở đây
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        UserManager.getInstance().dangXuat();
                        getActivity().finish();
                    }
                });

                // Nút Hủy
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì cả, chỉ đóng hộp thoại
                    }
                });

                // Tạo và hiển thị hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), ChangePasswordActivity.class );
                startActivity(intent);
                return;
            }
        });



        btnChinhSuaHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateMyProfile.class);
                startActivity(intent);
            }
        });
        btnLichSu.setOnClickListener( v->{
            Intent intent= new Intent(getActivity(), LichSuDonHangActivity.class );
            startActivity( intent );
        } );
        btnDonHang.setOnClickListener( v->{
            Intent intent= new Intent(getActivity(), ChiTietDonHangActivity.class );
            startActivity( intent );
        } );

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);

            }
        });




    }

}