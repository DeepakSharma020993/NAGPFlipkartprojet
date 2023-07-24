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
import com.nagpassignment.flipkart.utils.JsonDataReader;
import com.nagpassignment.flipkart.utils.PageData;
import com.nagpassignment.flipkart.utils.ReporterUtil;


public class SearchPageTest extends BaseTest {

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


	@Test
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
	
	@Test
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


	@Test
	public void verifySearchPageElements() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);
		java.util.List<PageData> searchPageData = JsonDataReader.getPageData("SearchPage","elementName","expectedResult");
		for (PageData data : searchPageData) {
			String elementName = data.getElementName();
			String expectedValue = data.getExpectedValue();
			ReporterUtil.reportStep(extentTest, "Searching product - "+elementName);	
			searchPage.searchProduct(elementName,driver);
			assertionUtil.verifyResult(driver, extentTest,searchPage.getSearchCategory() ,expectedValue, "Verify Search Result");


		}
	}
	
	
	@Test
	public void verifySortFunctionality() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);
		java.util.List<PageData> searchPageData = JsonDataReader.getPageData("SearchPage","elementName","expectedResult");
		String elementName = searchPageData.get(1).getElementName();
		searchPage.searchProduct(elementName,driver);
		ReporterUtil.reportStep(extentTest, " Applying price sorting from low to high");		
		assertionUtil.verifyTrue(driver, extentTest,searchPage.isPriceSortedLowToHigh(),"Verifying that prices are sorted low to high");
		ReporterUtil.reportStep(extentTest, " Applying price sorting from High to low");
		assertionUtil.verifyTrue(driver, extentTest,searchPage.isPriceSortedHighToLow(),"Verifying that prices are sorted high to low");
		
	}
	
	@Test
	public void verifyPriceFilter() throws InterruptedException {
		searchPage = new FlipkartSearchPage(driver);
		java.util.List<PageData> searchPageData = JsonDataReader.getPageData("SearchPage","elementName","expectedResult");
		java.util.List<PageData> filterPriceData = JsonDataReader.getPageData("PriceFilter","MinValue","MaxValue");
		String elementName = searchPageData.get(1).getElementName();
		searchPage.searchProduct(elementName,driver);
		for(int i =0 ;i<filterPriceData.size();i++) {
			ReporterUtil.reportStep(extentTest, " Applying price filter from "+filterPriceData.get(i).getElementName()+" to "+filterPriceData.get(i).getExpectedValue());
			assertionUtil.verifyTrue(driver, extentTest,searchPage.verifyPriceRanges(filterPriceData.get(i).getElementName(), filterPriceData.get(i).getExpectedValue(),extentTest),"Verifying that prices are between "+filterPriceData.get(i).getElementName()+" to "+filterPriceData.get(i).getExpectedValue());
		}
		
		
	}


	@AfterClass()
	public void teardown() {

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
