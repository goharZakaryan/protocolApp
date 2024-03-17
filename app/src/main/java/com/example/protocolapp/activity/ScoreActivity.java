package com.example.protocolapp.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.Score;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {


    private MaterialButton score1, score2, score3, score4, score5;
    private Button send, back;
    private Long protocolId;
    private int scoreNumber;
    private Score score;
    private EditText commentEt;
    private String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        int darkColor = ContextCompat.getColor(this, R.color.light_gray);
        int lightColor = ContextCompat.getColor(this, R.color.black);

        findViewById();
        setButtonClickListener(lightColor,darkColor);
        protocolId = getIntent().getLongExtra("protocolId", 0);


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        commentEt = findViewById(R.id.commentET);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);
        send = findViewById(R.id.send);
    }

    private void setButtonClickListener(int lightColor, int darkColor) {

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
            startActivity(intent);
        });
        score5.setOnClickListener(v -> {
            if (isDarkColor(score5.getIconTint(), darkColor)) {
                score5.setIconTint(ColorStateList.valueOf(lightColor));
                score4.setIconTint(ColorStateList.valueOf(lightColor));
                score3.setIconTint(ColorStateList.valueOf(lightColor));
                score2.setIconTint(ColorStateList.valueOf(lightColor));
                score1.setIconTint(ColorStateList.valueOf(lightColor));
            } else {
                score5.setIconTint(ColorStateList.valueOf(darkColor));
                score4.setIconTint(ColorStateList.valueOf(darkColor));
                score3.setIconTint(ColorStateList.valueOf(darkColor));
                score2.setIconTint(ColorStateList.valueOf(darkColor));
                score1.setIconTint(ColorStateList.valueOf(darkColor));
            }
            scoreNumber = 5;
        });
        score4.setOnClickListener(v -> {
            if (isDarkColor(score4.getIconTint(), darkColor)) {
                score4.setIconTint(ColorStateList.valueOf(lightColor));
                score3.setIconTint(ColorStateList.valueOf(lightColor));
                score2.setIconTint(ColorStateList.valueOf(lightColor));
                score1.setIconTint(ColorStateList.valueOf(lightColor));
            } else {
                score4.setIconTint(ColorStateList.valueOf(darkColor));
                score5.setIconTint(ColorStateList.valueOf(darkColor));

                score3.setIconTint(ColorStateList.valueOf(darkColor));
                score2.setIconTint(ColorStateList.valueOf(darkColor));
                score1.setIconTint(ColorStateList.valueOf(darkColor));
            }
            scoreNumber=4;

        });
        score3.setOnClickListener(v -> {

            if (isDarkColor(score3.getIconTint(), darkColor)) {
                score3.setIconTint(ColorStateList.valueOf(lightColor));
                score2.setIconTint(ColorStateList.valueOf(lightColor));
                score1.setIconTint(ColorStateList.valueOf(lightColor));
            } else {
                score3.setIconTint(ColorStateList.valueOf(darkColor));
                score4.setIconTint(ColorStateList.valueOf(darkColor));
                score5.setIconTint(ColorStateList.valueOf(darkColor));

                score2.setIconTint(ColorStateList.valueOf(darkColor));
                score1.setIconTint(ColorStateList.valueOf(darkColor));
            }  scoreNumber=3;

        });
        score2.setOnClickListener(v -> {
           scoreNumber=2;
           if (isDarkColor(score2.getIconTint(), darkColor)) {
                score2.setIconTint(ColorStateList.valueOf(lightColor));
                score1.setIconTint(ColorStateList.valueOf(lightColor));
            } else {
                score5.setIconTint(ColorStateList.valueOf(darkColor));
                score4.setIconTint(ColorStateList.valueOf(darkColor));
                score3.setIconTint(ColorStateList.valueOf(darkColor));
                score2.setIconTint(ColorStateList.valueOf(darkColor));

               score1.setIconTint(ColorStateList.valueOf(darkColor));
            }
        });
        score1.setOnClickListener(v -> {

            if (isDarkColor(score1.getIconTint(), darkColor)) {
                score1.setIconTint(ColorStateList.valueOf(lightColor));
            } else {
                score1.setIconTint(ColorStateList.valueOf(darkColor));
                if(scoreNumber>1){
                score2.setIconTint(ColorStateList.valueOf(darkColor));
                score3.setIconTint(ColorStateList.valueOf(darkColor));
                score4.setIconTint(ColorStateList.valueOf(darkColor));
                score5.setIconTint(ColorStateList.valueOf(darkColor));}
            }
            scoreNumber=1;
        });
        send.setOnClickListener(v -> {
            assignValues();
            btnPostRequest();
        });

    }

    private void assignValues() {
        comment = commentEt.getText().toString();

        score = new Score(protocolId,comment, scoreNumber );
    }

    private void btnPostRequest() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<String> call = apiInterface.saveScore(score);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code()==302) {

                    Intent intent = new Intent(ScoreActivity.this, FinalPageActivity.class);
                    startActivity(intent);
                } else {
                    // Handle error response
                    String error = "Error: " + response.code();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
            }
        });
    }

    private boolean isDarkColor(ColorStateList colorStateList, int darkColor) {
        if (colorStateList != null && colorStateList.getDefaultColor() == darkColor) {
            return true;
        }
        return false;
    }
}
