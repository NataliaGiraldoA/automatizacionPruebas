package com.demoblaze.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultLogger {
    private static Path logFile;

    public static synchronized void init(String name) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path dir = Paths.get("target", "logs");
        try { Files.createDirectories(dir); } catch (Exception ignored) {}
        logFile = dir.resolve(name + "_" + ts + ".log");
    }

    private static String ts() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static synchronized void write(String line) {
        try {
            Files.writeString(logFile, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {}
    }

    public static void logRegistro(String email, boolean success) {
        write(ts() + " REGISTRO email=" + nv(email) + " resultado=" + (success ? "SUCCESS" : "FAILED"));
    }

    public static void logProducto(String categoria, String subCategoria, String producto, int cantidad) {
        write(ts() + " CARRITO categoria=" + nv(categoria) + " subcategoria=" + nv(subCategoria) + " producto=" + nv(producto) + " cantidad=" + cantidad);
    }

    public static void logLogin(String email, boolean success) {
        write(ts() + " LOGIN email=" + nv(email) + " resultado=" + (success ? "SUCCESS" : "FAILED"));
    }

    public static void logOpenUrl(String url, boolean success) {
        write(ts() + " OPEN_URL url=" + nv(url) + " resultado=" + (success ? "SUCCESS" : "FAILED"));
    }

    private static String nv(String s) {
        return (s == null || s.isEmpty()) ? "-" : s;
    }
}