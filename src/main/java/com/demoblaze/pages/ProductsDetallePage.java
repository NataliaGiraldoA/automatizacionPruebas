package com.demoblaze.pages;

import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsDetallePage extends BasePage{

    WebDriverWaits waits;

    public ProductsDetallePage(WebDriver driver){
        super(driver);
        this.waits = new WebDriverWaits(driver);
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
        waits.waitForVisibility(quantity());
        waits.waitForClickable(quantity());
        driver.findElement(quantity()).clear();

        driver.findElement(quantity()).sendKeys(quantity);
        waits.waitForClickable(btnAddCart());
        driver.findElement(btnAddCart()).click();


        try {
            waits.fluentWaitForSuccessMessage(successMessage());
        } catch (Exception e) {
            System.out.println("No se pudo confirmar el agregado al carrito");
        }
    }

    // Sobrecarga para cantidad como int
    public void agregarCarrito(int cantidad){
        agregarCarrito(String.valueOf(cantidad));
    }

    // Método para verificar si el mensaje de éxito se muestra
    public boolean isSuccessMessageDisplayed(){
        try {
            waits.fluentWaitForSuccessMessage(successMessage());
            return driver.findElement(successMessage()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Método para obtener el texto del mensaje de éxito
    public String getSuccessMessageText(){
        try {
            waits.waitForVisibility(successMessage());
            return driver.findElement(successMessage()).getText();
        } catch (Exception e) {
            return "";
        }
    }
}