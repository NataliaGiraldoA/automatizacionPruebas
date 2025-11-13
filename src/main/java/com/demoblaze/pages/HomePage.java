package com.demoblaze.pages;

import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    WebDriverWaits waits;
    public HomePage(WebDriver driver) {
        super(driver);
        this.waits = new WebDriverWaits(driver);
    }

    private By category(String category) {
        return By.xpath("//ul[@class='nav navbar-nav']/li/a[normalize-space(text())='" + category + "']");
    }

    private By subcategory(String subcategory) {
        return By.xpath("//ul[@class='nav navbar-nav']/li//a[contains(normalize-space(),'" + subcategory + "')]");
    }

    private By btnAccount() {
        return By.xpath("//span[contains(text(),'Account')]");
    }

    private By btnRegister() {
        return By.xpath("//a[contains(@href,'route=account/register')]");
    }

    private By btnLogin() {
        return By.xpath("//a[contains(@href,'route=account/login')]");
    }

    private By searchBox() {
        return By.xpath("//*[@id='search']/input");
    }

    private By searchButton() {
        return By.xpath("//div[@id='search']//button");
    }

    private By btnLogout() {
        return By.xpath("//a[contains(@href,'route=account/logout')]");
    }


    public void clickAccount() {
        waits.waitForClickable(btnAccount());
        driver.findElement(btnAccount()).click();
    }

    public void clickRegister() {
        waits.waitForClickable(btnRegister());
        driver.findElement(btnRegister()).click();
    }

    public void clickLogin() {
        waits.waitForClickable(btnLogin());
        driver.findElement(btnLogin()).click();
    }
    public boolean isLoginDisplayed() {
        try {
            waits.waitForVisibility(btnLogin());
            return driver.findElement(btnLogin()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccountDisplayed() {
        try {
            waits.waitForVisibility(btnAccount());
            return driver.findElement(btnAccount()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void clickLogout() {
        waits.waitForClickable(btnAccount());
        driver.findElement(btnAccount()).click();
        waits.waitForClickable(btnLogout());
        driver.findElement(btnLogout()).click();
    }

    public boolean isUserLoggedIn() {
        try {
            return driver.findElements(btnLogout()).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void selectCategory(String category) {
        waits.waitForClickable(category(category));
        driver.findElement(category(category)).click();
    }

    public void selectSubCategory(String subcategory) {
        waits.waitForClickable(subcategory(subcategory));
        driver.findElement(subcategory(subcategory)).click();
    }

    // Búsqueda de Productos
    public void buscarProducto(String nombreProducto) {
        waits.waitForVisibility(searchBox());
        waits.waitForClickable(searchBox());
        driver.findElement(searchBox()).clear();
        driver.findElement(searchBox()).sendKeys(nombreProducto);
        waits.waitForClickable(searchButton());
        driver.findElement(searchButton()).click();
    }

}
