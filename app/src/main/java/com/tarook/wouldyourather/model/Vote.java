package com.tarook.wouldyourather.model;

public class Vote {
    private int id;
    private int wyrId;
    private int optionId;
    private int userId;

    public Vote(int id, int wyrId, int optionId, int userId) {
        this.id = id;
        this.wyrId = wyrId;
        this.optionId = optionId;
        this.userId = userId;
    }

    public Vote(int wyrId, int optionId, int userId) {
        this.wyrId = wyrId;
        this.optionId = optionId;
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

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
