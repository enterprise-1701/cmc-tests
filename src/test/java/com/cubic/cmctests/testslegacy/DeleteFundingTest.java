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
//Quality Center Test IDs: 77374, 77373
//#################################################################################

public class DeleteFundingTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String STATUS = "Active";
	private static final String NO_RECORD = "No records found";
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	static WebDriver driver;
	static String browser;
	private static final Integer PAYMENT_TYPE = 1;
	CoreTest coreTest = new CoreTest();
	RESTActions restActions;

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws InterruptedException {

		Logging.setLogConsole();
		Logging.setLogFile();
		Log.info("Setup Started");
		Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
		Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		Log.info("Setup Completed");
	}

	//User should not be able to delete a single funding source 
	@Test(priority = 1, enabled = true)
	public void deleteFundingSource(ITestContext context) throws Exception {
		String testCaseName = "29936:deleteFundingSource";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29936");
			getDeleteFundingSourceConfirmation(driver);
			ConfirmationPage cPage = new ConfirmationPage(driver);
			Utils.waitTime(5000);
			cPage.clickConfirmDelete(driver);
			Utils.waitTime(5000);
			Assert.assertEquals(cPage.getNoRecordFoundFundingSource(driver), NO_RECORD);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// fail due to absolute path
	@Test(priority = 2, enabled = true)
	public void deleteFundingSourceClose(ITestContext context) throws Exception {
		String testCaseName = "185966:deleteFundingSourceClose";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185966");
			getDeleteFundingSourceConfirmation(driver);
			ConfirmationPage cPage = new ConfirmationPage(driver);
			Utils.waitTime(5000);
			cPage.clickCloseFundingSource(driver);
			Assert.assertEquals(cPage.getFundingSourceStatus(driver), STATUS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 3, enabled = true)
	public void deleteFundingSourceCancel(ITestContext context) throws Exception {
		String testCaseName = "29935:deleteFundingSourceCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29935");
			getDeleteFundingSourceConfirmation(driver);
			ConfirmationPage cPage = new ConfirmationPage(driver);
			cPage.clickCancel(driver);
			Assert.assertEquals(cPage.getFundingSourceStatus(driver), STATUS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// private method - create customer and funding source and attempt to delete
	// funding source
	private WebDriver getDeleteFundingSourceConfirmation(WebDriver driver) throws Exception {

		// Create customer test data via api nis call
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// returning to selenium testing
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, email);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);

		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		Utils.waitTime(5000);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, PAYMENT_TYPE);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		Utils.waitTime(5000);

		// click delete funding source
		nPage3.clickFundingSourceExpand(driver);
		Utils.waitTime(3000);
		nPage3.deleteFundingSource(driver);
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