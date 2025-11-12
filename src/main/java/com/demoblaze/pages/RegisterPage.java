package com.demoblaze.pages;

import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage{
    WebDriverWaits waits;
    public RegisterPage(WebDriver driver){
        super(driver);
        this.waits = new WebDriverWaits(driver);
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
    private final By fieldErrors    = By.cssSelector(".text-danger");
    private final By successMessage = By.xpath("//*[@id='content']//p[contains(text(),'Congratulations')]");
    private final By errorAlert     = By.cssSelector(".alert.alert-danger");


    private void fillField(By locator, String value) {
        waits.waitForVisibility(locator);
        waits.waitForClickable(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
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
        enterConfirmPassword(confirmPassword);
    }
    public void clickNewsletterYes(){
        waits.waitForClickable(newsletterYes);
        driver.findElement(newsletterYes).click();
    }

    public void clickNewsletterNo(){
        waits.waitForClickable(newsletterNo);
        driver.findElement(newsletterNo).click();
    }

    public void clickPrivacyPolicy(){
        waits.waitForClickable(privacyPolicy);
        driver.findElement(privacyPolicy).click();
    }
    public void clickContinue(){
        waits.waitForClickable(continueBtn);
        driver.findElement(continueBtn).click();
    }
    public void waitForAnyOutcome() {
        waits.waitForAnyOf(successMessage, errorAlert, fieldErrors);
    }

    public boolean textDanger(){
        try{
            waits.waitForVisibility(fieldErrors);
            return driver.findElement(fieldErrors).isDisplayed();
        } catch (Exception e){
            return false;
        }
    }

    public String getFieldErrors() {
        try {
            waits.waitForVisibility(fieldErrors);
            return driver.findElement(fieldErrors).getText();
        } catch (Exception e) {
            return "";
        }
    }
    public boolean isRegistrationSuccessful() {
        try {
            waits.waitForVisibility(successMessage);
            return driver.findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ErrorMessage() {
        try {
            waits.waitForVisibility(errorAlert);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    public String getErrorMessage() {
        waits.waitForVisibility(errorAlert);
        return driver.findElement(errorAlert).getText();
    }

}
