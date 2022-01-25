package com.larryhsiao.kahoot_result.takes;

import com.larryhsiao.clotho.file.TextFile;
import com.larryhsiao.clotho.stream.StreamString;
import com.larryhsiao.kahoot_result.players.Player;
import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
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

public class TkKahootPersonalReport implements Take {
    @Override
    public Response act(Request req) throws IOException {
        try (final InputStream stream = new RqMtBase(req).part("kahoot_report")
            .iterator().next().body()) {
            final List<Player> players = new ScoredPlayers(
                new PlayersImpl(),
                new KahootScores(stream)
            ).all().stream().sorted(
                (t1, t2) -> (int) (t2.score() - t1.score())
            ).collect(Collectors.toList());
            final String time = SimpleDateFormat.getDateTimeInstance().format(new Date());
            new TextFile(
                new File(
                    "/home/larryhsiao/kahoot/reports/" + time + "_last_personal_kahoot_report.html"
                ),
                new StreamString(
                    new RsVelocity(
                        getClass().getResource("/result.html.vm"),
                        new RsVelocity.Pair("result", records(players))
                    ).body()
                ).value()
            ).value();
            return new RsVelocity(
                getClass().getResource("/result.html.vm"),
                new RsVelocity.Pair("result", records(players))
            );
        }
    }

    private String records(List<Player> players) {
        final StringBuilder strBuilder = new StringBuilder();
        int counter = 1;
        for (Player player : players) {
            if (counter > 90) {
                break;
            }
            if (player.id().isEmpty() || player.id().length() < 10){
                continue;
            }
            strBuilder.append("No. ")
                .append(counter)
                .append(" ")
                .append(player.id())
                .append(" ")
                .append(player.name())
                .append(" ")
                .append(player.email())
                .append("</br>");
            counter++;
        }
        return strBuilder.toString();
    }
}
