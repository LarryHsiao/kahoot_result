package com.larryhsiao.kahoot_result.takes;

import com.larryhsiao.clotho.file.TextFile;
import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
import com.larryhsiao.kahoot_result.teams.TeamsImpl;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.multipart.RqMtBase;
import org.takes.rs.RsVelocity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TkKahootReport implements Take {
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
                stringBuilder.append("<table>");
                stringBuilder.append("<thead><tr><th colspan=\"3\">");
                stringBuilder.append("team ")
                    .append(team.name())
                    .append(", ")
                    .append(team.players().size())
                    .append(" players, totalScore: ")
                    .append(team.totalScore());
                stringBuilder.append("</th></tr></thead>");
                stringBuilder.append("<tbody>");
                team.players().forEach(player -> {
                    stringBuilder.append("<tr>");
                    stringBuilder.append("<td>").append(player.id()).append("</td>");
                    stringBuilder.append("<td>").append(player.name()).append("</td>");
                    stringBuilder.append("<td>").append(player.score()).append("</td>");
                    stringBuilder.append("</tr>");
                });
                stringBuilder.append("</tbody>");
                stringBuilder.append("</table>");
            });
            new TextFile(
                new File(
                    "/home/larryhsiao/kahoot/reports/last_kahoot_report.html"
                ),
                stringBuilder.toString()
            ).value();
            return new RsVelocity(
                getClass().getResource("/result.html.vm"),
                new RsVelocity.Pair("result", stringBuilder.toString())
            );
        }
    }
}
