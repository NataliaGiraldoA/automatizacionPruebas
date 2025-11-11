package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderLogin;
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
        homePage.navigateTo(Constants.BASE_URL);
        homePage.clickAccount();
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        if (expectedResult.equalsIgnoreCase("SUCCESS")) {
            boolean loginExitoso = loginPage.isLoginSuccessful();
            softAssert.assertTrue(
                    loginExitoso,
                    "Login tuvo que haber sido exitoso pero falló" + email
            );


        } else {
            boolean loginExitoso = loginPage.isLoginSuccessful();
            if(!loginExitoso){
                boolean errorVisible = loginPage.isErrorDisplayed();
                String errorText = loginPage.getErrorText();

                softAssert.assertTrue(
                        errorVisible,
                        "Debió aparecer mensaje de error para: " + email
                );

                softAssert.assertFalse(
                        errorText.isEmpty(),
                        "El mensaje de error está vacío para: " + email
                );

                softAssert.assertTrue(
                        errorText.toLowerCase().contains("warning") ||
                                errorText.toLowerCase().contains("incorrect"),
                        "Mensaje de error inválido: '" + errorText + "'"
                );
            }
            softAssert.assertFalse(
                    loginExitoso,
                    "Login debió fallar pero fue exitoso para: " + email
            );

            }
        softAssert.assertAll();
    }


}
