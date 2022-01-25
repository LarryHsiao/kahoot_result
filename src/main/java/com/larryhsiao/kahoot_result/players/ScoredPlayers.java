package com.larryhsiao.kahoot_result.players;

import com.larryhsiao.kahoot_result.scores.DummyScore;
import com.larryhsiao.kahoot_result.scores.Score;
import com.larryhsiao.kahoot_result.scores.Scores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScoredPlayers extends WrappedPlayers {
    private final Scores scores;
    private final Map<String, Score> emailMap = new HashMap<>();
    private final Map<String, Score> nickNameMap = new HashMap<>();

    public ScoredPlayers(Players players, Scores scores) {
        super(players);
        this.scores = scores;
    }

    private void initialMap() {
        if (!emailMap.isEmpty()) {
            return;
        }

        emailMap.putAll(
            scores.all().stream().collect(
                Collectors.toMap(Score::userEmail, Function.identity(), (o1, o2) -> o1))
        );
        nickNameMap.putAll(
            scores.all().stream().collect(
                Collectors.toMap(Score::nickName, Function.identity(), (o1, o2) -> o1)
            )
        );
    }

    @Override
    public List<Player> all() {
        initialMap();
        return super.all().stream().map(this::getScoredPlayer).collect(Collectors.toList());
    }

    @Override
    public Optional<Player> byId(String id) {
        initialMap();
        Player player = super.byId(id).orElse(null);
        return player == null ? Optional.empty() : Optional.of(
            getScoredPlayer(player)
        );
    }

    @Override
    public Optional<Player> byEmail(String email) {
        initialMap();
        Player player = super.byEmail(email).orElse(null);
        return player == null ? Optional.empty() : Optional.of(
            getScoredPlayer(player)
        );
    }

    @Override
    public Optional<Player> byName(String name) {
        initialMap();
        Player player = super.byName(name).orElse(null);
        return player == null ? Optional.empty() : Optional.of(
            getScoredPlayer(player)
        );
    }

    private Player getScoredPlayer(Player it) {
        return new WrappedPlayer(it) {
            @Override
            public double score() {
                return Optional.ofNullable(
                    emailMap.get(it.email())
                ).orElseGet(() ->
                    Optional.ofNullable(emailMap.get(it.id() + "@cmoney.com.tw")).orElseGet(() ->
                        Optional.ofNullable(nickNameMap.get(it.name()))
                            .orElseGet(() ->
                                Optional.ofNullable(nickNameMap.get(it.id())).orElseGet(() -> {
                                    for (String nickName : nickNameMap.keySet()) {
                                        if (nickName.toLowerCase().contains(it.id())) {
                                            return nickNameMap.get(nickName);
                                        }
                                        if (nickName.toLowerCase().contains(it.name())) {
                                            return nickNameMap.get(nickName);
                                        }
                                        if (nickName.toLowerCase().contains(it.email())) {
                                            return nickNameMap.get(nickName);
                                        }
                                        if (nickName.toLowerCase().contains(it.id() + "@cmoney.com.tw")) {
                                            return nickNameMap.get(nickName);
                                        }
                                    }
                                    return new DummyScore();
                                })
                            )
                    )
                ).value();
            }
        };
    }
}
