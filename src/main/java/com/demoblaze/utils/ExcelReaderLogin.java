package com.demoblaze.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReaderLogin {

    public static Object[][] readLoginData(String excelPath) {

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("LoginData");

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {

                Row row = sheet.getRow(i);

                data[i - 1][0] = getCellValue(row.getCell(0)); // Email
                data[i - 1][1] = getCellValue(row.getCell(1)); // Password
                data[i - 1][2] = getCellValue(row.getCell(2)); // ResultadoEsperado
            }

            return data;

        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
