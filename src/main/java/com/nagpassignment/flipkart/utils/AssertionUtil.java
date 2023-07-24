package com.nagpassignment.flipkart.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.nagpassignment.flipkart.base.BaseTest;
import com.nagpassignment.flipkart.listeners.MyTestListener;

public class AssertionUtil extends BaseTest{
	
	
	
	public  void verifyResult(WebDriver driver,ExtentTest extentTest, String actualResult, String expectedResult, String message) {
        extentTest.info("Performing assertion: " + message);
        extentTest.info("Actual Result: " + actualResult);
        extentTest.info("Expected Result: " + expectedResult);

        try {
            Assert.assertEquals(actualResult, expectedResult, message);
            ReporterUtil.reportPass(extentTest, "Assertion Passed - "+message);
           
        } catch (AssertionError e) {
        	ReporterUtil.reportFail(extentTest, "Assertion Failed - "+ message);
            extentTest.fail(e);
            captureAndAppendScreenshot(extentTest, driver,e);
        }
    }
	
	public  void verifyTrue(WebDriver driver,ExtentTest extentTest, boolean actualResult,  String message) {
        extentTest.info("Performing assertion: " + message);
     
        try {
        	 Assert.assertTrue(actualResult, message);
        	 ReporterUtil.reportPass(extentTest, "Assertion Passed - "+message);
        } catch (AssertionError e) {
        	ReporterUtil.reportFail(extentTest, "Assertion Failed - "+ message);
            extentTest.fail(e);
            captureAndAppendScreenshot(extentTest, driver,e);
        }
    }
	
	private  void captureAndAppendScreenshot(ExtentTest extentTest, WebDriver driver, AssertionError e) {
        try {
            // Capture screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
            
            // Specify the location to save the screenshot
            String testName = MyTestListener.getTestCaseName();
            String screenshotPath = "src/main/java/com/nagpassignment/flipkart/reporting/screenshots/"+testName+"_"+e.getMessage()+".png";
            
            FileUtils.copyFile(screenshotFile, new File(screenshotPath));
            // Append screenshot to report
            extentTest.addScreenCaptureFromPath(screenshotPath);
        } catch (Exception error) {
            extentTest.warning("Error occurred while capturing and appending screenshot: " + error.getMessage());
        }
    }

}
