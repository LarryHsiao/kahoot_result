package com.larryhsiao.kahoot_result;

import com.larryhsiao.clotho.Source;
import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
import com.larryhsiao.kahoot_result.teams.TeamsImpl;

public class CalculateScore implements Source<String> {

    @Override
    public String value() {
        new KahootScores().all();
        new TeamsImpl(
            new ScoredPlayers(
                new PlayersImpl(),
                new KahootScores()
            )
        ).all().forEach(team -> {
            System.out.print(team.name());
            System.out.println(" team, " + team.players().size() + " players:");
            team.players().forEach(player -> {
                System.out.print(player.id());
                System.out.print(" ");
                System.out.print(player.name());
                System.out.print(" ");
                System.out.print(player.email());
                System.out.print(" ");
                System.out.print(player.score());
                System.out.println();
            });
            System.out.println();
        });
        return "";
    }
}
