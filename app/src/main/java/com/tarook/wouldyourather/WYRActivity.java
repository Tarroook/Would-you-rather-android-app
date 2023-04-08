package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.tarook.wouldyourather.model.Vote;
import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

public class WYRActivity extends AppCompatActivity {

    private AppCompatButton option1;
    private AppCompatButton option2;
    private TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyractivity);

        setupViews();

        WouldYouRather wyr = SQLiteManager.getInstance(this).getWYR(getIntent().getIntExtra("wyrId", -1));
        if (wyr != null) {
            question.setText(wyr.getQuestion());
            option1.setText(wyr.getOptions().get(0));
            option2.setText(wyr.getOptions().get(1));
            option1.setOnClickListener(v -> {
                SQLiteManager.getInstance(this).addVote(new Vote(wyr.getId(), 0, getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1)));
                finish();
            });

            option2.setOnClickListener(v -> {
                SQLiteManager.getInstance(this).addVote(new Vote(wyr.getId(), 1, getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1)));
                finish();
            });
        }else {
            Toast.makeText(this, "Error: WYR not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupViews(){
        option1 = findViewById(R.id.left_button_game);
        option2 = findViewById(R.id.right_button_game);
        question = findViewById(R.id.title_textview_game);
    }
}