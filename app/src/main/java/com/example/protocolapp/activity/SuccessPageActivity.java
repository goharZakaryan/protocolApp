package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class SuccessPageActivity extends AppCompatActivity {
    private MaterialButton toTheProtocols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        findViewById();
        setButtonClickListener();
    }

    private void findViewById() {
        toTheProtocols = findViewById(R.id.toTheProtocols);
    }

    private void setButtonClickListener() {
        toTheProtocols.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessPageActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }



}