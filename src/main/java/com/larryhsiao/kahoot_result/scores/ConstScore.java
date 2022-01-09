package com.larryhsiao.kahoot_result.scores;

public class ConstScore implements Score {
    private final String userId;
    private final String nickName;
    private final double score;

    public ConstScore(String userId, String nickName, double score) {
        this.userId = userId;
        this.nickName = nickName;
        this.score = score;
    }

    @Override
    public String userEmail() {
        return userId;
    }

    @Override
    public String nickName() {
        return nickName;
    }

    @Override
    public double value() {
        return score;
    }
}
