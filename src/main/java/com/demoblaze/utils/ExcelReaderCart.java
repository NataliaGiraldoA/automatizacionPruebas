package com.demoblaze.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelReaderCart {

    public static Object[][] readProductos(String path) {
        String excelPath = (path == null || path.isBlank()) ? Constants.PRODUCTOS_BUSQUEDA : path;

        try (InputStream is = new FileInputStream(excelPath);
             Workbook wb = new XSSFWorkbook(is)) {

            Sheet sheet = wb.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();

            int firstRowIdx = sheet.getFirstRowNum();
            int lastRowIdx = sheet.getLastRowNum();
            int totalRows = lastRowIdx - firstRowIdx;

            // 4 columnas: Categoria, SubCategoria, Producto, Cantidad
            int COLS = 4;
            Object[][] data = new Object[totalRows][COLS];

            // Saltar header (firstRowIdx + 1)
            for (int i = firstRowIdx + 1, out = 0; i <= lastRowIdx; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String categoria = fmt.formatCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String subCategoria = fmt.formatCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String producto = fmt.formatCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String cantidadStr = fmt.formatCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();

                int cantidad = 1;
                try {
                    cantidad = Integer.parseInt(cantidadStr);
                } catch (NumberFormatException e) {
                    // mantener cantidad = 1
                }

                data[out][0] = categoria;
                data[out][1] = subCategoria;
                data[out][2] = producto;
                data[out][3] = cantidad;
                out++;
            }

            // Recortar filas vacías
            int filled = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i][2] != null && !data[i][2].toString().isEmpty()) filled++;
            }

            if (filled != data.length) {
                Object[][] trimmed = new Object[filled][COLS];
                for (int i = 0, j = 0; i < data.length; i++) {
                    if (data[i][2] != null && !data[i][2].toString().isEmpty()) {
                        System.arraycopy(data[i], 0, trimmed[j++], 0, COLS);
                    }
                }
                return trimmed;
            }

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Excel de productos: " + excelPath, e);
        }
    }
}