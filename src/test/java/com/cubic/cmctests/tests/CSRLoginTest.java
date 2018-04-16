package com.cubic.cmctests.tests;

import com.cubic.accelerators.WebDriverActions;
import com.cubic.accelerators.WebDriverEngine;
import com.cubic.cmcjava.constants.AppConstants;
import com.cubic.cmcjava.dataproviders.CMCDataProviderSource;
import com.cubic.cmcjava.lib.CommonLib;
import com.cubic.cmcjava.page.LaunchPadPage;
import com.cubic.genericutils.GenericConstants;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.util.Hashtable;

public class CSRLoginTest extends WebDriverEngine {
    @Test(dataProvider = AppConstants.DATA_PROVIDER, dataProviderClass = CMCDataProviderSource.class)
    public void C11862_CSRLoginTest(ITestContext context, Hashtable<String, String> data) throws Throwable {
        String testCaseName = data.get("TestCase_Description");
        WebDriverActions action = setupAutomationTest(context, testCaseName);
        CommonLib commonLib = new CommonLib(data, action);

        try {
            if (data.get(GenericConstants.RUN_MODE).equals(GenericConstants.RUN_MODE_YES)) {
                commonLib.navigateToBaseURLAndLogin();
                action.assertTrue(action.waitForVisibilityOfElement(LaunchPadPage.WelcomeHeader, "Dashboard page welcome header"), "The user has successfully logged in");
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
