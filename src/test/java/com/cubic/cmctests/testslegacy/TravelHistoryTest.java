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
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################
//Failing due to travel history not working in PIL

public class TravelHistoryTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String TRAVEL_MODE = "Transit";
	private static final String TOKEN = "Bankcard";
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

	// STA-724 - view unregistered customer travel history
	// failing due to view details bug 
	@Test(enabled = true)
	public void viewTravelHistoryUnregistredCustomer(ITestContext context) throws Exception {
		String testCaseName = "29987:viewTravelHistoryUnregisteredCustomer";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29987");
			// create travel history via soap call
			SOAPClientSAAJ sClient = new SOAPClientSAAJ();
			CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
			String validCCNumber = ccGenerator.generate("4", 16);
			String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
			System.out.println("cc number being used is " + validCCNumber);
			System.out.println("account id being returned is " + accountID);

			// check cmc for travel history based on cc
			// takes around 6 minutes for travel history to show on cmc
			Log.info("waiting for travel history to display on cmc");
			Utils.waitTime(360000);

			coreTest.signIn(driver);
			TokenSearchPage tPage = getTokenSearchPage();
			tPage.enterBankNumber(driver, validCCNumber);
			tPage.selectExpMonth(driver);
			tPage.selectExpYear(driver);
			tPage.clickSearchToken(driver);
			Utils.waitTime(5000);
			tPage.clickViewDetails(driver);


			UnregistredCustomerPage uPage = new UnregistredCustomerPage(driver);
			Assert.assertEquals(uPage.getStatus(driver), "Active");
			Assert.assertEquals(uPage.getTokenType(driver), "Bankcard");

			Log.info("viewTravelHistoryUnregistredCustomer");
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// STA-686 - view registered customer travel history subaccount
	@Test(enabled = true)
	public void viewTravelHistoryRegistredCustomerSubAccount(ITestContext context) throws Exception {
		String testCaseName = "29990:viewTravelHistoryRegisteredCustomerSubAccount";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29990");
			// create travel history via soap call
			SOAPClientSAAJ sClient = new SOAPClientSAAJ();
			CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
			String validCCNumber = ccGenerator.generate("4", 16);
			String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
			System.out.println("cc number being used is " + validCCNumber);
			System.out.println("account id being returned is " + accountID);

			// create account and link it to cc
			coreTest.signIn(driver);
			coreTest.createCustomer(driver);
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

			String postTab = sClient.travelHistoryPostTab(validCCNumber);
			Log.info("second tab was " + postTab);

			// takes around 6 minutes for travel history to show on cmc
			Log.info("waiting for travel history to display on cmc");
			Utils.waitTime(360000);

			// checking travel history under subaccount page
			TokenSearchSubSystemPage ssPage = new TokenSearchSubSystemPage(driver);
			ssPage.clickTravelHistory(driver);

			Assert.assertEquals(ssPage.getTravelMode(driver), TRAVEL_MODE);
			Assert.assertEquals(ssPage.getToken(driver), TOKEN);

			Log.info("viewTravelHistoryRegistredCustomerSubAccount Completed");
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
	private TokenSearchPage getTokenSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		TokenSearchPage tPage = new TokenSearchPage(driver);
		return tPage;
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}