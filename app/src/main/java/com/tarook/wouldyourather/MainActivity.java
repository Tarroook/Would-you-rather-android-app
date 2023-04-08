package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.tarook.wouldyourather.model.WYRAdapter;
import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setupViews();
        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        ArrayList<WouldYouRather> wyrList = sqLiteManager.getAllWYRS();
        debugDB(wyrList);
        WouldYouRather.WYRLIST = wyrList;
        setupAdapter();
        profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent;
            if(getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1) == -1)
                intent = new Intent(MainActivity.this, ConnectionActivity.class);
            else
                intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // check if user is connected, if not redirect to connection activity
            // then check if user has already voted for this wyr, if not redirect to wyr activity
            // else redirect to results activity
            Intent intent;
            if(getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1) == -1)
                intent = new Intent(MainActivity.this, ConnectionActivity.class);
            else {
                if(sqLiteManager.hasVotedFor(getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1), WouldYouRather.WYRLIST.get(position).getId()))
                    intent = new Intent(MainActivity.this, ResultActivity.class);
                else
                    intent = new Intent(MainActivity.this, WYRActivity.class);

                intent.putExtra("wyrId", WouldYouRather.WYRLIST.get(position).getId());
            }
            startActivity(intent);
        });

        //fillListTemp();
        //this.deleteDatabase("wyrDB");
    }

    private void debugDB(ArrayList<WouldYouRather> wyrList) {
        Log.d("WYR", "onCreate: " + wyrList.size() + " WYRs loaded from DB" + wyrList.toString());
        for(WouldYouRather wyr : wyrList){
            Log.d("WYR", "onCreate: " + wyr.toString());
            Log.d("WYR", "onCreate: " + wyr.getOptions().toString());
        }
    }

    private void setupAdapter() {
        WYRAdapter adapter = new WYRAdapter(this, WouldYouRather.WYRLIST);
        listView.setAdapter(adapter);
    }

    private void setupViews() {
        listView = findViewById(R.id.wyr_list);
    }

    private void fillListTemp(){
        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);

        WouldYouRather wyr = new WouldYouRather("Would you rather be a cat or a dog?");
        wyr.addOption("Cat");
        wyr.addOption("Dog");
        sqLiteManager.addWYR(wyr);

        WouldYouRather wyr2 = new WouldYouRather("Would you rather be able to fly or be invisible?");
        wyr2.addOption("Fly");
        wyr2.addOption("Invisible");
        sqLiteManager.addWYR(wyr2);

        WouldYouRather wyr3 = new WouldYouRather("Would you rather be a billionaire or be a genius?");
        wyr3.addOption("Billionaire");
        wyr3.addOption("Genius");
        sqLiteManager.addWYR(wyr3);

        WouldYouRather wyr4 = new WouldYouRather("Would you rather be a smart fella or be a fart smella?");
        wyr4.addOption("Smart fella");
        wyr4.addOption("Fart smella");
        sqLiteManager.addWYR(wyr4);
    }
}