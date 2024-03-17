package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class DonePageActivity extends AppCompatActivity {
    private MaterialButton done;
private Long protocolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        findViewById();
        protocolId = getIntent().getLongExtra("protocolId",0);

        setButtonClickListener();
    }

    private void findViewById() {
        done = findViewById(R.id.done);
    }

    private void setButtonClickListener() {
        done.setOnClickListener(v -> {
            Intent intent = new Intent(DonePageActivity.this, CongratsActivity.class);
            intent.putExtra("protocolId", protocolId); // Use finalSteps here

            startActivity(intent);
        });

    }



}