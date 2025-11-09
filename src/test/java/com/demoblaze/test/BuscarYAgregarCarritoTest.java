package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductsPage;
import com.demoblaze.pages.ProductsDetallePage;
import com.demoblaze.pages.CartPage;
import com.demoblaze.utils.Constants;
import com.demoblaze.utils.ExcelReaderCart;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class BuscarYAgregarCarritoTest extends BaseTest {

    @Test
    public void testBuscarYAgregarProductosDesdeExcel() {
        // Leer productos del Excel
        Object[][] productos = ExcelReaderCart.readProductos(null);

        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage detallePage = new ProductsDetallePage(driver);
        CartPage cartPage = new CartPage(driver);

        // Map para productos esperados
        Map<String, Integer> productosEsperados = new HashMap<>();

        System.out.println("=== Iniciando prueba de búsqueda y agregado al carrito ===");
        System.out.println("Total de productos a procesar: " + productos.length);

        // Agregar productos al carrito
        for (int i = 0; i < productos.length; i++) {
            String categoria = (String) productos[i][0];
            String subCategoria = (String) productos[i][1];
            String nombreProducto = (String) productos[i][2];
            int cantidad = (int) productos[i][3];

            System.out.println("\n--- Producto " + (i+1) + " de " + productos.length + " ---");
            System.out.println("Categoría: " + categoria);
            System.out.println("SubCategoría: " + subCategoria);
            System.out.println("Producto: " + nombreProducto);
            System.out.println("Cantidad: " + cantidad);

            try {
                homePage.navigateTo(Constants.BASE_URL);
                homePage.buscarProducto(nombreProducto);

                // Esperar brevemente para que se carguen los resultados
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                boolean productVisible = productsPage.isProductDisplayed(nombreProducto);

                // Esperar brevemente para que el producto se agregue al carrito
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Assert.assertTrue(productVisible,
                        "El producto '" + nombreProducto + "' no aparece en los resultados");
                System.out.println("✓ Producto encontrado");

                productsPage.selectProduct(nombreProducto);
                System.out.println("✓ Producto seleccionado");

                detallePage.agregarCarrito(cantidad);

        // Esperar brevemente para que se cargue el carrito
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

                System.out.println("✓ Agregado al carrito");

        boolean carritoVacio = cartPage.isCarritoVacio();
        Assert.assertFalse(carritoVacio, "El carrito está vacío");

            } catch (Exception e) {
                System.err.println("✗ Error: " + e.getMessage());
                Assert.fail("Error procesando: " + nombreProducto);
            }
        }

        // Validar carrito
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🛒 VALIDANDO CARRITO");
        System.out.println("=".repeat(60));

        cartPage.irAlCarrito();
        System.out.println("✓ En el carrito");

        Assert.assertFalse(cartPage.isCarritoVacio(), "El carrito está vacío");
        System.out.println("✓ Carrito contiene productos");

        cartPage.imprimirCarrito();

        int esperados = productosEsperados.size();
        int enCarrito = cartPage.getTotalProductos();

        Assert.assertEquals(enCarrito, esperados,
                "Productos en carrito no coinciden. Esperados: " + esperados + ", En carrito: " + enCarrito);
        System.out.println("✓ Número de productos correcto: " + enCarrito);

        // Validar cada producto
        System.out.println("\n--- Validando productos ---");
        for (Map.Entry<String, Integer> entry : productosEsperados.entrySet()) {
            String nombre = entry.getKey();
            int cantidadEsperada = entry.getValue();

            System.out.println("\nValidando: " + nombre);

            Assert.assertTrue(cartPage.isProductoEnCarrito(nombre),
                    "Producto '" + nombre + "' no está en el carrito");
            System.out.println("✓ Encontrado");

            int cantidadReal = cartPage.getCantidadProducto(nombre);
            Assert.assertEquals(cantidadReal, cantidadEsperada,
                    "Cantidad incorrecta para '" + nombre + "'");
            System.out.println("✓ Cantidad correcta: " + cantidadReal);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ VALIDACIÓN EXITOSA");
        System.out.println("=".repeat(60));
    }

}