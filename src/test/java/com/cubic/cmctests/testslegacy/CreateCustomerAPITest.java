package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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

public class CreateCustomerAPITest {

	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	boolean saveEmail = true;
	Logger Log = Logger.getLogger(CreateCustomerAPITest.class.getName());

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws InterruptedException {

		/*
		Log = Logging.setLogConsole();
		Logging.setLogFile();
		*/
		driver = Utils.openBrowser(browser);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		Log.info("Setup Completed");
	}

	//Test update to GIT
	@Test(priority = 1, enabled = true)
	public void createNewCustomerApi() throws Exception {

		// Create customer test data via api rest call
	    Log.info("185943");
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6, 10);
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// return to selenium testing 
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterEmail(driver, email);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickContiune(driver);
		Utils.waitTime(5000);
		

		// Verify customer data on cmc UI
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		Assert.assertEquals(nPage3.getFname(driver), cData.getFname());
		Assert.assertEquals(nPage3.getLname(driver), cData.getLname());
		Assert.assertEquals(nPage3.getEmail(driver), email);
		Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
		Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), cData.getAddress());
		
		driver.close();
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