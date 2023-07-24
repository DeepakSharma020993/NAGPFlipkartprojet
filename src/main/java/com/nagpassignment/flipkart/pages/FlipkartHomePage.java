package com.nagpassignment.flipkart.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nagpassignment.flipkart.base.BasePage;

/**
 * Page class for Flipkart Home page.
 */
public class FlipkartHomePage extends BasePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//input[@name='q']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//img[@title='Flipkart']")
    private WebElement logoLocator;

    @FindBy(xpath = "(//div[@class='_3LU4EM'])[1]")
    private WebElement firstFeaturedItem;

    @FindBy(xpath = "//div[@class='_2kgArB _2CP_Bu']")
    public WebElement featureItemNextButton;

    /**
     * Constructor for FlipkartHomePage class.
     *
     * @param driver the WebDriver object.
     */
    public FlipkartHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofMillis(1000));
        wait.until(ExpectedConditions.visibilityOf(searchInput));
    }

    /**
     * Enter the search query in the search input field.
     *
     * @param query the search query to enter.
     */
    public void enterSearchQuery(String query) {
        searchInput.sendKeys(query);
    }

    /**
     * Click the search button.
     */
    public void clickSearchButton() {
        searchButton.click();
    }

    /**
     * Check if the Flipkart logo is displayed.
     *
     * @return true if the logo is displayed, false otherwise.
     */
    public boolean isLogoDisplayed() {
        try {
            return logoLocator.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Get the list of all category items displayed on the home page.
     *
     * @return the list of category items.
     */
    public List<String> getAllCategoryItems() {
        List<String> returnList = new ArrayList<>();
        List<WebElement> categoryElements = driver.findElements(By.xpath("//div[@class ='_37M3Pb' ]/div/a/div[2]"));
        for (WebElement categoryElement : categoryElements) {
            returnList.add(categoryElement.getText());
        }
        return returnList;
    }

    /**
     * Check if the first featured item is displayed on the home page.
     *
     * @return true if the first featured item is displayed, false otherwise.
     */
    public Boolean isFirstFeaturedItemDisplaying() {
        return firstFeaturedItem.isDisplayed();
    }

    /**
     * Get the total number of displayed items on the home page.
     *
     * @return the total number of displayed items.
     */
    public int getTotalDisplayedItems() {
        return driver.findElements(By.xpath("//a[@class='s1Q9rs']")).size();
    }
}
