package com.nagpassignment.flipkart.utils;

public class PageData {
    private String elementName;
    private String expectedValue;

    public PageData(String elementName, String expectedValue) {
        this.elementName = elementName;
        this.expectedValue = expectedValue;
    }

    public String getElementName() {
        return elementName;
    }

    public String getExpectedValue() {
        return expectedValue;
    }
}

