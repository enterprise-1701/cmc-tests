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

import com.cubic.cmcjava.dao.DBAutomation;
import com.cubic.cmcjava.pageobjects.BasePage;
import com.cubic.cmcjava.pageobjects.LinkAccountPage;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class LinkAccountDBTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String email;
	private static boolean subSystemRecordFound;
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

	@Test(priority = 1, enabled = true)
	public void createLinkAccountDBcheck(ITestContext context) throws Exception {
		String testCaseName = "186003:createLinkAccountDBcheck";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("186003");
			// create balance via soap call
			SOAPClientSAAJ sClient = new SOAPClientSAAJ();
			CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
			String validCCNumber = ccGenerator.generate("4", 16);
			String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
			Log.info("cc number being used is " + validCCNumber);
			Log.info("account id being returned is " + accountID);
			Log.info("waiting for ABP to get updated");
			for (int i = 0; i < 6; i++) {
				Thread.sleep(60000);
				driver.navigate().refresh();
			}
			Log.info("WaitTime ended for ABP to get updated");

			// create account and link it to cc
			coreTest.signIn(driver);
			coreTest.createCustomer(driver);
			email = coreTest.getEmail();
			BasePage bPage = new BasePage(driver);
			bPage.clickLinkAccount(driver);
			LinkAccountPage lPage = new LinkAccountPage(driver);

			// use cc number from soap call to link account
			lPage.enterBankAccount(driver, validCCNumber);
			lPage.selectExpMonth(driver);
			lPage.selectExpYear(driver, 2);
			lPage.clickSearchToken(driver);
			lPage.enterNickName(driver, "adam");
			lPage.clickLinkAccount(driver);

			// check db for new linked account subysystem
			Log.info("checking the database now");
			Utils.waitTime(60000);
			DBAutomation dbAuto = new DBAutomation();
			dbAuto.dbCmsConnect();
			Log.info("Cms connected");
			int customerID = dbAuto.dbFindCustomerId(email);
			Log.info("customer id found in db: " + customerID);
			dbAuto.dbDisconnect();

			DBAutomation dbAuto2 = new DBAutomation();
			dbAuto2.dbOamConnect();
			Log.info("Oam connected");
			subSystemRecordFound = dbAuto2.dbFindSubSystem(customerID);
			dbAuto2.dbDisconnectOAM();
			Assert.assertTrue(subSystemRecordFound, "subsystem record was not found");
			driver.close();
			Log.info("createLinkAccountDBcheck Completed");
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@Test(priority = 2, enabled = true)
	public void createLinkAccountCancellDBcheck(ITestContext context) throws Exception {
		String testCaseName = "186004:createLinkAccountCancelDBcheck";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("186004");
			// create balance via soap call
			SOAPClientSAAJ sClient = new SOAPClientSAAJ();
			CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
			String validCCNumber = ccGenerator.generate("4", 16);
			String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
			Log.info("cc number being used is " + validCCNumber);
			Log.info("account id being returned is " + accountID);
			Log.info("waiting for ABP to get updated");
			for (int i = 0; i < 6; i++) {
				Thread.sleep(60000);
				driver.navigate().refresh();
			}

			// create account and link it to cc
			coreTest.signIn(driver);
			coreTest.createCustomer(driver);
			email = coreTest.getEmail();
			BasePage bPage = new BasePage(driver);
			bPage.clickLinkAccount(driver);
			LinkAccountPage lPage = new LinkAccountPage(driver);

			// use cc number from soap call to link account
			lPage.enterBankAccount(driver, validCCNumber);
			lPage.selectExpMonth(driver);
			lPage.selectExpYear(driver, 2);
			lPage.clickSearchToken(driver);
			lPage.enterNickName(driver, "adam");
			lPage.clickCancel(driver);

			// check db for new linked account subysystem
			Log.info("checking the database now");
			Utils.waitTime(60000);
			DBAutomation dbAuto = new DBAutomation();
			dbAuto.dbCmsConnect();
			Log.info("Cms connected");
			int customerID = dbAuto.dbFindCustomerId(email);
			Log.info("customer id found in db: " + customerID);
			dbAuto.dbDisconnect();

			DBAutomation dbAuto2 = new DBAutomation();
			dbAuto2.dbOamConnect();
			Log.info("Oam connected");
			subSystemRecordFound = dbAuto2.dbFindSubSystem(customerID);
			dbAuto2.dbDisconnectOAM();
			Assert.assertFalse(subSystemRecordFound, "subsystem record was found in canceled linked account");
			driver.close();
			Log.info("createLinkAccountDBcheck Completed");
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Completed");
		Reporter.log("TearDown Completed");
		driver.quit();

	}
}