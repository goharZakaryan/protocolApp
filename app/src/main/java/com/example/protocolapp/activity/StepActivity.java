package com.example.protocolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {


    private Button performance;
    private Button back;
    private Long protocolId;
    private TextView number, name, description;
    private ArrayList<Step> stepList;
    private int index;
    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_page);
        findViewById();
        stepList = getIntent().getParcelableArrayListExtra("stepList");
        index = getIntent().getIntExtra("index", 0);
        step = getIntent().getParcelableExtra("step");
        protocolId = getIntent().getLongExtra("protocolId", 0);

        assignValues(step);
        setButtonClickListener();


    }

    private void findViewById() {
        back = findViewById(R.id.back);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        performance = findViewById(R.id.performance);
    }

    private void setButtonClickListener() {

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StepActivity.this, MainActivity.class);
            startActivity(intent);
        });
        performance.setOnClickListener(v -> {
            Integer number=Integer.parseInt(step.getNumber())+1;

            if (stepList.size() > index) {
                stepList.remove(step);

                for (Step step : stepList) {
                    if (String.valueOf(number).equals(step.getNumber())) {
                        Intent intent = new Intent(StepActivity.this, StepActivity.class);
                        intent.putParcelableArrayListExtra("stepList", stepList); // Use finalSteps here
                        intent.putExtra("index", ++index); // Use finalSteps here
                        intent.putExtra("protocolId", protocolId);
                        intent.putExtra("step", step); // Use finalSteps here

                        this.startActivity(intent);
                        startActivity(intent);
                    }
                }

            } else {
                Intent intent = new Intent(StepActivity.this, DonePageActivity.class);
                intent.putExtra("protocolId", protocolId); // Use finalSteps here
                this.startActivity(intent);
                startActivity(intent);
            }

        });

    }

    private void assignValues(Step step) {
        number.setText("шаг" + step.getNumber());
        name.setText(step.getName());
        description.setText(step.getDescription());

    }

}
