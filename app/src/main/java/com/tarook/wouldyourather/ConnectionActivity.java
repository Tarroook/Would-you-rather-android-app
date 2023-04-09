package com.tarook.wouldyourather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.util.SQLiteManager;

public class ConnectionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CONNECTION = 3;
    public final static String SHARED_PREFS = "sharedPrefs";
    public static final String CONNECTED_PROFILE = "connectedProfile";
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                connectionButton.setEnabled(!s.toString().isEmpty());
                if(!s.toString().isEmpty() && SQLiteManager.getInstance(ConnectionActivity.this).getUserByUsername(s.toString()) != null){
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

                Profile profile = SQLiteManager.getInstance(ConnectionActivity.this).getUserByUsername(name.getText().toString());
                if(profile == null){
                    SQLiteManager.getInstance(ConnectionActivity.this).addUser(new Profile(ConnectionActivity.this, name.getText().toString(), "This is my epic description !"));
                    profile = SQLiteManager.getInstance(ConnectionActivity.this).getUserByUsername(name.getText().toString());
                }

                sharedPreferences.edit().putInt(CONNECTED_PROFILE, profile.getId()).apply();

                finish();
            }
        });

    }
}