package com.demoblaze.pages;

import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.xpath;

public class CartPage extends BasePage {

    private WebDriverWaits waits;

    public CartPage(WebDriver driver) {
        super(driver);
        this.waits = new WebDriverWaits(driver);
    }

    // Elementos
    private By cartLink() {
        return xpath("//a[contains(@href, 'checkout/cart')]");
    }
    private By productInCart(String nombre){
        return xpath("//table[contains(@class,'table') and contains(@class,'table-bordered')]//td[contains(@class,'text-left')]//a[contains(text(),'"+nombre+"')]");
    }

    private By quantity(String nombre){
        return By.xpath("//table[.//thead]//tbody//tr[.//a[contains(text(), '" + nombre + "')]]//input[contains(@name, 'quantity')]");
    }
/*
    private By quantity(){
        return By.xpath("//div[@class='input-group btn-block']//input[@class='form-control']");
    }
*/
    public void irAlCarrito() {
        waits.waitForClickable(cartLink());
        driver.findElement(cartLink()).click();
        waits.waitForUrlContains("checkout/cart");
    }

    public boolean isProductoEnCarrito(String nombre) {
        try {
            waits.waitForVisibility(productInCart(nombre));
            return driver.findElement(productInCart(nombre)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getCantidadProducto(String nombre) {
        try {
            WebElement input = driver.findElement(quantity(nombre));
            return Integer.parseInt(input.getAttribute("value"));
        } catch (Exception e) {
            return 0;
        }
    }




}