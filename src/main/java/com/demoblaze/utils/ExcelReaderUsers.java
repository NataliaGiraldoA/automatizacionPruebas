package com.demoblaze.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelReaderUsers {
    public static Object[][] readUsers(String path) {
        String excelPath = (path == null || path.isBlank()) ? Constants.USERS : path;

        try (InputStream is = new FileInputStream(excelPath);
             Workbook wb = new XSSFWorkbook(is)) {

            Sheet sheet = wb.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();

            // Índices de filas
            int firstRowIdx = sheet.getFirstRowNum();   // normalmente 0 (header)
            int lastRowIdx  = sheet.getLastRowNum();    // última fila con algo
            int totalRows   = lastRowIdx - firstRowIdx; // sin contar header

            // 5 columnas + confirm
            int COLS = 6;
            Object[][] data = new Object[totalRows][COLS];

            // i = firstRowIdx + 1 para saltar el header
            for (int i = firstRowIdx + 1, out = 0; i <= lastRowIdx; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // fila vacía

                // Leer por índice fijo
                String first = fmt.formatCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String last  = fmt.formatCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String email = fmt.formatCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String phone = fmt.formatCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String pass  = fmt.formatCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();

                data[out][0] = first;
                data[out][1] = last;
                data[out][2] = email;
                data[out][3] = phone;
                data[out][4] = pass;
                data[out][5] = pass; // confirmación = pass
                out++;
            }

            // Recortar si hubo filas vacías
            int filled = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i][0] != null) filled++;
            }
            if (filled != data.length) {
                Object[][] trimmed = new Object[filled][COLS];
                for (int i = 0, j = 0; i < data.length; i++) {
                    if (data[i][0] != null) {
                        System.arraycopy(data[i], 0, trimmed[j++], 0, COLS);
                    }
                }
                return trimmed;
            }

            return data;
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Excel: " + excelPath, e);
        }
    }

}
