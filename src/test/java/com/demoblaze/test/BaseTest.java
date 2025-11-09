package com.demoblaze.test;

import com.demoblaze.utils.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    protected WebDriver driver;

    @BeforeTest
    public void setup(){
        //Configurar el Driver
        WebDriverManager.edgedriver().setup();

        //Crear una instancia de WebDriver para Edge
        driver = new EdgeDriver();
        driver.manage().window().maximize();

        // Navegar a la URL base
        driver.get(Constants.BASE_URL);
    }

    @AfterTest
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}