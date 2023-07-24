package com.nagpassignment.flipkart.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.nagpassignment.flipkart.listeners.MyTestListener;
import com.nagpassignment.flipkart.utils.TestResultHandler;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class BaseTest {
	protected WebDriver uiDriver;
	protected EventFiringWebDriver driver;
	protected ExtentReports extentReports;
	protected ExtentTest extentTest;
	protected Properties properties;
	public static  Logger logger = LogManager.getLogger(BaseTest.class);
	MyTestListener myTestListener = new MyTestListener();

	@BeforeSuite
	public void setUp() {
		
		// Load the properties file
		loadProperties();

		// Set up Extent Report configurations
		extentReports = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("src/main/java/com/nagpassignment/flipkart/reporting/ExtentReport.html");
		extentReports.attachReporter(htmlReporter);
		logger.info("Extent Report Initialized successfully");

	}

	@BeforeMethod
	public void beforeMethodUI() {
	    String browserName = properties.getProperty("browser.name");

	    if (browserName.equalsIgnoreCase("chrome")) {
	        WebDriverManager.chromedriver().setup();
	        ChromeOptions chromeOptions = new ChromeOptions();
	        chromeOptions.addArguments("--start-maximized");
	        uiDriver = new ChromeDriver(chromeOptions);
	        driver = new EventFiringWebDriver(uiDriver);
	        driver.register(myTestListener);
	    } else if (browserName.equalsIgnoreCase("firefox")) {
	        WebDriverManager.firefoxdriver().setup();
	        FirefoxOptions firefoxOptions = new FirefoxOptions();
	        firefoxOptions.addArguments("--start-maximized");
	        uiDriver = new FirefoxDriver(firefoxOptions);
	        driver = new EventFiringWebDriver(uiDriver);
	        driver.register(myTestListener);
	    } else if (browserName.equalsIgnoreCase("ie")) {
	        WebDriverManager.iedriver().setup();
	        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
	        ieOptions.addCommandSwitches("--start-maximized");
	        uiDriver = new InternetExplorerDriver(ieOptions);
	        driver = new EventFiringWebDriver(uiDriver);
	        driver.register(myTestListener);
	    }

	    driver.manage().window().maximize();
	    // Set implicit wait time
	    long globalWait = Long.parseLong(properties.getProperty("global.wait"));
	    driver.manage().timeouts().implicitlyWait(globalWait, TimeUnit.SECONDS);
	    driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	    driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
	}





	@AfterSuite
	public void tearDown() {
		extentReports.flush();
		TestResultHandler.moveTestResults();
		driver.quit();

		logger.info("WebDriver closed successfully.");
	}

	@AfterMethod
	public void afterMethodUI() {
		driver.unregister(myTestListener);
		driver.close();
		driver.quit();
	}

	private void loadProperties() {
		properties = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/com/nagpassignment/flipkart/config/config.properties")) {
			properties.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to load properties file.", e);
		}
	}


}
