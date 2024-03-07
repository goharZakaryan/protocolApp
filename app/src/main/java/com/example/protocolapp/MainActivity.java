package com.example.protocolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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
            Intent intent = new Intent(MainActivity.this, ModeActivity.class);
            startActivity(intent);
        });

    }
    private  void findViewById(){
        firstView = findViewById(R.id.view);
        materialButton = findViewById(R.id.button2);
    }
}