package com.larryhsiao.kahoot_result.players;

public class RawPlayer implements Player {
    private final String id;
    private final String name;
    private final String email;

    public RawPlayer(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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
}
