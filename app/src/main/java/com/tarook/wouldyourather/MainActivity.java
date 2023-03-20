package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.tarook.wouldyourather.model.WYRAdapter;
import com.tarook.wouldyourather.model.WouldYouRather;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setupViews();
        setupAdapter();
        fillListTemp();
    }

    private void setupAdapter() {
        WYRAdapter adapter = new WYRAdapter(this, WouldYouRather.WYRLIST);
        listView.setAdapter(adapter);
    }

    private void setupViews() {
        listView = findViewById(R.id.wyr_list);
    }

    private void fillListTemp(){
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Cat");
        answers.add("Dog");
        WouldYouRather.WYRLIST.add(new WouldYouRather(0, "Would you rather be a cat or a dog?", answers));

        answers = new ArrayList<>();
        answers.add("Fly");
        answers.add("Invisible");
        WouldYouRather.WYRLIST.add(new WouldYouRather(1, "Would you rather be able to fly or be invisible?", answers));

        answers = new ArrayList<>();
        answers.add("Be a billionaire");
        answers.add("Be a genius");
        WouldYouRather.WYRLIST.add(new WouldYouRather(2, "Would you rather be a billionaire or a genius?", answers));

        answers = new ArrayList<>();
        answers.add("Be a smart fella");
        answers.add("Be a fart smella");
        WouldYouRather.WYRLIST.add(new WouldYouRather(3, "Would you rather be a smart fella or a fart smella?", answers));
    }
}