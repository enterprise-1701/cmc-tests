package com.cubic.cmctests.testslegacy;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.pageobjects.CreateRuleSetTrialPage;
import com.cubic.cmcjava.pageobjects.DashboardPage;
import com.cubic.cmcjava.pageobjects.FinancePage;
import com.cubic.cmcjava.utils.Global;
import com.cubic.cmcjava.utils.Logging;
import com.cubic.cmcjava.utils.Utils;

public class RuleSetTrialTest extends RESTEngine {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	
	WebDriver driver;
	CoreTest coreTest = new CoreTest();
	RESTActions restActions;
	
	@Parameters({"browser", "executionenv"})
	@BeforeMethod
	public void setUp(String browser, String executionenv) throws InterruptedException {

//		Logging.setLogConsole();
//		Logging.setLogFile();
//		Log.info("Setup Started");
//		Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
//		Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser, executionenv);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//		Log.info("Setup Completed");
	}	

	@Test(priority = 1, enabled = true)
	public void createRuleSetTrial(ITestContext context) throws Exception {
		String testCaseName = "161529:createRuleSetTrial";

		try {
			restActions = setupAutomationTest(context, testCaseName);
			restActions.successReport("test", "test");
			Log.info("161529");
			coreTest.signIn(driver);

			FinancePage finPage = getFinancePage();
			finPage.clickRuleSetTrialRunsLink(driver);
			finPage.clickCreateTrialLink(driver);

			CreateRuleSetTrialPage createTrialPage = new CreateRuleSetTrialPage(driver);
			createTrialPage.selectRuleSet(driver, "Rule Set No. 1");
			createTrialPage.setSettlementFromDate(driver, "09/01/2017");
			createTrialPage.setSettlementToDate(driver, "11/06/2017");
			createTrialPage.setTransactionFromDate(driver, "11/15/2017");
			createTrialPage.setTransactionToDate(driver, "11/27/2017");
			createTrialPage.selectAcquirer(driver, "ABP");
			createTrialPage.setBatch(driver, "0");
			createTrialPage.clickSubmitButton(driver);
			driver.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			teardownAutomationTest(context, testCaseName);
		}
	}
	
	private FinancePage getFinancePage() throws InterruptedException, AWTException {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickFinanceTab(driver);
		FinancePage finPage = new FinancePage(driver);
		return finPage;
	}
}

