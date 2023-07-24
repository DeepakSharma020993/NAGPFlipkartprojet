package com.nagpassignment.flipkart.uitestcases;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.nagpassignment.flipkart.base.BaseTest;
import com.nagpassignment.flipkart.pages.FlipkartProductDetailPage;
import com.nagpassignment.flipkart.pages.FlipkartSearchPage;
import com.nagpassignment.flipkart.utils.AssertionUtil;
import com.nagpassignment.flipkart.utils.ExcelUtils;
import com.nagpassignment.flipkart.utils.RetryAnalyzer;

import org.openqa.selenium.JavascriptExecutor;

public class ProductDetailTest extends BaseTest {

	private AssertionUtil assertionUtil = new AssertionUtil();;
	private FlipkartSearchPage searchPage ;
	private FlipkartProductDetailPage productDetailPage;


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

	@Test(groups = "brokenLinks", description = "Verify no broken links on the page", priority = 1)
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

	@Test(groups = "brokenImages", description = "Verify no broken images on the page", priority = 2)
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

	@Test(dataProvider = "testData",retryAnalyzer = RetryAnalyzer.class,groups = "productDetail,Regression", description = "Verify correct rates after discount")
	public void VerifyCorrectRatesAfterDiscount(String productName) throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);
		productDetailPage = new FlipkartProductDetailPage(driver);
		searchPage.goToProductDetailPage(productName, driver);
		productDetailPage.verifyCorrectRatesAfterDiscount(extentTest);
		assertionUtil.verifyTrue(driver, extentTest, productDetailPage.verifyCorrectRatesAfterDiscount(extentTest), "Verifying correct rates after applying discount");
	}


	@Test(retryAnalyzer = RetryAnalyzer.class,groups = "Delivery", description = "Verify valid and invalid pin codes", priority = 4)
	public void VerifyValiadAndInvalidPincode() throws InterruptedException {
		//Do it with data provider	
		searchPage = new FlipkartSearchPage(driver);;
		productDetailPage = new FlipkartProductDetailPage(driver);;
		searchPage.goToProductDetailPage("computer", driver);
		productDetailPage.enterPincode("262501");
		assertionUtil.verifyTrue(driver, extentTest,productDetailPage.validateValidPincode(),"Verifying Delivery date for valid pin code");
		//productDetailPage.changePincode();
		driver.navigate().refresh();
		productDetailPage.enterPincode("4444");
		assertionUtil.verifyTrue(driver, extentTest,productDetailPage.validateInvalidPincode(),"Verifying error  for invalid pin code");



	}

	@Test(groups = "Sanity", description = "Verify add to cart functionality", priority = 5)
	public void VerifyAddToCart() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);;
		productDetailPage = new FlipkartProductDetailPage(driver);;
		searchPage.goToProductDetailPage("Router", driver);
		assertionUtil.verifyResult(driver, extentTest,productDetailPage.getCartButtonName(),"ADD TO CART" , "");
		productDetailPage.addItemToCart(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class ='_2KpZ6l _2ObVJD _3AWRsL']")));
		driver.navigate().back();
		assertionUtil.verifyResult(driver, extentTest,productDetailPage.getCartButtonName(),"GO TO CART" , "");
	}

	@DataProvider(name = "testData")
	public Object[][] getTestData() {
	    return ExcelUtils.getTestData("src/main/java/com/nagpassignment/flipkart/data/ProductName.xlsx");
	}
	
	@AfterMethod
	public void teardownAfterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(Status.FAIL, MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
			extentTest.fail(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
		} else {
			extentTest.log(Status.PASS, MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
		}
	}



}
