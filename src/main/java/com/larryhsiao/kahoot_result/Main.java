package com.larryhsiao.kahoot_result;

import com.larryhsiao.kahoot_result.players.PlayersImpl;
import com.larryhsiao.kahoot_result.players.ScoredPlayers;
import com.larryhsiao.kahoot_result.scores.KahootScores;
import com.larryhsiao.kahoot_result.teams.TeamsImpl;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtCli;
import org.takes.rs.RsHtml;

public class Main {
    public static void main(String[] args) throws Exception {
        new TeamsImpl(
            new ScoredPlayers(
                new PlayersImpl(),
                new KahootScores()
            )
        ).all().stream().sorted(
            (t1, t2) -> (int) ((t2.totalScore() * 100) - (t1.totalScore() * 100))
        ).forEach(team -> {
            System.out.println(
                "team " + team.name() + ", " + team.players().size() + " players, totalScore: " +
                    team.totalScore()
            );
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
        new FtCli(
            new TkFork(
                new FkRegex("/", new TkFork(
                    new FkMethods(
                        "GET",
                        new Take() {
                            @Override
                            public Response act(Request req) {
                                return new RsHtml(
                                    getClass().getResource("/upload.html")
                                );
                            }
                        }
                    )
                )),
                new FkRegex("/upload", new TkFork(
                    new FkMethods(
                        "POST",
                        new Take() {
                            @Override
                            public Response act(Request req) {
                                return new RsHtml(
                                    getClass().getResource("/result.html")
                                );
                            }
                        }
                    )
                ))
            ),
            args
        ).start(Exit.NEVER);
    }
}
