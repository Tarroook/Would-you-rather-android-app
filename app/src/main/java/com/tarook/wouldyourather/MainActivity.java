package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tarook.wouldyourather.model.WYRAdapter;
import com.tarook.wouldyourather.model.WouldYouRather;
import com.tarook.wouldyourather.util.SQLiteManager;

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
        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        //this.deleteDatabase("wyrDB");
        ArrayList<WouldYouRather> wyrList = sqLiteManager.getAllWYRS();
        Log.d("WYR", "onCreate: " + wyrList.size() + " WYRs loaded from DB" + wyrList.toString());
        for(WouldYouRather wyr : wyrList){
            Log.d("WYR", "onCreate: " + wyr.toString());
            Log.d("WYR", "onCreate: " + wyr.getOptions().toString());
        }
        WouldYouRather.WYRLIST = wyrList;
        setupAdapter();
        //fillListTemp();
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
        /*
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

         */
    }
}