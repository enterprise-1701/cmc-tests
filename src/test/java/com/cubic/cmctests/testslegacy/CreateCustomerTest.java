package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.utils.*;
import com.cubic.cmctests.testslegacy.CoreTest;

//#################################################################################
//
//#################################################################################

public class CreateCustomerTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String phoneNumber;
	private static String email;

	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	UserData userData = new UserData();
	boolean saveEmail = true;
	RESTActions restActions;

	@Parameters({"browser", "executionenv"})
	@BeforeMethod
	public void setUp(String browser, String executionenv) throws InterruptedException {

		//Logging.setLogConsole();
		//Logging.setLogFile();
		//Log.info("Setup Started");
		//Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
		//Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser, executionenv);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		//Log.info("Setup Completed");
	}

	// pass
	@Test(priority = 1, enabled = true)
	public void createNewCustomer(ITestContext context) throws Exception {
		String testCaseName = "29920:createNewCustomer";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29920");
			coreTest.signIn(driver);
			coreTest.createCustomer(driver);
			email = coreTest.getEmail();
			phoneNumber = coreTest.getPhone();
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Assert.assertEquals(nPage3.getFname(driver), Global.FNAME);
			Log.info("Actual results " + nPage3.getFname(driver) + " matches expected results " + Global.FNAME);
			Assert.assertEquals(nPage3.getLname(driver), Global.LNAME);
			Log.info("Actual results " + nPage3.getLname(driver) + " matches expected results " + Global.LNAME);
			Assert.assertEquals(nPage3.getEmail(driver), email);
			Log.info("Actual results " + nPage3.getEmail(driver) + " matches expected results " + email);
			Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
			Log.info("Actual results " + nPage3.getPhone(driver) + " matches expected results " + phoneNumber);
			//Assert.assertEquals(nPage3.getAddress1(driver), Global.COMPLETE_ADDRESS);
			//Log.info("Actual results " + nPage3.getAddress1(driver) + " matches expected results " + Global.COMPLETE_ADDRESS);
			Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + nPage3.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 2, enabled = true)
	public void createCustomerCancel(ITestContext context) throws Exception {
		String testCaseName = "29913:createCustomerCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29913");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			Utils.waitTime(7000);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			CreateCustomerPage nPage = new CreateCustomerPage(driver);
			nPage.clickNewCustomer(driver);
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			nPage.enterFirstname(driver, Global.FNAME);
			nPage.clickCancel(driver);
			nPage.clickNewCustomer(driver);
			Assert.assertEquals(nPage.getFirstname(driver), "");
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			nPage.enterFirstname(driver, Global.FNAME);
			nPage.enterLastname(driver, Global.LNAME);
			email = Utils.randomEmailString();
			nPage.enterEmail(driver, email);
			phoneNumber = Utils.randomPhoneNumber();
			nPage.enterPhone(driver, phoneNumber);
			nPage.clickContinue(driver);
			NewCustomerPage nPaget = new NewCustomerPage(driver);
			nPaget.clickCancel(driver);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 3, enabled = true)
	public void createCustomerContactTypeNotSelected(ITestContext context) throws Exception {
		String testCaseName = "29914:createCustomerContactTypeNotSelected";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29914");
			coreTest.signIn(driver);
			NewCustomerPage nPage = createCustomerFirstPage(true);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.selectState(driver);
			nPage.enterPostalCode(driver, Global.POSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.selectSecurityQ(driver);
			nPage.enterSecuirtyA(driver, Global.SECURITYA);
			nPage.enterUserName(driver, Utils.randomUsernameString());
			nPage.enterPin(driver, Global.PIN);
			nPage.clickPinLabel(driver);
			nPage.enterDob(driver, Global.DOB);
			Assert.assertFalse(nPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 4, enabled = true)
	public void createCustomerInvalidEmail(ITestContext context) throws Exception {
		String testCaseName = "29916:createCustomerInvalidEmail";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29916");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			Utils.waitTime(5000);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			CreateCustomerPage nPage = new CreateCustomerPage(driver);
			nPage.clickNewCustomer(driver);
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			nPage.enterFirstname(driver, Global.FNAME);
			nPage.enterLastname(driver, Global.LNAME);
			nPage.enterEmail(driver, Utils.randomUsernameString());
			Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
			Assert.assertEquals(nPage.getEmailError(driver), Global.INVALID_EMAIL);
			nPage.enterEmail(driver, Utils.randomEmailString());
			phoneNumber = "8589778888";
			nPage.enterPhone(driver, phoneNumber);
			nPage.clickContinue(driver);
			NewCustomerPage nPaget = new NewCustomerPage(driver);
			nPaget.selectContactType(driver, Global.CONTACTTYPE);
			nPaget.selectCountry(driver);
			nPaget.enterAddress(driver, Global.ADDRESS);
			nPaget.enterCity(driver, Global.CITY);
			nPaget.enterPostalCode(driver, Global.POSTAL);
			nPaget.enterEmail(driver, Utils.randomUsernameString());
			Assert.assertFalse(nPaget.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Assert.assertEquals(nPaget.getEmailError(driver), Global.INVALID_EMAIL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 5, enabled = true)
	public void createCustomerInvalidPin(ITestContext context) throws Exception {
		String testCaseName = "29918:createCustomerInvalidPIN";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29918");
			coreTest.signIn(driver);
			NewCustomerPage nPage = createCustomerFirstPage(true);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.enterPostalCode(driver, Global.POSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.selectSecurityQ(driver);
			nPage.enterSecuirtyA(driver, Global.SECURITYA);
			nPage.enterUserName(driver, Utils.randomUsernameString());
			nPage.enterPin(driver, Global.BAD_PIN);
			Assert.assertEquals(nPage.getPinError(driver), Global.PIN_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 6, enabled = true)
	public void createCustomerInvalidUserName(ITestContext context) throws Exception {
		String testCaseName = "29919:createCustomerInvalidUserName";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29919");
			coreTest.signIn(driver);
			NewCustomerPage nPage = createCustomerFirstPage(true);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.enterPostalCode(driver, Global.POSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.enterUserName(driver, Global.USER);
			Assert.assertEquals(nPage.getUserNameError(driver), Global.INVALID_EMAIL);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pass
	@Test(priority = 7, enabled = true)
	public void createCustomerDuplicateUserName(ITestContext context) throws Exception {
		String testCaseName = "29915:createCustomerDuplicateUserName";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29915");
			coreTest.signIn(driver);
			coreTest.createCustomerClickHome(driver);

			// Sign back in and attempt to create a second customer with duplicate
			// email
			saveEmail = false;
			NewCustomerPage nPage = createCustomerFirstPage(saveEmail);

			nPage.selectContactType(driver, Global.CONTACTTYPE);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.selectState(driver);
			nPage.enterPostalCode(driver, Global.POSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.selectSecurityQ(driver);
			nPage.enterSecuirtyA(driver, Global.SECURITYA);

			coreTest.getUserData();
			// using email address for username based on new nextwave api
			Log.info("email being retreived is: " + userData.getEmail());
			coreTest.getUserData();
			nPage.enterUserName(driver, userData.getEmail());
			nPage.enterPin(driver, Global.PIN);
			nPage.clickPinLabel(driver);
			nPage.enterDob(driver, Global.DOB);
			nPage.clickSubmit(driver);
			Assert.assertEquals(nPage.getDuplicateError(driver), Global.DUPLICATE_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 8, enabled = true)
	public void createCustomerInvalidPhone(ITestContext context) throws Exception {
		String testCaseName = "29917:createCustomerInvalidPhone";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29917");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			CreateCustomerPage nPage = new CreateCustomerPage(driver);
			nPage.clickNewCustomer(driver);
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			nPage.enterFirstname(driver, Global.FNAME);
			nPage.enterLastname(driver, Global.LNAME);
			email = Utils.randomEmailString();
			nPage.enterEmail(driver, email);
			nPage.enterPhone(driver, Global.INVALID_PHONE);
			// Assert.assertEquals(nPage.g(driver), Global.INVALID_PHONE );
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
	public void createCustomerNameMissing(ITestContext context) throws Exception {
		String testCaseName = "185951:createCustomerNameMissing";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185951");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			CreateCustomerPage nPage = new CreateCustomerPage(driver);
			nPage.clickNewCustomer(driver);
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			email = Utils.randomEmailString();
			nPage.enterEmail(driver, email);
			Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
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
	public void createCustomerEmailMissing(ITestContext context) throws Exception {
		String testCaseName = "185952:createCustomerEmailMissing";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185952");
			coreTest.signIn(driver);
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
			CreateCustomerPage nPage = new CreateCustomerPage(driver);
			nPage.clickNewCustomer(driver);
			nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
			nPage.enterFirstname(driver, Global.FNAME);
			nPage.enterLastname(driver, Global.LNAME);
			Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// pin should not contain user name
	@Test(priority = 11, enabled = true)
	public void createCustomerInvalidPinHasUserName(ITestContext context) throws Exception {
		String testCaseName = "185953:createCustomerInvalidPinHasUserName";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185953");
			coreTest.signIn(driver);
			NewCustomerPage nPage = createCustomerFirstPage(true);
			nPage.selectContactType(driver, Global.CONTACTTYPE);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.selectState(driver);
			nPage.enterPostalCode(driver, Global.POSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.selectSecurityQ(driver);
			nPage.enterSecuirtyA(driver, Global.SECURITYA);
			nPage.enterUserName(driver, "test1234@yahoo.com");
			nPage.enterPin(driver, "1234");
			nPage.clickPinLabel(driver);
			nPage.enterDob(driver, Global.DOB);
			nPage.clickSubmit(driver);
			Utils.waitTime(5000);
			Assert.assertEquals(nPage.getPinError2(driver), Global.PIN_ERROR2);
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
	public void createCustomerPostalCodeMissing(ITestContext context) throws Exception {
		String testCaseName = "185954:createCustomerPostalCodeMissing";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185954");
			coreTest.signIn(driver);
			NewCustomerPage nPage = createCustomerFirstPage(true);
			nPage.selectContactType(driver, Global.CONTACTTYPE);
			nPage.selectCountry(driver);
			nPage.enterAddress(driver, Global.ADDRESS);
			nPage.enterCity(driver, Global.CITY);
			nPage.selectState(driver);
			// postal code missing
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			nPage.selectPhoneType(driver, Global.PHONETYPE);
			nPage.selectSecurityQ(driver);
			nPage.enterSecuirtyA(driver, Global.SECURITYA);
			nPage.enterUserName(driver, "adam@yahoo.com");
			nPage.enterPin(driver, Global.PIN);
			nPage.clickPinLabel(driver);
			nPage.enterDob(driver, Global.DOB);
			Assert.assertFalse(nPage.isSubmitEnabled(driver), "Submit button should be disabled");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// CCD-584 - Using + in gmail account
	@Test(priority = 13, enabled = true)
	public void createNewCustomerGmailPlus(ITestContext context) throws Exception {
		String testCaseName = "185955:createNewCustomerGmailPlus";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185955");
			coreTest.signIn(driver);
			email = Utils.randomEmailStringGmailPlus();
			coreTest.createCustomer(driver, email);

			Log.info("gmail plus generated is: " + email);
			phoneNumber = "(858) 977-8888";
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Assert.assertEquals(nPage3.getFname(driver), Global.FNAME);
			Log.info("Actual results " + nPage3.getFname(driver) + " matches expected results " + Global.FNAME);
			Assert.assertEquals(nPage3.getLname(driver), Global.LNAME);
			Log.info("Actual results " + nPage3.getLname(driver) + " matches expected results " + Global.LNAME);
			Assert.assertEquals(nPage3.getEmail(driver), email);
			Log.info("Actual results " + nPage3.getEmail(driver) + " matches expected results " + email);
			Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
			Log.info("Actual results " + nPage3.getPhone(driver) + " matches expected results " + phoneNumber);
			Assert.assertEquals(nPage3.getAddress1(driver), Global.COMPLETE_ADDRESS);
			Log.info("Actual results " + nPage3.getAddress1(driver) + " matches expected results "
					+ Global.COMPLETE_ADDRESS);
			Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), Global.ADDRESS);
			Log.info("Actual results " + nPage3.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

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
	private NewCustomerPage createCustomerFirstPage(boolean saveEmail) throws Exception {

		if (saveEmail == true) {
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			Utils.waitTime(5000);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
		}

		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);

		if (saveEmail == true) {
			System.out.println("inside the if statement " + saveEmail);
			userData.setEmail(email);
		}

		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		nPage.clickContinue(driver);
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		return nPaget;

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}