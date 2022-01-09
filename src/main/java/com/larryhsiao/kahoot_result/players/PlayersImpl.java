package com.larryhsiao.kahoot_result.players;

import com.larryhsiao.kahoot_result.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayersImpl implements Players {
    private final List<Player> players = new ArrayList<>();
    private final Map<String, Player> idMap = new HashMap<>();
    private final Map<String, Player> emailMap = new HashMap<>();
    private final Map<String, Player> nameMap = new HashMap<>();

    @Override
    public List<Player> all() {
        if (!players.isEmpty()) {
            return players;
        } else {
            try (final XSSFWorkbook book = new XSSFWorkbook(new File(
                getClass().getResource("/players.xlsx").toURI()
            ))) {
                final List<Player> players = new ArrayList<>();
                final XSSFSheet sheet = book.getSheetAt(0);
                for (Row cells : sheet) {
                    if (cells.getRowNum() < 1) {
                        continue;
                    }
                    final String email = new CellValue(cells, 1).value();
                    final String id = new CellValue(cells, 4).value();
                    final String name = new CellValue(cells, 3).value();
                    players.add(new RawPlayer(id, name, email));
                }
                idMap.putAll(players.stream().collect(
                    Collectors.toMap(Player::id, Function.identity(), (o1, o2)-> o1))
                );
                emailMap.putAll(players.stream().collect(
                    Collectors.toMap(Player::email, Function.identity(), (o1, o2)-> o1)));
                nameMap.putAll(players.stream().collect(
                    Collectors.toMap(Player::name, Function.identity(), (o1, o2)-> o1))
                );
                return players;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<Player> byId(String id) {
        all();
        return Optional.ofNullable(idMap.get(id));
    }

    @Override
    public Optional<Player> byEmail(String email) {
        all();
        Player player = emailMap.get(email);
        if (player == null) {
            player = byId(email.split("@")[0]).orElse(null);
        }
        return Optional.ofNullable(player);
    }

    @Override
    public Optional<Player> byName(String name) {
        all();
        final Player player = nameMap.get(name);
        return Optional.ofNullable(player);
    }

    @Override
    public Optional<Player> byKeyword(String keyword) {
        try {
            return Optional.ofNullable(byEmail(keyword)
                .orElseGet(() ->
                    byId(keyword).orElseGet(() ->
                        byName(keyword).orElse(null)
                    )
                )
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
