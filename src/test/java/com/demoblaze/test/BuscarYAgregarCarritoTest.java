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

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Buscar el producto
        homePage.buscarProducto(nombreProducto);

        // Verificar que el producto aparece en los resultados
        Assert.assertTrue(productsPage.isProductDisplayed(nombreProducto),
                "El producto '" + nombreProducto + "' no aparece en los resultados de búsqueda");
        System.out.println("✓ Producto encontrado en resultados");

        // Seleccionar el producto
        productsPage.selectProduct(nombreProducto);
        System.out.println("✓ Producto seleccionado");

        // Agregar al carrito
        detallePage.agregarCarrito(cantidad);
        System.out.println("✓ Agregado al carrito (cantidad: " + cantidad + ")");

        // Ir al carrito
        cartPage.irAlCarrito();
        System.out.println("✓ Navegado al carrito");

        // Validar que el producto está en el carrito
        Assert.assertTrue(cartPage.isProductoEnCarrito(nombreProducto),
                "El producto '" + nombreProducto + "' no está en el carrito");
        System.out.println("✓ Producto encontrado en carrito");

        // Validar la cantidad
        int cantidadReal = cartPage.getCantidadProducto(nombreProducto);
        Assert.assertEquals(cantidadReal, cantidad,
                "La cantidad del producto '" + nombreProducto + "' no es la esperada");
        System.out.println("✓ Cantidad correcta: " + cantidadReal);

        System.out.println("✅ Validación exitosa para: " + nombreProducto);
    }
}