package com.nagpassignment.flipkart.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.nagpassignment.flipkart.base.BasePage;

/**
 * Page class for Flipkart Cart page.
 */
public class FlipKartCartPage extends BasePage {
    private WebDriver driver;

    @FindBy(xpath = "//div[text() = 'Are you sure you want to remove this item?']")
    private WebElement removeItemConfirmationElement;

    @FindBy(xpath = "(//div[@class = 'td-FUv WDiNrH']/div)[2]")
    private WebElement confirmationRemoveButton;

    @FindBy(xpath = "//div[text() = 'Missing Cart items?']")
    private WebElement emptyCart;

    @FindBy(xpath = "//div[@class = 'z4Ha90']")
    private WebElement totalPriceInCart;

    /**
     * Constructor for FlipKartCartPage class.
     *
     * @param driver the WebDriver object.
     */
    public FlipKartCartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Get the total number of items in the cart.
     *
     * @return the total number of items in the cart.
     */
    public int getTotalItemsInCart() {
        List<WebElement> elements = driver.findElements(By.xpath("//div[@class = 'zab8Yh _10k93p']"));
        return elements.size();
    }

    /**
     * Navigate to the product page.
     */
    public void navigateToProductPage() {
        driver.switchTo().window(mainWindowHandle);
    }

    /**
     * Remove all products from the cart.
     */
    public void removeAllProductsFromCart() {
        List<WebElement> elements = driver.findElements(By.xpath("//div[text() = 'Remove']"));
        for (WebElement element : elements) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();
            String text = removeItemConfirmationElement.getText();
            confirmationRemoveButton.click();
        }
    }

    /**
     * Check if the cart is empty.
     *
     * @return true if the cart is empty, false otherwise.
     */
    public boolean isCartEmpty() {
        return emptyCart.isDisplayed();
    }

    /**
     * Calculate the total price of all items in the cart.
     *
     * @return the total price of all items in the cart.
     */
    public int addPriceOfAllItemInCart() {
        int totalPrice = 0;
        List<WebElement> elements = driver.findElements(By.xpath("//span[@class = '_2-ut7f _1WpvJ7']"));
        for (WebElement element : elements) {
            totalPrice = totalPrice + Integer.valueOf(element.getText().substring(1).replace(",", ""));
        }
        return totalPrice;
    }

    /**
     * Get the total price of the cart.
     *
     * @return the total price of the cart.
     */
    public String getTotalCartPrice() {
        return totalPriceInCart.getText().replace(",", "").replace("₹", "");
    }
}
