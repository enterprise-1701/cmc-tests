package com.cubic.cmctests.testslegacy;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

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

import com.cubic.cmcjava.pageobjects.BasePage;
import com.cubic.cmcjava.pageobjects.ConfirmationPage;
import com.cubic.cmcjava.pageobjects.ContactDetailsPage;
import com.cubic.cmcjava.pageobjects.CreateFundingPage;
import com.cubic.cmcjava.pageobjects.LinkAccountPage;
import com.cubic.cmcjava.pageobjects.NewCustomerDisplayPage;
import com.cubic.cmcjava.utils.CreditCardNumberGenerator;
import com.cubic.cmcjava.utils.Global;
import com.cubic.cmcjava.utils.Logging;
import com.cubic.cmcjava.utils.Utils;

//#################################################################################
//Quality Center Test IDs: 76715, 76713, 76714, 76716
//#################################################################################

public class EmailTest extends RESTEngine {

	private String host = "bb-corp-cas01.corp.cubic.cub";
	private String username = "cts.systemtest";
	private String password = "gd6N6MCkCdDa9Rc##";
	private String folder = "Inbox/Cmc";
	private String email = "ray.khorrami@cubic.com";
	private static Logger Log = Logger.getLogger(Logger.class.getName());
	CoreTest coreTest = new CoreTest();
	static WebDriver driver;
	boolean emailFound = true;
	int retryCount;
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

