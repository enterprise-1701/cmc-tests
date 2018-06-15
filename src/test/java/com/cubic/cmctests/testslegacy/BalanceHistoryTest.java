package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.restapi.ApiCustomerPost;
import com.cubic.cmcjava.utils.*;

//#################################################################################
//
//#################################################################################

public class BalanceHistoryTest extends RESTEngine {

    private static Logger Log = Logger.getLogger(Logger.class.getName());
    private static final String TRANSACTION_AMOUNT2 = "$28.00";
    private static final String ENTRY_TYPE2 = "Purse Load";
    private static final String PURSE_RC = "Default Purse";
    private static final String NO_RECORD_FOUND = "No records found";
    private static final String TOKEN_TYPE = "Bankcard";
    private static final String STATUS = "Active";
    private static final String DEVICE = "SAAJ-Auto";
    RESTActions restActions;

    static WebDriver driver;
    static String browser;
    private static String phoneNumber;
    private static String email;
    private static CustomerData cData;
    CoreTest coreTest = new CoreTest();
    CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
    String validCCNumber;

    @Parameters({"browser", "executionenv"})
    @BeforeMethod
    public void setUp(String browser, String executionenv) throws InterruptedException {

        // Logging.setLogConsole();
        // Logging.setLogFile();
//        Log.info("Setup Started");
        // Log.info("Current OS: " +
        // WindowsUtils.readStringRegistryValue(Global.OS));
//        Log.info("Current Browser: " + browser);
        driver = Utils.openBrowser(browser, executionenv);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//        Log.info("Setup Completed");
    }

    // STA-721 - view unregistered customer balance history dynamic
    @Test(enabled = true)
    public void viewBalanceHistoryUnregisteredDynamic(ITestContext context) throws Exception {

        String testCaseName = "29984: viewBalanceHistoryUnregisteredDynamic";
        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29984");
            // create travel balance via soap call

            SOAPClientSAAJ sClient = new SOAPClientSAAJ();
            CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
            String validCCNumber = ccGenerator.generate("4", 16);
            Log.info("New CC Number is:  " + validCCNumber);
            String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
            Log.info("cc number being used is " + validCCNumber);
            Log.info("account id being returned is " + accountID);

            Log.info("wait time for balnce history to display on cmc");
            for (int i = 0; i < 2; i++) {
                Thread.sleep(60000);
                driver.navigate().refresh();
            }

            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();

            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Utils.waitTime(15000);

            tPage.clickViewDetails(driver);
            BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);

            Assert.assertTrue(
                    restActions.assertTrue(bdPage.getTokenType(driver).equals(TOKEN_TYPE), "assertion failed"));
            Assert.assertTrue(restActions.assertTrue(bdPage.getStatus(driver).equals(STATUS), "assertion failed"));
            Assert.assertTrue(restActions.assertTrue(bdPage.getDevice(driver).equals(DEVICE), "assertion failed"));

            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
            restActions.failureReport("Exception caught in catch block", "Exception is: " + e);
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // view registered customer balance history dynamic
    @Test(enabled = true)
    public void viewBalanceHistoryRegisteredDynamic(ITestContext context) throws Exception {

        String testCaseName = "29985: viewBalanceHistoryRegisteredDynamic";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29985");

            registerCustomerAndCreateBalance(driver);
            BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);

