package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderUsers;
import com.demoblaze.utils.ResultLogger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegistroUsuariosTest extends BaseTest {
    @BeforeClass
    public void initLogger() {
        ResultLogger.init("RegistroUsuarios");
    }
    @DataProvider(name = "usersFromExcel")
    public Object[][] usersFromExcel() {
        return ExcelReaderUsers.readUsers(Constants.USERS);
    }

    @AfterMethod
    public void logout() {
        try {
            HomePage homePage = new HomePage(driver);
            // Si el usuario está logueado, hacer logout
            if (homePage.isUserLoggedIn()) {
                homePage.clickLogout();
                System.out.println("Logout hecho");
            }
        } catch (Exception e) {
            System.out.println("No se encontraba autenticado");
        }
    }

    @Test(dataProvider = "usersFromExcel")
    public void RegistroUsuarios(String first, String last, String email,
                                 String phone, String pass, String confirm, String expectedResult) {
        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage(driver);
        homePage.navigateTo(Constants.BASE_URL);
        homePage.clickAccount();
        homePage.clickRegister();

        if ("SUCCESS".equalsIgnoreCase(expectedResult)) {
            String[] parts = email.split("@");
            if (parts.length == 2) {
                email = parts[0] + "+" + System.currentTimeMillis() + "@" + parts[1];
            }
        }

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillRegistrationForm(first, last, email, phone, pass, confirm);
        registerPage.clickNewsletterNo();
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();
        registerPage.waitForAnyOutcome();

        boolean success = registerPage.isRegistrationSuccessful();
        boolean hasAlert = registerPage.ErrorMessage();
        boolean hasFieldErrors = registerPage.textDanger();

        ResultLogger.logRegistro(email, success);

        if ("SUCCESS".equalsIgnoreCase(expectedResult)) {
            softAssert.assertTrue(success,
                    "Debió registrarse correctamente, pero no lo hizo.");
        } else {
            softAssert.assertFalse(success, "No debía registrarse (marcado como FAILED).");
            softAssert.assertTrue(hasAlert || hasFieldErrors,
                    "Debía aparecer al menos un mensaje de error.");
            if (hasAlert) {
                softAssert.assertFalse(registerPage.getErrorMessage().isEmpty(), "La alerta superior está vacía.");
            }
        }

        softAssert.assertAll();
    }

}