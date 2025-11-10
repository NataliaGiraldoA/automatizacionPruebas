package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderUsers;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
                System.out.println("✓ Logout exitoso");
            }
        } catch (Exception e) {
            System.out.println("No fue necesario hacer logout o ya estaba deslogueado");
        }
    }

    @Test(dataProvider = "usersFromExcel")
    public void RegistroUsuarios(String first, String last, String email,
                                 String phone, String pass, String confirm){
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo(Constants.BASE_URL);

        homePage.clickAccount();
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillRegistrationForm(first, last, email, phone, pass, confirm);
        registerPage.clickNewsletterYes();
        registerPage.clickPrivacyPolicy();
        registerPage.clickContinue();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
            "El registro fallo para el usuario: " + email);

        /*String successMessage = registerPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Congratulations") || successMessage.contains("Success"),
            "No aparecio el mensaje de exito esperado");*/
    }
}
