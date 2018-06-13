package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import org.apache.log4j.Logger;
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

import org.openqa.selenium.JavascriptExecutor;

//#################################################################################
//
//#################################################################################

public class CreateFundingTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String EXPIRATION = "01/2022";
	private static final String CARDTYPE = "Active";
	private static final String POSTAL = "92122";
	private static final Integer PAYMENT_TYPE = 1;
	private static CustomerData cData;
	private static String phoneNumber;
	private static String email;
	private static boolean address = true;
	static WebDriver driver;
	static String browser;
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

	@Test(priority = 1, enabled = false)
	public void createFundingSource(ITestContext context) throws Exception {
		String testCaseName = "29927:createFundingSource";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29927");
			createNewCustomer(driver);
			// create funding source using UIf
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(8000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);
			Assert.assertEquals(cPage.getCardType(driver), Global.CCTYPE);
			Log.info("Actual results " + cPage.getCardType(driver) + " matches expected results " + Global.CCTYPE);
			Assert.assertEquals(cPage.getCardNumber(driver), Global.CCMASKED);
			Log.info("Actual results " + cPage.getCardNumber(driver) + " matches expected results " + Global.CCMASKED);
			Assert.assertEquals(cPage.getCardExpiration(driver), EXPIRATION);
			Log.info("Actual results " + cPage.getCardExpiration(driver) + " matches expected results " + EXPIRATION);
			Assert.assertEquals(cPage.getCardStatus(driver), CARDTYPE);
			Log.info("Actual results " + cPage.getCardStatus(driver) + " matches expected results " + CARDTYPE);
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
	public void createFundingSourceNewBillingAddress(ITestContext context) throws Exception {
		String testCaseName = "29921:createFundingSourceNewBillingAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29921");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickNewAddress(driver);
			cPage.selectCountry(driver);
			cPage.enterNewBillingAddress(driver, Global.NEWADDRESS);
			cPage.enterCity(driver, Global.NEWCITY);
			cPage.selectState(driver);
			cPage.enterPostalCode(driver, POSTAL);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);

			Assert.assertEquals(cPage.getCCname(driver), Global.CCNAME);
			Log.info("Actual results " + cPage.getCCname(driver) + " matches expected results " + Global.CCNAME);
			Assert.assertEquals(cPage.getCardType(driver), Global.CCTYPE);
			Log.info("Actual results " + cPage.getCardType(driver) + " matches expected results " + Global.CCTYPE);
			Assert.assertEquals(cPage.getCardNumber(driver), Global.CCMASKED);
			Log.info("Actual results " + cPage.getCardNumber(driver) + " matches expected results " + Global.CCMASKED);
			Assert.assertEquals(cPage.getCardExpiration(driver), EXPIRATION);
			Log.info("Actual results " + cPage.getCardExpiration(driver) + " matches expected results " + EXPIRATION);
			Assert.assertEquals(cPage.getCardStatus(driver), CARDTYPE);
			Log.info("Actual results " + cPage.getCardStatus(driver) + " matches expected results " + CARDTYPE);
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
	public void createFundingSourceCancel(ITestContext context) throws Exception {
		String testCaseName = "29922:createFundingSourceCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29922");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickCancel(driver);
			Utils.waitTime(7000);
			Assert.assertFalse(cPage.isNameOnCardDisplayed(driver), "name on card should not be displayed!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}


	@Test(priority = 4, enabled = false)
	public void createFundingSourceTypeNotSelected(ITestContext context) throws Exception {
		String testCaseName = "29926:createFundingSourceTypeNotSelected";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29926");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, 0);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			Assert.assertEquals(cPage.getFieldError(driver), Global.REQUIREDFIELD_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 5, enabled = true)
	public void createCustomerInvalidEmail(ITestContext context) throws Exception {
		String testCaseName = "185956:createCustomerInvalidEmail";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185956");
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
			nPage.enterEmail(driver, Utils.randomUsernameString());
			Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
			Assert.assertEquals(nPage.getEmailError(driver), Global.INVALID_EMAIL);
			nPage.enterEmail(driver, Utils.randomEmailString());
			nPage.enterPhone(driver, Utils.randomPhoneNumber());
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

	@Test(priority = 6, enabled = false)
	public void createCustomerFundingInvalidCC(ITestContext context) throws Exception {
		String testCaseName = "29923:createCustomerFundingInvalidCC";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29923");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.INVALID_CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(cPage.getCCError(driver), "Card Number does not match with credit card type.");
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
	public void createCustomerFundingInvalidName(ITestContext context) throws Exception {
		String testCaseName = "185957:createCustomerFundingInvalidName";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185957");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertFalse(cPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 8, enabled = false)
	public void createFundingSourceInvalidPanTooLong(ITestContext context) throws Exception {
		String testCaseName = "185958:createFundingSourceInvalidPanTooLong";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185958");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, "41111112222222222222");
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(cPage.getCCPanError(driver), Global.PAN_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	
	@Test(priority = 9, enabled = false)
	public void createFundingSourceInvalidCCType(ITestContext context) throws Exception {
		String testCaseName = "934079:createFundingSourceInvalidCCType";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("934079");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			Utils.waitTime(3000);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCCwithSpace(driver, "411111111111111");
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(cPage.getCCPanError(driver), Global.PAN_ERROR2);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 10, enabled = false)
	public void createFundingSourceInvalidExpirationDate(ITestContext context) throws Exception {
		String testCaseName = "185960:createFundingSourceInvalidExpirationDate";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185960");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectInvalidMonth(driver);
			cPage.selectInvalidYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(cPage.getCCPanError(driver), Global.PAN_ERROR3);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Test case for second funding source with a different cc
	@Test(priority = 11, enabled = false)
	public void createFundingSourceTwoCards(ITestContext context) throws Exception {
		String testCaseName = "185961:createFundingSourceTwoCards";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185961");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);

			Assert.assertEquals(cPage.getCardType(driver), Global.CCTYPE);
			Log.info("Actual results " + cPage.getCardType(driver) + " matches expected results " + Global.CCTYPE);
			Assert.assertEquals(cPage.getCardNumber(driver), Global.CCMASKED);
			Log.info("Actual results " + cPage.getCardNumber(driver) + " matches expected results " + Global.CCMASKED);
			Assert.assertEquals(cPage.getCardExpiration(driver), EXPIRATION);
			Log.info("Actual results " + cPage.getCardExpiration(driver) + " matches expected results " + EXPIRATION);
			Assert.assertEquals(cPage.getCardStatus(driver), CARDTYPE);
			Log.info("Actual results " + cPage.getCardStatus(driver) + " matches expected results " + CARDTYPE);

			nPage3.clickFundingSource(driver);
			cPage.enterName(driver, Global.CCNAME2);
			cPage.enterCC(driver, Global.CC2);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);

			// Assertions on second card
			Assert.assertEquals(cPage.getCardType2(driver), Global.CCTYPE);
			Log.info("Actual results " + cPage.getCardType2(driver) + " matches expected results " + Global.CCTYPE);
			Assert.assertEquals(cPage.getCardNumber2(driver), Global.CCMASKED2);
			Log.info("Actual results " + cPage.getCardNumber2(driver) + " matches expected results " + Global.CCMASKED2);
			Assert.assertEquals(cPage.getCardExpiration2(driver), EXPIRATION);
			Log.info("Actual results " + cPage.getCardExpiration2(driver) + " matches expected results " + EXPIRATION);
			Assert.assertEquals(cPage.getCardStatus2(driver), CARDTYPE);
			Log.info("Actual results " + cPage.getCardStatus2(driver) + " matches expected results " + CARDTYPE);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Test case for changing primary card
	@Test(priority = 12, enabled = false)
	public void createFundingSourceChangePrimaryCard(ITestContext context) throws Exception {
		String testCaseName = "29924:createFundingSourceChangePrimaryCard";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29924");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(8000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(5000);

			nPage3.clickFundingSource(driver);
			cPage.enterName(driver, Global.CCNAME2);
			cPage.enterCC(driver, Global.CC2);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSetPrimary(driver);
			cPage.clickYesWarning(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);
			cPage.clickCardNumber2(driver);

			// Assertions on second card
			Assert.assertTrue(cPage.isSetPrimaryEnabled(driver), "Set primary should not be enabled");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Test case for attempting to change primary card and canceling
	@Test(priority = 13, enabled = false)
	public void createFundingSourceChangePrimaryCardCancel(ITestContext context) throws Exception {
		String testCaseName = "29925:createFundingSourceChangePrimaryCardCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29925");
			createNewCustomer(driver);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(8000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(8000);

			nPage3.clickFundingSource(driver);
			cPage.enterName(driver, Global.CCNAME2);
			cPage.enterCC(driver, Global.CC2);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSetPrimary(driver);
			cPage.clickNoWarning(driver);
			cPage.clickCancel(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);

			// Assertions on set as primary check box
			Assert.assertTrue(cPage.isSetPrimaryEnabled(driver), "Set primary should not be enabled");


			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// fail due to CCBO-7819
	// #################################################################################
	// Fix verification for CCD-853
	// #################################################################################
	@Test(priority = 14, enabled = false)
	public void createFundingNoAddress(ITestContext context) throws Exception {
		String testCaseName = "185962:createFundingNoAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185962");
			address = false;
			createNewCustomer(driver, address);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(3000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, PAYMENT_TYPE);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickNewAddress(driver);
			cPage.selectCountry(driver);
			cPage.enterNewBillingAddress(driver, Global.NEWADDRESS);
			cPage.enterCity(driver, Global.NEWCITY);
			cPage.selectState(driver);
			cPage.enterPostalCode(driver, POSTAL);
			cPage.clickSubmit(driver);
			Utils.waitTime(7000);
			nPage3.clickFundingSourceExpand(driver);
			Utils.waitTime(3000);

			Assert.assertEquals(cPage.getCCname(driver), Global.CCNAME);
			Log.info("Actual results " + cPage.getCCname(driver) + " matches expected results " + Global.CCNAME);
			Assert.assertEquals(cPage.getCardType(driver), Global.CCTYPE);
			Log.info("Actual results " + cPage.getCardType(driver) + " matches expected results " + Global.CCTYPE);
			Assert.assertEquals(cPage.getCardNumber(driver), Global.CCMASKED);
			Log.info("Actual results " + cPage.getCardNumber(driver) + " matches expected results " + Global.CCMASKED);
			Assert.assertEquals(cPage.getCardStatus(driver), CARDTYPE);
			Log.info("Actual results " + cPage.getCardStatus(driver) + " matches expected results " + CARDTYPE);
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
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterEmail(driver, email);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);
		return driver;
	}

	private WebDriver createNewCustomer(WebDriver driver, boolean address) throws Exception {

		// Create customer test data via api rest call
		if (address) {
			cData = ApiCustomerPost.apiPostSuccess();
		} else {
			cData = ApiCustomerPost.apiPostSuccessNoAddress();
			Log.info("API No Address is being called!");
		}
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// return to selenium testing
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
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