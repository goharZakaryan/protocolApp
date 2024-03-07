package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class UserPageActivity extends AppCompatActivity {
    private MaterialButton add, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        findViewById();
        setButtonClickListener();


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);

    }

    private void setButtonClickListener() {

        back.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, MainActivity.class);
            startActivity(intent);
        });
        add.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, AddProtocolActivity.class);
            startActivity(intent);
        });
    }

}
