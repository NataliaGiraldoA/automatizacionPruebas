package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductsPage;
import com.demoblaze.pages.ProductsDetallePage;
import com.demoblaze.pages.CartPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderCart;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class BuscarYAgregarCarritoTest extends BaseTest {



    @DataProvider(name = "productosFromExcel")
    public Object[][] productosFromExcel() {
        return ExcelReaderCart.readProductos(Constants.PRODUCTOS_BUSQUEDA);
    }

    @Test(dataProvider = "productosFromExcel")
    public void testBuscarYAgregarProductoAlCarrito(String categoria, String subCategoria,
                                                    String nombreProducto, int cantidad, String expectedResult) {

        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage detallePage = new ProductsDetallePage(driver);
        CartPage cartPage = new CartPage(driver);

        homePage.navigateTo(Constants.BASE_URL);
        homePage.buscarProducto(nombreProducto);
        SoftAssert softAssert = new SoftAssert();

        Assert.assertTrue(productsPage.isProductDisplayed(nombreProducto),
                "El producto '" + nombreProducto + "' no aparece en los resultados de búsqueda");

        productsPage.selectProduct(nombreProducto);
        detallePage.agregarCarrito(cantidad);
        cartPage.irAlCarrito();
        boolean productInCart = cartPage.isProductoEnCarrito(nombreProducto);
        if("SUCCESS".equalsIgnoreCase(expectedResult)){
            softAssert.assertTrue(productInCart,
                    "Debió registrarse correctamente, pero no lo hizo.");
        } else {
            softAssert.assertFalse(productInCart,
                    "El producto '" + nombreProducto + "' no debería estar en el carrito");

        }
        softAssert.assertAll();
    }


}