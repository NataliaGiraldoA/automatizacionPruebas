package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage{

    public RegisterPage(WebDriver driver){
        super(driver);
    }

    private By firstName(){
        return By.id("input-firstname");
    }
    private By lastName(){
        return By.id("input-lastname");
    }
    private By telephone(){
        return By.id("input-telephone");
    }
    private By email(){
        return By.id("input-email");
    }
    private By password(){
        return By.id("input-password");
    }
    private By confirmPassword(){
        return By.id("input-confirm");
    }
    private final By newsletterYes = By.cssSelector("input[name='newsletter'][value='1']");
    private final By newsletterNo  = By.cssSelector("input[name='newsletter'][value='0']");
    private final By privacyPolicy = By.name("agree");
    private final By continueBtn   = By.cssSelector("input[type='submit'][value='Continue']");

    private void fillField(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        if (element != null) {
            element.clear();
            element.sendKeys(value);
        }
    }

    public void enterFirstName(String firstName) {
        fillField(firstName(), firstName);
    }

    public void enterLastName(String lastName) {
        fillField(lastName(), lastName);
    }

    public void enterEmail(String email) {
        fillField(email(), email);
    }

    public void enterPhone(String phone) {
        fillField(telephone(), phone);
    }

    public void enterPassword(String password) {
        fillField(password(), password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        fillField(confirmPassword(), confirmPassword);
    }

    public void fillRegistrationForm(String firstName, String lastName, String email,
                                     String phone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        wait.until(ExpectedConditions.elementToBeClickable(newsletterYes));
        enterConfirmPassword(confirmPassword);
    }
    public void clickNewsletterYes(){
        wait.until(ExpectedConditions.elementToBeClickable(newsletterYes));
        driver.findElement(newsletterYes).click();
    }

    public void clickNewsletterNo(){
        wait.until(ExpectedConditions.elementToBeClickable(newsletterNo));
        driver.findElement(newsletterNo).click();
    }

    public void clickPrivacyPolicy(){
        wait.until(ExpectedConditions.elementToBeClickable(privacyPolicy));
        driver.findElement(privacyPolicy).click();
    }
    public void clickContinue(){
        wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
        driver.findElement(continueBtn).click();
    }

    public boolean isRegistrationSuccess() {
        try {
            By successMessage = By.cssSelector(".alert.alert-success");
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return driver.findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        try {
            By successMessage = By.cssSelector(".alert.alert-success");
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return driver.findElement(successMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            By errorMessage = By.cssSelector(".alert.alert-danger");
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return driver.findElement(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

}
