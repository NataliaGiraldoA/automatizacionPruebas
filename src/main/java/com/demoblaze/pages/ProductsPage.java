package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage{

    public ProductsPage(WebDriver driver){
        super(driver);
    }

    //crear los elementos con los que vamos a interactuar en la pag
    private By product(String product){
        return By.xpath("//a[contains(text(),'"+product+"')]");
    }

    //Metodo
    public void selectProduct(String product){
        waits.until(ExpectedConditions.visibilityOfElementLocated(product(product)));
        waits.until(ExpectedConditions.elementToBeClickable(product(product)));
        driver.findElement(product(product)).click();
    }
    private By noProductsMessage(){
        return By.xpath("//p[contains(text(),'There is no product')]");
    }

    public boolean isProductDisplayed(String product){
        try {
            waits.until(ExpectedConditions.visibilityOfElementLocated(product(product)));
            WebElement productElement = driver.findElement(product(product));
            return productElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Metodo para verificar si hay resultados
    public boolean hayResultados(){
        try {
            return !driver.findElement(noProductsMessage()).isDisplayed();
        } catch (Exception e) {
            return true; // Si no encuentra el mensaje de "no hay productos", significa que hay resultados
        }
    }
}
