package com.lab03.UI;

import com.lab03.BaseTest;
import com.lab03.pages.CartPage;
import com.lab03.pages.CheckoutPage;
import com.lab03.pages.InventoryPage;
import com.lab03.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SwagLabsTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"invalid_user", "wrong_password", false}
        };
    }

    @Test(dataProvider = "loginData", priority = 1)
    public void testLoginValidation(String username, String password, boolean expectedSuccess) {
        WebDriver driver = getDriver();
        driver.get("https://www.saucedemo.com/");
        
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        if (expectedSuccess) {
            InventoryPage inventoryPage = new InventoryPage(driver);
            Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Title should be displayed after successful login");
            Assert.assertEquals(inventoryPage.getTitleText(), "Products");
        } else {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid/locked logins");
            Assert.assertTrue(loginPage.getErrorMessageText().contains("Epic sadface"), "Error message should contain 'Epic sadface'");
        }
    }

    @Test(priority = 2)
    public void testEndToEndCheckout() {
        WebDriver driver = getDriver();
        driver.get("https://www.saucedemo.com/");
        
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isTitleDisplayed());
        
        // Add item to cart
        String itemName = "Sauce Labs Backpack";
        inventoryPage.addToCart(itemName);
        Assert.assertEquals(inventoryPage.getCartCount(), 1, "Cart count should be 1 after adding an item");
        
        // Navigate to Cart
        inventoryPage.clickCart();
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isItemInCart(itemName), "Item should exist in the cart");
        
        // Checkout
        cartPage.clickCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillInformation("TestFirstName", "TestLastName", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();
        
        // Assert complete
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Order should be marked as complete");
        Assert.assertEquals(checkoutPage.getCompleteHeaderText(), "Thank you for your order!", "Success message mismatch");
    }
}
