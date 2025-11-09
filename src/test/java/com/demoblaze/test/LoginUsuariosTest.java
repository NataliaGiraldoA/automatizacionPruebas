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

public class LoginUsuariosTest extends BaseTest {

    @DataProvider(name = "loginFromExcel")
    public Object[][] loginFromExcel() {
        return ExcelReaderLogin.readLoginData(Constants.LOGIN_DATA);
    }

    @Test(dataProvider = "loginFromExcel")
    public void LoginUsuarios(String email, String password, String expectedResult) {

        HomePage homePage = new HomePage(driver);
        WebDriverWaits waits = new WebDriverWaits(driver);

        homePage.navigateTo(Constants.BASE_URL);
        homePage.clickAccount();
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);

        // Campos
        waits.waitForVisibility(By.id("input-email"));
        loginPage.enterEmail(email);

        waits.waitForVisibility(By.id("input-password"));
        loginPage.enterPassword(password);

        waits.waitForClickable(By.cssSelector("input[value='Login']"));
        loginPage.clickLogin();

        // Estado después del intento
        boolean success = loginPage.isLoginSuccessful();
        boolean error = loginPage.isErrorDisplayed();
        String errorText = loginPage.getErrorText();
        String currentUrl = driver.getCurrentUrl();

        // =============================
        // VALIDACIÓN FINAL
        // =============================

        if (expectedResult.equalsIgnoreCase("SUCCESS")) {

            Assert.assertTrue(
                    success,
                    "FALLO: Se esperaba un login exitoso, pero no se encontró el botón Logout. " +
                            "Email=" + email + " | URL=" + currentUrl + " | Error='" + errorText + "'"
            );

        } else { // FAIL esperado

            Assert.assertFalse(
                    success,
                    "FALLO CRÍTICO: Se esperaba fallo pero el sistema permitió el login. " +
                            "Email=" + email + " | URL=" + currentUrl
            );

            Assert.assertTrue(
                    error,
                    "ERROR: El login falló como se esperaba, pero NO se mostró mensaje de advertencia. "
                            + "Email=" + email + " | URL=" + currentUrl
            );

            Assert.assertTrue(
                    errorText.contains("Warning"),
                    "ERROR: El mensaje de error no contiene 'Warning'. Texto recibido: " + errorText
            );
        }
    }
}
