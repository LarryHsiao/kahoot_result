package com.larryhsiao.kahoot_result;

import com.larryhsiao.clotho.Source;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

public class CalculateScore implements Source<String> {
    private final static int SHEET_IDX_FINAL_SCORE = 1;
    private final static int COLUMN_IDX_PLAYER_EMAIL = 2;
    private final static int COLUMN_IDX_PLAYER_SCORE = 3;

    @Override
    public String value() {
        try (final XSSFWorkbook book = new XSSFWorkbook(new File(
            getClass().getResource("/sample.xlsx").toURI()
        ))) {
            final XSSFSheet sheet = book.getSheetAt(SHEET_IDX_FINAL_SCORE);
            for (Row cells : sheet) {
                if (cells.getRowNum() <= 2){
                    continue;
                }
                final Cell userCell = cells.getCell(COLUMN_IDX_PLAYER_EMAIL);
                System.out.print(userCell.getStringCellValue());
                System.out.print(" ");
                final Cell scoreCell = cells.getCell(COLUMN_IDX_PLAYER_SCORE);
                System.out.print(""+scoreCell.getNumericCellValue());
                System.out.println();
            }
            return "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
