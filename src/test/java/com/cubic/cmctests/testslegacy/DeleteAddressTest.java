package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.restapi.ApiCustomerPost;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class DeleteAddressTest extends RESTEngine{

	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	private static Logger Log = Logger.getLogger(Logger.class.getName());
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	RESTActions restActions;

	@Parameters({"browser", "executionenv"})
	@BeforeMethod
	public void setUp(String browser, String executionenv) throws InterruptedException {

//		Logging.setLogConsole();
//		Logging.setLogFile();
//		Log.info("Setup Started");
//		Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
//		Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser, executionenv);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//		Log.info("Setup Completed");
	}

	@Test(priority = 1, enabled = true)
	public void deleteAddressCancel(ITestContext context) throws Exception {
		String testCaseName = "29931:deleteAddressCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29931");
			createNewCustomer(driver);
			Utils.waitTime(5000);
			SearchPage sPage = new SearchPage(driver);
			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);
			sPage.clickDeleteAddress(driver);
			DeleteAddressDetailsPage delPage = new DeleteAddressDetailsPage(driver);
			Utils.waitTime(5000);
			delPage.clickCancel(driver);
			Utils.waitTime(5000);
			Assert.assertTrue(sPage.isDeleteAddressDisplayed(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void deleteAddressConfirmCancel(ITestContext context) throws Exception {
		String testCaseName = "29934:deleteAddressConfirmCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29934");
			createNewCustomer(driver);
			Utils.waitTime(5000);
			SearchPage sPage = new SearchPage(driver);
			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);
			sPage.clickDeleteAddress(driver);
			DeleteAddressDetailsPage delPage = new DeleteAddressDetailsPage(driver);
			delPage.clickConfirmDelete(driver);
			Utils.waitTime(5000);
			delPage.clickSecondCancel(driver);
			Utils.waitTime(5000);
			Assert.assertTrue(sPage.isDeleteAddressDisplayed(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	private WebDriver createNewCustomer(WebDriver driver) throws Exception {

		// Create customer test data via api rest call
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// return to selenium testing
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, email);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickContiune(driver);
		return driver;
	}

	private SearchPage getSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		SearchPage sPage = new SearchPage(driver);
		return sPage;
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}