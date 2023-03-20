package com.tarook.wouldyourather.model;

import java.util.ArrayList;

public class WouldYouRather {
    private int id;
    private String question;
    private ArrayList<String> options;

    public WouldYouRather(int id, String question, ArrayList<String> options) {
        this.id = id;
        this.question = question;
        this.options = options;
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
}
