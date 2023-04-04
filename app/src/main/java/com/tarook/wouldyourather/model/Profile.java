package com.tarook.wouldyourather.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tarook.wouldyourather.R;

import java.util.ArrayList;

public class Profile {

    public static ArrayList<Profile> profiles = new ArrayList<>();

    private int id;
    private String name;
    private String description;

    private Bitmap profilePicture;

    public Profile(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Profile(int id, String name, String description, Bitmap profilePicture){ // this constructor should only be used when we get the data from the database because the id is already set
        this.id = id;
        this.name = name;
        this.description = description;
        this.profilePicture = profilePicture;
    }

    public Profile(Context context, String name, String description){
        this.name = name;
        this.description = description;
        this.profilePicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultpp);
    }

    public Profile(String name, String description, Bitmap profilePicture) { // this constructor should only be used when we create a new user
        this.name = name;
        this.description = description;
        this.profilePicture = profilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }
}
