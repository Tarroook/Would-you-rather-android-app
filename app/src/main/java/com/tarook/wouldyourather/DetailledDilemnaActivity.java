package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DetailledDilemnaActivity extends AppCompatActivity {

    private TextView question;
    private Button option1, option2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailled_dilemna);

        question = findViewById(R.id.textView3);
        option1 = findViewById(R.id.button);
        option2 = findViewById(R.id.button2);

        Intent intent = getIntent();
        String getQuestion = intent.getStringExtra("question");
        String getOption1 = intent.getStringExtra("option1");
        String getOption2 = intent.getStringExtra("option2");

        question.setText(getQuestion);
        option1.setText(getOption1);
        option2.setText(getOption2);

    }
}