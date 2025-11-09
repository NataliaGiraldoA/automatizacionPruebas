package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.RegisterPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderUsers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegistroUsuariosTest extends BaseTest{
    @DataProvider(name = "usersFromExcel")
    public Object[][] usersFromExcel() {
        return ExcelReaderUsers.readUsers(Constants.USERS);
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

        Assert.assertTrue(registerPage.isRegistrationSuccess(),
            "El registro fallo para el usuario: " + email);

        String successMessage = registerPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Congratulations") || successMessage.contains("Success"),
            "No aparecio el mensaje de exito esperado");
    }
}
