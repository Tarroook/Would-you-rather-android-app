package com.tarook.wouldyourather.model;

import java.util.ArrayList;

public class WouldYouRather {
    public static ArrayList<WouldYouRather> WYRLIST = new ArrayList<>();
    private int id;
    private String question;
    private ArrayList<String> options;

    public WouldYouRather(String question){ // this constructor should only be used when we create a new WYR
        this.question = question;
        options = new ArrayList<>();
    }
    public WouldYouRather(int id, String question) { // this constructor should only be used when we get the data from the database because the id is already set
        this.id = id;
        this.question = question;
        options = new ArrayList<>();
    }


    public void addOption(String option) {
        options.add(option);
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "WouldYouRather{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", options=" + options +
                '}';
    }
}
