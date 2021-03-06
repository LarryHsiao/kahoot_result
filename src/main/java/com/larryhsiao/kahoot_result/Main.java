package com.larryhsiao.kahoot_result;

import com.larryhsiao.kahoot_result.takes.TkItems;
import com.larryhsiao.kahoot_result.takes.TkKahootPersonalReport;
import com.larryhsiao.kahoot_result.takes.TkKahootReport;
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
                new FkRegex("/upload_personal", new TkFork(
                    new FkMethods(
                        "POST",
                        new TkKahootPersonalReport()
                    ),
                    new FkMethods(
                        "GET",
                        new Take() {
                            @Override
                            public Response act(Request req) {
                                return new RsHtml(
                                    getClass().getResource("/upload_personal.html")
                                );
                            }
                        }
                    )
                )),
                new FkRegex("/upload", new TkFork(
                    new FkMethods(
                        "POST",
                        new TkKahootReport()
                    )
                )),
                new FkRegex("/items", new TkFork(
                    new FkMethods(
                        "GET",
                        new TkItems()
                    )
                ))
            ),
            args
        ).start(Exit.NEVER);
    }
}
