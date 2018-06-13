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

public class SearchTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String NORECORD = "No records found";
	private static final String CRTYPE = "Individual";
	private static final String CTYPE = "Primary";
	private static final String DUPLICATE_FNAME = "robert";
	private static final String DUPLICATE_LNAME = "downton";
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	private static String postalCode;
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

	// Dynamic Search
	@Test(priority = 1, enabled = false)
	public void searchCustomerVerifiedTest(ITestContext context) throws Exception {
		String testCaseName = "29961:searchCustomerVerifiedTest";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29961");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6, 10);
			Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

			// returning to selenium testing
			coreTest.signIn(driver);
			Utils.waitTime(10000);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			sPage.clickSearch(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			Log.info("Actual results " + sPage.getPhone(driver) + " matches " + phoneNumber);
			Assert.assertEquals(sPage.getCustomerType(driver), CRTYPE);
			Log.info("Actual results " + sPage.getCustomerType(driver) + " matches " + CRTYPE);
			Assert.assertEquals(sPage.getContactType(driver), CTYPE);
			Log.info("Actual results " + sPage.getContactType(driver) + " matches " + CTYPE);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			sPage.clickRecord(driver);
			sPage.clickSecurityBox(driver);
			sPage.clickContiune(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
			Log.info("Actual results " + sPage.getPhone(driver) + " matches " + phoneNumber);
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

	@Test(priority = 2, enabled = false)
	public void searchCustomerNotVerifiedTest(ITestContext context) throws Exception {
		String testCaseName = "185971:searchCustomerNotVerfiedTest";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185971");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, Global.FNAME);
			sPage.enterLastname(driver, Global.LNAME);
			sPage.clickSearch(driver);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			Utils.waitTime(5000);
			sPage.clickRecord(driver);
			sPage.clickNotVerified(driver);
			Assert.assertTrue(sPage.isRecordPresent(driver), "customer records are not being displayed!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 3, enabled = false)
	public void searchCustomerNoResultsTest(ITestContext context) throws Exception {
		String testCaseName = "29963:searchCustomerNoResultsTest";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29963");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, Global.FNAME2);
			sPage.enterLastname(driver, Global.LNAME2);
			sPage.enterEmail(driver, Global.EMAIL);
			sPage.enterPhone(driver, Global.PHONE2);
			sPage.clickSearch(driver);
			Assert.assertEquals(sPage.getNoRecordFound(driver), NORECORD);
			Log.info("Actual results " + sPage.getNoRecordFound(driver) + " matches expected results " + NORECORD);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 4, enabled = true)
	public void searchCustomerTypeNotSelected(ITestContext context) throws Exception {
		String testCaseName = "29964:searchCustomerTypeNotSelected";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29964");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			SearchPage sPage = new SearchPage(driver);
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerInvalidType(driver);
			sPage.enterFirstname(driver, Global.FNAME2);
			sPage.enterLastname(driver, Global.LNAME2);
			sPage.enterEmail(driver, Global.EMAIL);
			sPage.enterPhone(driver, Global.PHONE2);
			Utils.waitTime(3000);
			Assert.assertFalse(sPage.isSearchEnabled(driver), "Search button should not be enabled!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}
	
	
	@Test(priority = 5, enabled = false)
	public void searchCustomerInvalidEmail(ITestContext context) throws Exception {
		String testCaseName = "29962:searchCustomerInvalidEmail";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29962");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, Global.FNAME);
			sPage.enterLastname(driver, Global.LNAME);
			sPage.enterEmail(driver, Global.FNAME);
			Assert.assertEquals(sPage.getEmailError(driver), Global.INVALID_EMAIL);
			Assert.assertFalse(sPage.isSearchEnabled(driver), "Search button should not be enabled!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 6, enabled = false)
	public void searchCustomerCheckDuplicateTestFname(ITestContext context) throws Exception {
		String testCaseName = "185972:searchCustomerCheckDuplicateTestFname";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185972");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterFirstname(driver, DUPLICATE_FNAME);
			sPage.clickSearch(driver);
			Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 7, enabled = false)
	public void searchCustomerCheckDuplicateTestLname(ITestContext context) throws Exception {
		String testCaseName = "185973:searchCustomerCheckDuplicateTestLname";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185973");
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterLastname(driver, DUPLICATE_LNAME);
			sPage.clickSearch(driver);
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
	

	// Postal code Search
	@Test(priority = 8, enabled = false)
	public void searchCustomerPostalCode(ITestContext context) throws Exception {
		String testCaseName = "29965:searchCustomerPostalCode";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29965");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			postalCode = cData.getPostal();
			Log.info("Email, phone number and postal code from API:  " + email + " " + phoneNumber + " " + postalCode);

			// returning to selenium testing use data from API to search for
			// customer
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			sPage.enterPostalCode(driver, postalCode);
			sPage.clickSearch(driver);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			Utils.waitTime(5000);
			sPage.clickRecord(driver);
			sPage.clickSecurityBox(driver);
			sPage.clickContiune(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getContactTypeTableTwo(driver), CTYPE);
			Log.info("Actual results " + sPage.getContactTypeTableTwo(driver) + " matches " + CTYPE);
			Log.info("Postal Code retrieved: " + sPage.getAddress(driver).substring(29, 34));
			Assert.assertEquals(sPage.getAddress(driver).substring(29, 34), Global.POSTAL);
			Log.info("Actual results " + sPage.getAddress(driver).substring(29, 34) + " matches " + Global.POSTAL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Postal code Search error
	@Test(priority = 9, enabled = false)
	public void searchCustomerPostalCodeError(ITestContext context) throws Exception {
		String testCaseName = "29966:searchCustomerPostalCodeError";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29966");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			postalCode = cData.getPostal();
			Log.info("Email, phone number and postal code from API:  " + email + " " + phoneNumber + " " + postalCode);

			// returning to selenium testing use data from API to search for
			// customer
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterPostalCode(driver, postalCode);
			sPage.clickSearch(driver);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			Assert.assertEquals(sPage.getPostalCodeError(driver), Global.POSTALCODE_SEARCH_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Postal code Search new search
	@Test(priority = 10, enabled = false)
	public void searchCustomerPostalCodeNewSearch(ITestContext context) throws Exception {
		String testCaseName = "29967:searchCustomerPostalCodeNewSearch";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29967");
			// Create customer test data via api rest call
			cData = ApiCustomerPost.apiPostSuccess();
			email = cData.getEmail();
			phoneNumber = cData.getPhone();
			postalCode = cData.getPostal();
			Log.info("Email, phone number and postal code from API:  " + email + " " + phoneNumber + " " + postalCode);

			// returning to selenium testing use data from API to search for
			// customer
			coreTest.signIn(driver);
			SearchPage sPage = getSearchPage();
			sPage.selectSearchTypeCustomer(driver);
			sPage.clickCustomerType(driver, "Individual");
			sPage.enterEmail(driver, email);
			sPage.enterPostalCode(driver, postalCode);
			sPage.clickNewSearch(driver);
			Assert.assertEquals(sPage.getPostalCode(driver), "");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
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