package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class CongratsActivity extends AppCompatActivity {
    private MaterialButton congrats;
private Long protocolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
        findViewById();
        protocolId = getIntent().getLongExtra("protocolId",0);

        setButtonClickListener();
    }

    private void findViewById() {
        congrats = findViewById(R.id.congrats);
    }

    private void setButtonClickListener() {
        congrats.setOnClickListener(v -> {
            Intent intent = new Intent(CongratsActivity.this, ScoreActivity.class);
            intent.putExtra("protocolId", protocolId); // Use finalSteps here

            startActivity(intent);
        });

    }



}