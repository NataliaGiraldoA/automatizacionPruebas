package com.demoblaze.test;

import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductsDetallePage;
import com.demoblaze.pages.ProductsPage;
import com.demoblaze.utils.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ComprarTest extends BaseTest{

    @Test
    public void ComprarProductos(){
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ProductsDetallePage productsDetallePage = new ProductsDetallePage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        homePage.selectCategory("Desktops");
        homePage.selectSubCategory("Mac");

        String productName = "iMac";

        // Validar que el producto esté visible
        boolean isVisible = productsPage.isProductDisplayed(productName);
        Assert.assertTrue(isVisible,
            "El producto " + productName + " no esta visible");

        productsPage.selectProduct(productName);

        // Agregar al carrito con cantidad específica
        productsDetallePage.agregarCarrito("3");

    }
}
