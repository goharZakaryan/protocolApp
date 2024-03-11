package com.example.protocolapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.Step;
import com.example.protocolapp.model.User;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProtocolActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Map<Integer, EditText> stepEditTextList = new HashMap<>();
    private Map<Integer, EditText> instructionList = new HashMap<>();
    private List<Step> stepList = new ArrayList<>();

    private MaterialButton save, back;
    private LinearLayout editTextContainer;
    private Button addStep;
    private int stepCounter = 2;
    private int marginTop = 2600;
    private Protocol protocol;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button selectImageButton;
    private ImageView imageView;
    private VideoView videoView;
    private View fileView;

    private EditText nameET, taskListET, taskListAuthorET, instructionEditText, stepEditText, description1ET, newEditText;
    private String name, taskList, taskListAuthor, description1, newText,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);
        findViewById();
        setButtonClickListener();
        email=getIntent().getStringExtra("email");

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                Log.e("VideoView", "Error occurred: " + what + ", " + extra);
                return false; // Return true if you want to handle the error, false otherwise
            }
        });


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        nameET = findViewById(R.id.nameET);
        taskListET = findViewById(R.id.taskListET);
        taskListAuthorET = findViewById(R.id.taskListAuthorET);
        save = findViewById(R.id.save);
        description1ET = findViewById(R.id.description1ET);
        newEditText = findViewById(R.id.newEditText);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        fileView = findViewById(R.id.fileView);


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
            collectValuesFromEditTexts();
            assignValues();
            btnPostRequest();
            Intent intent = new Intent(AddProtocolActivity.this, SuccessPageActivity.class);
            stepList = new ArrayList<>();
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
        description1 = description1ET.getText().toString();
        newText = newEditText.getText().toString();
        Step step1 = new Step("1", description1, newText);
        stepList.add(step1);
        protocol = new Protocol( name, taskList, taskListAuthor, stepList,new User(email));
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
        intent.setType("*/*"); // Allow all types of files
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            try {
                String fileType = getContentResolver().getType(fileUri);
                if (fileType != null) {
                    if (fileType.startsWith("image/")) {
                        // Handle image file
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE); // Show the ImageView
                    } else if (fileType.startsWith("video/")) {
                        videoView.setVideoURI(fileUri);
                        videoView.setVisibility(View.VISIBLE);

                        MediaController mediaController = new MediaController(this);
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.start();
                        imageView.setVisibility(View.GONE);
                        fileView.setVisibility(View.GONE);
                    } else if (fileType.startsWith("text/")) {
                        // Handle text file
                        String text = readTextFile(fileUri);
//                        fileView.setText(text);
                        fileView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readTextFile(Uri fileUri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(fileUri)));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        reader.close();
        return stringBuilder.toString();
    }


    private void createInputs() {
        // Create LinearLayout as a container for each set of views
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(50, marginTop, 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        editTextContainer.setId(stepCounter);
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
                if (stepCounter == 3) {
                    marginTop = 2600;
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
        stepEditText = new EditText(this);
        stepEditText.setHint("Описание");
        stepEditText.setWidth(900);
        stepEditText.setHeight(200);
        stepEditText.setId(stepCounter);
        stepEditText.setBackgroundColor(Color.WHITE);

        linearLayout.addView(stepEditText);

        // Create TextView for additional instruction
        TextView instructionTextView = new TextView(this);
        instructionTextView.setText("Инструкция");
        instructionTextView.setTextSize(20);
        linearLayout.addView(instructionTextView);

        // Create EditText for additional instruction
        instructionEditText = new EditText(this);
        instructionEditText.setHint("Текст");
        instructionEditText.setWidth(900);
        instructionEditText.setHeight(200);
        instructionEditText.setBackgroundColor(Color.WHITE);
        instructionList.put(stepCounter, instructionEditText);
        stepEditTextList.put(stepCounter, stepEditText);

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

    private void collectValuesFromEditTexts() {
        for (Map.Entry<Integer, EditText> editText : stepEditTextList.entrySet()) {
            String instruction = instructionList.get(editText.getKey()).getText().toString();
            String text = editText.getValue().getText().toString();
            Step step1 = new Step(editText.getKey().toString(), instruction, text);
            stepList.add(step1);
            System.out.println(text);
            // You can store these values in an appropriate data structure or perform any other operations as needed
        }
    }
    private void permission(){

// Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission has already been granted
            // Perform the operation that requires this permission
        }

    }
}
