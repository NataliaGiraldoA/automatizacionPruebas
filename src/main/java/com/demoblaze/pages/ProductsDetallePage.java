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
        driver.findElement(quantity()).clear();
        waits.waitForClickable(quantity());

        driver.findElement(quantity()).sendKeys(quantity);

        driver.findElement(btnAddCart()).click();

        waits.waitForClickable(btnAddCart());
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
}