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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.protocolapp.R;
import com.example.protocolapp.adapter.AdapterProtocol;
import com.example.protocolapp.model.Device;
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
    private Spinner spinner;
   private List<String> optionsArray=new ArrayList<>();

    private Map<Integer, EditText> stepEditTextList = new HashMap<>();
    private Map<Integer, LinearLayout> llForUpload = new HashMap<>();
    private Map<Integer, LinearLayout> ll = new HashMap<>();
    private Map<Integer, EditText> instructionList = new HashMap<>();
    private Map<Integer, List<String>> filePathList = new HashMap<>();
    private List<Step> stepList = new ArrayList<>();
    private List<Uri> fPathList = new ArrayList<>();

    private MaterialButton save, back;
    private LinearLayout editTextContainer, upload, linearLayout, second;
    private Button addStep;
    private int stepCounter = 2;
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
        requestToServer();

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
        second = findViewById(R.id.second);
        spinner = findViewById(R.id.spinner);


        editTextContainer = findViewById(R.id.main);
        addStep = findViewById(R.id.addStep);
        selectImageButton = findViewById(R.id.uploadImageButton);
    }

    private void setButtonClickListener() {
        selectImageButton.setOnClickListener(view -> {
            linearLayout = new LinearLayout(this);

            filepathCount = 0;

            llForUpload.put(filepathCount, linearLayout);
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
        Step step1 = new Step("1", description1, newText, filePathList.get(0));
        stepList.add(step1);
        protocol = new Protocol(protocolId, name, taskList, taskListAuthor, stepList, new User(email));
    }

    private void btnPostRequest() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        for (int i = 0; i < fPathList.size(); i++) {
            File file = new File(Uri.parse(fPathList.get(i).toString()).toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("fileParts", file.getName(), requestFile);
            fileParts.add(filePart);

        }


        Call<Void> call = apiInterface.save(protocol);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 302) {
                    // Handle successful response
                    Intent intent = new Intent(AddProtocolActivity.this, SuccessPageActivity.class);
                    stepList = new ArrayList<>();
                    startActivity(intent);
                } else {
                    Toast.makeText(AddProtocolActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error: " + t.getMessage(), t);
                Toast.makeText(AddProtocolActivity.this, "Login ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select file to upload "), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            fPathList.add(Uri.parse(getPath(fileUri)));
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
                        // Your existing code for handling videos
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
                        // Additional configurations or operations related to the videoView
                    } else if (fileType.startsWith("text/")) {
                        // Handle text file
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

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    private void createInputs() {
        // Create LinearLayout as a container for each set of views
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(stepCounter);
        editTextContainer.setId(stepCounter);

        // Create TextView for step description
        TextView stepTextView = new TextView(this);
        stepTextView.setText("Шаг " + stepCounter);
        stepTextView.setHeight(100);
        stepTextView.setBackgroundColor(Color.LTGRAY);

        stepTextView.setTextSize(15);
        stepTextView.setId(stepCounter);

        // Set drawable icon for deletion
        stepTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_do_disturb_on_24, 0);
        stepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepCounter >= 3) {
                    stepCounter--;
                }
                editTextContainer.removeView(ll.get(stepCounter));
            }
        });


        linearLayout.addView(stepTextView);
        TextView descriptionStep = new TextView(this);
        descriptionStep.setText("Protocol name");
        descriptionStep.setTextSize(15);
        descriptionStep.setId(stepCounter);

        linearLayout.addView(descriptionStep);

        // Create EditText for step description
        stepEditText = new EditText(this);
        stepEditText.setHint("Name");
        stepEditText.setBackgroundColor(Color.LTGRAY);
        stepEditText.setWidth(900);
        stepEditText.setHeight(100);
        stepEditText.setId(stepCounter);

        linearLayout.addView(stepEditText);

        // Create TextView for additional instruction
        TextView instructionTextView = new TextView(this);
        instructionTextView.setText("Instruction");
        instructionTextView.setTextSize(15);
        linearLayout.addView(instructionTextView);

        // Create EditText for additional instruction
        instructionEditText = new EditText(this);
        instructionEditText.setHint("text");
        instructionEditText.setBackgroundColor(Color.LTGRAY);
        instructionEditText.setWidth(900);
        instructionEditText.setHeight(200);
        instructionList.put(stepCounter, instructionEditText);
        stepEditTextList.put(stepCounter, stepEditText);

        linearLayout.addView(instructionEditText);

        // Create TextView for image upload
        TextView imageTextView = new TextView(this);
        imageTextView.setText("Upload photos, videos and files");
        imageTextView.setTextSize(16);
        linearLayout.addView(imageTextView);

        // Create Button for image selection
        Button uploadImageButton = new Button(this);
        uploadImageButton.setText("upload");
        uploadImageButton.setId(stepCounter);
        uploadImageButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_attach_file_24, 0, 0, 0);
        linearLayout.addView(uploadImageButton);

        uploadImageButton.setOnClickListener(view -> {
            filepathCount = uploadImageButton.getId();
            LinearLayout linearLayoutU = new LinearLayout(this);
            llForUpload.put(filepathCount, linearLayoutU);
            linearLayout.addView(linearLayoutU);
            uploadFile = true;
            openFileChooser();

        });
        editTextContainer.addView(linearLayout);
        ll.put(stepCounter, linearLayout);
        RelativeLayout.LayoutParams RL = (RelativeLayout.LayoutParams) second.getLayoutParams();
        RL.addRule(RelativeLayout.BELOW, linearLayout.getId());
        second.setLayoutParams(RL);


        stepCounter++;
    }

    private void collectValuesFromEditTexts() {
        for (Map.Entry<Integer, EditText> editText : stepEditTextList.entrySet()) {
            String instruction = instructionList.get(editText.getKey()).getText().toString();
            String text = editText.getValue().getText().toString();
            List<String> uris = filePathList.get(Integer.valueOf(editText.getKey().toString()));
            Step step1 = new Step(editText.getKey().toString(), instruction, text, uris);
            stepList.add(step1);
            System.out.println(text);
            // You can store these values in an appropriate data structure or perform any other operations as needed
        }
    }

    private void spinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }
    private void requestToServer() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Device>> call = apiInterface.findAllDevices();
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful()) {
                    List<Device> devices=response.body();

                    for (int i = 0; i < devices.size(); i++) {
                        optionsArray.add(devices.get(i).getName());
                    }

                } else {
                    // Handle unsuccessful response
                    Toast.makeText(AddProtocolActivity.this, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                // Handle request failure
                t.printStackTrace();
                Toast.makeText(AddProtocolActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Thread.sleep(501);
                    spinner();
        }catch (Exception e) {
        }

    }
}
