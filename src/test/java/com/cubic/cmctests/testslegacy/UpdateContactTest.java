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

public class UpdateContactTest extends RESTEngine {

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

	// Create new customer, search for that customer and update contact details
	@Test(priority = 1, enabled = true)
	public void updateContact(ITestContext context) throws Exception {
		String testCaseName = "185990:updateContact";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185990");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);

			sPage.clickContact(driver);
			Utils.waitTime(7000);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.enterFname(driver, Global.FNAME2);
			contactPage.enterLname(driver, Global.LNAME2);
			Utils.waitTime(7000);
			contactPage.clickSubmit(driver);
			Utils.waitTime(7000);


			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME2);
			Log.info("Actual results " + sPage.getFirstName(driver) + Global.FNAME2);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME2);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME2);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update contact
	// details and cancel
	@Test(priority = 2, enabled = true)
	public void updateContactCancel(ITestContext context) throws Exception {
		String testCaseName = "185991:updateContactCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185991");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			String phoneNumber = "(858) 614-0263";

			sPage.clickContact(driver);
			Utils.waitTime(7000);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.enterFname(driver, Global.FNAME2);
			contactPage.enterLname(driver, Global.LNAME2);

			Utils.waitTime(7000);
			contactPage.clickCancel(driver);
			Utils.waitTime(7000);
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

	// Create new customer, search for that customer and update contact details
	// and address
	@Test(priority = 3, enabled = true)
	public void updateContactAndAddress(ITestContext context) throws Exception {
		String testCaseName = "185992:updateContactAndAddress";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185992");
			createNewCustomer(driver);

			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			Utils.waitTime(7000);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.enterFname(driver, Global.FNAME2);
			contactPage.enterLname(driver, Global.LNAME2);
			contactPage.clickNewAddress(driver);
			contactPage.selectCountry(driver);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-350)", "");
			contactPage.enterAddress(driver, Global.NEWADDRESS);
			contactPage.enterCity(driver, Global.NEWCITY);
			contactPage.selectState(driver);
			contactPage.enterPostalCode(driver, Global.NEWPOSTAL);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
			Utils.waitTime(5000);
			contactPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME2);
			Log.info("Actual results " + sPage.getFirstName(driver) + Global.FNAME2);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME2);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME2);
			Assert.assertEquals(sPage.getAddress(driver), Global.NEW_FULLADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver) + " matches " + Global.NEW_FULLADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer attempt to update contact
	// details and address and cancel
	@Test(priority = 4, enabled = true)
	public void updateContactAndAddressCancel(ITestContext context) throws Exception {
		String testCaseName = "185993:updateContactAndAddressCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185993");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			Utils.waitTime(7000);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			Utils.waitTime(7000);
			contactPage.enterFname(driver, Global.FNAME2);
			contactPage.enterLname(driver, Global.LNAME2);
			contactPage.clickNewAddress(driver);
			contactPage.selectCountry(driver);
			contactPage.enterAddress(driver, Global.NEWADDRESS);
			contactPage.enterCity(driver, Global.NEWCITY);
			contactPage.selectState(driver);
			contactPage.enterPostalCode(driver, Global.NEWPOSTAL);
			Utils.waitTime(7000);
			contactPage.clickCancel(driver);
			Utils.waitTime(7000);
			Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
			Log.info("Actual results " + sPage.getFirstName(driver) + Global.FNAME);
			Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
			Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
			Assert.assertEquals(sPage.getAddress(driver), Global.FULLADDRESS);
			Log.info("Actual results " + sPage.getAddress(driver) + " matches " + Global.FULLADDRESS);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update without
	// inputting all required fields
	@Test(priority = 5, enabled = true)
	public void updateContactCheckRequiredFields(ITestContext context) throws Exception {
		String testCaseName = "185994:updateContactCheckRequiredFields";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185994");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.deleteLname(driver);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Success button enabled? " + contactPage.isSubmitEnabled(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update
	// inputting invalid phone number
	@Test(priority = 6, enabled = true)
	public void updateContactInvalidPhone(ITestContext context) throws Exception {
		String testCaseName = "185995:updateContactInvalidPhone";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185995");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			Utils.waitTime(7000);
			contactPage.enterPhone(driver, Global.INVALID_PHONE);
			Utils.waitTime(7000);
			contactPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertTrue(contactPage.isSubmitDisplayed(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update
	// inputting invalid email
	@Test(priority = 7, enabled = true)
	public void updateContactInvalidEmail(ITestContext context) throws Exception {
		String testCaseName = "185996:updateContactInvalidEmail";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185996");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);

			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			Utils.waitTime(7000);
			contactPage.enterEmail(driver, "test");
			Utils.waitTime(7000);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Submit button enabled? " + contactPage.isSubmitEnabled(driver));
			//Assert.assertEquals(contactPage.getInvalidEmailError(driver), Global.INVALID_EMAIL_ERROR);
			//Log.info("Actual results " + contactPage.getInvalidEmailError(driver) + "matches" + Global.INVALID_EMAIL_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update
	// inputting invalid username
	@Test(priority = 8, enabled = true)
	public void updateContactInvalidUserName(ITestContext context) throws Exception {
		String testCaseName = "185997:updateContactInvalidUserName";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185997");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			Utils.waitTime(7000);
			contactPage.enterUsername(driver, "x");
			Utils.waitTime(7000);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Success button enabled? " + contactPage.isSubmitEnabled(driver));
			//Assert.assertEquals(contactPage.getInvalidEmailError(driver), Global.INVALID_EMAIL_ERROR);
			//Log.info("Actual results " + contactPage.getInvalidEmailError(driver) + "matches" + Global.INVALID_EMAIL_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// Create new customer, search for that customer, attempt to update
	// inputting invalid pin
	@Test(priority = 9, enabled = true)
	public void updateContactInvalidPin(ITestContext context) throws Exception {
		String testCaseName = "185998:updateContactInvalidPin";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185998");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			Utils.waitTime(7000);
			contactPage.enterPin(driver, "1");
			Utils.waitTime(7000);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Success button enabled? " + contactPage.isSubmitEnabled(driver));
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
	public void updateContactSecurityAnswermissing(ITestContext context) throws Exception {
		String testCaseName = "185999:updateContactSecurityAnswerMissing";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185999");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			Utils.waitTime(7000);
			sPage.clickContact(driver);
			Utils.waitTime(7000);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.enterSecurityAnswer(driver, "");
			Utils.waitTime(7000);
			contactPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Success button enabled? " + contactPage.isSubmitEnabled(driver));
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	//Failing due to the bug - CCD-1111
	@Test(priority = 11, enabled = true)
	public void updateContactOtherPhonesMissing(ITestContext context) throws Exception {
		String testCaseName = "186000:updateContactOtherPhonesMissing";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("186000");
			createNewCustomer(driver);
			Utils.waitTime(7000);
			SearchPage sPage = new SearchPage(driver);
			sPage.clickContact(driver);
			ContactDetailsPage contactPage = new ContactDetailsPage(driver);
			contactPage.selectPhoneType2(driver);
			contactPage.selectPhoneType3(driver);
			Utils.waitTime(7000);
			contactPage.clickSubmit(driver);
			Utils.waitTime(7000);
			Assert.assertFalse(contactPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
			Log.info("Success button enabled? " + contactPage.isSubmitEnabled(driver));
			//Assert.assertEquals(contactPage.getRequiredFieldError(driver), Global.REQUIREDFIELD_ERROR);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	    // Create new customer, search for that customer, attempt to update contact
		// details and close
		@Test(priority = 12, enabled = true)
		public void updateContactClose(ITestContext context) throws Exception {
			String testCaseName = "186001:updateContactClose";

			try {
				restActions = setupAutomationTest(context, testCaseName);
				restActions.successReport("test", "test");
				Log.info("186001");
				createNewCustomer(driver);
				Utils.waitTime(7000);
				SearchPage sPage = new SearchPage(driver);
				String phoneNumber = "(858) 614-0263";

				sPage.clickContact(driver);
				Utils.waitTime(7000);
				ContactDetailsPage contactPage = new ContactDetailsPage(driver);
				contactPage.enterFname(driver, Global.FNAME2);
				contactPage.enterLname(driver, Global.LNAME2);

				Utils.waitTime(7000);
				contactPage.clickClose(driver);
				Utils.waitTime(7000);
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
		Utils.waitTime(10000);
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