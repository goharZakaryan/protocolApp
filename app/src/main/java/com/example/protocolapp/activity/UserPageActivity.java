package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protocolapp.R;
import com.example.protocolapp.adapter.AdapterProtocol;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.User;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {
    private MaterialButton add, back;
    private RecyclerView contactRv;
    private   List<Protocol> protocols;

    private AdapterProtocol adapterProtocol;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        findViewById();
        contactRv.setHasFixedSize(true);

        email = getIntent().getStringExtra("email");
        requestToServer();
        loadData();
        setButtonClickListener();


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);
        contactRv = findViewById(R.id.contactRv);


    }

    private void setButtonClickListener() {

        back.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, MainActivity.class);
            startActivity(intent);
        });
        add.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, AddProtocolActivity.class);
            intent.putExtra("email", email);

            startActivity(intent);
        });
    }

    private void loadData() {



    }

    private void requestToServer() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Protocol>> call = apiInterface.findProtocolByEmail(email);
        call.enqueue(new Callback<List<Protocol>>() {
            @Override
            public void onResponse(Call<List<Protocol>> call, Response<List<Protocol>> response) {
                if (response.isSuccessful()) {
                    protocols = response.body();
                    if (protocols != null) {
                        adapterProtocol = new AdapterProtocol(UserPageActivity.this, protocols);
                        contactRv.setLayoutManager(new LinearLayoutManager(UserPageActivity.this));
                        contactRv.setAdapter(adapterProtocol);
                    } else {
                        // Handle null response body
                        Toast.makeText(UserPageActivity.this, "Empty response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(UserPageActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Protocol>> call, Throwable t) {
                // Handle request failure
                t.printStackTrace();
                Toast.makeText(UserPageActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        requestToServer();
    }

}
