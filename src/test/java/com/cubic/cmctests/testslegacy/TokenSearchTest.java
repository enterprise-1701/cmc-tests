package com.cubic.cmctests.testslegacy;

import java.util.concurrent.TimeUnit;

import com.cubic.accelerators.RESTActions;
import com.cubic.accelerators.RESTEngine;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.pageobjects.*;
import com.cubic.cmcjava.utils.*;
import org.testng.xml.dom.ITagFactory;

//#################################################################################
//
//#################################################################################

public class TokenSearchTest extends RESTEngine {

    private static Logger Log = Logger.getLogger(Logger.class.getName());
    private static final String TRANSACTION = "P2P-1, P2P-1";
    private static final String AMOUNT = "$5.00";
    private static final String NO_RECORD_FOUND = "No Records Found.";
    private static final String VALID_CC = "4605803622930046";
    private static final String TRANSIT_ACCOUNT = "490000034432";

    static WebDriver driver;
    static String browser;
    CoreTest coreTest = new CoreTest();
    UserData userData = new UserData();
    CreditCardNumberGenerator ccGen = new CreditCardNumberGenerator();
    RESTActions restActions;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser) throws InterruptedException {

//        Logging.setLogConsole();
//        Logging.setLogFile();
//        Log.info("Setup Started");
//        Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
//        Log.info("Current Browser: " + browser);
        driver = Utils.openBrowser(browser);
//        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//        Log.info("Setup Completed");
    }

    // Search token invalid month
    @Test(priority = 1, enabled = true)
    public void searchTokenInvalidMonth(ITestContext context) throws Exception {
        String testCaseName = "27798:searchTokenInvalidMonth";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("27798");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "38520000023237");
            tPage.selectExpMonth(driver);
            tPage.selectInvalidExpMonth(driver);
            tPage.selectExpYear(driver);
            Assert.assertEquals(tPage.getRequiredFieldErrorMonth(driver), Global.REQUIREDFIELD_ERROR);
            Assert.assertFalse(tPage.isSearchTokenEnabled(driver));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token invalid year
    @Test(priority = 2, enabled = true)
    public void searchTokenInvalidYear(ITestContext context) throws Exception {
        String testCaseName = "185334:searchTokenInvalidYear";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185334");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "38520000023237");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.selectInvalidExpYear(driver);
            Assert.assertEquals(tPage.getRequiredFieldErrorYear(driver), Global.REQUIREDFIELD_ERROR);
            Assert.assertFalse(tPage.isSearchTokenEnabled(driver));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token missing bank card number
    @Test(priority = 3, enabled = true)
    public void searchTokenMissingBankCard(ITestContext context) throws Exception {
        String testCaseName = "185335:searchTokenMissingBankCard";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185335");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            Assert.assertFalse(tPage.isSearchTokenEnabled(driver));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token new search
    @Test(priority = 4, enabled = true)
    public void searchTokenNewSearch(ITestContext context) throws Exception {
        String testCaseName = "29972:searchTokenNewSearch";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29972");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "38520000023237");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickNewSearch(driver);
            Assert.assertEquals(tPage.getBankNumber(driver), "");
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token no record found
    @Test(priority = 5, enabled = false)
    public void searchTokenNoRecordFound(ITestContext context) throws Exception {
        String testCaseName = "27800:searchTokenNoRecordFound";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("27800");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "38520000023237");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token no record found new search
    @Test(priority = 6, enabled = false)
    public void searchTokenNoRecordFoundNewSearch(ITestContext context) throws Exception {
        String testCaseName = "185360:searchTokenNoRecordFoundNewSearch";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185360");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "38520000023237");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            tPage.clickResultsNewSearch(driver);
            Assert.assertEquals(tPage.getBankNumber(driver), "");
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token invalid token search
    @Test(priority = 7, enabled = false)
    public void searchTokenInvalidTokenSearch(ITestContext context) throws Exception {
        String testCaseName = "29970:searchTokenInvalidTokenSearch";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29970");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, Global.INVALID_CC);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            tPage.enterNickName(driver, "joe");
            tPage.clickRegisterCustomer(driver);
            Assert.assertEquals(tPage.getInavlidTokenError(driver), "Invalid Token");
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Search token invalid token search new search
    @Test(priority = 8, enabled = false)
    public void searchTokenInvalidTokenNewSearch(ITestContext context) throws Exception {
        String testCaseName = "185366:searchTokenInvalidTokenNewSearch";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185366");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, Global.INVALID_CC);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            tPage.enterNickName(driver, "joe");
            tPage.clickRegisterCustomer(driver);
            Assert.assertEquals(tPage.getInavlidTokenError(driver), "Invalid Token");
            tPage.clickInvalidTokenNewSearch(driver);
            Assert.assertEquals(tPage.getBankNumber(driver), "");
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // STA-698 - Search and view unregistered customer
    @Test(priority = 9, enabled = false)
    public void searchUnregistredCustomer(ITestContext context) throws Exception {
        String testCaseName = "29968:searchUnregisteredCustomer";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29968");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, VALID_CC);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Utils.waitTime(5000);
            tPage.clickViewDetails(driver);
            Utils.waitTime(5000);
            UnregistredCustomerPage uPage = new UnregistredCustomerPage(driver);
            Assert.assertEquals(uPage.getAccountNumber(driver), TRANSIT_ACCOUNT);
            Assert.assertEquals(uPage.getStatus(driver), "Active");
            Assert.assertEquals(uPage.getTokenType(driver), "Bankcard");

            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // STA-698 - Search and view registered customer
    @Test(priority = 10, enabled = false)
    public void searchRegistredCustomer(ITestContext context) throws Exception {
        String testCaseName = "29973:searchRegisteredCustomer";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("29973");
            // Register a new customer using token
            // First generate transit account by using no records found
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();

            // generate a new CC number that is not in the system
            CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
            String validCCNumber = ccGenerator.generate("4", 16);
            Log.info("CC number is:  " + validCCNumber);

            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            tPage.enterTokenVerificationNickName(driver, Global.NICKNAME);
            tPage.clickTokenVerificationRegisterCustomer(driver);

            // get the subsystem account id to use for verification later
            Assert.assertTrue(tPage.isLinkAccountConfirmationDisplayed(driver));
            String accountID = tPage.getLinkAccountConfirmationNumber(driver);
            Log.info("Link Account confirmation message is:  " + tPage.getLinkAccountConfirmationNumber(driver));
            accountID = accountID.substring(16, 28);
            Log.info("Account ID is:  " + accountID);
            coreTest.createCustomerTokenSearch(driver);
            tPage.clickHome(driver);

            // search for new registred customer using token
            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);

            // Assertions on the registered customer based on token search
            TokenSearchCustomerVerifiPage vPage = new TokenSearchCustomerVerifiPage(driver);
            Assert.assertTrue(vPage.getName(driver).contains(Global.FNAME));
            Assert.assertTrue(vPage.getContact(driver).contains("Primary"));
            Assert.assertTrue(vPage.getStatus(driver).contains("Active"));
            Assert.assertTrue(vPage.getAccount(driver).contains(accountID));
            vPage.clickSecurityBox(driver);
            vPage.clickContinue(driver);

            TokenSearchCustomerDetailsPage dPage = new TokenSearchCustomerDetailsPage(driver);
            Assert.assertEquals(dPage.getFirstName(driver), Global.FNAME);
            Assert.assertEquals(dPage.getEmail(driver), userData.getEmail());
            Assert.assertTrue(dPage.getAccount(driver).contains(accountID));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Fail due to CCBO-8318
    @Test(priority = 11, enabled = false)
    public void searchRegistredCustomerVerifiedThreeInfo(ITestContext context) throws Exception {
        String testCaseName = "185925:searchRegisteredCustomerVerifiedThreeInfo";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185925");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();

            // generate a new CC number that is not in the system
            CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
            String validCCNumber = ccGenerator.generate("4", 16);

            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            tPage.enterTokenVerificationNickName(driver, "joe");
            tPage.clickTokenVerificationRegisterCustomer(driver);

            // get the subsystem account id to use for verification later
            Assert.assertTrue(tPage.isLinkAccountConfirmationDisplayed(driver));
            String accountID = tPage.getLinkAccountConfirmationNumber(driver);
            Log.info("Link Account confirmation message is:  " + tPage.getLinkAccountConfirmationNumber(driver));
            accountID = accountID.substring(16, 28);
            Log.info("Account ID is:  " + accountID);
            coreTest.createCustomerTokenSearch(driver);
            tPage.clickHome(driver);

            // search for new registred customer using token
            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            Utils.waitTime(5000);
            tPage.clickSearchToken(driver);

            TokenSearchCustomerVerifiPage vPage = new TokenSearchCustomerVerifiPage(driver);
            Assert.assertTrue(vPage.getName(driver).contains(Global.FNAME));
            Assert.assertTrue(vPage.getContact(driver).contains("Primary"));
            Assert.assertTrue(vPage.getStatus(driver).contains("Active"));
            Assert.assertTrue(vPage.getAccount(driver).contains(accountID));
            vPage.clickAddressBox(driver);
            vPage.clickDobBox(driver);
            vPage.clickNameBox(driver);
            vPage.clickContinue(driver);
            TokenSearchCustomerDetailsPage dPage = new TokenSearchCustomerDetailsPage(driver);
            Assert.assertEquals(dPage.getFirstName(driver), Global.FNAME);
            Assert.assertEquals(dPage.getEmail(driver), userData.getEmail());
            Assert.assertTrue(dPage.getAccount(driver).contains(accountID));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // Fail due to CCBO-8318
    // Search and view subsystem page for registered customer
    @Test(priority = 12, enabled = false)
    public void viewRegistredCustomerSubsystemPage(ITestContext context) throws Exception {
        String testCaseName = "185926:viewRegisteredCustomerSubsystemPage";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185926");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();

            // generate a new CC number that is not in the system
            CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
            String validCCNumber = ccGenerator.generate("4", 16);

            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            tPage.enterTokenVerificationNickName(driver, "joe");
            tPage.clickTokenVerificationRegisterCustomer(driver);

            // get the subsystem account id to use for verification later
            Assert.assertTrue(tPage.isLinkAccountConfirmationDisplayed(driver));
            String accountID = tPage.getLinkAccountConfirmationNumber(driver);
            Log.info("Link Account confirmation message is:  " + tPage.getLinkAccountConfirmationNumber(driver));
            accountID = accountID.substring(16, 28);
            Log.info("Account ID is:  " + accountID);
            coreTest.createCustomerTokenSearch(driver);
            tPage.clickHome(driver);

            // search for new registered customer using token
            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);

            TokenSearchCustomerVerifiPage vPage = new TokenSearchCustomerVerifiPage(driver);
            Assert.assertTrue(vPage.getName(driver).contains(Global.FNAME));
            Assert.assertTrue(vPage.getContact(driver).contains("Primary"));
            Assert.assertTrue(vPage.getStatus(driver).contains("Active"));
            Assert.assertTrue(vPage.getAccount(driver).contains(accountID));
            vPage.clickSecurityBox(driver);
            vPage.clickContinue(driver);

            TokenSearchCustomerDetailsPage dPage = new TokenSearchCustomerDetailsPage(driver);
            Assert.assertTrue(dPage.getAccount(driver).contains(accountID));

            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

   
    // token verification test
    @Test(priority = 13, enabled = false)
    public void searchTokenVerification(ITestContext context) throws Exception {
        String testCaseName = "185927:searchTokenVerification";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185927");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, "4605803622930046");
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Utils.waitTime(15000);

            Assert.assertEquals(tPage.getTokenVerificationTransaction(driver), TRANSACTION);
            Assert.assertEquals(tPage.getTokenVerificationAmount(driver), AMOUNT);

            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // CCD-839
    // Fail due to CCBO-8318
    @Test(priority = 15, enabled = false)
    public void tokenSearchWithSpace(ITestContext context) throws Exception {
        String testCaseName = "185928:tokenSearchWithSpace";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185928");
            // Register a new customer using token with space
            // First generate transit account by using no records found
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();

            // generate a new CC number that is not in the system
            CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
            String validCCNumber = ccGenerator.generate("4", 16);

            // Add space at the end of CC number
            validCCNumber = validCCNumber + "  " + "!";
            Log.info("CC number is: " + validCCNumber);

            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            tPage.enterTokenVerificationNickName(driver, "joe");
            tPage.clickTokenVerificationRegisterCustomer(driver);

            // get the subsystem account id to use for verification later
            Assert.assertTrue(tPage.isLinkAccountConfirmationDisplayed(driver));
            String accountID = tPage.getLinkAccountConfirmationNumber(driver);
            Log.info("Link Account confirmation message is:  " + tPage.getLinkAccountConfirmationNumber(driver));
            accountID = accountID.substring(16, 28);
            Log.info("Account ID is:  " + accountID);
            coreTest.createCustomerTokenSearch(driver);
            tPage.clickHome(driver);

            // search for new registred customer using token with space
            tPage.enterBankNumber(driver, validCCNumber);
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);

            // Assertions on the registered customer based on token search
            TokenSearchCustomerVerifiPage vPage = new TokenSearchCustomerVerifiPage(driver);
            Assert.assertTrue(vPage.getName(driver).contains(Global.FNAME));
            Assert.assertTrue(vPage.getContact(driver).contains("Primary"));
            Assert.assertTrue(vPage.getStatus(driver).contains("Active"));
            Assert.assertTrue(vPage.getAccount(driver).contains(accountID));
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    // CCD-320
    @Test(priority = 16, enabled = false)
    public void searchTokenClickHome(ITestContext context) throws Exception {
        String testCaseName = "185929:searchTokenClickHome";

        try {
            restActions = setupAutomationTest(context, testCaseName);
            restActions.successReport("test", "test");
            Log.info("185929");
            coreTest.signIn(driver);
            TokenSearchPage tPage = getTokenSearchPage();
            tPage.enterBankNumber(driver, ccGen.generate("4", 16));
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            tPage.clickHome(driver);
            Assert.assertEquals(tPage.getBankNumber(driver), "");

            // repeat test for header search button
            tPage.enterBankNumber(driver, ccGen.generate("4", 16));
            tPage.selectExpMonth(driver);
            tPage.selectExpYear(driver);
            tPage.clickSearchToken(driver);
            Assert.assertEquals(tPage.getNoRecordFoundError(driver), NO_RECORD_FOUND);
            tPage.clickSearchHeader(driver);
            Assert.assertEquals(tPage.getBankNumber(driver), "");
            driver.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            restActions.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    private TokenSearchPage getTokenSearchPage() throws Exception {
        DashboardPage dashPage = new DashboardPage(driver);
        dashPage.clickCustomerTab(driver);
        dashPage.switchToFrame(driver);
        TokenSearchPage tPage = new TokenSearchPage(driver);
        return tPage;
    }

    @AfterMethod
    public void tearDown() {
        Log.info("TearDown Complete");
        Reporter.log("TearDown Complete");
        driver.quit();

    }
}