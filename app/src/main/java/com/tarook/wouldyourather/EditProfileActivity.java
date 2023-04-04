package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.util.SQLiteManager;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT_PROFILE = 2;
    public final static String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;

    private EditText nameChange;
    private EditText descriptionChange;
    private Button confirmButton;
    private TextView nameTakenMessage;
    private String name;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);


        nameChange = findViewById(R.id.edit_profile_activity_name);
        descriptionChange = findViewById(R.id.edit_profile_activity_description);
        nameTakenMessage = findViewById(R.id.edit_profile_activity_username_taken);
        confirmButton = findViewById(R.id.edit_profile_activity_confirm_button);
        confirmButton.setEnabled(false);

        Profile profile = getProfile();
        if(profile == null){
            Toast.makeText(getApplicationContext(), "Error: profile not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            name = profile.getName();
            description = profile.getDescription();

            nameChange.setText(name);
            descriptionChange.setText(description);

            nameChange.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    confirmButton.setEnabled(enableButton());
                }
            });
            descriptionChange.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    confirmButton.setEnabled(enableButton());
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Profile changed.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateProfile();
                        }
                    }, 700);
                }
            });
        }
    }


    private boolean enableButton(){
        return (nameNotAlreadyTaken() && !nameChange.getText().toString().equals("") && !descriptionChange.getText().toString().equals("") && !(nameChange.getText().toString().equals(name) && descriptionChange.getText().toString().equals(description)));
    }

    private boolean nameNotAlreadyTaken(){
        if(!nameChange.getText().toString().equals(name)){
            if(SQLiteManager.getInstance(this).getUserByUsername(nameChange.getText().toString()) != null){
                nameTakenMessage.setText("This name is already taken.");
                return false;
            }
        }
        nameTakenMessage.setText("");
        return true;
    }

    private Profile getProfile(){
        return SQLiteManager.getInstance(this).getUserById(sharedPreferences.getInt(ConnectionActivity.CONNECTED_PROFILE, -1));
    }


    private void updateProfile(){
        Profile profile = getProfile();
        profile.setName(nameChange.getText().toString());
        profile.setDescription(descriptionChange.getText().toString());
        SQLiteManager.getInstance(this).updateUser(profile);
        finish();
    }

}