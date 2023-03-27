package com.tarook.wouldyourather.model;

import java.util.ArrayList;

public class ProfileDatabase {

    private static ArrayList<Profile> profiles = new ArrayList<>();

    private static int id = 0;


    public static void addProfile(String name, String description){
        profiles.add(new Profile(id, name, description));
        id++;
    }

    public static Profile getProfile(String name){
        for(Profile p : profiles){
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

    public static Profile getProfile(int id){
        for(Profile p : profiles){
            if(p.getId()==id) return p;
        }
        return null;
    }

    public static void updateProfile(int id, String name, String description){
        for(Profile p : profiles){
            if(p.getId()==id){
                p.setName(name);
                p.setDescription(description);
            }
        }
    }


}