            Assert.assertTrue(restActions.assertTrue(bdPage.getPurse(driver).equals(PURSE_RC), "assertion failed"));
            Assert.assertTrue(
                    restActions.assertTrue(bdPage.getEntryType(driver).equals(ENTRY_TYPE2), "assertion failed"));
            Assert.assertTrue(restActions.assertTrue(bdPage.getTransactionAmount(driver).equals(TRANSACTION_AMOUNT2),
                    "assertion failed"));

            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
            restActions.failureReport("Exception caught in catch block", "Exception is: " + e);
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }

    }

    // view registered customer balance history details
    @Test(enabled = true)
    public void viewBalanceHistoryDetailsRegisteredDynamic(ITestContext context) throws Exception {

        String testCaseName = "29986: viewBalanceHistoryDetailsRegisteredDynamic";

        try {

            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29986");

            registerCustomerAndCreateBalance(driver);
            BalanceHistoryPage bhPage = new BalanceHistoryPage(driver);
            Utils.waitTime(5000);
            bhPage.clickRow(driver);
            BalanceHistoryDetailPage bdPage = new BalanceHistoryDetailPage(driver);

            Assert.assertTrue(restActions.assertTrue(bdPage.getPurse(driver).equals(PURSE_RC), "assertion failed"));
            Assert.assertTrue(
                    restActions.assertTrue(bdPage.getEntryType(driver).equals(ENTRY_TYPE2), "assertion failed"));

            driver.close();

        } catch (Exception e) {
            e.printStackTrace();
            restActions.failureReport("Exception caught in catch block", "Exception is: " + e);
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // CCD-851 - balance history filter
    @Test(enabled = true)
    public void viewBalanceHistoryFilterTest(ITestContext context) throws Exception {

        String testCaseName = "185941: viewBalanceHistoryFilterTest";

        try {

            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185941");

            registerCustomerAndCreateBalance(driver);
            BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);
            Utils.waitTime(5000);
            bdPage.selectTransactionType(driver, "Charge");
            bdPage.clickBalanceHistoryFilter(driver);
            Utils.waitTime(5000);

            bdPage.selectTransactionType(driver, "PurseLoad");

            bdPage.clickBalanceHistoryFilter(driver);
            Utils.waitTime(5000);

            Assert.assertTrue(restActions.assertTrue(bdPage.getNoRecordFound(driver).equals(NO_RECORD_FOUND),
                    "assertion failed"));
            Assert.assertTrue(restActions.assertTrue(bdPage.getPurse(driver).equals(PURSE_RC), "assertion failed"));
            Assert.assertTrue(
                    restActions.assertTrue(bdPage.getEntryType(driver).equals(ENTRY_TYPE2), "assertion failed"));

            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
            restActions.failureReport("Exception caught in catch block", "Exception is: " + e);
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }

    }

    //
    @Test(enabled = true)
    public void viewUpdatedBalanceHistoryTest(ITestContext context) throws Exception {

        String testCaseName = "185942: viewUpdatedBalanceHistoryTest";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185942");
            Boolean balanceUpdated = false;

            // create a tap, create oneaccount and link token to oneaccount
            loadValueAndRegisterCustomer(driver);
            NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);

            // create another tap with the original CC
            SOAPClientSAAJ sClient = new SOAPClientSAAJ();
            Log.info("cc number being used the second time is " + validCCNumber);
            sClient.postTapSOAPCall(validCCNumber);
            Log.info("wait 1 minute for trip to get generated");
            for (int i = 0; i < 2; i++) {
                Thread.sleep(60000);
                driver.navigate().refresh();
            }
            String initialBalance = (nPage3.getAccountBalances(driver).substring(1));
            Log.info("initial balance is: " + Double.valueOf(initialBalance));

            // Go back and do another search on the customer to see the updated
            // account balance

            nPage3.clickHome(driver);
            SearchPage sPage = new SearchPage(driver);
            sPage.selectSearchTypeCustomer(driver);
            sPage.clickCustomerType(driver, "Traveler");
            sPage.enterEmail(driver, email);
            sPage.clickSearch(driver);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
            Utils.waitTime(5000);
            sPage.clickRecord(driver);
            sPage.clickSecurityBox(driver);
            sPage.clickContiune(driver);

            Log.info("updated balance is: " + Double.valueOf(nPage3.getAccountBalances(driver).substring(1)));

            // Assertion based on updated account balance less than initial
            // account balance

            if (Double.valueOf(nPage3.getAccountBalances(driver).substring(1)) < Double.valueOf(initialBalance)) {
                balanceUpdated = true;
            }

            Assert.assertTrue(restActions.assertTrue((balanceUpdated), "assertion failed"));

            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
            restActions.failureReport("Exception caught in catch block", "Exception is: " + e);
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }

    }

    // private methods
    private WebDriver registerCustomerAndCreateBalance(WebDriver driver) throws Exception {

        // create balance via soap api
        SOAPClientSAAJ sClient = new SOAPClientSAAJ();
        CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
        String validCCNumber = ccGenerator.generate("4", 16);
        String accountID = sClient.createABPAccountSOAPCall(validCCNumber);
        Log.info("cc number being used is " + validCCNumber);
        Log.info("account id being returned is " + accountID);

        // Create customer test data via rest api
        cData = ApiCustomerPost.apiPostSuccess();
        email = cData.getEmail();
        phoneNumber = cData.getPhone();
        Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

        // return to selenium testing
        coreTest.signIn(driver);
        SearchPage sPage = getSearchPage();
        sPage.selectSearchTypeCustomer(driver);
        sPage.clickCustomerType(driver, "Traveler");
        sPage.enterEmail(driver, email);
        sPage.clickSearch(driver);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
        Utils.waitTime(8000);
        sPage.clickRecord(driver);
        sPage.clickSecurityBox(driver);
        sPage.clickContiune(driver);

        BasePage bPage = new BasePage(driver);
        Utils.waitTime(5000);
        bPage.clickLinkAccount(driver);
        LinkAccountPage lPage = new LinkAccountPage(driver);

        // use cc number from soap call to link account
        lPage.enterBankAccount(driver, validCCNumber);
        lPage.selectExpMonth(driver);
        lPage.selectExpYear(driver, 2);
        lPage.clickSearchToken(driver);
        lPage.enterNickName(driver, Global.NICKNAME);
        lPage.clickRegisterAndLink(driver);
        Utils.waitTime(12000);

        // check balance history
        bPage.clickBalanceHistory(driver);
        return driver;
    }

    // load value on CC and register customer and link
    private WebDriver loadValueAndRegisterCustomer(WebDriver driver) throws Exception {

        // load value on token via soap api
        SOAPClientSAAJ sClient = new SOAPClientSAAJ();
        validCCNumber = ccGenerator.generate("4", 16);
        sClient.loadValueSOAPCall(validCCNumber);
        Log.info("cc number being used is " + validCCNumber);

        // Create customer test data via rest api
        cData = ApiCustomerPost.apiPostSuccess();
        email = cData.getEmail();
        phoneNumber = cData.getPhone();
        Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

        // return to selenium testing
        coreTest.signIn(driver);
        SearchPage sPage = getSearchPage();
        sPage.selectSearchTypeCustomer(driver);
        sPage.clickCustomerType(driver, "Traveler");
        sPage.enterEmail(driver, email);
        sPage.clickSearch(driver);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
        Utils.waitTime(8000);
        sPage.clickRecord(driver);
        sPage.clickSecurityBox(driver);
        sPage.clickContiune(driver);

        // use cc number from soap call to link account
        BasePage bPage = new BasePage(driver);
        Utils.waitTime(8000);
        bPage.clickLinkAccount(driver);
        LinkAccountPage lPage = new LinkAccountPage(driver);
        lPage.enterBankAccount(driver, validCCNumber);
        lPage.selectExpMonth(driver);
        lPage.selectExpYear(driver, 2);
        lPage.clickSearchToken(driver);
        lPage.enterNickName(driver, Global.NICKNAME);
        lPage.clickRegisterAndLink(driver);

        return driver;
    }

    private TokenSearchPage getTokenSearchPage() throws Exception {
        DashboardPage dashPage = new DashboardPage(driver);
        dashPage.clickCustomerTab(driver);
        dashPage.switchToFrame(driver);
        TokenSearchPage tPage = new TokenSearchPage(driver);
        return tPage;
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