package com.larryhsiao.kahoot_result.takes;

import com.larryhsiao.kahoot_result.Main;
import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
import com.larryhsiao.kahoot_result.teams.TeamsImpl;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.multipart.RqMtBase;
import org.takes.rs.RsHtml;
import org.takes.rs.RsVelocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class KahootReportTake implements Take {
    @Override
    public Response act(Request req) throws IOException {
        try (final InputStream stream = new RqMtBase(req).part("kahoot_report")
            .iterator().next().body()
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            new TeamsImpl(
                new ScoredPlayers(
                    new PlayersImpl(),
                    new KahootScores(stream)
                )
            ).all().stream().sorted(
                (t1, t2) -> (int) ((t2.totalScore() * 100) - (t1.totalScore() * 100))
            ).forEach(team -> {
                stringBuilder.append("team ").append(team.name()).append(", ")
                    .append(String.valueOf(team.players().size())).append(" players, totalScore: ")
                    .append(String.valueOf(team.totalScore()));
                stringBuilder.append("\n");
                team.players().forEach(player -> {
                    stringBuilder.append(player.id());
                    stringBuilder.append(" ");
                    stringBuilder.append(player.name());
                    stringBuilder.append(" ");
                    stringBuilder.append(player.email());
                    stringBuilder.append(" ");
                    stringBuilder.append(player.score());
                    stringBuilder.append("\n");
                });
                stringBuilder.append("\n");
            });
            return new RsVelocity(
                getClass().getResource("/result.html.vm"),
                new RsVelocity.Pair("result", stringBuilder.toString())
            );
        }
    }
}
