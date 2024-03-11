package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.google.android.material.button.MaterialButton;

public class ProtocolActivity extends AppCompatActivity {
    private MaterialButton scannerButton, activeMode, silentMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        findViewById();
        setButtonClickListener();
        String activeModeText = "Active Mode \n\n Для новичка, проведёт Вас сначала до \n конца вместе с голосовым помощником";
        String silentModeText = "Silent Mode \n\n Для профи, не будет мешать, напомнит \n только при случае необходимости";

        SpannableString spannableStringAM = createSpannableString(activeModeText);
        SpannableString spannableStringSM = createSpannableString(silentModeText);

        activeMode.setText(spannableStringAM);
        silentMode.setText(spannableStringSM);
    }

    private void findViewById() {
        scannerButton = findViewById(R.id.scannerButton);
        activeMode = findViewById(R.id.activeMode);
        silentMode = findViewById(R.id.silentMode);
    }

    private void setButtonClickListener() {
        scannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProtocolActivity.this, MainActivity.class);
            startActivity(intent);
        });
        activeMode.setOnClickListener(v -> {
            Intent intent = new Intent(ProtocolActivity.this, ExecuteProtocolActivity.class);
            startActivity(intent);
        });
    }

    private SpannableString createSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), 12, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}