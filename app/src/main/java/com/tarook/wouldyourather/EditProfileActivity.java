package com.tarook.wouldyourather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarook.wouldyourather.model.Profile;
import com.tarook.wouldyourather.util.SQLiteManager;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT_PROFILE = 2;
    public final static String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;
    private ImageView picture;
    private Button changePictureButton;
    private EditText nameChange;
    private EditText descriptionChange;
    private Button confirmButton;
    private TextView nameTakenMessage;
    private TextView descTooLongMessage;

    private String name;
    private String description;
    private boolean pictureChanged;
    private Bitmap newPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        picture = findViewById(R.id.edit_profile_activity_picture);
        changePictureButton = findViewById(R.id.edit_profile_activity_button_picture);
        nameChange = findViewById(R.id.edit_profile_activity_name);
        descriptionChange = findViewById(R.id.edit_profile_activity_description);
        nameTakenMessage = findViewById(R.id.edit_profile_activity_username_taken);
        descTooLongMessage = findViewById(R.id.edit_profile_activity_description_too_long);
        confirmButton = findViewById(R.id.edit_profile_activity_confirm_button);
        confirmButton.setEnabled(false);

        descriptionChange.setMaxLines(10);

        pictureChanged = false;

        Profile profile = getProfile();
        if(profile == null){
            Toast.makeText(getApplicationContext(), "Error: profile not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            name = profile.getName();
            description = profile.getDescription();

            picture.setImageBitmap(profile.getProfilePicture());
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


            changePictureButton.setOnClickListener(v -> {
                // opens the camera and takes a picture
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            assert data != null;
            newPicture = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(newPicture);
            pictureChanged = true;
            confirmButton.setEnabled(enableButton());
        }
    }


    private boolean enableButton(){
        if(pictureChanged) return (nameNotAlreadyTaken() && descriptionNotTooLong() && !nameChange.getText().toString().equals("") && !descriptionChange.getText().toString().equals(""));
        else return (nameNotAlreadyTaken() && descriptionNotTooLong() && !nameChange.getText().toString().equals("") && !descriptionChange.getText().toString().equals("") && !(nameChange.getText().toString().equals(name) && descriptionChange.getText().toString().equals(description)));
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

    private boolean descriptionNotTooLong(){
        if(descriptionChange.getLineCount() > ProfileActivity.DESCRIPTION_MAX_LINES){
            descTooLongMessage.setText("The description cannot take more than " + ProfileActivity.DESCRIPTION_MAX_LINES + " lines.");
            return false;
        }
        descTooLongMessage.setText("");
        return true;
    }

    private Profile getProfile(){
        return SQLiteManager.getInstance(this).getUserById(sharedPreferences.getInt(ConnectionActivity.CONNECTED_PROFILE, -1));
    }


    private void updateProfile(){
        Profile profile = getProfile();
        profile.setName(nameChange.getText().toString());
        profile.setDescription(descriptionChange.getText().toString());
        if(pictureChanged) profile.setProfilePicture(newPicture);
        SQLiteManager.getInstance(this).updateUser(profile);
        finish();
    }

}