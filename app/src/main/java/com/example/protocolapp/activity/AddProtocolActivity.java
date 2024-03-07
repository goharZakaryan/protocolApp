package com.example.protocolapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProtocolActivity extends AppCompatActivity {
    private MaterialButton save, back;
    private LinearLayout editTextContainer;
    private Button addStep;
    private int stepCounter = 2;
    private int marginTop = 2600;
    private Protocol protocol;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button selectImageButton;
    private ImageView imageView;

    private EditText nameET, taskListET, taskListAuthorET;
    private String name, taskList, taskListAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);
        findViewById();
        setButtonClickListener();


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        nameET = findViewById(R.id.nameET);
        taskListET = findViewById(R.id.taskListET);
        taskListAuthorET = findViewById(R.id.taskListAuthorET);
        save = findViewById(R.id.save);
        imageView = findViewById(R.id.imageView);


        editTextContainer = findViewById(R.id.editTextContainer);
        addStep = findViewById(R.id.addStep);
        selectImageButton = findViewById(R.id.uploadImageButton);
    }

    private void setButtonClickListener() {
        selectImageButton.setOnClickListener(view -> openFileChooser());

        back.setOnClickListener(v -> {
            Intent intent = new Intent(AddProtocolActivity.this, MainActivity.class);
            startActivity(intent);
        });
        save.setOnClickListener(v -> {
            assignValues();
            btnPostRequest();
            Intent intent = new Intent(AddProtocolActivity.this, SuccessPageActivity.class);

            startActivity(intent);
        });
        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputs();
            }
        });
    }

    private void assignValues() {
        name = nameET.getText().toString();
        taskList = taskListET.getText().toString();
        taskListAuthor = taskListAuthorET.getText().toString();
        protocol = new Protocol("1", name, taskList, taskListAuthor, new ArrayList<>());
    }

    private void btnPostRequest() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<String> call = apiInterface.save(protocol);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    String result = response.body();
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE); // Show the ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void createInputs() {
        // Create LinearLayout as a container for each set of views
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(80, marginTop, 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        editTextContainer.addView(linearLayout);

        // Create TextView for step description
        TextView stepTextView = new TextView(this);
        stepTextView.setText("Шаг " + stepCounter);
        stepTextView.setBackgroundColor(Color.WHITE);
        stepTextView.setHeight(200);

        stepTextView.setTextSize(20);
        stepTextView.setId(stepCounter);

        // Set drawable icon for deletion
        stepTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_do_disturb_on_24, 0);
        stepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepCounter==3){
                    marginTop=2600;
                    stepCounter--;
                }
                editTextContainer.removeView(linearLayout);
            }
        });


        linearLayout.addView(stepTextView);
        TextView descriptionStep = new TextView(this);
        descriptionStep.setText("Описание шага");
        descriptionStep.setTextSize(20);
        descriptionStep.setId(stepCounter);

        linearLayout.addView(descriptionStep);

        // Create EditText for step description
        EditText stepEditText = new EditText(this);
        stepEditText.setHint("Описание");
        stepEditText.setWidth(900);
        stepEditText.setHeight(200);
        stepEditText.setBackgroundColor(Color.WHITE);

        linearLayout.addView(stepEditText);

        // Create TextView for additional instruction
        TextView instructionTextView = new TextView(this);
        instructionTextView.setText("Инструкция");
        instructionTextView.setTextSize(20);
        linearLayout.addView(instructionTextView);

        // Create EditText for additional instruction
        EditText instructionEditText = new EditText(this);
        instructionEditText.setHint("Текст");
        instructionEditText.setWidth(900);
        instructionEditText.setHeight(200);
        instructionEditText.setBackgroundColor(Color.WHITE);
        linearLayout.addView(instructionEditText);

        // Create TextView for image upload
        TextView imageTextView = new TextView(this);
        imageTextView.setText("Загрузить изображение");
        imageTextView.setTextSize(16);
        linearLayout.addView(imageTextView);

        // Create Button for image selection
        Button uploadImageButton = new Button(this);
        uploadImageButton.setText("Выбрать изображение");
        linearLayout.addView(uploadImageButton);

        // Create ImageView to display uploaded image
        ImageView imageView = new ImageView(this);
        linearLayout.addView(imageView);

        // Increment step counter
        stepCounter++;

        // Increase marginTop for the next set of views
        marginTop = 20; // Adjust this value as needed
    }
}
