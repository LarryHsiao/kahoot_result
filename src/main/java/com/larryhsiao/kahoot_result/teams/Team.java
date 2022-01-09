package com.larryhsiao.kahoot_result.teams;

import com.larryhsiao.kahoot_result.players.Player;

import java.util.List;

public interface Team {
    String name();
    List<Player> players();
}
