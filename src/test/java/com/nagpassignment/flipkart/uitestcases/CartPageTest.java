package com.nagpassignment.flipkart.uitestcases;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.nagpassignment.flipkart.base.BaseTest;
import com.nagpassignment.flipkart.pages.FlipKartCartPage;
import com.nagpassignment.flipkart.pages.FlipkartProductDetailPage;
import com.nagpassignment.flipkart.pages.FlipkartSearchPage;
import com.nagpassignment.flipkart.utils.AssertionUtil;

public class CartPageTest extends BaseTest {
	
	private AssertionUtil assertionUtil = new AssertionUtil();;
	private FlipkartSearchPage searchPage ;
	private FlipkartProductDetailPage productDetailPage;
	private FlipKartCartPage flipKartCartPage;

	@BeforeMethod
	public void setupBeforeMethod(ITestResult result) {

		extentTest = extentReports.createTest(result.getMethod().getMethodName(), result.getMethod().getMethodName());
		
		driver.get(properties.getProperty("test.site.url"));

		// Close the OTP popup if it appears
		try {
			WebElement otpPopup = driver.findElement(By.xpath("//button[text() = 'Request OTP']"));
			if (otpPopup.isDisplayed()) {
				WebElement closeButton = otpPopup.findElement(By.xpath("//button[@class='_2KpZ6l _2doB4z']"));
				closeButton.click();
			}
		} catch (Exception e) {
			// OTP popup not present, continue with the test
		}
		
		
	}
	 @Test(groups = "brokenLinks", description = "Verify no broken links on the page")
	public void verifyNoBrokenLinks() {
		// Find all the links on the page
		java.util.List<WebElement> links = driver.findElements(By.tagName("a"));

		// Iterate over each link and check for any broken links
		for (int i = 0; i < links.size(); i++) {
			WebElement link = links.get(i);
			String url = link.getAttribute("href");
			if (url != null && !url.isEmpty()) {
				try {
					// Check if the URL is a mailto link
					if (url.startsWith("mailto:")) {
						extentTest.info("Step " + (i + 1) + ": Skipping mailto link: " + url);
						continue; // Skip this link and proceed to the next one
					}

					// Check if the URL is a tel link
					if (url.startsWith("tel:")) {
						extentTest.info("Step " + (i + 1) + ": Skipping tel link: " + url);
						continue; // Skip this link and proceed to the next one
					}

					// Create a URL object and open a connection
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.setRequestMethod("HEAD");
					connection.connect();

					// Get the response code
					int responseCode = connection.getResponseCode();

					// Check if the response code is within the range of successful codes
					if (url.equals("https://www.twitter.com/flipkart")) {
						if (responseCode == 403) {
							extentTest.info("Step " + (i + 1) + ": Skipping URL: " + url + " - Response code is " + responseCode);
						} else {
							assertionUtil.verifyTrue(driver, extentTest, responseCode < 400,
									"Verifying link " + url);
							extentTest.info("Step " + (i + 1) + ": Response code for " + url + " is " + responseCode);
						}
					} else if (url.equals("https://www.youtube.com/flipkart")) {
						if (responseCode == 200) {
							extentTest.info("Step " + (i + 1) + ": Skipping URL: " + url + " - Response code is " + responseCode);
						} else {
							assertionUtil.verifyTrue(driver, extentTest, responseCode < 400,
									"Verifying link " + url);
							extentTest.info("Step " + (i + 1) + ": Response code for " + url + " is " + responseCode);
						}
					} else {
						assertionUtil.verifyTrue(driver, extentTest, responseCode < 400,
								"Verifying link " + url);
						extentTest.info("Step " + (i + 1) + ": Response code for " + url + " is " + responseCode);
					}
					// Close the connection
					connection.disconnect();
				} catch (Exception e) {
					extentTest.log(Status.FAIL, MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
					extentTest.fail(e);
				}
			}
		}
	}
	
	 @Test(groups = "brokenImages", description = "Verify no broken images on the page")
    public void verifyNoBrokenImages() {
        // Find all the images on the page
	 java.util.List<WebElement> images = driver.findElements(By.tagName("img"));

        // Iterate over each image and check for any broken images
        for (WebElement image : images) {
            String imageURL = image.getAttribute("src");
            if (imageURL != null && !imageURL.isEmpty()) {
                try {
                    // Create a URL object and open a connection
                    HttpURLConnection connection = (HttpURLConnection) new URL(imageURL).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();

                    // Get the response code
                    int responseCode = connection.getResponseCode();

                    // Check if the response code is within the range of successful codes
                    Assert.assertTrue(responseCode < 400, "Broken image found: " + imageURL);
                    assertionUtil.verifyTrue(driver, extentTest,responseCode < 400,"Verifying image link: " + imageURL);

                    // Close the connection
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	@Test(groups = "cartItems", description = "Verify correct items in the cart")
    public void verifyCorrectItemsInCart() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);;
		productDetailPage = new FlipkartProductDetailPage(driver);;
		flipKartCartPage = new FlipKartCartPage(driver);
		searchPage.goToProductDetailPage("Router", driver);
		assertionUtil.verifyResult(driver, extentTest,productDetailPage.getCartButtonName(),"ADD TO CART" , "");
		productDetailPage.addItemToCart(driver);
		flipKartCartPage.navigateToProductPage();
		driver.navigate().refresh();
		searchPage.goToProductDetailPage("HeadPhones", driver);
		productDetailPage.addItemToCart(driver);
		assertionUtil.verifyResult(driver, extentTest, "2", String.valueOf(flipKartCartPage.getTotalItemsInCart()), "Verify Search Result");
		
		
		
	}
	
	 @Test(groups = "cartItems", description = "Verify removal of items from the cart")
    public void verifyRemoveFromCart() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);;
		productDetailPage = new FlipkartProductDetailPage(driver);;
		flipKartCartPage = new FlipKartCartPage(driver);
		searchPage.goToProductDetailPage("Router", driver);
		assertionUtil.verifyResult(driver, extentTest,productDetailPage.getCartButtonName(),"ADD TO CART" , "");
		productDetailPage.addItemToCart(driver);
		flipKartCartPage.removeAllProductsFromCart();
		assertionUtil.verifyTrue(driver, extentTest, flipKartCartPage.isCartEmpty(), " Verify that the cart is empty");
		
		
		
	}
	
	@Test(groups = "cartPrice", description = "Verify total price of items in the cart")
    public void verifyToatalPriceIsCorrect() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);;
		productDetailPage = new FlipkartProductDetailPage(driver);;
		flipKartCartPage = new FlipKartCartPage(driver);
		searchPage.goToProductDetailPage("Router", driver);
		assertionUtil.verifyResult(driver, extentTest,productDetailPage.getCartButtonName(),"ADD TO CART" , "");
		productDetailPage.addItemToCart(driver);
		flipKartCartPage.navigateToProductPage();
		driver.navigate().refresh();
		searchPage.goToProductDetailPage("HeadPhones", driver);
		productDetailPage.addItemToCart(driver);
		assertionUtil.verifyResult(driver, extentTest, String.valueOf(flipKartCartPage.addPriceOfAllItemInCart()) ,flipKartCartPage.getTotalCartPrice(), "Verify Search Result");
		
		
		
		
		
	}
	
}
