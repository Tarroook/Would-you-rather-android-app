package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tarook.wouldyourather.model.Vote;
import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

public class ResultActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView stat1;
    private TextView stat2;
    private TextView description;
    private TextView userChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setupViews();

        WouldYouRather wyr = SQLiteManager.getInstance(this).getWYR(getIntent().getIntExtra("wyrId", -1));
        if (wyr != null) {
            // get user choice by getting the option that has the same id as the user's vote and the same wyr id from db
            Vote userChoiceVote = SQLiteManager.getInstance(this).getVote(wyr.getId(), getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1));
            if (userChoiceVote != null) {
                userChoice.setText("You chose : " + wyr.getOptions().get(userChoiceVote.getOptionNumber()));
            }
            else {
                userChoice.setText("You didn't vote for this WYR");
            }

            int total = SQLiteManager.getInstance(this).getNumberOfVotesForWYR(wyr.getId());
            int option1 = SQLiteManager.getInstance(this).getNumberOfVotesForOption(wyr.getId(), 0);
            int option2 = SQLiteManager.getInstance(this).getNumberOfVotesForOption(wyr.getId(), 1);
            // max for progress bar is 100 so we need to divide by 100
            progressBar.setProgress((int) ((float) option1 / total * 100));

            stat1.setText(option1 + " votes for " + wyr.getOptions().get(0));
            stat2.setText(option2 + " votes for " + wyr.getOptions().get(1));

            StringBuilder descriptionText = new StringBuilder();
            descriptionText.append("Most people would rather ");
            if (option1 > option2) {
                descriptionText.append(wyr.getOptions().get(0));
            }else if (option2 > option1) {
                descriptionText.append(wyr.getOptions().get(1));
            }
            else {
                descriptionText.append("neither");
            }
            descriptionText.append(".");
            description.setText(descriptionText.toString());
        }
        else {
            description.setText("Error: WYR not found");
        }
    }

    private void setupViews(){
        progressBar = findViewById(R.id.stat_bar);
        stat1 = findViewById(R.id.stat1_textview_result);
        stat2 = findViewById(R.id.stat2_textview_result);
        description = findViewById(R.id.description_textview_results);
        userChoice = findViewById(R.id.userchoice_textview_result);
    }
}