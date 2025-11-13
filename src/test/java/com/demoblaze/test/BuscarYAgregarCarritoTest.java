package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductsPage;
import com.demoblaze.pages.ProductsDetallePage;
import com.demoblaze.pages.CartPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderCart;
import com.demoblaze.utils.ResultLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class BuscarYAgregarCarritoTest extends BaseTest {
    @BeforeClass
    public void initLogger() {
        ResultLogger.init("BuscarYAgregarCarrito");
    }


    @DataProvider(name = "productosFromExcel")
    public Object[][] productosFromExcel() {
        return ExcelReaderCart.readProductos(Constants.PRODUCTOS_BUSQUEDA);
    }


    @Test(dataProvider = "productosFromExcel")
    public void testBuscarYAgregarProductoAlCarrito(String categoria, String subCategoria,
                                                    String nombreProducto, int cantidad, String expectedResult) {

        SoftAssert softAssert = new SoftAssert();

        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage detallePage = new ProductsDetallePage(driver);
        CartPage cartPage = new CartPage(driver);

        homePage.navigateTo(Constants.BASE_URL);
        homePage.buscarProducto(nombreProducto);

        Assert.assertTrue(productsPage.isProductDisplayed(nombreProducto),
                "El producto '" + nombreProducto + "' no aparece en los resultados de búsqueda");

        productsPage.selectProduct(nombreProducto);
        detallePage.agregarCarrito(cantidad);

        cartPage.irAlCarrito();

        boolean productInCart = cartPage.isProductoEnCarrito(nombreProducto);
        if("SUCCESS".equalsIgnoreCase(expectedResult)){
            softAssert.assertTrue(productInCart,
                    "El producto '" + nombreProducto + "' debería estar en el carrito");

            int cantidadEnCarrito = cartPage.getCantidadProducto(nombreProducto);
            softAssert.assertEquals(cantidadEnCarrito, cantidad,
                    "La cantidad del producto '" + nombreProducto + "' es incorrecta. " +
                            "Esperada: " + cantidad + ", Encontrada: " + cantidadEnCarrito);



        } else {
            softAssert.assertFalse(productInCart,
                    "El producto '" + nombreProducto + "' no debería estar en el carrito, pero sí está");
            softAssert.assertAll();
        }
        ResultLogger.logProducto(categoria, subCategoria, nombreProducto, cantidad,productInCart);
    }


}