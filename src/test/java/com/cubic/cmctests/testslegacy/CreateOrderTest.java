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

public class CreateOrderTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String PURSE = "Default Purse";
	private static CustomerData cData;
	private static String phoneNumber;
	private static String email;
	private static final Integer PAYMENT_TYPE = 1;

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

	@Test(priority = 1, enabled = true)
	public void createOrderSubmit(ITestContext context) throws Exception {
		String testCaseName = "29930:createOrderSubmit";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("29930");
			createNewCustomer(driver);
			// create order using UI
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
			Utils.waitTime(3000);

			cPage.clickCreateOrder(driver);
			CreateOrderPage oPage = new CreateOrderPage(driver);
			oPage.selectOrderType(driver);
			oPage.selectPurseType(driver);
			Utils.waitTime(5000);
			oPage.selectOrderAmount(driver);
			oPage.clickAddtoCart(driver);
			Utils.waitTime(10000);

			nPage3.clickCart(driver);
			ShoppingCartPage sPage = new ShoppingCartPage(driver);
			sPage.clickCheckOut(driver);
			Utils.waitTime(5000);
			sPage.clickPlaceOrder(driver);
			Utils.waitTime(5000);

			oPage.clickBalanceHistoryExpand(driver);
			Assert.assertEquals(oPage.getPurse(driver), PURSE);
			Assert.assertEquals(oPage.getEntryType(driver), Global.ENTRY_TYPE2);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}

	// TODO: Find a test case number for this
	@Test(priority = 2, enabled = true)
	public void createOrderCancel(ITestContext context) throws Exception {
		String testCaseName = ":createOrderCancel";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("");
			createNewCustomer(driver);
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
			cPage.clickCreateOrder(driver);
			CreateOrderPage oPage = new CreateOrderPage(driver);
			oPage.selectOrderType(driver);
			oPage.selectPurseType(driver);
			oPage.selectOrderAmount(driver);
			oPage.clickCancel(driver);
			Assert.assertTrue(oPage.isCreateOrderDisplayed(driver), "create order link should be displayed!");
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
		Utils.waitTime(7000);
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