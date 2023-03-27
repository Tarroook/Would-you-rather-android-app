package com.tarook.wouldyourather.model;

public class Profile {

    private int id;
    private String name;
    private String description;

    public Profile(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public Profile(String name, String description){
        this.name = name;
        this.description = description;
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

}
