package com.lab03.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By checkoutButton = By.id("checkout");
    private By cartItems = By.className("cart_item");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isItemInCart(String itemName) {
        try {
            // Wait for cart items to be present first
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems));

            // Find all item name elements and check text
            List<WebElement> nameElements = driver.findElements(By.className("inventory_item_name"));
            for (WebElement element : nameElements) {
                if (element.getText().trim().equals(itemName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }
}
