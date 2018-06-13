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

public class UpdateAddressTest extends RESTEngine {

	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	private static Logger Log = Logger.getLogger(Logger.class.getName());
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	RESTActions restActions;

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws InterruptedException {

//		Logging.setLogConsole();
//		Logging.setLogFile();
//		Log.info("Setup Started");
//		Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
//		Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//		Log.info("Setup Completed");
	}

	// Create new customer, search for that customer and enter new address
	@Test(priority = 1, enabled = true)
	public void addNewAddress(ITestContext context) throws Exception {
		String testCaseName = "29975:addNewAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29975");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getEmail(driver), email);
			Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			sPage.clickNewAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.NEWADDRESS);
			addPage.enterCity(driver, Global.NEWCITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, Global.NEWPOSTAL);
			addPage.clickSubmit(driver);
			Utils.waitTime(7000);
			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);

			Assert.assertEquals(sPage.getNewAddress(driver), Global.NEWADDRESS);
			Assert.assertEquals(sPage.getNewCity(driver), Global.NEWCITY);
			Assert.assertEquals(sPage.getNewPostalCode(driver), Global.NEWPOSTAL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// test after canceling new address is not created
	@Test(priority = 2, enabled = true)
	public void addNewAddressCancel(ITestContext context) throws Exception {
		String testCaseName = "934077:addNewAddressCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("934077");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(15000);

			sPage.clickNewAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.NEWADDRESS);
			addPage.enterCity(driver, Global.NEWCITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, Global.NEWPOSTAL);
			addPage.clickCancel(driver);

			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);
			// checking for null here since there should be no new address inserted
			Assert.assertEquals(sPage.getNewAddress(driver), null);
			Assert.assertEquals(sPage.getNewCity(driver), null);
			Assert.assertEquals(sPage.getNewPostalCode(driver), null);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer and update address
	// Failing due to CCBO-8324
	@Test(priority = 3, enabled = true)
	public void updateAddress(ITestContext context) throws Exception {
		String testCaseName = "185986:updateAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185986");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);

			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Log.info("Actual results " + sPage.getPhone(driver) + " matches " + coreTest.getPhone());
			Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);
			sPage.clickInsideAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.NEWADDRESS);
			addPage.enterCity(driver, Global.NEWCITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, Global.NEWPOSTAL);
			Utils.waitTime(3000);
			addPage.clickSubmit(driver);
			Utils.waitTime(3000);

			String sNewAddress = sPage.getUpdatedAddress(driver);
			Assert.assertEquals(sNewAddress, Global.NEWADDRESS);
			Assert.assertEquals(sPage.getCity(driver), Global.NEWCITY);
			Assert.assertEquals(sPage.getZip(driver), Global.NEWPOSTAL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Cancel update address and test that address stays the same
	@Test(priority = 4, enabled = true)
	public void updateAddressCancel(ITestContext context) throws Exception {
		String testCaseName = "185987:updateAddressCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185987");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(5000);
			sPage.clickExpandAddress(driver);
			Utils.waitTime(5000);
			sPage.clickInsideAddress(driver);
			Utils.waitTime(5000);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.NEWADDRESS);
			addPage.enterCity(driver, Global.NEWCITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, Global.NEWPOSTAL);
			Utils.waitTime(5000);
			addPage.clickCancel(driver);
			Utils.waitTime(5000);

			Assert.assertEquals(sPage.getUpdatedAddress(driver), Global.ADDRESS);
			Assert.assertEquals(sPage.getCity(driver), Global.CITY);
			Assert.assertEquals(sPage.getZip(driver), Global.POSTAL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Negative test cases
	@Test(priority = 5, enabled = true)
	public void addNewAddressCheckRequiredFields(ITestContext context) throws Exception {
		String testCaseName = "185988:addNewAddressCheckRequiredFields";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185988");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(5000);
			sPage.clickNewAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);

			// country not selected
			addPage.selectCountry(driver);
			addPage.selectNoCountry(driver);
			Utils.waitTime(3000);
			Assert.assertFalse(addPage.isSubmitEnabled(driver));
			addPage.selectCountry(driver);

			// Input a value for address and delete it
			addPage.enterAddress(driver, "x");
			addPage.deleteAddress(driver);
			Assert.assertFalse(addPage.isSubmitEnabled(driver));
			addPage.enterAddress(driver, Global.NEWADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}


	@Test(priority = 6, enabled = true)
	public void addNewAddressInvalidData(ITestContext context) throws Exception {
		String testCaseName = "185989:addNewAddressInvalidData";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185989");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);

			Utils.waitTime(7000);
			sPage.clickNewAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.NEWADDRESS);
			addPage.enterCity(driver, Global.NEWCITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, "1");
			addPage.clickSubmit(driver);
			Assert.assertEquals(addPage.getPostalError(driver), "Invalid zip code.");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void addNewAddressDuplicateAddress(ITestContext context) throws Exception {
		String testCaseName = "29976:addNewAddressDuplicateAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29976");
			createNewCustomer(driver);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(15000);
			sPage.clickNewAddress(driver);
			AddressDetailsPage addPage = new AddressDetailsPage(driver);
			addPage.selectCountry(driver);
			addPage.enterAddress(driver, Global.ADDRESS);
			addPage.enterCity(driver, Global.CITY);
			addPage.selectState(driver);
			addPage.enterPostalCode(driver, Global.POSTAL);
			addPage.clickSubmit(driver);
			Utils.waitTime(15000);
			Assert.assertEquals(addPage.getAddressError(driver), Global.ADDRESS_ERROR);
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
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(8000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
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