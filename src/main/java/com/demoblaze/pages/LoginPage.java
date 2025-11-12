package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.demoblaze.utils.WebDriverWaits;
public class LoginPage extends BasePage {
    private WebDriverWaits waits;
    public LoginPage(WebDriver driver) {
        super(driver);
        this.waits = new WebDriverWaits(driver);
    }

    private By emailInput() { return By.id("input-email"); }
    private By passwordInput() { return By.id("input-password"); }
    private By loginButton() { return By.cssSelector("input[value='Login']"); }
    private By alertMessage() { return By.cssSelector(".alert-danger"); }

    public void enterEmail(String email) {
        waits.waitForVisibility(emailInput());
        driver.findElement(emailInput()).clear();
        driver.findElement(emailInput()).sendKeys(email);
    }

    public void enterPassword(String password) {
        waits.waitForVisibility(passwordInput());
        driver.findElement(passwordInput()).clear();
        driver.findElement(passwordInput()).sendKeys(password);
    }

    public void clickLogin() {
        waits.waitForClickable(loginButton());
        driver.findElement(loginButton()).click();
    }

    public boolean isLoginSuccessful(){
        try{
            waits.fluentWaitForPageRedirect(By.cssSelector("#content > h2:nth-child(1)"));
            WebElement accountMessage = driver.findElement(By.cssSelector("#content > h2:nth-child(1)"));
            return accountMessage.isDisplayed();
        } catch (Exception e){
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            waits.fluentWaitForErrorAlert(alertMessage());
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
