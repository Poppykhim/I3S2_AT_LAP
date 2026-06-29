package com.lab03.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InventoryPage {
    private WebDriver driver;

    // Locators
    private By titleLabel = By.className("title");
    private By cartIcon = By.className("shopping_cart_link");
    private By cartBadge = By.className("shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isTitleDisplayed() {
        try {
            return driver.findElement(titleLabel).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitleText() {
        return driver.findElement(titleLabel).getText();
    }

    public void addToCart(String itemName) {
        // Find add to cart button for specific item using xpath
        String xpath = String.format("//div[text()='%s']/ancestor::div[@class='inventory_item_description']//button", itemName);
        driver.findElement(By.xpath(xpath)).click();
    }

    public void clickCart() {
        driver.findElement(cartIcon).click();
    }

    public int getCartCount() {
        try {
            WebElement badge = driver.findElement(cartBadge);
            return Integer.parseInt(badge.getText());
        } catch (Exception e) {
            return 0;
        }
    }
}
