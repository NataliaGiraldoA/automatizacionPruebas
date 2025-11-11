package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderUsers;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegistroUsuariosTest extends BaseTest{
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

        String mensajeErrorText = registerPage.getErrorMessage();

        boolean registroExitoso = registerPage.isRegistrationSuccessful();
        if (!registroExitoso) {
            boolean mensajeError = registerPage.ErrorMessage();
            softAssert.assertFalse(mensajeError, "El test falló, no se hizo registro de usuario - Mensaje de error: " + mensajeErrorText);
        }
        softAssert.assertTrue(registroExitoso,"Se realizó el registro del usuario");
        softAssert.assertAll();

    }
}
