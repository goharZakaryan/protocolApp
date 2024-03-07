package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.example.protocolapp.db.DbHelper;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton  login, back;
    private EditText emailEt, passwordEt;
    private String email, password;
    private DbHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        setButtonClickListener();
        dbHelper=new DbHelper(this);
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
            Intent intent = new Intent(LoginActivity.this, UserPageActivity.class);
            login();
            startActivity(intent);
        });
    }
    private String  login() {
        email = emailEt.getText().toString();
        password = passwordEt.getText().toString();

        if (!email.isEmpty() || !password.isEmpty()) {

            String byEmail = dbHelper.findByEmail(email);

            Toast.makeText(getApplicationContext(), "Login Successfully...."+byEmail, Toast.LENGTH_SHORT).show();
        return byEmail;
        }
        Toast.makeText(getApplicationContext(), "Login doesnt Successfully...."+email, Toast.LENGTH_SHORT).show();

        return null;
    }
}
