package com.cubic.cmctests.testslegacy;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.dao.DBAutomation;

//#################################################################################
//Generic test to check DB connections
//#################################################################################

// Don't run in regression
public class DBTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());

	static WebDriver driver;
	static String browser;

	CoreTest coreTest = new CoreTest();

	@Parameters("browser")
	/*
	@BeforeMethod
	public void setUp(String browser) throws InterruptedException {

		
		Logging.setLogConsole();
		Logging.setLogFile();
		Log.info("Setup Started");
		 
	}
	*/

	@Test(enabled = true)
	public void checkDB() throws Exception {

		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbOamConnect();
		//dbAuto.dbFindCustomer("WR7jIPKSkqYN@gmail.com");
	

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
	

	}
}