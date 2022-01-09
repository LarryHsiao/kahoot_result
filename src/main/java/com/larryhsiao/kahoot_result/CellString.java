package com.larryhsiao.kahoot_result;

import com.larryhsiao.clotho.Source;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CellString implements Source<String> {
    private final Row row;
    private final int index;

    public CellString(Row row, int index) {
        this.row = row;
        this.index = index;
    }

    @Override
    public String value() {
        try {
            final Cell cell = row.getCell(index);
            String result = "";
            switch (cell.getCellType()) {
                case STRING:
                    result += cell.getStringCellValue();
                    break;
                case NUMERIC:
                    result += String.valueOf(((int) cell.getNumericCellValue()));
                    break;
                default:
                    result += cell.toString();
                    break;
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }
}
