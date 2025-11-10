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


        waits.waitForVisibility(By.id("input-email"));
        loginPage.enterEmail(email);

        waits.waitForVisibility(By.id("input-password"));
        loginPage.enterPassword(password);

        waits.waitForClickable(By.cssSelector("input[value='Login']"));
        loginPage.clickLogin();


        boolean success = loginPage.isLoginSuccessful();
        boolean errorVisible = loginPage.isErrorDisplayed();
        String errorText = loginPage.getErrorText();
        String url = driver.getCurrentUrl();


        if (expectedResult.equalsIgnoreCase("SUCCESS")) {

            Assert.assertTrue(
                    success,
                    "ERROR: Se esperaba un login exitoso, pero no se redirigió a la página 'My Account'.\n" +
                            "Email: " + email + "\n" +
                            "URL actual: " + url + "\n" +
                            "Mensaje de error (si lo hubo): " + errorText
            );

        } else {

            Assert.assertFalse(
                    success,
                    "ERROR: El login debía fallar, pero se ingresó correctamente.\n" +
                            "Email: " + email + "\n" +
                            "URL actual: " + url
            );

            if (errorVisible) {

                Assert.assertTrue(
                        errorText.contains("Warning"),
                        "ERROR: Se esperaba que el mensaje de error contuviera 'Warning'.\n" +
                                "Mensaje recibido: " + errorText
                );

            } else {
                Assert.assertTrue(
                        url.contains("route=account/login"),
                        "ERROR: No se detectó alerta y el navegador no permaneció en la página de login.\n" +
                                "Email: " + email + "\n" +
                                "URL actual: " + url
                );
            }
        }
    }
}
