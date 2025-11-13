package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderLogin;
import com.demoblaze.utils.ResultLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginUsuariosTest extends BaseTest {
    @BeforeClass
    public void initLogger() {
        ResultLogger.init("LoginUsuarios");
    }

    @DataProvider(name = "loginFromExcel")
    public Object[][] loginFromExcel() {
        return ExcelReaderLogin.readLoginData(Constants.LOGIN_DATA);
    }

    @Test(dataProvider = "loginFromExcel")
    public void LoginUsuarios(String email, String password, String expectedResult) {
        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage(driver);
        homePage.navigateTo(Constants.BASE_URL);
        Assert.assertTrue(homePage.isAccountDisplayed(), "No se pudo ingresar al menu de account");
        homePage.clickAccount();
        Assert.assertTrue(homePage.isLoginDisplayed(), "No se pudo ingresar al login");
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        boolean loginExitoso = loginPage.isLoginSuccessful();

        ResultLogger.logLogin(email, loginExitoso);

        if (expectedResult.equalsIgnoreCase("SUCCESS")) {
            softAssert.assertTrue(
                    loginExitoso,
                    "Login tuvo que haber sido exitoso pero falló" + email
            );

        } else {
                softAssert.assertFalse(loginExitoso, "Login debió fallar para: " + email);
                if (!loginExitoso) {
                    boolean errorDisplayed = loginPage.isErrorDisplayed();
                    softAssert.assertTrue(errorDisplayed,
                            "Mensaje de error no visible");
                }

            }
        softAssert.assertAll();
    }


}
