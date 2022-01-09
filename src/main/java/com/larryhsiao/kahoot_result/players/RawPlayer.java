package com.larryhsiao.kahoot_result.players;

public class RawPlayer implements Player {
    private final String id;
    private final String name;
    private final String email;
    private final double score;

    public RawPlayer(String id, String name, String email) {
        this(id, name, email, 0);
    }

    public RawPlayer(String id, String name, String email, double score) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.score = score;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public double score() {
        return score;
    }
}
