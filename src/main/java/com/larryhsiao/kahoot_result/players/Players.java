package com.larryhsiao.kahoot_result.players;

import java.util.List;
import java.util.Optional;

public interface Players {
    List<Player> all();

    Optional<Player> byId(String id);

    Optional<Player> byEmail(String email);

    Optional<Player> byName(String name);
}
