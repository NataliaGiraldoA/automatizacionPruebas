package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsDetallePage extends BasePage{

    public ProductsDetallePage(WebDriver driver){
        super(driver);
    }

    //Elementos
    private By quantity(){
        return By.name("quantity");
    }

    private By btnAddCart(){
        return By.id("button-cart");
    }

    private By successMessage(){
        return By.cssSelector(".alert-success");
    }

    //metodo
    public void agregarCarrito(String quantity){
        // Limpiar y establecer cantidad
        driver.findElement(quantity()).clear();
        // Esperar que el campo de cantidad esté visible y clickable
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantity()));
        wait.until(ExpectedConditions.elementToBeClickable(quantity()));

        driver.findElement(quantity()).sendKeys(quantity);

        // Click en agregar al carrito
        driver.findElement(btnAddCart()).click();
        // Esperar que el botón esté clickable
        wait.until(ExpectedConditions.elementToBeClickable(btnAddCart()));


        // Esperar confirmación (opcional)
        try {
        // Esperar confirmación
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage()));
        } catch (Exception e) {
            System.out.println("No se pudo confirmar el agregado al carrito");
        }
    }

    // Sobrecarga para cantidad como int
    public void agregarCarrito(int cantidad){
        agregarCarrito(String.valueOf(cantidad));
    }
}