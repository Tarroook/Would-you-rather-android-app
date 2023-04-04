package com.tarook.wouldyourather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.util.SQLiteManager;

public class ProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PROFILE = 1;
    public final static String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;
    private TextView name;
    private TextView description;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);


        name = findViewById(R.id.profile_activity_name);
        description = findViewById(R.id.profile_activity_description);
        editButton = findViewById(R.id.profile_activity_edit_button);

        Profile profile = getProfile();
        if(profile == null){
            Intent intent = new Intent(ProfileActivity.this, ConnectionActivity.class);
            startActivity(intent);
        }
        else {
            name.setText(profile.getName());
            description.setText(profile.getDescription());

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            //startActivity(intent);
                            startActivityForResult(intent, EditProfileActivity.REQUEST_CODE_EDIT_PROFILE);
                        }
                    }, 500);
                }
            });
        }
    }

    private Profile getProfile(){
        return SQLiteManager.getInstance(this).getUserById(sharedPreferences.getInt(ConnectionActivity.CONNECTED_PROFILE, -1));
    }

    private void refreshProfile(){
        Profile profile = getProfile();
        if(profile != null) {
            name.setText(profile.getName());
            description.setText(profile.getDescription());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshProfile();
    }
}