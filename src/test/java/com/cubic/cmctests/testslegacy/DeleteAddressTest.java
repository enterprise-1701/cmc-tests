package com.cubic.cmctests.tests;

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

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.restapi.ApiCustomerPost;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class DeleteAddressTest {

	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	private static Logger Log = Logger.getLogger(Logger.class.getName());
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();

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
	public void deleteAddressCancel() throws Exception {

	    Log.info("29931");
		createNewCustomer(driver);
		Utils.waitTime(5000);
		SearchPage sPage = new SearchPage(driver);
		sPage.clickExpandAddress(driver);
		Utils.waitTime(5000);
		sPage.clickDeleteAddress(driver);
		DeleteAddressDetailsPage delPage = new DeleteAddressDetailsPage(driver);
		Utils.waitTime(5000);
		delPage.clickCancel(driver);
		Utils.waitTime(5000);
		Assert.assertTrue(sPage.isDeleteAddressDisplayed(driver));
		driver.close();

	}
	
	@Test(priority = 2, enabled = true)
	public void deleteAddressConfirmCancel() throws Exception {

	    Log.info("29934");
		createNewCustomer(driver);
		Utils.waitTime(5000);
		SearchPage sPage = new SearchPage(driver);
		sPage.clickExpandAddress(driver);
		Utils.waitTime(5000);
		sPage.clickDeleteAddress(driver);
		DeleteAddressDetailsPage delPage = new DeleteAddressDetailsPage(driver);
		delPage.clickConfirmDelete(driver);
		Utils.waitTime(5000);
		delPage.clickSecondCancel(driver);
		Utils.waitTime(5000);
		Assert.assertTrue(sPage.isDeleteAddressDisplayed(driver));
		driver.close();

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
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
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