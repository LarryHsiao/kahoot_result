package com.larryhsiao.kahoot_result.teams;

import com.larryhsiao.kahoot_result.CellValue;
import com.larryhsiao.kahoot_result.players.Player;
import com.larryhsiao.kahoot_result.players.Players;
import com.larryhsiao.kahoot_result.players.RawPlayer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamsImpl implements Teams {
    private final List<Team> teams = new ArrayList<>();
    private final Players players;

    public TeamsImpl(Players players) {
        this.players = players;
    }

    @Override
    public List<Team> all() {
        if (!teams.isEmpty()) {
            return teams;
        } else {
            try (final XSSFWorkbook book = new XSSFWorkbook(new File(
                getClass().getResource("/teams.xlsx").toURI()
            ))) {
                final List<Team> result = new ArrayList<>();
                final XSSFSheet sheet = book.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() < 1) {
                        continue;
                    }
                    final Cell teamNameCell = row.getCell(0);
                    if (teamNameCell == null) {
                        continue;
                    }
                    final String teamName = String.valueOf(
                        (int) teamNameCell.getNumericCellValue()
                    );
                    ArrayList<Player> playersInTeam = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        final String name = new CellValue(row, 1 + (i * 2)).value();
                        final String id = new CellValue(row, 2 + (i * 2)).value();
                        Player player = players.byId(id).orElseGet(() ->
                            players.byName(name).orElse(null)
                        );
                        if (player != null) {
                            playersInTeam.add(player);
                        }else{
                            System.out.println("Not found: " + id + " " + name);
                            playersInTeam.add(new RawPlayer(
                                id,
                                name,
                                ""
                            ));
                        }
                    }
                    result.add(new ConstTeam(teamName, playersInTeam));
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
