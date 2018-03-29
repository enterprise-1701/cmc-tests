package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.dao.DBAutomation;
import com.cubic.cmcjava.pageobjects.CreateCustomerPage;
import com.cubic.cmcjava.pageobjects.DashboardPage;
import com.cubic.cmcjava.pageobjects.NewCustomerPage;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//Quality Center Test IDs: 71940
//#################################################################################

public class CreateCustomerDBTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String email;
	private static boolean contactRecordFound;
	private static boolean accountRecordFound;
	private static boolean addressRecordFound;
	private static boolean securityRecordFound;
	private static boolean phoneRecordFound;
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


	@Test(priority=1 , enabled = true)
	public void createNewCustomerDBCheckContactTable() throws Exception{
	
	    Log.info("185944"); 
		coreTest.signIn(driver);
	    coreTest.createCustomer(driver);  
	    email = coreTest.getEmail();
	    Log.info("Email being passed to DAO: " + email);
	    
	    DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    contactRecordFound = dbAuto.dbFindCustomer(email);
	    dbAuto.dbDisconnect();
	    Assert.assertTrue(contactRecordFound, "customer record was not found");
		driver.close();
		Log.info("createNewCustomerDBCheckContactTable Completed");
	    
	}
	
	@Test(priority=2 , enabled = true)
	public void createNewCustomerDBCheckAccountTable() throws Exception{
	
	    Log.info("185945"); 
		coreTest.signIn(driver);
	    coreTest.createCustomer(driver);  
	    email = coreTest.getEmail();   
	    Utils.waitTime(25000);
	    
	    //Checking two different databases 
	    DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    dbAuto.dbOamConnect();
	    accountRecordFound = dbAuto.dbFindAccount(email);
	    dbAuto.dbDisconnect();
	    Assert.assertTrue(accountRecordFound, "account record was not found");
		driver.close();
		Log.info("createNewCustomerDBCheckAccountTable Completed");
		    
	}
		
	@Test(priority=3 , enabled = true)
	public void createNewCustomerDBCheckAddressTable() throws Exception{
	
	    Log.info("185946"); 
		coreTest.signIn(driver);
	    coreTest.createCustomer(driver);  
	    email = coreTest.getEmail();   
	    Utils.waitTime(25000);
	    
	    DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    addressRecordFound = dbAuto.dbFindAddress(email);
	    dbAuto.dbDisconnect();
	    Assert.assertTrue(addressRecordFound, "address record was not found");
		driver.close();
		Log.info("createNewCustomerDBCheckAddressTable Completed");
		    
	}
	
	
	@Test(priority=4 , enabled = true)
	public void createNewCustomerDBCheckSecurityTable() throws Exception{
	
	    Log.info("185947"); 
		coreTest.signIn(driver);
	    coreTest.createCustomer(driver);  
	    email = coreTest.getEmail();   
	    DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    Utils.waitTime(5000);
	    securityRecordFound = dbAuto.dbFindSecurityAnswer(email);
	    dbAuto.dbDisconnect();
	    Assert.assertTrue(securityRecordFound, "security record was not found");
		driver.close();
		Log.info("createNewCustomerDBCheckSecurityTable Completed");
		    
	}
	
	@Test(priority=5 , enabled = true)
	public void createNewCustomerDBCheckPhoneTable() throws Exception{
	
	    Log.info("185948"); 
		coreTest.signIn(driver);
	    coreTest.createCustomer(driver);  
	    email = coreTest.getEmail();   
	    Utils.waitTime(15000);
	    
	    DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    phoneRecordFound = dbAuto.dbFindPhone(email);
	    dbAuto.dbDisconnect();
	    Assert.assertTrue(phoneRecordFound, "phone record was not found");
		driver.close();
		Log.info("createNewCustomerDBCheckPhoneTable Completed");
		    
	}
	
	
	@Test(priority=6 , enabled = true)
	public void createNewCustomerCancelDBCheck() throws Exception{
	
	    Log.info("185949"); 
		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		nPage.enterPhone(driver, Global.PHONE);
		nPage.clickContinue(driver);
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.clickCancel(driver);
		DBAutomation dbAuto = new DBAutomation();
	    dbAuto.dbCmsConnect();
	    contactRecordFound  = dbAuto.dbFindCustomer(email);
	    Assert.assertFalse(contactRecordFound , "customer record should not be created");
	    dbAuto.dbDisconnect();
		driver.close();
		Log.info("createNewCustomerCancelDB Completed");
		    
	}
	
	
	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Completed");
		Reporter.log("TearDown Completed");
		driver.quit();

	}
}