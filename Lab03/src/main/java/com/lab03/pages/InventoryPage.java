package com.lab03.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By titleLabel = By.className("title");
    private By cartIcon = By.className("shopping_cart_link");
    private By cartBadge = By.className("shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isTitleDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(titleLabel)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(titleLabel)).getText();
    }

    public void addToCart(String itemName) {
        // Find add to cart button for specific item using xpath
        String xpath = String.format("//div[text()='%s']/ancestor::div[@class='inventory_item_description']//button", itemName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void clickCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }

    public int getCartCount() {
        try {
            WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            return Integer.parseInt(badge.getText());
        } catch (Exception e) {
            return 0;
        }
    }
}
