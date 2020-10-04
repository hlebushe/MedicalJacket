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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<String> getFullMedicationList() throws IOException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cipla_draft.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet) {
            result.add(row.getCell(0).getStringCellValue());
        }

        return result;
    }

    public List<String> getMedications(String diagnosis) throws IOException { List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cipla_draft.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(diagnosis);
        boolean found = matcher.find();

        if (found) {
            String[] splitArray = diagnosis.split("\\s+");
            String first = splitArray[0];
            String second = splitArray[1];

            List<Row> foundRows = new ArrayList<>();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        if (cell.getStringCellValue().contains(first)) {
                            foundRows.add(row);
                        }
                    }
                }
            }

            for (Row row : foundRows) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        if (cell.getStringCellValue().contains(second)) {
                            result.add(row.getCell(0).getStringCellValue());
                        }
                    }
                }
            }

        } else {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        if (cell.getStringCellValue().contains(diagnosis)) {
                            result.add(row.getCell(0).getStringCellValue());
                        }
                    }
                }
            }
        }

        return result;

    }

    public List<String> getCombination(String medication) throws IOException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cipla_draft.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().contains(medication)) {
                result.add(row.getCell(3).getStringCellValue());
            }
        }

        return result;

    }

}
