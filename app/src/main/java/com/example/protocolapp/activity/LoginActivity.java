package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.example.protocolapp.db.DbHelper;
import com.example.protocolapp.model.User;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton login, back;
    private EditText emailEt, passwordEt;
    private String email, password;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        setButtonClickListener();
        dbHelper = new DbHelper(this);
//        dbHelper.insertContact("user@gmail.com","user");


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        passwordEt = findViewById(R.id.passwordEt);
        emailEt = findViewById(R.id.emailEt);
        login = findViewById(R.id.login);

    }

    private void setButtonClickListener() {

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(v -> {
            login();

        });
    }

    private void login() {
        email = emailEt.getText().toString();
        password = passwordEt.getText().toString();
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiInterface.login2(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==302) {
                    Intent intent = new Intent(LoginActivity.this, UserPageActivity.class);
                    intent.putExtra("email", email);

                    startActivity(intent);

                } else {
                    // Login failed, handle error
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Request failed, handle error
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