	@Test(priority = 1, enabled = true)
	public void checkEmailNewCustomer(ITestContext context) throws Exception {
		String testCaseName = "29938:checkEmailNewCustomer";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29938");
			// Create New Customer
			coreTest.signIn(driver);
			coreTest.createCustomerEmailTest(driver, email);
			Log.info("New customer created");
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(60000);

			// Check Welcome Email
			Assert.assertTrue(checkEmailSubjectInterval("Welcome New Customer"), "Welcome New Customer Email Test Failed");
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 2, enabled = true)
	public void checkEmailCreateOrder(ITestContext context) throws Exception {
		String testCaseName = "29939:checkEmailCreateOrder";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29939");
			boolean emailFound = false;

			// Create New Customer
			coreTest.signIn(driver);
			coreTest.createOrderSubmit(driver, email);
			Log.info("New customer created, New order created");
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(300000);

			// Check both create order emails
			Properties props = System.getProperties();
			props.put("mail.imaps.auth.plain.disable", "true");
			try {
				Session session = Session.getDefaultInstance(props, null);
				Store store = session.getStore("imaps");
				store.connect(host, 993, username, password);
				Folder inbox = store.getFolder(folder);
				inbox.open(Folder.READ_ONLY);

				Log.info("Number of new emails: " + inbox.getUnreadMessageCount());
				Assert.assertTrue((inbox.getUnreadMessageCount() > 0), ("No New Emails!"));

				Message messages[] = inbox.getMessages();
				Log.info("Total number of emails: " + inbox.getMessages().length);

				int i = (inbox.getMessages().length - 5);
				while (i < (inbox.getMessages().length)) {

					Log.info("i is at email number : " + i);
					String newSubject = messages[i].getSubject();
					Log.info("message subject at : " + i + " is: " + newSubject);
					if (newSubject.contains("OneAccount: Order Update Notification")
							|| newSubject.contains("OneAccount: Payment Receipt")) {
						emailFound = true;
						break;
					}

					i++;
				}

				Assert.assertTrue(emailFound, "Create order email was not recevied");
				Log.info("checkEmailCreateOrder Compelted");

			} catch (Exception e) {
				e.printStackTrace();

			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 3, enabled = true)
	public void checkEmailLinkAccount(ITestContext context) throws Exception {
		String testCaseName = "29937:checkEmailLinkAccount";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29937");
			// Create New Customer
			coreTest.signIn(driver);
			coreTest.createCustomerEmailTest(driver, email);
			Log.info("New customer created");

			// Link account
			BasePage bPage = new BasePage(driver);
			bPage.clickLinkAccount(driver);
			LinkAccountPage lPage = new LinkAccountPage(driver);
			Utils.waitTime(10000);
			CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
			lPage.enterBankAccount(driver, ccGenerator.generate("4", 16));
			lPage.selectExpMonth(driver);
			lPage.selectExpYear(driver, 2);
			lPage.clickSearchToken(driver);
			lPage.enterNickNameAccountNotFound(driver, "adam");
			lPage.clickRegisterAndLink(driver);
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(180000);

			// Check Link Account Email
			Assert.assertTrue(checkEmailSubjectInterval("Link Account Confirmation"),
					"Link Account Confirmation Email Test Failed");
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 4, enabled = true)
	public void checkEmailUpdateContact(ITestContext context) throws Exception {
		String testCaseName = "29940:checkEmailUpdateContact";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29940");
			// Create New Customer and update contact
			coreTest.signIn(driver);
			coreTest.createCustomerEmailTest(driver, email);
			Log.info("New customer created");

			// Update contact
			coreTest.updateContact(driver, email);
			Log.info("Customer contact info updated");
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(180000);

			Assert.assertTrue(checkEmailSubjectInterval("Customer Contact Update"),
					"Customer Contact Update Email Test Failed");
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// STA-828
	@Test(priority = 5, enabled = true)
	public void checkEmailResetPassword(ITestContext context) throws Exception {
		String testCaseName = "185968:checkEmailResetPassword";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185968");
			// Create New Customer and update contact
			coreTest.signIn(driver);
			coreTest.createCustomerEmailTest(driver, email);


			// Reset password
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(15000);
			nPage3.clickContact(driver);
			ContactDetailsPage cdPage = new ContactDetailsPage(driver);
			Utils.waitTime(5000);
			cdPage.clickResetPassword(driver);
			ConfirmationPage cPage = new ConfirmationPage(driver);
			cPage.clickYes(driver);
			Log.info("Password reset completed");

			// Check password reset email
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(180000);
			Assert.assertTrue(checkEmailSubjectInterval("Password Reset Verification"),
					"Password Reset Verification Email Test Failed");
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
	public void checkEmailDeleteFunding(ITestContext context) throws Exception {
		String testCaseName = "185969:checkEmailDeleteFunding";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("185969");
			// create customer and funding source
			coreTest.signIn(driver);
			coreTest.createCustomerEmailTest(driver, email);
			NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
			Utils.waitTime(8000);
			nPage3.clickFundingSource(driver);
			CreateFundingPage cPage = new CreateFundingPage(driver);
			cPage.selectPaymentType(driver, 2);
			cPage.enterName(driver, Global.CCNAME);
			cPage.enterCC(driver, Global.CC);
			cPage.selectMonth(driver);
			cPage.selectYear(driver);
			cPage.clickSubmit(driver);
			Utils.waitTime(5000);
			nPage3.clickFundingSourceExpand(driver);

			// click delete funding source
			nPage3.deleteFundingSource(driver);
			ConfirmationPage conPage = new ConfirmationPage(driver);
			conPage.clickConfirmDelete(driver);

			// Check delete funding email
			Log.info("Waiting for emails to get generated");
			Utils.waitTime(180000);
			Assert.assertTrue(checkEmailSubjectInterval("Customer Funding Source Update"),
					"Customer Funding Source Update Email Test Failed");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	
	private boolean checkEmailSubjectInterval(String subject) throws InterruptedException {

		Properties props = System.getProperties();
		props.put("mail.imaps.auth.plain.disable", "true");

		while (emailFound = false || retryCount < 10) {

			try {
				Session session = Session.getDefaultInstance(props, null);
				Store store = session.getStore("imaps");
				store.connect(host, 993, username, password);
				Folder inbox = store.getFolder(folder);
				inbox.open(Folder.READ_ONLY);

				Log.info("Number of new emails: " + inbox.getUnreadMessageCount());
				Assert.assertTrue((inbox.getUnreadMessageCount() > 0), ("No New Emails!"));

				Message messages[] = inbox.getMessages();
				Log.info("Total number of emails: " + inbox.getMessages().length);

				int i = (inbox.getMessages().length - 10);

				while (i < (inbox.getMessages().length)) {

					Log.info("i is at email number : " + i);
					String newSubject = messages[i].getSubject();
					Log.info("message subject at : " + i + " is: " + newSubject);

					if (newSubject.equals(subject)) {
						emailFound = true;
						Log.info("Email found in iteration " + retryCount);
						return emailFound;
					} else {

						Log.info("Email not found in iteration " + retryCount);
					}
					
					i++;

				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			retryCount++;
			Log.info("wait time of 1 minute between intervals");
			Utils.waitTime(60000);

		}

		return emailFound;

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}

}