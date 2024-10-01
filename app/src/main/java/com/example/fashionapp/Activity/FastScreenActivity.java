package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;

public class FastScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_screen);
        UserManager.getInstance().init(this);


        Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 500 );
    }

    private void nextActivity() {
        UserManager userManager = UserManager.getInstance();

        //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!userManager.isLoggedIn()) {
            //chua login
            Intent intent = new Intent( this, LoginActivity.class );
            startActivity( intent );

        } else {
            //da login
            Intent intent = new Intent( this, MainActivity.class );
            startActivity( intent );

        }
        finish();
    }
}