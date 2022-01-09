package com.larryhsiao.kahoot_result.scores;

public class DummyScore implements Score{
    @Override
    public String userEmail() {
        return "";
    }

    @Override
    public String nickName() {
        return "";
    }

    @Override
    public double value() {
        return 0;
    }
}
