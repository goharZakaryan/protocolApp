package com.example.protocolapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private View firstView;
    private MaterialButton materialButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        findViewById();
//        firstView.setBackgroundColor(getResources().getColor(R.color.white));
        materialButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
    private  void findViewById(){
        firstView = findViewById(R.id.view);
        materialButton = findViewById(R.id.button2);
    }
}