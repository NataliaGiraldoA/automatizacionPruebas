package com.demoblaze.pages;

import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductsPage extends BasePage{
    private WebDriverWaits waits;
    public ProductsPage(WebDriver driver){
        super(driver);
        this.waits = new WebDriverWaits(driver);
    }

    // Crear los elementos con los que vamos a interactuar en la pag
    private By product(String product){
        return By.xpath("//a[contains(text(),'"+product+"')]");
    }

    //Metodo
    public void selectProduct(String product){
        waits.waitForVisibility(product (product));
        waits.waitForClickable(product (product));
        driver.findElement(product(product)).click();
    }
    public boolean isProductDisplayed(String product){
        try {
            waits.waitForVisibility(product(product));
            WebElement productElement = driver.findElement(product(product));
            return productElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
