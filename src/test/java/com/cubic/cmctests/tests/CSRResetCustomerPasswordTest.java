package com.cubic.cmctests.tests;

import com.cubic.accelerators.WebDriverActions;
import com.cubic.accelerators.WebDriverEngine;
import com.cubic.cmcjava.constants.AppConstants;
import com.cubic.cmcjava.dataproviders.CMCDataProviderSource;
import com.cubic.cmcjava.lib.CommonLib;
import com.cubic.cmcjava.lib.ContactDetailsLib;
import com.cubic.cmcjava.lib.CustomerSearchLib;
import com.cubic.genericutils.GenericConstants;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

public class CSRResetCustomerPasswordTest extends WebDriverEngine {
    private String host = "bb-corp-cas01.corp.cubic.cub";
    private String username = "cts.systemtest";
    private String password = "gd6N6MCkCdDa9Rc##";
    private String folder = "Inbox/Cmc";

    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void C11627_CSRResetCustomerPasswordTest(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CommonLib commonLib = new CommonLib(data, action);
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
