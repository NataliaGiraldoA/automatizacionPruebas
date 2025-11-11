package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderLogin;
import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginUsuariosTest extends BaseTest {

    @DataProvider(name = "loginFromExcel")
    public Object[][] loginFromExcel() {
        return ExcelReaderLogin.readLoginData(Constants.LOGIN_DATA);
    }

    @Test(dataProvider = "loginFromExcel")
    public void LoginUsuarios(String email, String password, String expectedResult) {
        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage(driver);
        WebDriverWaits waits = new WebDriverWaits(driver);

        homePage.navigateTo(Constants.BASE_URL);
        homePage.clickAccount();
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);

        waits.waitForVisibility(By.id("input-email"));
        loginPage.enterEmail(email);

        waits.waitForVisibility(By.id("input-password"));
        loginPage.enterPassword(password);

        waits.waitForClickable(By.cssSelector("input[value='Login']"));
        loginPage.clickLogin();

        boolean errorVisible = loginPage.isErrorDisplayed();
        boolean success = loginPage.isLoginSuccessful();
        String errorText = loginPage.getErrorText();

        String url = driver.getCurrentUrl();

        if (expectedResult.equalsIgnoreCase("SUCCESS")) {
            softAssert.assertTrue(
                    success,
                    "ERROR: Se esperaba login exitoso pero no se redirigió a la página 'My Account'. " +
                            "URL actual: " + url + " | Mensaje de error: " + errorText
            );
            System.out.println("✓ Login exitoso para: " + email);
        } else {
            softAssert.assertFalse(
                    success,
                    "FALLO: El login debía fallar pero redirigió a 'My Account'. URL: " + url
            );

            softAssert.assertTrue(
                    errorVisible,
                    "ERROR: Se esperaba un mensaje de error pero no apareció. Email: " + email
            );

            softAssert.assertTrue(
                    errorText.contains("Warning"),
                    "ERROR: El mensaje de error NO contiene 'Warning'. Mensaje recibido: " + errorText
            );
        }

        softAssert.assertAll();
    }
}
