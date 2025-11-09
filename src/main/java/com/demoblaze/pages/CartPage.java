package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Elementos - Selectores más generales
    private By cartLink() {
        return By.xpath("//a[contains(@href, 'checkout/cart')]");
    }

    private By emptyMessage() {
        return By.xpath("//*[contains(text(),'empty')]");
    }

    private By allTableRows() {
        return By.xpath("//table//tbody//tr");
    }

    private By allProductLinks() {
        return By.xpath("//table//tbody//tr//td//a[not(contains(@class,'btn'))]");
    }

    private By quantityInputs() {
        return By.name("quantity");
    }

    // Métodos
    public void irAlCarrito() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(cartLink())).click();
        wait.until(ExpectedConditions.urlContains("checkout/cart"));
    }

    public boolean isCarritoVacio() {
        try {
            return driver.findElement(emptyMessage()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getTotalProductos() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(quantityInputs()));
            List<WebElement> inputs = driver.findElements(quantityInputs());
            return inputs.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isProductoEnCarrito(String nombre) {
        try {
            List<WebElement> links = driver.findElements(allProductLinks());
            for (WebElement link : links) {
                if (link.getText().contains(nombre)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getCantidadProducto(String nombre) {
        try {
            List<WebElement> rows = driver.findElements(allTableRows());
            for (WebElement row : rows) {
                if (row.getText().contains(nombre)) {
                    WebElement input = row.findElement(By.name("quantity"));
                    return Integer.parseInt(input.getAttribute("value"));
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getProductosEnCarrito() {
        List<String> productos = new ArrayList<>();
        try {
            List<WebElement> links = driver.findElements(allProductLinks());
            for (WebElement link : links) {
                String nombre = link.getText().trim();
                if (!nombre.isEmpty()) {
                    productos.add(nombre);
                }
            }
        } catch (Exception e) {
            // ignorar
        }
        return productos;
    }

    public void imprimirCarrito() {
        System.out.println("\n=== Contenido del Carrito ===");

        if (isCarritoVacio()) {
            System.out.println("El carrito está vacío");
            return;
        }

        List<String> productos = getProductosEnCarrito();
        System.out.println("Total de productos: " + productos.size());

        for (int i = 0; i < productos.size(); i++) {
            String nombre = productos.get(i);
            int cantidad = getCantidadProducto(nombre);
            System.out.println((i + 1) + ". " + nombre + " - Cantidad: " + cantidad);
        }
        System.out.println("==============================\n");
    }
}