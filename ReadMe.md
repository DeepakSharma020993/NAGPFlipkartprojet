
# Flipkart Test Automation Project

This is a test automation project for Flipkart built using the TestNG framework with Maven.

## Introduction

This project aims to automate the testing of Flipkart's website functionalities. It utilizes Selenium WebDriver for browser automation, TestNG for test management, and Maven for project build and dependency management.

## Features

- Test cases for various scenarios including login, search functionality, navigation, etc.
- Extent Reports integration for detailed test reports.
- Capturing and appending screenshots for failed test cases.
- Test data management using external files.
- Page Object Model (POM) design pattern for better code organization and maintenance.
- Integration with GitHub Actions for continuous integration and automated testing.

## Setup and Execution

1. Clone the repository to your local machine.

2. Make sure you have the following software installed:
   - Java JDK (version 8 or higher)
   - Maven

3. Update the test configuration and credentials in the configuration file located at `src/test/resources/config.properties`.

4. Open a terminal or command prompt and navigate to the project directory.

5. Run the tests using Maven:

**mvn clean test -Dsurefire.suiteXmlFiles= mavenproject\testng.xml**


6. After the test execution, the test reports will be generated in the `Current test results_timestamp` directory.

## Test Results

Test results and reports can be found in the `Current test results_timestamp` directory after test execution. The reports provide detailed information about test cases, their status, and any failures encountered during the execution.

## Contributing

Contributions are welcome! If you find any issues or want to enhance the existing test suite, feel free to create a pull request.


