package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private By emailInput() { return By.id("input-email"); }
    private By passwordInput() { return By.id("input-password"); }
    private By loginButton() { return By.cssSelector("input[value='Login']"); }
    private By alertMessage() { return By.cssSelector(".alert-danger"); }

    public void enterEmail(String email) {
        driver.findElement(emailInput()).clear();
        driver.findElement(emailInput()).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput()).clear();
        driver.findElement(passwordInput()).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton()).click();
    }


    public boolean isLoginSuccessful() {
        try {
            WebElement accountHeader = driver.findElement(By.xpath("//h2[text()='My Account']"));
            return accountHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(alertMessage()).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorText() {
        try {
            return driver.findElement(alertMessage()).getText();
        } catch (Exception e) {
            return "";
        }
    }
}