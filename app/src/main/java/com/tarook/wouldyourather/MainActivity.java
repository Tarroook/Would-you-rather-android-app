package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.tarook.wouldyourather.model.TTSManager;
import com.tarook.wouldyourather.model.WYRAdapter;
import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button profileButton, addButton;
    private SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setupViews();
        sqLiteManager = SQLiteManager.getInstance(this);


        profileButton.setOnClickListener(v -> {
            Intent intent;
            if (getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1) == -1)
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
            if (getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1) == -1)
                intent = new Intent(MainActivity.this, ConnectionActivity.class);
            else {
                if (sqLiteManager.hasVotedFor(WouldYouRather.WYRLIST.get(position).getId(), getSharedPreferences(ProfileActivity.SHARED_PREFS, MODE_PRIVATE).getInt("connectedProfile", -1)))
                    intent = new Intent(MainActivity.this, ResultActivity.class);
                else
                    intent = new Intent(MainActivity.this, WYRActivity.class);

                intent.putExtra("wyrId", WouldYouRather.WYRLIST.get(position).getId());
            }
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddWYRActivity.class);
            startActivity(intent);
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("wyrChannel", "Would You Rather", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //fillListTemp();
        //this.deleteDatabase("wyrDB");
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendNotification();
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "wyrChannel")
                .setSmallIcon(R.drawable.pissbaby)
                .setContentTitle("Welcome to Would You Rather")
                .setContentText("Start by creating your account :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if(checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }
        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        WouldYouRather.WYRLIST = sqLiteManager.getAllWYRS();
        setupAdapter();
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
        profileButton = findViewById(R.id.profile_button);
        addButton = findViewById(R.id.add_button);
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