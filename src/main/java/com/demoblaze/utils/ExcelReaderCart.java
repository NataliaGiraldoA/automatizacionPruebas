package com.demoblaze.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelReaderCart {

    public static Object[][] readProductos(String path) {
        // Validar path (compatible con Java 8)
        String excelPath = (path == null || path.trim().isEmpty()) ? Constants.PRODUCTOS_BUSQUEDA : path;

        try (InputStream is = new FileInputStream(excelPath);
             Workbook wb = new XSSFWorkbook(is)) {

            // Validar que el workbook tenga hojas
            if (wb.getNumberOfSheets() == 0) {
                return new Object[0][4];
            }

            Sheet sheet = wb.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();

            int firstRowIdx = sheet.getFirstRowNum();
            int lastRowIdx = sheet.getLastRowNum();

            // Si no hay filas o solo hay header, retornar vacío
            if (lastRowIdx <= firstRowIdx) {
                return new Object[0][4];
            }

            // 4 columnas: Categoria, SubCategoria, Producto, Cantidad
            int COLS = 5;
            int totalRows = lastRowIdx - firstRowIdx;
            Object[][] data = new Object[totalRows][COLS];

            // Saltar header (firstRowIdx + 1)
            int out = 0;
            for (int i = firstRowIdx + 1; i <= lastRowIdx; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String categoria = fmt.formatCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String subCategoria = fmt.formatCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String producto = fmt.formatCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String cantidadStr = fmt.formatCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();
                String expectedResult = fmt.formatCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).trim();

                // Saltar filas donde el producto esté vacío
                if (producto.isEmpty()) continue;

                int cantidad = 1;
                try {
                    cantidad = Integer.parseInt(cantidadStr);
                } catch (NumberFormatException e) {
                    try {
                        // Intentar parsear como double por si viene con decimales (ej: 1.0)
                        double d = Double.parseDouble(cantidadStr);
                        cantidad = (int) d;
                    } catch (NumberFormatException e2) {
                        // mantener cantidad = 1
                    }
                }

                data[out][0] = categoria;
                data[out][1] = subCategoria;
                data[out][2] = producto;
                data[out][3] = cantidad;
                data[out][4] = expectedResult;
                out++;
            }

            // Recortar al tamaño real (solo filas válidas)
            if (out < data.length) {
                Object[][] trimmed = new Object[out][COLS];
                System.arraycopy(data, 0, trimmed, 0, out);
                return trimmed;
            }

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Excel de productos: " + excelPath, e);
        }
    }
}