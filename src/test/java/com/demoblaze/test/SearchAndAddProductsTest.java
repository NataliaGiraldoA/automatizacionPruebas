package com.demoblaze.test;

import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductsDetallePage;
import com.demoblaze.pages.ProductsPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderCart;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SearchAndAddProductsTest extends BaseTest{
    @Test
    public void SearchAndAddProducts(){
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage detallePage = new ProductsDetallePage(driver);
        CartPage cartPage = new CartPage(driver);

        Object[][] productos = ExcelReaderCart.readProductos(Constants.AllProductsCart);
        SoftAssert softAssert = new SoftAssert();

        for(Object[] producto : productos) {
            String nombreProducto = (String) producto[2];
            int cantidad = (int) producto[3];
            String expectedResult = (String) producto[4];

            // Solo agregar productos que deberían tener éxito
            if(!"SUCCESS".equalsIgnoreCase(expectedResult)) {
                System.out.println("No se está agregando : " + nombreProducto + "debido a que el test debe de fallar (expectedResult: " + expectedResult + ")");
                continue;
            }

            try {
                homePage.navigateTo(Constants.BASE_URL);
                homePage.buscarProducto(nombreProducto);

                boolean hayResultados = productsPage.hayResultados();

                if(hayResultados) {
                    boolean isProductDisplayed = productsPage.isProductDisplayed(nombreProducto);
                    softAssert.assertTrue(isProductDisplayed,
                        "El producto '" + nombreProducto + "' NO se encontró en los resultados de búsqueda");

                    if(isProductDisplayed) {
                        productsPage.selectProduct(nombreProducto);
                        detallePage.agregarCarrito(cantidad);
                        /*
                        boolean successMessageDisplayed = detallePage.isSuccessMessageDisplayed();
                        softAssert.assertTrue(successMessageDisplayed,
                            "No se mostró el mensaje de éxito al agregar '" + nombreProducto + "' al carrito");
                        */
                        
                    }
                } else {
                    softAssert.assertTrue(hayResultados,
                            "La búsqueda de '" + nombreProducto + "' NO devolvió resultados");
                }
            } catch (Exception e) {
                softAssert.fail("Error al procesar producto '" + nombreProducto + "': " + e.getMessage());
            }
        }

        cartPage.irAlCarrito();


        //Verificar que todos los productos estén en el carrito
        int productosEsperados = 0;
        int productosEncontrados = 0;

        for(Object[] producto : productos) {
            String nombreProducto = (String) producto[2];
            int cantidadEsperada = (int) producto[3];
            String expectedResult = (String) producto[4];

            if("SUCCESS".equalsIgnoreCase(expectedResult)) {
                productosEsperados++;
                boolean enCarrito = cartPage.isProductoEnCarrito(nombreProducto);

                softAssert.assertTrue(enCarrito,
                        "El producto '" + nombreProducto + "' NO se encuentra en el carrito");

                if(enCarrito) {
                    productosEncontrados++;

                    int cantidadEnCarrito = cartPage.getCantidadProducto(nombreProducto);
                    softAssert.assertEquals(cantidadEnCarrito, cantidadEsperada,
                        "La cantidad del producto '" + nombreProducto + "' en el carrito NO coincide. " +
                        "Esperada: " + cantidadEsperada + ", Encontrada: " + cantidadEnCarrito);

                } else {
                    softAssert.assertTrue(enCarrito, "El producto '" + nombreProducto + "' NO se encuentra en el carrito");
                }
            }
        }
        softAssert.assertEquals(productosEncontrados, productosEsperados,
                "No todos los productos esperados están en el carrito. " +
                "Esperados: " + productosEsperados + ", Encontrados: " + productosEncontrados);

        softAssert.assertAll();

    }
}
