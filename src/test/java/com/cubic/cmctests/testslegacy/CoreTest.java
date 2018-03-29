package com.cubic.cmctests.testslegacy;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//Core classes for sign-in and create customer  
//#################################################################################

public class CoreTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String email;
	private static String phoneNumber;
	private static String userId;
	UserData userData = new UserData();

	public WebDriver getLabStatus(WebDriver driver) throws Exception {
		StatusPage statusPage = new StatusPage(driver);
		Log.info("QA LAB VERSION: " + statusPage.getVersion(driver));
		Log.info("QA LAB BUILD: " + statusPage.getBuild(driver));
		Log.info("QA LAB MACHINE: " + statusPage.getMachine(driver));
		return driver;
	}

	public WebDriver signIn(WebDriver driver) throws Exception {
		PilLabPage pilPage = new PilLabPage(driver);
		pilPage.getLandingPage(Global.URL2);
		pilPage.enterUserName(driver, Global.KEYCLOAKUSER);
		pilPage.enterPassword(driver, Global.KEYCLOAKPASS);
		pilPage.clickLogin(driver);
		Utils.waitTime(5000);
		return driver;
	}
	
	public WebDriver signIn(WebDriver driver, String url, String user, String password ) throws Exception {
		PilLabPage pilPage = new PilLabPage(driver);
		pilPage.getLandingPage(url);
		Utils.waitTime(5000);
		pilPage.enterUserName(driver, user);
		pilPage.enterPassword(driver, password);
		pilPage.clickLogin(driver);
		Utils.waitTime(5000);
		return driver;
	}	
	
	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phoneNumber;
	}

	public String getUserid() {
		return userId;
	}

	public UserData getUserData() {
		return userData;
	}

	public WebDriver createCustomer(WebDriver driver) throws Exception {

		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		Utils.waitTime(5000);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		userData.setEmail(email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6, 10);
		nPage.clickContinue(driver);
		Utils.waitTime(4000);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, userData.getEmail());
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}

	public WebDriver updateContact(WebDriver driver, String email) throws Exception {

		SearchPage sPage = new SearchPage(driver);
		Utils.waitTime(15000);
		sPage.clickContact(driver);
		ContactDetailsPage contactPage = new ContactDetailsPage(driver);
		
		contactPage.enterFname(driver, Global.FNAME2);
		contactPage.enterLname(driver, Global.LNAME2);
		contactPage.clickSubmit(driver);
	    Utils.waitTime(5000);
		return driver;

	}

	public WebDriver createCustomer2(WebDriver driver) throws Exception {

		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		userData.setEmail(email);
		phoneNumber = Utils.randomPhoneNumber();
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, userData.getEmail());
		nPaget.enterPin(driver, Global.PIN);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}

	// Token Search for NYC-CMC - valid token
	public WebDriver createCustomerTokenSearch(WebDriver driver) throws Exception {

		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		userData.setEmail(email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, userData.getEmail());
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);

		// Check for link account warning on the page
		Assert.assertTrue(nPaget.isLinkWarningDisplayed(driver), "Linking Account warning is not displayed!");
		Log.info("Link account warning displayed: " + nPaget.getLinkWarning(driver));
		nPaget.clickTokenSearchSubmit(driver);
		Utils.waitTime(5000);
		return driver;
	}

	// Token Search for NYC-CMC - invalid token
	public WebDriver createCustomerTokenSearchInvalidToken(WebDriver driver) throws Exception {

		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		userData.setEmail(email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, userData.getEmail());
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}

	public WebDriver createCustomerClickHome(WebDriver driver) throws Exception {

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
		userData.setEmail(email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6, 10);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, email);
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		Utils.waitTime(5000);
		nPaget.clickHome(driver);
		return driver;
	}
	
	//Use for unformatted phone number 
	public WebDriver createCustomerClickHomeTwo(WebDriver driver) throws Exception {

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
		userData.setEmail(email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		userData.setPhone(phoneNumber);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, email);
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		Utils.waitTime(5000);
		nPaget.clickHome(driver);
		return driver;
	}


	public WebDriver createCustomer(WebDriver driver, String email) throws Exception {

		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		nPage.enterEmail(driver, email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		nPage.clickContinue(driver);

		// look for 'YES' button
		if ( nPage.yesButtonIsDisplayed() ) {
			// if found, click 'YES' button
			nPage.clickContinueCreate(driver);
		}
		
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);

		String userName = Utils.randomEmailString();
		nPaget.enterUserName(driver, userName);
		userData.setUsername(email);
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}
	
	public WebDriver createCustomerEmailTest(WebDriver driver, String email) throws Exception {

		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		nPage.enterEmail(driver, email);
		phoneNumber = "8589778888";
		nPage.enterPhone(driver, phoneNumber);
		nPage.clickContinue(driver);
		Utils.waitTime(3000);
		nPage.clickContinueCreate(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);

		String userName = Utils.randomEmailString();
		nPaget.enterUserName(driver, userName);
		userData.setUsername(email);
		nPaget.enterPin(driver, Global.PIN);
		nPaget.clickPinLabel(driver);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}

	public WebDriver createCustomer(WebDriver driver, String fname, String lname, String phone, String email)
			throws Exception {

		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickSwitch(driver);
		nPage.clickCreateCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, fname);
		nPage.enterLastname(driver, lname);
		nPage.enterEmail(driver, email);
		nPage.enterPhone(driver, phone);
		nPage.clickContinue(driver);

		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.selectState(driver);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPaget.selectPhoneType(driver, Global.PHONETYPE);
		nPaget.selectSecurityQ(driver);
		nPaget.enterSecuirtyA(driver, Global.SECURITYA);
		nPaget.enterUserName(driver, email);
		userData.setUsername(email);
		nPaget.enterPin(driver, Global.PIN);
		nPaget.enterDob(driver, Global.DOB);
		nPaget.clickSubmit(driver);
		return driver;
	}

	public WebDriver createOrderSubmit(WebDriver driver, String email) throws Exception {

		createCustomer(driver, email);
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, 2);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		cPage.clickCreateOrder(driver);
		CreateOrderPage oPage = new CreateOrderPage(driver);
		oPage.selectOrderType(driver);
		oPage.selectPurseType(driver);
		oPage.selectOrderAmount(driver);
		oPage.clickAddtoCart(driver);
		return driver;
	}
}