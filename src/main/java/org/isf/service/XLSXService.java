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

        for (Row row : sheet2) {
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
            Row descriptionRow = sheet1.getRow(cellReference.getRow());
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

        for (Row row : sheet2) {
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
                    Row descriptionRow = sheet1.getRow(cellReference.getRow());
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
        File file = new File(classLoader.getResource("medication_list.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet1 = wb.getSheetAt(0);
        Sheet sheet2 = wb.getSheetAt(1);
        Sheet sheet3 = wb.getSheetAt(2);
        Sheet sheet4 = wb.getSheetAt(3);
        Sheet sheet5 = wb.getSheetAt(4);

        for (Row row : sheet1) {
            result.add(row.getCell(0).getStringCellValue());
        }
        for (Row row : sheet2) {
            result.add(row.getCell(0).getStringCellValue());
        }
        for (Row row : sheet3) {
            result.add(row.getCell(0).getStringCellValue());
        }
        for (Row row : sheet4) {
            result.add(row.getCell(0).getStringCellValue());
        }
        for (Row row : sheet5) {
            result.add(row.getCell(0).getStringCellValue());
        }

        return result;
    }

    public List<String> getMedications(String diagnosis) throws IOException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("medication_list.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet1 = wb.getSheetAt(0);
        Sheet sheet2 = wb.getSheetAt(1);
        Sheet sheet3 = wb.getSheetAt(2);
        Sheet sheet4 = wb.getSheetAt(3);
        Sheet sheet5 = wb.getSheetAt(4);

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(diagnosis);
        boolean found = matcher.find();

        if (found) {
            String[] splitArray = diagnosis.split("\\s+");
            String first = splitArray[0];
            String second = splitArray[1];

            result = findMedicationsWithType(result, first, second, sheet1);
            result = findMedicationsWithType(result, first, second, sheet2);
            result = findMedicationsWithType(result, first, second, sheet3);
            result = findMedicationsWithType(result, first, second, sheet4);
            result = findMedicationsWithType(result, first, second, sheet5);

        } else {
            result = findMedications(result, diagnosis, sheet1);
            result = findMedications(result, diagnosis, sheet2);
            result = findMedications(result, diagnosis, sheet3);
            result = findMedications(result, diagnosis, sheet4);
            result = findMedications(result, diagnosis, sheet5);
        }

        return result;

    }

    public List<String> getCombination(String medication) {
        List<String> result = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("medication_list.xlsx").getFile());

            FileInputStream fis = new FileInputStream(file);

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Sheet sheet1 = wb.getSheetAt(0);
            Sheet sheet2 = wb.getSheetAt(1);
            Sheet sheet3 = wb.getSheetAt(2);
            Sheet sheet4 = wb.getSheetAt(3);
            Sheet sheet5 = wb.getSheetAt(4);
            Boolean found = false;

            for (Row row : sheet1) {
                if (row.getCell(0).getStringCellValue().contains(medication)) {
                    result.add(row.getCell(2).getStringCellValue());
                    found = true;
                    break;
                }
            }


            if (!found) {
                for (Row row : sheet2) {
                    if (row.getCell(0).getStringCellValue().contains(medication)) {
                        result.add(row.getCell(2).getStringCellValue());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                for (Row row : sheet3) {
                    if (row.getCell(0).getStringCellValue().contains(medication)) {
                        result.add(row.getCell(2).getStringCellValue());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                for (Row row : sheet4) {
                    if (row.getCell(0).getStringCellValue().contains(medication)) {
                        result.add(row.getCell(2).getStringCellValue());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                for (Row row : sheet5) {
                    if (row.getCell(0).getStringCellValue().contains(medication)) {
                        result.add(row.getCell(2).getStringCellValue());
                        found = true;
                        break;
                    }
                }
            }

            return result;
        } catch (Exception e) {
            return result;
        }

    }

    public List<String> findMedicationsWithType(List<String> resultList, String first, String second, Sheet sheet) {
        List<Row> foundRows = new ArrayList<>();

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().toLowerCase().contains(first.toLowerCase())) {
                        foundRows.add(row);
                    }
                }
            }
        }

        for (Row row : foundRows) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().toLowerCase().contains(second.toLowerCase())) {
                        resultList.add(row.getCell(0).getStringCellValue());
                    }
                }
            }
        }

        return resultList;
    }

    public List<String> findMedications(List<String> resultList, String diagnosis, Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().toLowerCase().contains(diagnosis.toLowerCase())) {
                        resultList.add(row.getCell(0).getStringCellValue());
                    }
                }
            }
        }

        return resultList;
    }

    public List<String> getRadiologyList() throws IOException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("examinations.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(1);

        for (Row row : sheet) {
            result.add(row.getCell(1).getStringCellValue());
        }

        return result;
    }

    public List<String> getPathologyList() throws IOException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("examinations.xlsx").getFile());

        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet) {
            result.add(row.getCell(1).getStringCellValue());
        }

        return result;
    }

}
