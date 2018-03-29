package com.cubic.cmctests.tests;

import com.cubic.accelerators.WebDriverActions;
import com.cubic.accelerators.WebDriverEngine;
import com.cubic.cmcjava.constants.AppConstants;
import com.cubic.cmcjava.dataproviders.CMCDataProviderSource;
import com.cubic.cmcjava.lib.CMCHomeLib;
import com.cubic.cmcjava.lib.CommonLib;
import com.cubic.cmcjava.lib.ContactDetailsLib;
import com.cubic.cmcjava.lib.CustomerDetailsLib;
import com.cubic.cmcjava.lib.CustomerSearchLib;
import com.cubic.cmcjava.lib.KeyCloakLib;
import com.cubic.cmcjava.page.LaunchPadPage;
import com.cubic.genericutils.GenericConstants;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

public class demoCMCTest extends WebDriverEngine {
    private String host = "bb-corp-cas01.corp.cubic.cub";
    private String username = "cts.systemtest";
    private String password = "gd6N6MCkCdDa9Rc##";
    private String folder = "Inbox/Cmc";

    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void C11862_CSRLoginTest(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CommonLib commonLib = new CommonLib(data, action);
        KeyCloakLib keyCloakLib = new KeyCloakLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                commonLib.navigateToBaseURLAndLogin();
                action.assertTrue(action.waitForVisibilityOfElement(LaunchPadPage.WelcomeHeader, "Dashboard page welcome header"), "The user has successfully logged in");
                keyCloakLib.logOutifLoggedIn();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            action.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void C11627_CSRResetCustomerPasswordTest(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CommonLib commonLib = new CommonLib(data, action);
        KeyCloakLib keyCloakLib = new KeyCloakLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                Date beforeTest = new Date();
                CustomerSearchLib searchLib = commonLib.navigateToBaseURLAndLogin()
                        .navigateToCustomer()
                        .setSearchType(data.get("SEARCH_TYPE"))
                        .setEmail(data.get("EMAIL"))
                        .searchCustomer()
                        .selectFirstCustomer();

                ContactDetailsLib contactDetailsLib = searchLib.acceptSecurityAndContinue()
                        .openContactDetails()
                        .resetPasswordAndConfirm();

                Properties props = System.getProperties();
                props.put("mail.imaps.auth.plain.disable", "true");

                javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
                javax.mail.Store store = session.getStore("imaps");
                store.connect(host, 993, username, password);
                Folder inbox = store.getFolder(folder);
                inbox.open(Folder.READ_ONLY);

                action.assertTrue((inbox.getUnreadMessageCount() > 0), "No New Emails!");
                Message messages[] = inbox.getMessages();
                int i = (inbox.getMessages().length - 10);
                boolean emailFound = true;

                while (i < (inbox.getMessages().length)) {
                    String newSubject = messages[i].getSubject();

                    if (newSubject.equals(data.get("SUBJECT")) && messages[i].getReceivedDate().after(beforeTest)) {
                        emailFound = true;
                        messages[i].setFlag(Flags.Flag.DELETED, true);
                        inbox.close(true);
                        break;
                    }
                    i++;
                }
                if(emailFound) action.successReport("Subject Validation", "Email with valid subject found");
                else action.failureReport("Subject Validation", "No email with valid subject found");
                keyCloakLib.logOutifLoggedIn();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            action.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void C11841_ViewActivityHistoryTest(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CommonLib commonLib = new CommonLib(data, action);
        KeyCloakLib keyCloakLib = new KeyCloakLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                CustomerSearchLib searchLib = commonLib.navigateToBaseURLAndLogin()
                        .navigateToCustomer()
                        .setSearchType(data.get("SEARCH_TYPE"))
                        .setEmail(data.get("EMAIL"))
                        .searchCustomer()
                        .selectFirstCustomer();

                CustomerDetailsLib contactDetailsLib = searchLib.acceptSecurityAndContinue().expandActivityHistory();

                action.assertTrue(contactDetailsLib.isActivityHistoryVisible(), "Activity History Table");
                keyCloakLib.logOutifLoggedIn();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            action.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    /**
     * TestRail - C12483
     * View Travel History - Trips - Date Range Search Criteria - Default Dates
     *
     * Verifies that there are default dates when selecting to view the travel history for a customer on the CMC
     */
    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void viewTravelHistory_DateRangeDefaultTEST(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CMCHomeLib cmcHome = new CMCHomeLib(data, action);
        KeyCloakLib keyCloakLib = new KeyCloakLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                CustomerSearchLib searchLib = cmcHome.navigateToBaseURLAndLogin()
                        .navigateToCustomer()
                        .setSearchType(data.get("SEARCH_TYPE"))
                        .setLastName(data.get("LAST_NAME"))
                        .searchCustomer()
                        .selectFirstCustomer();

                CustomerDetailsLib detailsLib = searchLib.acceptSecurityAndContinue()
                        .expandTravelHistory();

                String fromDate = detailsLib.getTravelHistoryFromDate();
                String toDate = detailsLib.getTravelHistoryToDate();
                String today = DateTimeFormatter.ofPattern("YYYY-M-d").format(LocalDate.now());
                String weekAgo = DateTimeFormatter.ofPattern("YYYY-M-d").format(LocalDate.now().minusDays(7));

                action.assertTextStringMatching(fromDate, weekAgo);
                action.assertTextStringMatching(toDate, today);
                keyCloakLib.logOutifLoggedIn();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            action.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } catch (Throwable t) {
            t.printStackTrace();
            action.failureReport("Unhandled Throwable", t.getMessage());
            throw new Throwable(t);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }

    /**
     * TestRail - C12486
     * View Travel History - Row Count Search Criteria - Default Value
     *
     * Verifies that there is a default row count value when selecting to view the travel history for a customer on the CMC
     */
    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void viewTravelHistory_RowCountDefaultTEST(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CMCHomeLib cmcHome = new CMCHomeLib(data, action);
        KeyCloakLib keyCloakLib = new KeyCloakLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                CustomerSearchLib searchLib = cmcHome.navigateToBaseURLAndLogin()
                        .navigateToCustomer()
                        .setSearchType(data.get("SEARCH_TYPE"))
                        .setLastName(data.get("LAST_NAME"))
                        .searchCustomer()
                        .selectFirstCustomer();

                CustomerDetailsLib detailsLib = searchLib.acceptSecurityAndContinue()
                        .expandTravelHistory();

                action.assertTextStringMatching(detailsLib.getTravelHistoryRowCount(), data.get("EXPECTED"));
                keyCloakLib.logOutifLoggedIn();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            action.failureReport("Unhandled Exception Thrown", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            teardownAutomationTest(context, testCaseName);
        }
    }
}
