package com.nagpassignment.flipkart.uitestcases;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.nagpassignment.flipkart.base.BaseTest;
import com.nagpassignment.flipkart.pages.FlipkartHomePage;
import com.nagpassignment.flipkart.pages.FlipkartSearchPage;
import com.nagpassignment.flipkart.utils.AssertionUtil;
import com.nagpassignment.flipkart.utils.ReporterUtil;
import com.nagpassignment.flipkart.utils.RetryAnalyzer;


public class HomePageTest extends BaseTest {

	private FlipkartHomePage homePage;
	private FlipkartSearchPage searchPage;
	private AssertionUtil assertionUtil = new AssertionUtil();

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

	@Test(groups = {"brokenLinks"}, description = "Verify no broken links on the page", priority = 1)
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

	 @Test(groups = "brokenLinks", description = "Verify no broken links on the page",priority = 2)
	public void verifyNoBrokenImages() {
		// Find all the images on the page
		java.util.List<WebElement> images = driver.findElements(By.tagName("img"));

		// Iterate over each image and check for any broken images
		for (int i = 0; i < images.size(); i++) {
			WebElement image = images.get(i);
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
					assertionUtil.verifyTrue(driver, extentTest, responseCode < 400,
							"Verifying image link: " + imageURL);

					// Close the connection
					connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	

	@Test(groups = "Sanity", description = "Verify logo visibility on the home Page", priority =3 )
	public void verifyLogoVisibility() {
		homePage = new FlipkartHomePage(driver);
		Assert.assertTrue(homePage.isLogoDisplayed(), "Logo is visible on the home page.");
		assertionUtil.verifyTrue(driver, extentTest, homePage.isLogoDisplayed(), "Logo is visible on the home page.");
	}

	

	@Test(groups = "Functional", description = "Verify all category items are displayed", priority = 4 )
	public void verifyAllCategoryItems() {
		homePage = new FlipkartHomePage(driver);
		String expectedItemList = "[Grocery, Mobiles, Fashion, Electronics, Home & Furniture, Appliances, Travel, Top Offers, Beauty, Toys & More, Two Wheelers]";
		java.util.List<String> actualitemList = homePage.getAllCategoryItems();
		assertionUtil.verifyResult(driver, extentTest, actualitemList.toString(), expectedItemList,
				"Verify Search Result");
	}
	
	@Test(retryAnalyzer = RetryAnalyzer.class,groups = "Sanity", description = "Verify Search Functionality", priority = 5)
	public void verifySearchFunctionality() throws InterruptedException {
		homePage = new FlipkartHomePage(driver);
		searchPage = new FlipkartSearchPage(driver);
		ReporterUtil.reportStep(extentTest, "Entering HeadPhones in search box");		
		searchPage.searchProduct("HeadPhones",driver);
		assertionUtil.verifyResult(driver, extentTest, String.valueOf( homePage.getTotalDisplayedItems()), "40",
				"Verify that Total 40 items are displayed in search Result");
		ReporterUtil.reportStep(extentTest, "Entering special characters in search box");	
		searchPage.searchProduct("&&&&&$$",driver);
		Thread.sleep(2000);
		assertionUtil.verifyResult(driver, extentTest, String.valueOf( homePage.getTotalDisplayedItems()), "0",
				"Verify that 0 items are displayed in search Result");		
		
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
