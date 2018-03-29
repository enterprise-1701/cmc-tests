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
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class LinkAccountTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	static WebDriver driver;
	static String browser;

	CoreTest coreTest = new CoreTest();
	AccountData acctData = new AccountData();

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
	public void linkAccountCancelTest() throws Exception {

	    Log.info("29941");
		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.enterBankAccount(driver, Global.CC);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickCancel(driver);
		Assert.assertTrue(lPage.isLinkAccountDisplayed(driver));
		Log.info("linkAccountCancelTest Completed");
		driver.close();
	}

	@Test(priority = 2, enabled = true)
	public void linkAccountCardExpirationMissingTest() throws Exception {

	    Log.info("186005");
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickContiune(driver);
		sPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.enterBankAccount(driver, Global.CC);
		Assert.assertFalse(lPage.isSearchTokenEnabled(driver));
		driver.close();

	}

	@Test(priority = 3, enabled = true)
	public void linkAccountBankAccountMissingTest() throws Exception {

	    Log.info("186006");
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.selectSearchTypeCustomer(driver);
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		Utils.waitTime(5000);
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 175)", "");
		sPage.clickContiune(driver);
		sPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		Assert.assertFalse(lPage.isSearchTokenEnabled(driver));
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