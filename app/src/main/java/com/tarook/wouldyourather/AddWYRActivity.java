package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

public class AddWYRActivity extends AppCompatActivity {

    private EditText question, option1, option2;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wyractivity);

        setupViews();

        submit.setOnClickListener(v -> {
            WouldYouRather wyr = new WouldYouRather(question.getText().toString());
            wyr.addOption(option1.getText().toString());
            wyr.addOption(option2.getText().toString());
            SQLiteManager.getInstance(this).addWYR(wyr);
            finish();
        });
    }

    private void setupViews(){
        question = findViewById(R.id.question_editText_form);
        option1 = findViewById(R.id.option1_edittext_form);
        option2 = findViewById(R.id.option2_edittext_form);
        submit = findViewById(R.id.submit_button_form);
    }
}