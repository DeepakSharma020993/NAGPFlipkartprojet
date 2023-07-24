package com.nagpassignment.flipkart.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.nagpassignment.flipkart.base.BasePage;
import com.nagpassignment.flipkart.utils.ListSortChecker;

/**
 * Page class for Flipkart Search page.
 */
public class FlipkartSearchPage extends BasePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By priceElementsLocator = By.xpath("//div[@class='_30jeq3']");

    @FindBy(xpath = "//input[@name='q']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[text()='Price -- Low to High']")
    private WebElement sortPriceLowToHigh;

    @FindBy(xpath = "//div[text()='Price -- High to Low']")
    private WebElement sortPriceHighToLow;

    @FindBy(xpath = "(//select[@class = '_2YxCDZ'])[1]")
    private WebElement minPriceCombo;

    @FindBy(xpath = "(//select[@class = '_2YxCDZ'])[2]")
    private WebElement maxPriceCombo;

    @FindBy(xpath = "(//a[@class = 's1Q9rs'])[1]")
    private WebElement firstProductLink;

    /**
     * Constructor for FlipkartSearchPage class.
     *
     * @param driver the WebDriver object.
     */
    public FlipkartSearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
     * Search for a product using the given query.
     *
     * @param query  the search query.
     * @param driver the WebDriver object.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public void searchProduct(String query, WebDriver driver) throws InterruptedException {
        int maxRetries = 3;
        int retries = 0;

        while (retries < maxRetries) {
            try {
                searchInput.clear();
                searchInput.click();
                searchInput.sendKeys(Keys.CONTROL + "a");
                searchInput.sendKeys(Keys.DELETE);
                searchInput.sendKeys(Keys.CONTROL + "a");
                searchInput.sendKeys(Keys.DELETE);
                enterSearchQuery(query);
                clickSearchButton();
                mainWindowHandle = driver.getWindowHandle();
                break; // Break the loop if no stale element exception occurs
            } catch (StaleElementReferenceException e) {
                // Increment retries count and re-fetch the elements
                retries++;
                searchInput = driver.findElement(By.xpath("//input[@name='q']"));
                searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
            }
        }
        Thread.sleep(3000);
    }

    /**
     * Get the search category of the current search.
     *
     * @return the search category.
     */
    public String getSearchCategory() {
        List<WebElement> searchCategoryTreeList = driver.findElements(By.xpath("//a[@class='_2whKao']"));
        return searchCategoryTreeList.get(searchCategoryTreeList.size() - 1).getText();
    }

    /**
     * Check if the prices are sorted in low to high order.
     *
     * @return true if prices are sorted in low to high order, false otherwise.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public Boolean isPriceSortedLowToHigh() throws InterruptedException {
        sortPriceLowToHigh.click();
        return ListSortChecker.isSorted(getPriceList(), ListSortChecker.SortingType.ASCENDING);
    }

    /**
     * Check if the prices are sorted in high to low order.
     *
     * @return true if prices are sorted in high to low order, false otherwise.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public Boolean isPriceSortedHighToLow() throws InterruptedException {
        sortPriceHighToLow.click();
        return ListSortChecker.isSorted(getPriceList(), ListSortChecker.SortingType.DESCENDING);
    }

    /**
     * Verify if the prices are within the specified range.
     *
     * @param minRange    the minimum price range.
     * @param maxRange    the maximum price range.
     * @param extentTest  the ExtentTest object for reporting.
     * @return true if all prices are within the range, false otherwise.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public Boolean verifyPriceRanges(String minRange, String maxRange, ExtentTest extentTest) throws InterruptedException {
        Select select = new Select(minPriceCombo);
        select.selectByVisibleText(minRange);
        select = new Select(maxPriceCombo);
        select.selectByVisibleText(maxRange);
        Boolean returnFlag = true;
        for (String price : getPriceList()) {
            if (Integer.valueOf(price.substring(1).replace(",", "")) < Integer.valueOf(minRange.substring(1)) || Integer.valueOf(price.substring(1).replace(",", "")) > Integer.valueOf(maxRange.substring(1))) {
                extentTest.info("Test fail for price " + price);
                return false;
            }
        }
        return returnFlag;
    }

    private List<String> getPriceList() throws InterruptedException {
        List<String> priceList = new ArrayList<String>();
        List<WebElement> priceElementsList = driver.findElements(priceElementsLocator);
        Thread.sleep(8000);

        int maxRetries = 3;
        int retries = 0;

        while (retries < maxRetries) {
            try {
                for (WebElement priceElement : priceElementsList) {
                    priceList.add(priceElement.getText());
                }
                break; // Break the loop if no stale element exception occurs
            } catch (StaleElementReferenceException e) {
                // Increment retries count and re-fetch the elements
                retries++;
                priceElementsList = driver.findElements(priceElementsLocator);
            }
        }
        return priceList;
    }

    /**
     * Go to the product detail page for the specified product.
     *
     * @param productName the name of the product.
     * @param driver      the WebDriver object.
     * @return an instance of FlipkartProductDetailPage.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public FlipkartProductDetailPage goToProductDetailPage(String productName, WebDriver driver) throws InterruptedException {
        searchProduct(productName, driver);

        // Store the current window handle
        String mainWindowHandle = driver.getWindowHandle();

        // Click on the first product link to open in a new window
        clickOnFirstProductLink(driver);

        // Switch to the new window
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                if (driver.getTitle().contains("Buy Products Online at Best Price")) {
                    break;
                }
            }
        }

        mainWindowHandle = driver.getWindowHandle();

        // Create and return an instance of FlipkartProductDetailPage
        return new FlipkartProductDetailPage(driver);
    }

    private void clickOnFirstProductLink(WebDriver driver) {
        By productLinkLocator = By.xpath("(//a[@class='s1Q9rs'])[1]");

        boolean isClicked = false;
        int retryCount = 0;
        int maxRetries = 3;

        while (!isClicked && retryCount < maxRetries) {
            try {
                WebElement productLink = driver.findElement(productLinkLocator);
                productLink.click();
                isClicked = true;
            } catch (StaleElementReferenceException e) {
                // Stale element exception occurred, retry clicking the link
                retryCount++;
                System.out.println("Stale element exception occurred. Retrying click operation... Retry count: " + retryCount);
            }
        }

        if (!isClicked) {
            // Maximum retries reached, handle the failure scenario
            System.out.println("Failed to click on the first product link after " + maxRetries + " attempts.");
            // Add appropriate error handling logic here
        }
    }
}
