package com.demoblaze.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait waits;

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.waits = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url){
        driver.get(url);
    }
}
