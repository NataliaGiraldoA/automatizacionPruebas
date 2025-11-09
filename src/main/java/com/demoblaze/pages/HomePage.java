package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ==============================
    // Selectores
    // ==============================

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

    // ==============================
    // Métodos / Acciones
    // ==============================

    public void clickAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(btnAccount()));
        driver.findElement(btnAccount()).click();
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(btnRegister()));
        driver.findElement(btnRegister()).click();
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogin()));
        driver.findElement(btnLogin()).click();
    }

    public void selectCategory(String category) {
        wait.until(ExpectedConditions.elementToBeClickable(category(category)));
        driver.findElement(category(category)).click();
    }

    public void selectSubCategory(String subcategory) {
        wait.until(ExpectedConditions.elementToBeClickable(subcategory(subcategory)));
        driver.findElement(subcategory(subcategory)).click();
    }

    // ==============================
    // Búsqueda de Productos
    // ==============================

    public void buscarProducto(String nombreProducto) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox()));
        wait.until(ExpectedConditions.elementToBeClickable(searchBox()));
        driver.findElement(searchBox()).clear();
        driver.findElement(searchBox()).sendKeys(nombreProducto);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton()));
        driver.findElement(searchButton()).click();
    }

    // ==============================
    // Navegación por categorías
    // ==============================

    public void navegarPorCategorias(String categoria, String subcategoria) {
        if (categoria != null && !categoria.isEmpty()) {
            selectCategory(categoria);
        }
        if (subcategoria != null && !subcategoria.isEmpty()) {
            selectSubCategory(subcategoria);
        }
    }
}
