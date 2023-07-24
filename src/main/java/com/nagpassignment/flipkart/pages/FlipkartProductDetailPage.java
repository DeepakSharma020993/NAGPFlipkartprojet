package com.nagpassignment.flipkart.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.nagpassignment.flipkart.base.BasePage;
import com.nagpassignment.flipkart.base.BaseTest;

/**
 * Page class for Flipkart Product Detail page.
 */
public class FlipkartProductDetailPage extends BasePage {
    private WebDriver driver;

    /**
     * Constructor for FlipkartProductDetailPage class.
     *
     * @param driver the WebDriver object.
     */
    public FlipkartProductDetailPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@class='_30jeq3 _16Jk6d']")
    private WebElement discountedPriceElement;

    @FindBy(xpath = "//div[@class='_3I9_wc _2p6lqe']")
    private WebElement actualPriceElement;

    @FindBy(xpath = "//div[@class='_3Ay6Sb _31Dcoz']/span")
    private WebElement discountPercent;

    @FindBy(xpath = "//input[@id='pincodeInputId']")
    private WebElement pincodeInput;

    @FindBy(xpath = "//span[text()='Check']")
    private WebElement pincodeCheck;

    @FindBy(xpath = "//span[text()='Change']")
    private WebElement pincodeChange;

    @FindBy(xpath = "//div[text()='Delivery by']")
    private WebElement deliveryDate;

    @FindBy(xpath = "//div[text()='Not a valid pincode']")
    private WebElement invalidPincodeError;

    @FindBy(xpath = "//button[@class='_2KpZ6l _2U9uOA _3v1-ww']")
    private WebElement addTocart;

    /**
     * Verify if the displayed discounted price is correct after applying the discount.
     *
     * @param extentTest the ExtentTest object to log the result.
     * @return true if the discounted price is correct, false otherwise.
     */
    public Boolean verifyCorrectRatesAfterDiscount(ExtentTest extentTest) {
        double actualPrice = Double.parseDouble(actualPriceElement.getText().substring(1).replace(",", ""));
        double discountedPrice = Double.parseDouble(discountedPriceElement.getText().substring(1).replace(",", ""));
        String discountPercentageStr = discountPercent.getText();
        int discountPercentage = Integer.parseInt(discountPercentageStr.substring(0, discountPercentageStr.indexOf("%")));
        double expectedDiscountedPrice = actualPrice - (actualPrice * discountPercentage / 100);
        extentTest.info("expectedDiscountedPrice = " + expectedDiscountedPrice + " ,actualDiscountedPrice= " + discountedPrice);
        return expectedDiscountedPrice == discountedPrice;
    }

    /**
     * Enter the pincode for delivery.
     *
     * @param pincode the pincode to enter.
     */
    public void enterPincode(String pincode) {
        pincodeInput.clear();
        pincodeInput.click();
        pincodeInput.sendKeys(Keys.CONTROL + "a");
        pincodeInput.sendKeys(Keys.DELETE);
        pincodeInput.sendKeys(pincode);
        pincodeCheck.click();
    }

    /**
     * Change the entered pincode.
     */
    public void changePincode() {
        pincodeChange.click();
        pincodeInput.sendKeys(Keys.CONTROL + "a");
        pincodeInput.sendKeys(Keys.DELETE);
    }

    /**
     * Validate if a valid pincode is displayed.
     *
     * @return true if a valid pincode is displayed, false otherwise.
     */
    public boolean validateValidPincode() {
        return deliveryDate.isDisplayed();
    }

    /**
     * Validate if an invalid pincode error is displayed.
     *
     * @return true if an invalid pincode error is displayed, false otherwise.
     */
    public boolean validateInvalidPincode() {
        return invalidPincodeError.isDisplayed();
    }

    /**
     * Add the item to the cart.
     *
     * @param driver the WebDriver object.
     */
    public void addItemToCart(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addTocart);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addTocart);
    }

    /**
     * Get the name of the cart button.
     *
     * @return the name of the cart button.
     */
    public String getCartButtonName() {
        return addTocart.getText();
    }
}
