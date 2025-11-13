package com.demoblaze.test;

import com.demoblaze.pages.BasePage;
import com.demoblaze.utils.ResultLogger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OpenURLTest extends BaseTest{
    @BeforeClass
    public void initLogger() {
        ResultLogger.init("OpenURL");
    }

    @Test
    public void OpenUrl(){
        BasePage basePage = new BasePage(driver);

        basePage.navigateTo("https://opencart.abstracta.us/");
        boolean ok = driver.getCurrentUrl().contains("opencart.abstracta.us");
        ResultLogger.logOpenUrl("https://opencart.abstracta.us/", ok);
    }

}
