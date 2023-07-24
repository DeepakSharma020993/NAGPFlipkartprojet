package com.nagpassignment.flipkart.listeners;


import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.nagpassignment.flipkart.base.BaseTest;

public class MyTestListener extends TestListenerAdapter implements ITestListener,WebDriverEventListener  {

	private static final Logger logger = BaseTest.logger;
	private static ThreadLocal<String> testCaseName = new ThreadLocal<>();
	@Override
	public void onTestStart(ITestResult result) {
		// Code to execute when a test starts
		String testName = result.getName();
		System.out.println("Test '" + testName + "' started.");
		logger.info("Test '" + testName + "' started.");
		testCaseName.set(testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// Code to execute when a test succeeds
		String testName = result.getName();
		System.out.println("Test '" + testName + "' passed.");
		logger.info("Test '" + testName + "' passed.");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// Code to execute when a test fails
		String testName = result.getName();
		System.out.println("Test '" + testName + "' failed.");
		logger.info("Test '" + testName + "' failed.");
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		System.out.println("Clicking on element: " + getElementDescription(element));
		logger.info("Clicking on element: " + getElementDescription(element));
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		System.out.println("Clicked on element: " + getElementDescription(element));
		logger.info("Clicked on element: " + getElementDescription(element));
	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub

	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub

	}

	private String getElementDescription(WebElement element) {
		String description = "Element [Tag: " + element.getTagName() + ", Text: " + element.getText() + "]";
		// You can enhance the description as needed to include more details about the element
		return description;
	}

	public static String getTestCaseName() {
		return testCaseName.get();
	}


}
