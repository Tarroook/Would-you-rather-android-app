package com.tarook.wouldyourather.model;

public class Vote {
    private int id;
    private int wyrId;
    private int optionNumber;
    private int userId;

    public Vote(int id, int wyrId, int optionNumber, int userId) {
        this.id = id;
        this.wyrId = wyrId;
        this.optionNumber = optionNumber;
        this.userId = userId;
    }

    public Vote(int wyrId, int optionNumber, int userId) {
        this.wyrId = wyrId;
        this.optionNumber = optionNumber;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWyrId() {
        return wyrId;
    }

    public void setWyrId(int wyrId) {
        this.wyrId = wyrId;
    }

    public int getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(int optionNumber) {
        this.optionNumber = optionNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
