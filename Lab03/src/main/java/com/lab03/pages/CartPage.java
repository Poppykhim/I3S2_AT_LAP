package com.lab03.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    private WebDriver driver;

    // Locators
    private By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isItemInCart(String itemName) {
        String xpath = String.format("//div[contains(@class, 'inventory_item_name') and text()='%s']", itemName);
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }
}
