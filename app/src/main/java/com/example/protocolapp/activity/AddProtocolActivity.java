package com.example.protocolapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.Step;
import com.example.protocolapp.model.User;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProtocolActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    private Map<Integer, EditText> stepEditTextList = new HashMap<>();
    private Map<Integer, LinearLayout> llForUpload = new HashMap<>();
    private Map<Integer, EditText> instructionList = new HashMap<>();
    private Map<Integer, List<String>> filePathList = new HashMap<>();
    private List<Step> stepList = new ArrayList<>();
    private List<String> fPathList = new ArrayList<>();

    private MaterialButton save, back;
    private LinearLayout editTextContainer, upload, linearLayout;
    private Button addStep;
    private int stepCounter = 2;
    private int marginTop = 2600;
    private Protocol protocol;
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean uploadFile;
    private Button selectImageButton;
    private ImageView imageView;
    private VideoView videoView;
    private View fileView;
    private Long protocolId;
    private int filepathCount;

    private EditText nameET, taskListET, taskListAuthorET, instructionEditText, stepEditText, description1ET, newEditText;
    private String name, taskList, taskListAuthor, description1, newText, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_protocol);
        findViewById();
        setButtonClickListener();
        email = getIntent().getStringExtra("email");
        protocolId = getIntent().getLongExtra("id", 0);


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
        upload = findViewById(R.id.upload);


        editTextContainer = findViewById(R.id.editTextContainer);
        addStep = findViewById(R.id.addStep);
        selectImageButton = findViewById(R.id.uploadImageButton);
    }

    private void setButtonClickListener() {
        selectImageButton.setOnClickListener(view -> {
            linearLayout = new LinearLayout(this);

            filepathCount = 0;

            llForUpload.put(filepathCount,linearLayout);
            upload.addView(linearLayout);
            openFileChooser();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(AddProtocolActivity.this, MainActivity.class);
            startActivity(intent);
        });
        save.setOnClickListener(v -> {
            collectValuesFromEditTexts();
            assignValues();
            btnPostRequest();

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
        protocol = new Protocol(protocolId, name, taskList, taskListAuthor, stepList, new User(email));
    }

    private void btnPostRequest() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        for (int i = 0; i < fPathList.size(); i++) {
            Uri fileUri = Uri.parse(fPathList.get(i));

            File file = new File(fileUri.toString());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file" + i, file.getName(), requestFile);
            fileParts.add(filePart);
        }
        Call<String> call = apiInterface.save(protocol, fileParts);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Intent intent = new Intent(AddProtocolActivity.this, SuccessPageActivity.class);
                    stepList = new ArrayList<>();
                    startActivity(intent);
                } else {
                    Toast.makeText(AddProtocolActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage(), t);
                Toast.makeText(AddProtocolActivity.this, "Login ", Toast.LENGTH_SHORT).show();
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
                        ImageView imageView1 = new ImageView(this);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        imageView1.setLayoutParams(new ViewGroup.LayoutParams(200, 200));

                        imageView1.setImageBitmap(bitmap);


                        List<String> strings;
                        if (filePathList.get(filepathCount) != null) {
                            strings = filePathList.get(filepathCount);
                            strings.add(fileUri.toString());
                        } else {
                            strings = new ArrayList<>();
                            strings.add(fileUri.toString());
                        }
                        llForUpload.get(filepathCount).addView(imageView1);
                        filePathList.put(filepathCount, strings);

                    } else if (fileType.startsWith("video/")) {
                        MediaController  mediaController = new MediaController(this);
                        ;
                        VideoView videoView = new VideoView(this);
                        videoView.setVideoURI(fileUri);
                        videoView.pause();
                        videoView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));

                        List<String> strings;
                        if (filePathList.get(filepathCount) != null) {
                            strings = filePathList.get(filepathCount);
                            strings.add(fileUri.toString());
                        } else {
                            strings = new ArrayList<>();
                            strings.add(fileUri.toString());
                        }
                        llForUpload.get(filepathCount).addView(videoView);
                        filePathList.put(filepathCount, strings);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setMediaController(mediaController);
                        mediaController.setMinimumHeight(1);
                    } else if (fileType.startsWith("text/")) {
                        // Handle text file
//                        fileView.setText(text);
                        fileView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                    }
                  getRealPathFromURI(fileUri);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) throws IOException {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        fPathList.add(path);

        return path;
    }
    private void createInputs() {
        // Create LinearLayout as a container for each set of views
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        layoutParams.setMargins(50, marginTop, 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(stepCounter);
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
        uploadImageButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_attach_file_24, 0, 0, 0);
        uploadImageButton.setOnClickListener(view -> {
            filepathCount = stepCounter;
           LinearLayout linearLayoutU = new LinearLayout(this);
            llForUpload.put(filepathCount,linearLayout);
            linearLayout.addView(linearLayoutU);
            uploadFile = true;
            openFileChooser();

        });
        linearLayout.addView(uploadImageButton);
        stepCounter++;
        marginTop = 20;
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

}
