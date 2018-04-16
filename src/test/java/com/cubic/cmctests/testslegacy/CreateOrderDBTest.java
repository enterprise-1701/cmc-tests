package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.dao.DBAutomation;
import com.cubic.cmcjava.pageobjects.CreateFundingPage;
import com.cubic.cmcjava.pageobjects.CreateOrderPage;
import com.cubic.cmcjava.pageobjects.DashboardPage;
import com.cubic.cmcjava.pageobjects.NewCustomerDisplayPage;
import com.cubic.cmcjava.pageobjects.SearchPage;
import com.cubic.cmcjava.pageobjects.ShoppingCartPage;
import com.cubic.cmcjava.restapi.ApiCustomerPost;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//Quality Center Test IDs: 77611, 77612
//#################################################################################

public class CreateOrderDBTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String email;
	private static boolean orderRecordFound;
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	private static CustomerData cData;
	private static String phoneNumber;
	private static final Integer PAYMENT_TYPE = 1;


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
	public void createOrderDBcheck() throws Exception {
	    
	    Log.info("185963");
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
		oPage.selectOrderAmount(driver);
		oPage.clickAddtoCart(driver);
		Utils.waitTime(10000);
		
		nPage3.clickCart(driver);
		ShoppingCartPage sPage = new ShoppingCartPage(driver);
		sPage.clickCheckOut(driver);
		Utils.waitTime(5000);
		sPage.clickPlaceOrder(driver);
		Utils.waitTime(5000);

		Log.info("checking the database now");
		Utils.waitTime(10000);
		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbCmsConnect();
		Log.info("Cms connected");
		int customerID = dbAuto.dbFindCustomerId(email);
		Log.info("customer id found in db: " + customerID);
		dbAuto.dbDisconnect();

		DBAutomation dbAuto2 = new DBAutomation();
		dbAuto2.dbOamConnect();
		Log.info("Oam connected");
		orderRecordFound = dbAuto2.dbFindJournalEntry(customerID);
		dbAuto2.dbDisconnectOAM();

		Assert.assertTrue(orderRecordFound, "error - order record was not found in the database");
		driver.close();
		Log.info("createOrderDBcheck Completed");

	}

	@Test(priority = 2, enabled = true)
	public void createOrderCancelDBcheck() throws Exception {
		
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

		Log.info("checking the database now");
		Utils.waitTime(10000);
		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbCmsConnect();
		Log.info("Cms connected");
		int customerID = dbAuto.dbFindCustomerId(email);
		Log.info("customer id found in db: " + customerID);
		dbAuto.dbDisconnect();

		DBAutomation dbAuto2 = new DBAutomation();
		dbAuto2.dbOamConnect();
		Log.info("Oam connected");
		orderRecordFound = dbAuto2.dbFindJournalEntry(customerID);
		dbAuto2.dbDisconnectOAM();

		Assert.assertFalse(orderRecordFound, "error - order record was found for a canceled order");
		driver.close();
		Log.info("createOrderDBcheck Completed");

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
		Log.info("TearDown Completed");
		Reporter.log("TearDown Completed");
		driver.quit();

	}
}