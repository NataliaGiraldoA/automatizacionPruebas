package com.demoblaze.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverWaits {

    private WebDriver driver;
    private WebDriverWait wait;

    public WebDriverWaits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForPresence(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void waitForAnyOf(By... locators) {
        ExpectedCondition<?>[] conditions = new ExpectedCondition[locators.length];
        for (int i = 0; i < locators.length; i++) {
            conditions[i] = ExpectedConditions.visibilityOfElementLocated(locators[i]);
        }
        wait.until(ExpectedConditions.or(conditions));
    }


    public void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    // FluentWait para mensaje de exito
    public void fluentWaitForMessage(By locator) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("El mensaje no apareció en el tiempo establecido");

        fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public void fluentWaitForPageRedirect(By locator) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("La página de cuenta no cargó después del login");

        fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

}
