package com.larryhsiao.kahoot_result.players;

public class WrappedPlayer implements Player {
    private final Player player;

    public WrappedPlayer(Player player) {this.player = player;}

    @Override
    public String id() {
        return player.id();
    }

    @Override
    public String name() {
        return player.name();
    }

    @Override
    public String email() {
        return player.email();
    }

    @Override
    public double score() {
        return player.score();
    }
}
