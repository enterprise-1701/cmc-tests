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

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.restapi.ApiCustomerPost;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class ResetPasswordTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String CONTACTS = "Contacts";
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;

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

	// STA-828
	@Test(priority = 1, enabled = true)
	public void resetPassword() throws Exception {

	    Log.info("29960");
		getPasswordConfirmation(driver);
		ConfirmationPage cPage = new ConfirmationPage(driver);
		cPage.clickYes(driver);
		Assert.assertEquals(cPage.getContacts(driver), CONTACTS);
		driver.close();

	}

	@Test(priority = 2, enabled = true)
	public void resetPasswordCancel() throws Exception {

	    Log.info("29959");
		getPasswordConfirmation(driver);
		ConfirmationPage cPage = new ConfirmationPage(driver);
		cPage.clickNo(driver);
		Assert.assertEquals(cPage.getContacts(driver), CONTACTS);
		driver.close();

	}

	@Test(priority = 3, enabled = true)
	public void resetPasswordClose() throws Exception {

	    Log.info("185970");
		getPasswordConfirmation(driver);
		ConfirmationPage cPage = new ConfirmationPage(driver);
		cPage.clickClose(driver);
		Assert.assertEquals(cPage.getContacts(driver), CONTACTS);
		driver.close();

	}

	// private methods
	private WebDriver getPasswordConfirmation(WebDriver driver) throws Exception {
		
		// Create customer test data via api nis call
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// returning to selenium testing
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		Utils.waitTime(3000);
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, email);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);
		Utils.waitTime(5000);

		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickContact(driver);
		ContactDetailsPage cdPage = new ContactDetailsPage(driver);
		Utils.waitTime(5000);
		cdPage.clickResetPassword(driver);
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