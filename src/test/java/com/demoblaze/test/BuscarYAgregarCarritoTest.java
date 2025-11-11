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

public class BuscarYAgregarCarritoTest extends BaseTest {

    private static final int CANTIDAD_MAXIMA = 99;

    @DataProvider(name = "productosFromExcel")
    public Object[][] productosFromExcel() {
        return ExcelReaderCart.readProductos(null);
    }

    @Test(dataProvider = "productosFromExcel")
    public void testBuscarYAgregarProductoAlCarrito(String categoria, String subCategoria,
                                                    String nombreProducto, int cantidad) {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage detallePage = new ProductsDetallePage(driver);
        CartPage cartPage = new CartPage(driver);

        System.out.println("\n=== Prueba de búsqueda y agregado al carrito ===");
        System.out.println("Categoría: " + categoria);
        System.out.println("SubCategoría: " + subCategoria);
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Cantidad: " + cantidad);

        // === VALIDACIONES DE CANTIDAD ===

        // Assert: La cantidad NO debe ser cero
        Assert.assertTrue(cantidad != 0,
                "La cantidad no puede ser 0. Cantidad recibida: " + cantidad);

        // Assert: La cantidad NO debe ser negativa
        Assert.assertTrue(cantidad > 0,
                "La cantidad debe ser mayor a 0 (no se permiten valores negativos). Cantidad recibida: " + cantidad);

        // Assert: La cantidad NO debe exceder el máximo
        Assert.assertTrue(cantidad <= CANTIDAD_MAXIMA,
                "La cantidad no debe exceder " + CANTIDAD_MAXIMA + ". Cantidad recibida: " + cantidad);

        // Assert: La cantidad debe estar en el rango válido (1 a CANTIDAD_MAXIMA)
        Assert.assertTrue(cantidad > 0 && cantidad <= CANTIDAD_MAXIMA,
                "La cantidad debe estar en el rango válido (1-" + CANTIDAD_MAXIMA + "). Cantidad recibida: " + cantidad);

        System.out.println("Validaciones de cantidad superadas");

        // === FLUJO PRINCIPAL ===

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Buscar el producto
        homePage.buscarProducto(nombreProducto);

        // Verificar que el producto aparece en los resultados
        Assert.assertTrue(productsPage.isProductDisplayed(nombreProducto),
                "El producto '" + nombreProducto + "' no aparece en los resultados de búsqueda");
        System.out.println("Producto encontrado en resultados");

        // Seleccionar el producto
        productsPage.selectProduct(nombreProducto);
        System.out.println("Producto seleccionado");

        // Agregar al carrito
        detallePage.agregarCarrito(cantidad);
        System.out.println("Agregado al carrito (cantidad: " + cantidad + ")");

        // Ir al carrito
        cartPage.irAlCarrito();
        System.out.println("Navegado al carrito");

        // === VALIDACIONES EN EL CARRITO ===

        // Assert: El producto debe estar en el carrito
        Assert.assertTrue(cartPage.isProductoEnCarrito(nombreProducto),
                "El producto '" + nombreProducto + "' no está en el carrito");
        System.out.println("Producto encontrado en carrito");

        // Obtener cantidad del carrito
        int cantidadReal = cartPage.getCantidadProducto(nombreProducto);

        // Assert: La cantidad en el carrito NO debe ser cero
        Assert.assertTrue(cantidadReal != 0,
                "La cantidad del producto en el carrito no puede ser 0. Cantidad actual: " + cantidadReal);

        // Assert: La cantidad en el carrito debe ser mayor a cero
        Assert.assertTrue(cantidadReal > 0,
                "La cantidad del producto en el carrito debe ser mayor a 0. Cantidad actual: " + cantidadReal);

        // Assert: La cantidad en el carrito NO debe exceder el máximo
        Assert.assertTrue(cantidadReal <= CANTIDAD_MAXIMA,
                "La cantidad del producto en el carrito excede el máximo permitido (" + CANTIDAD_MAXIMA + "). Cantidad actual: " + cantidadReal);

        // Assert: La cantidad en el carrito debe coincidir con la cantidad agregada
        Assert.assertEquals(cantidadReal, cantidad,
                "La cantidad del producto '" + nombreProducto + "' no es la esperada. Esperada: " + cantidad + ", Actual: " + cantidadReal);

        System.out.println("Cantidad correcta: " + cantidadReal);
        System.out.println("Validación exitosa para: " + nombreProducto);
    }
}