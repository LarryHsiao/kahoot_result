package com.larryhsiao.kahoot_result;

import com.larryhsiao.clotho.Source;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class CellDouble implements Source<Double> {
    private final Row row;
    private final int index;

    public CellDouble(Row row, int index) {
        this.row = row;
        this.index = index;
    }

    @Override
    public Double value() {
        try {
            final Cell cell = row.getCell(index);
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    return Double.valueOf(cell.getStringCellValue());
            }
            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
