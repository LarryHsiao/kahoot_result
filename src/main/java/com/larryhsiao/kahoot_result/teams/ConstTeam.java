package com.larryhsiao.kahoot_result.teams;

import com.larryhsiao.kahoot_result.players.Player;

import java.util.List;

public class ConstTeam implements Team {
    private final String name;
    private final List<Player> players;

    public ConstTeam(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<Player> players() {
        return players;
    }

    @Override
    public double totalScore() {
        return players.stream().mapToDouble(Player::score).sum();
    }
}
