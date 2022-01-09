package com.larryhsiao.kahoot_result.scores;

import com.larryhsiao.kahoot_result.CellDouble;
import com.larryhsiao.kahoot_result.CellString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KahootScores implements Scores {
    private final static int SHEET_IDX_FINAL_SCORE = 1;
    private final static int COLUMN_IDX_PLAYER_NAME = 1;
    private final static int COLUMN_IDX_PLAYER_EMAIL = 2;
    private final static int COLUMN_IDX_PLAYER_SCORE = 3;

    private final List<Score> scores = new ArrayList<>();

    @Override
    public List<Score> all() {
        if (!scores.isEmpty()) {
            return scores;
        }
        try (final XSSFWorkbook book = new XSSFWorkbook(new File(
            getClass().getResource("/kahoot_report.xlsx").toURI()
        ))) {
            final XSSFSheet sheet = book.getSheetAt(SHEET_IDX_FINAL_SCORE);
            for (Row cells : sheet) {
                if (cells.getRowNum() <= 2) {
                    continue;
                }
                final String email = new CellString(cells, COLUMN_IDX_PLAYER_EMAIL).value();
                final String nickName = new CellString(cells, COLUMN_IDX_PLAYER_NAME).value();
                if (email.isEmpty() && nickName.isEmpty()) {
                    continue;
                }
                scores.add(
                    new ConstScore(
                        email,
                        nickName,
                        new CellDouble(cells, COLUMN_IDX_PLAYER_SCORE).value()
                    )
                );
            }
            return scores;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
