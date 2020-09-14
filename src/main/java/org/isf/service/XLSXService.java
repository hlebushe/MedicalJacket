package org.isf.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.isf.models.DiseaseModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XLSXService {

    public String getSymptomInfo(String symptom, String infoType, String loc) throws IOException {
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
            Cell descriptionCell;

            switch (loc) {
                case "gu":
                    descriptionCell = descriptionRow.getCell(2);
                    break;
                case "hi":
                    descriptionCell = descriptionRow.getCell(5);
                    break;
                case "kok":
                    descriptionCell = descriptionRow.getCell(2);
                    break;
                case "te":
                    descriptionCell = descriptionRow.getCell(3);
                    break;
                case "ur":
                    descriptionCell = descriptionRow.getCell(4);
                    break;
                default:
                    descriptionCell = descriptionRow.getCell(2);
                    break;
            }

            if (descriptionCell == null || descriptionCell.getStringCellValue().equals("")) descriptionCell = descriptionRow.getCell(2);

            return descriptionCell.getStringCellValue();
        }
    }

    public List<DiseaseModel> getDiseasesList(String loc) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("diagnosis.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);

        List<DiseaseModel> list = new ArrayList<DiseaseModel>();

        Sheet sheet1 = wb.getSheetAt(0);
        Sheet sheet2 = wb.getSheetAt(1);

        for (Row row : sheet1) {
            DiseaseModel diseaseModel = new DiseaseModel();

            try {
                Hyperlink h = null;

                diseaseModel.setName(row.getCell(1).getStringCellValue());
                h = row.getCell(2).getHyperlink();

                if (diseaseModel.getName().isEmpty() || diseaseModel.getName() == null) {
                    continue;
                }

                if (h == null || !row.getCell(2).getStringCellValue().equals("Description")) {
                    diseaseModel.setDescription("No description");
                } else {
                    CellReference cellReference = new CellReference(h.getAddress());
                    Row descriptionRow = sheet2.getRow(cellReference.getRow());
                    Cell descriptionCell;

                    switch (loc) {
                        case "gu":
                            descriptionCell = descriptionRow.getCell(2);
                            break;
                        case "hi":
                            descriptionCell = descriptionRow.getCell(5);
                            break;
                        case "kok":
                            descriptionCell = descriptionRow.getCell(2);
                            break;
                        case "te":
                            descriptionCell = descriptionRow.getCell(3);
                            break;
                        case "ur":
                            descriptionCell = descriptionRow.getCell(4);
                            break;
                        default:
                            descriptionCell = descriptionRow.getCell(2);
                            break;
                    }

                    if (descriptionCell == null || descriptionCell.getStringCellValue().equals("")) descriptionCell = descriptionRow.getCell(2);
                    diseaseModel.setDescription(descriptionCell.getStringCellValue());
                }

                list.add(diseaseModel);
            } catch (Exception e) {
                continue;
            }
        }

        return list;
    }

}
