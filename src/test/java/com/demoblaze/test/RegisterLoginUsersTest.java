package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderLogin;
import com.demoblaze.utils.ExcelReaderUsers;
import com.demoblaze.utils.WebDriverWaits;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegisterLoginUsersTest extends BaseTest{

    @DataProvider(name = "usersFromExcel")
    public Object[][] usersFromExcel() {
        return ExcelReaderUsers.readUsers(Constants.USERS);
    }

    @DataProvider(name = "loginFromExcel")
    public Object[][] loginFromExcel() {
        return ExcelReaderLogin.readLoginData(Constants.LOGIN_DATA);
    }

    @AfterMethod
    public void logout() {
        try {
            HomePage homePage = new HomePage(driver);
            if (homePage.isUserLoggedIn()) {
                homePage.clickLogout();
                System.out.println("Logout exitoso");
            }
        } catch (Exception e) {
            System.out.println("ya estaba logout");
        }
    }

    @Test(dataProvider = "usersFromExcel", priority = 1)
    public void RegistroUsuarios(String first, String last, String email,
                                 String phone, String pass, String confirm) {
        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage(driver);
        homePage.navigateTo(Constants.BASE_URL);

        homePage.clickAccount();
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillRegistrationForm(first, last, email, phone, pass, confirm);
        registerPage.clickNewsletterNo();
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();

        boolean registroExitoso = registerPage.isRegistrationSuccessful();
        if (!registroExitoso) {
            boolean mensajeError = registerPage.ErrorMessage();
            softAssert.assertFalse(mensajeError, "El test falló, no se hizo registro de usuario");
        }
        softAssert.assertTrue(registroExitoso,"Se realizó el registro del usuario");
        softAssert.assertAll();

    }

    @Test(dataProvider = "loginFromExcel", priority = 2)
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
            System.out.println("✓ Login falló como se esperaba para: " + email);
        }

        softAssert.assertAll();
    }
}
