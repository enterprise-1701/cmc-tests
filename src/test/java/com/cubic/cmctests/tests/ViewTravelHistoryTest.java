package com.cubic.cmctests.tests;

import com.cubic.accelerators.WebDriverActions;
import com.cubic.accelerators.WebDriverEngine;
import com.cubic.cmcjava.constants.AppConstants;
import com.cubic.cmcjava.dataproviders.CMCDataProviderSource;
import com.cubic.cmcjava.lib.CMCHomeLib;
import com.cubic.cmcjava.lib.CustomerDetailsLib;
import com.cubic.cmcjava.lib.CustomerSearchLib;
import com.cubic.genericutils.GenericConstants;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

public class ViewTravelHistoryTest extends WebDriverEngine {

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
