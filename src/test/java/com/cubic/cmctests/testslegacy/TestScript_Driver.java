package com.cubic.cmctests.testslegacy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.Factory;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.cubic.backoffice.utils.BackOfficeUtils;
import com.cubic.genericutils.FileUtil;
import com.cubic.genericutils.GenericConstants;
import com.cubic.testrail.TestRailUtil;

//New TestScript Driver 
public class TestScript_Driver {
    private static Hashtable<String , String> propTable = GenericConstants.GENERIC_FW_CONFIG_PROPERTIES;
    private String testRailProjectID;
    private String testRailSuiteID;
    
    public static final String CLASS_NAME = "TestScript_Driver";
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);
    
    @SuppressWarnings("unchecked")
    @Factory
    @Parameters({"projectID","suiteID"})
        public Object[] factoryMethod(ITestContext context, @Optional String projectID,@Optional String suiteID) throws Exception {
        Object[] data = null;
        HashSet<String> testClassSet = null;
        LOG.info("INSIDE TestScript_Driver.factoryMethod()");

        // Get the list (HashSet) of UNIQUE Test Classes found in the currently running TestNG.xml Suite
        testClassSet = BackOfficeUtils.getTestClassListFromTestNG(context);
        
        // Check if TestRail Project ID is provided by TestNG.xml
        if ((projectID != null) && (!projectID.equals("%projectID%")) && (!projectID.equals("${ProjectID}"))) {
            testRailProjectID = projectID;
            LOG.info("testRailProjectID = projectID: " + testRailProjectID);
        }
        else if (propTable.get("Test_Rail_Project_ID") != null) {
            // Else, get it from Generic Properties file
            testRailProjectID = propTable.get("Test_Rail_Project_ID");
            LOG.info("testRailProjectID = propTable.get(Test_Rail_Project_ID): " + testRailProjectID);
        }
        
        // Check if TestRail Suite ID is provided by TestNG.xml
        if ((suiteID != null) && (!suiteID.equals("%suiteID%")) && (!suiteID.equals("${SuiteID}")) && (!suiteID.equals("${SuiteID}"))){
            testRailSuiteID = suiteID;
            LOG.info("testRailSuiteID = suiteID: " + testRailSuiteID);
        }
        else if (propTable.get("Test_Rail_Suite_ID") != null){
            // Else, get it from Generic Properties file
            testRailSuiteID = propTable.get("Test_Rail_Suite_ID");
            LOG.info("testRailSuiteID=propTable.get(Test_Rail_Suite_ID): " + testRailSuiteID);
        }
        
        JSONArray automationReferenceClasses = new JSONArray();
        JSONArray testRailCaseIDs = new JSONArray();
        
        // Get "reduced" JSONObject with ONLY test cases found in the TestNG.xml Suite
        JSONObject MyData=TestRailUtil.createTestClassListFromTestSet(testClassSet, projectID, suiteID);
        
        automationReferenceClasses = (JSONArray) MyData.get("TestClasses");        // Get Test Classes
        testRailCaseIDs = (JSONArray) MyData.get("TestRailCaseIDs");            // Get TestRail IDs for the above Classes (Tests)
        
        JSONParser parser = new JSONParser();
        String testRunPostRequestJSON = FileUtil.readFile(GenericConstants.TEST_CASES_TO_BE_EXECUTED_JSON_FILE_PATH+GenericConstants.TEST_RAIL_TEST_RUN_TEMPLATE_JSON);
        Object obj = parser.parse(testRunPostRequestJSON);
        JSONObject jsonObject = (JSONObject) obj;
        
        jsonObject.put("case_ids", testRailCaseIDs);
        
        String Data;
        List<Object> list = new ArrayList<Object>();
        
        // Cycle through test cases that came back with an "Automation Reference", to be included in the JSON file
        for (int i = 0; i < automationReferenceClasses.size(); i++) {              
            Object classObj;
            
            try {
                LOG.info("INDEX i: " + i + ", automationRefernceClasses.get(i).toString(): " + automationReferenceClasses.get(i).toString() + ", ADDING...");
                Data = this.getClass().getPackage() + "." + automationReferenceClasses.get(i).toString();        // Test Class is expected to be in same package as "this" class
                classObj = Class.forName(Data.split(" ")[1]).newInstance();
                list.add(classObj);
            } 
            catch (Exception e) {
                e.printStackTrace();
            }               
        }
        
        // Write to JSON file containing test cases/classes to run and update test results in TestRail
        try (FileWriter file = new FileWriter(GenericConstants.TEST_CASES_TO_BE_EXECUTED_JSON_FILE_PATH + GenericConstants.TEST_RAIL_TEST_RUN_TEMPLATE_JSON)) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        data = list.toArray();

        return data;
    } 
}
