package org.isf.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class XLSXService {

    public String getSymptomInfo(String symptom, String infoType) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("diagnosis.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);

        Sheet sheet1 = wb.getSheetAt(0);
        Sheet sheet2 = wb.getSheetAt(1);

        Row neededRow = null;

        for (Row row : sheet1) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(symptom)) {
                        neededRow = row;
                    }
                }
            }
        }

        if (neededRow == null) {
            return "No " + infoType;
        }

        Hyperlink h = null;

        for (Cell cell : neededRow) {
            if (cell.getRichStringCellValue().getString().trim().equals(infoType)) {
                h = cell.getHyperlink();
            }
        }

        if (h == null) {
            return "No " + infoType;
        } else {
            CellReference cellReference = new CellReference(h.getAddress());
            Row descriptionRow = sheet2.getRow(cellReference.getRow());
            Cell desciptionCell = descriptionRow.getCell(cellReference.getCol());

            return desciptionCell.getStringCellValue();
        }
    }

}
