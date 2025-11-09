package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderLogin;
import com.demoblaze.utils.ExcelReaderUsers;
import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterLoginUsersTest extends BaseTest{

    @DataProvider(name = "usersFromExcel")
    public Object[][] usersFromExcel() {
        return ExcelReaderUsers.readUsers(Constants.USERS);
    }

    @DataProvider(name = "loginFromExcel")
    public Object[][] loginFromExcel() {
        return ExcelReaderLogin.readLoginData(Constants.LOGIN_DATA);
    }

    @Test(dataProvider = "usersFromExcel", priority = 1)
    public void RegistroUsuarios(String first, String last, String email,
                                 String phone, String pass, String confirm) {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo(Constants.BASE_URL);

        homePage.clickAccount();
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillRegistrationForm(first, last, email, phone, pass, confirm);
        registerPage.clickNewsletterYes();
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();

        boolean registroExitoso = registerPage.isRegistrationSuccess();
        String mensajeExito = registerPage.getSuccessMessage();
        String mensajeError = registerPage.getErrorMessage();

        if (registroExitoso) {
            Assert.assertTrue(
                    mensajeExito.contains("Congratulations") ||
                            mensajeExito.contains("Success") ||
                            mensajeExito.contains("created"),
                    "Registro exitoso pero mensaje no esperado. Mensaje: " + mensajeExito
            );
        } else if (!mensajeError.isEmpty()) {
            System.out.println("Registro falló como se esperaba para: " + email + ". Error: " + mensajeError);
        } else {
            Assert.fail("No se pudo determinar el resultado del registro para: " + email);
        }
    }



    @Test(dataProvider = "loginFromExcel", priority = 2)
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
                    "FALLO: Se esperaba un login éxitoso, pero no se encontró el botón Logout. " +
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