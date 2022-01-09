package com.larryhsiao.kahoot_result.players;

import java.util.List;
import java.util.Optional;

public class WrappedPlayers implements Players {
    private final Players players;

    public WrappedPlayers(Players players) {
        this.players = players;
    }

    @Override
    public List<Player> all() {
        return players.all();
    }

    @Override
    public Optional<Player> byId(String id) {
        return players.byId(id);
    }

    @Override
    public Optional<Player> byEmail(String email) {
        return players.byEmail(email);
    }

    @Override
    public Optional<Player> byName(String name) {
        return players.byName(name);
    }
}
