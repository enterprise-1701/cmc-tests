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

public class SearchTest2 extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String CTYPE = "Primary";
	private static final String DUPLICATE_FNAME = "robert";
	private static final String DUPLICATE_LNAME = "downton";
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;

	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	UserData userData = new UserData();
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

	@Test(priority = 8, enabled = true)
	public void searchCustomerCheckDuplicateTestFnameLname(ITestContext context) throws Exception {
		String testCaseName = "185974:searchCustomerCheckDuplicateTestFnameLname";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185974");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, DUPLICATE_FNAME);
			sPage.enterLastname(driver, DUPLICATE_LNAME);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
			Assert.assertEquals(sPage.getLastName(driver), DUPLICATE_LNAME);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 9, enabled = true)
	public void searchCustomerCheckDuplicateTestPhone(ITestContext context) throws Exception {
		String testCaseName = "185976:searchCustomerCheckDuplicateTestPhone";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185976");
			createNewCustomer();
			Log.info("phone being retrieved is: " + coreTest.getPhone());
			SearchPage sPage = getSearchTab();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterPhone(driver, coreTest.getPhone());
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			phoneNumber = coreTest.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 10, enabled = true)
	public void searchCustomerCheckDuplicateTestEmail(ITestContext context) throws Exception {
		String testCaseName = "185977:searchCustomerCheckDuplicateTestEmail";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185977");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, coreTest.getEmail());
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 11, enabled = true)
	public void searchCustomerCheckDuplicateTestEmailPhone(ITestContext context) throws Exception {
		String testCaseName = "185978:searchCustomerCheckDuplicateTestEmailPhone";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185978");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			Utils.waitTime(5000);
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, coreTest.getEmail());
			sPage.enterPhone(driver, coreTest.getPhone());
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			phoneNumber = coreTest.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 12, enabled = true)
	public void searchCustomerCheckDuplicateTestAll(ITestContext context) throws Exception {
		String testCaseName = "185979:searchCustomerDuplicateTestAll";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185979");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, DUPLICATE_FNAME);
			sPage.enterLastname(driver, DUPLICATE_LNAME);
			sPage.enterEmail(driver, coreTest.getEmail());
			sPage.enterPhone(driver, coreTest.getPhone());
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
			Assert.assertEquals(sPage.getLastName(driver), DUPLICATE_LNAME);
			phoneNumber = coreTest.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// TODO: Find test case number
	// Test new search button on upper right of screen
	@Test(priority = 13, enabled = true)
	public void searchNewSearchButton(ITestContext context) throws Exception {
		String testCaseName = "815096:searchNewSearchButton";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("815096");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			sPage.clickNewCustomer(driver);
			CreateCustomerPage cPage = new CreateCustomerPage(driver);
			Assert.assertEquals(cPage.getCustomerLegend(driver), "Customer: Create Customer");
			sPage.clickHeaderSearch(driver);
			Assert.assertEquals(sPage.getSearchLegend(driver), "Customer: Search");
			sPage.clickCustomerType(driver, "Individual");
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Dynamic Search
	@Test(priority = 14, enabled = true)
	public void searchCustomerVerifiedThreeInfoTest(ITestContext context) throws Exception {
		String testCaseName = "185980:searchCustomerVerifiedThreeInfoTest";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185980");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

			// returning to selenium testing
			coreTest.signIn(driver);
			Utils.waitTime(10000);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			sPage.clickRecord(driver);
			sPage.clickNameBox(driver);
			sPage.clickAddressBox(driver);
			sPage.clickDobBox(driver);
			sPage.clickContiune(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getContactTypeTableTwo(driver), CTYPE);
			Log.info("Actual results " + sPage.getContactTypeTableTwo(driver) + " matches " + CTYPE);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// negative test case for searchCustomerVerifiedThreeInfoTest
	@Test(priority = 15, enabled = true)
	public void searchCustomerNotVerifiedThreeInfoTest(ITestContext context) throws Exception {
		String testCaseName = "185981:searchCustomerNotVerifiedThreeInfoTest";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185981");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

			// returning to selenium testing
			coreTest.signIn(driver);
			Utils.waitTime(10000);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			sPage.clickRecord(driver);
			sPage.clickNameBox(driver);
			sPage.clickAddressBox(driver);
			Assert.assertFalse(sPage.isContinueEnabled(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// clicking not verified button should still take the CSR to the customer
	// information page
	@Test(priority = 16, enabled = true)
	public void searchCustomerClickNotVerified(ITestContext context) throws Exception {
		String testCaseName = "185981:searchCustomerClickNotVerified";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185981");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);
			Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

			// returning to selenium testing
			coreTest.signIn(driver);
			Utils.waitTime(10000);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
			sPage.clickSearch(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			sPage.clickRecord(driver);
			sPage.clickNotVerified(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			Log.info("Actual results " + sPage.getPhone(driver) + " matches " + phoneNumber);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// private methods
	private void createNewCustomer() throws Exception {
		coreTest.signIn(driver);
		coreTest.createCustomerClickHomeTwo(driver);
	}

	private SearchPage getSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		SearchPage sPage = new SearchPage(driver);
		return sPage;
	}

	private SearchPage getSearchTab() throws Exception {
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