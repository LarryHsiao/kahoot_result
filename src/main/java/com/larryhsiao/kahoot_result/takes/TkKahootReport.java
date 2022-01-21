package com.larryhsiao.kahoot_result.takes;

import com.larryhsiao.clotho.file.TextFile;
import com.larryhsiao.clotho.stream.StreamString;
import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
import com.larryhsiao.kahoot_result.teams.Team;
import com.larryhsiao.kahoot_result.teams.TeamsImpl;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.multipart.RqMtBase;
import org.takes.rs.RsVelocity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TkKahootReport implements Take {
    @Override
    public Response act(Request req) throws IOException {
        try (final InputStream stream = new RqMtBase(req).part("kahoot_report")
            .iterator().next().body()
        ) {
            final List<Team> teams = new TeamsImpl(
                new ScoredPlayers(
                    new PlayersImpl(),
                    new KahootScores(stream)
                )
            ).all().stream().sorted(
                (t1, t2) -> (int) ((t2.totalScore() * 100) - (t1.totalScore() * 100))
            ).collect(Collectors.toList());
            String time = SimpleDateFormat.getDateTimeInstance().format(new Date());
            new TextFile(
                new File(
                    "/home/larryhsiao/kahoot/reports/" + time + "_last_kahoot_report.html"
                ),
                new StreamString(
                    new RsVelocity(
                        getClass().getResource("/result.html.vm"),
                        new RsVelocity.Pair("result", entireRecord(teams))
                    ).body()
                ).value()
            ).value();
            new TextFile(
                new File(
                    "/home/larryhsiao/kahoot/reports/" + time + "_last_kahoot_report_winner.html"
                ),
                new StreamString(
                    new RsVelocity(
                        getClass().getResource("/result.html.vm"),
                        new RsVelocity.Pair("result", winnerRecord(teams))
                    ).body()
                ).value()
            ).value();
            new TextFile(
                new File(
                    "/home/larryhsiao/kahoot/reports/" + time + "_last_kahoot_report_looser.html"
                ),
                new StreamString(
                    new RsVelocity(
                        getClass().getResource("/result.html.vm"),
                        new RsVelocity.Pair("result", looserRecord(teams))
                    ).body()
                ).value()
            ).value();
            return new RsVelocity(
                getClass().getResource("/result.html.vm"),
                new RsVelocity.Pair("result", winnerTeams(teams))
            );
        }
    }

    private String winnerTeams(List<Team> teams) {
        final StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (Team team : teams) {
            if (counter > 32){
                break;
            }
            stringBuilder.append(counter).append(": ");
            stringBuilder.append(team.name());
            stringBuilder.append("</br>");
            counter++;
        }
        return stringBuilder.toString();
    }

    private String entireRecord(List<Team> teams) {
        return recordHtml(teams, 0, 999);
    }

    private String looserRecord(List<Team> teams) {
        return recordHtml(teams, 32, 999);
    }

    private String winnerRecord(List<Team> teams) {
        return recordHtml(teams, 0, 32);
    }

    private String recordHtml(List<Team> teams, int skip, int take) {
        final StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        int taken = 0;
        for (Team team : teams) {
            if (counter < skip) {
                counter++;
                continue;
            }
            if (taken == take) {
                break;
            }
            taken++;
            counter++;
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
        }
        return stringBuilder.toString();
    }
}
