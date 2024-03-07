package com.example.protocolapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ExecuteProtokolActivity extends AppCompatActivity {
    private MaterialButton scannerButton, step1, step2, step3, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_protocol);
        findViewById();
        setButtonClickListener();

    }

    private void findViewById() {
        scannerButton = findViewById(R.id.scannerButton);
        back = findViewById(R.id.back);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
    }

    private void setButtonClickListener() {
        scannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExecuteProtokolActivity.this, MainActivity.class);
            startActivity(intent);
        });
        back.setOnClickListener(v -> {
            Intent intent = new Intent(ExecuteProtokolActivity.this, ModeActivity.class);
            startActivity(intent);
        });
    }


}