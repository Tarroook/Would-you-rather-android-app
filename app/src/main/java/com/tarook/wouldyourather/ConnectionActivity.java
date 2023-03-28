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
import android.widget.Toast;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.model.ProfileDatabase;

public class ConnectionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CONNECTION = 3;
    public final static String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;

    private EditText name;
    private Button connectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);


        name = findViewById(R.id.connection_activity_name);
        connectionButton = findViewById(R.id.connection_activity_confirm_button);
        connectionButton.setEnabled(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                connectionButton.setEnabled(!s.toString().isEmpty());
                //TODO
                if(!s.toString().isEmpty() && ProfileDatabase.getProfile(s.toString()) != null){
                    connectionButton.setText("Sign in");
                }
                else{
                    connectionButton.setText("Sign up");
                }
            }
        });

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You are connected.", Toast.LENGTH_SHORT).show();

                //TODO
                Profile profile = ProfileDatabase.getProfile(name.getText().toString());
                if(profile == null){
                    ProfileDatabase.addProfile(name.getText().toString(), "Empty description");
                    profile = ProfileDatabase.getProfile(name.getText().toString());
                }

                sharedPreferences.edit().putInt("connectedProfile", profile.getId()).apply();


                finish();
            }
        });

    }
}