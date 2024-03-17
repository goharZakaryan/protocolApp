package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protocolapp.R;
import com.example.protocolapp.adapter.AdapterProtocol;
import com.example.protocolapp.adapter.AdapterSteps;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecuteProtocolActivity extends AppCompatActivity {
    private MaterialButton scannerButton,  back;
    private TextView score, nameProtocol, authorProtocol, pass;
    private String protocolId;
    private AdapterSteps adapterSteps;
    private Protocol protocol;
    private RecyclerView stepRv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_protocol);
        findViewById();
        stepRv.setHasFixedSize(true);

        protocolId = getIntent().getStringExtra("id");
        setButtonClickListener();
        findById(protocolId);
//        loadSteps();

    }

    private void loadSteps(Protocol protocol) {
        adapterSteps = new AdapterSteps(ExecuteProtocolActivity.this,protocol.getSteps(), Long.valueOf(protocolId) );
        stepRv.setLayoutManager(new LinearLayoutManager(ExecuteProtocolActivity.this));
        stepRv.setAdapter(adapterSteps);
    }

    private void findViewById() {
        scannerButton = findViewById(R.id.scannerButton);
        back = findViewById(R.id.back);

        nameProtocol = findViewById(R.id.nameProtocol);
        authorProtocol = findViewById(R.id.authorProtocol);
        pass = findViewById(R.id.pass);
        score = findViewById(R.id.score);
        stepRv = findViewById(R.id.stepRv);

    }

    private void setButtonClickListener() {
        scannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExecuteProtocolActivity.this, MainActivity.class);
            startActivity(intent);
        });
        back.setOnClickListener(v -> {
            Intent intent = new Intent(ExecuteProtocolActivity.this, ModeActivity.class);
            startActivity(intent);
        });
    }

    private void findById(String protocolId) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Protocol> call = apiInterface.findById(protocolId);
        call.enqueue(new Callback<Protocol>() {
            @Override
            public void onResponse(Call<Protocol> call, Response<Protocol> response) {
                if (response.isSuccessful()) {
                    protocol = response.body();
                    assignValue(protocol);
                    loadSteps(protocol);

                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Protocol> call, Throwable t) {
                // Handle request failure
                t.printStackTrace();
            }
        });
    }

    private void assignValue(Protocol protocol) {
        nameProtocol.setText(protocol.getName());
        authorProtocol.setText(protocol.getTaskListAuthor());
        if (protocol.getScore()!=null){
            score.setText(String.valueOf(protocol.getScore().getRating()));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        findById(protocolId);
    }
}