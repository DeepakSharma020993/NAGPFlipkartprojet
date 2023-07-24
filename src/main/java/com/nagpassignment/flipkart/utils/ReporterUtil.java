package com.nagpassignment.flipkart.utils;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.nagpassignment.flipkart.base.BaseTest;

public class ReporterUtil extends BaseTest{
	

 public static void reportStep(ExtentTest extentTest,String message) {
	 
	 extentTest.info(MarkupHelper.createLabel(message, ExtentColor.BLACK));
	 
	 
 }
 
public static void reportPass(ExtentTest extentTest,String message) {
	 
	 extentTest.info(MarkupHelper.createLabel(message, ExtentColor.GREEN));
		 
 }


public static void reportFail(ExtentTest extentTest,String message) {
	 
	 extentTest.info(MarkupHelper.createLabel(message, ExtentColor.RED));
		 
}
	
	
}
