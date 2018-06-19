package com.cubic.cmctests.testslegacy;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cubic.cmcjava.utils.*;

// Don't run in regression
public class SauceLabTest {

	static String browser;
	CoreTest coreTest = new CoreTest();
	UserData userData = new UserData();
	boolean saveEmail = true;
	public static final String USERNAME = "cubic";
    public static final String ACCESS_KEY = "38f0a91e-f3a0-444b-ac89-d44140d1f18c";

	@Parameters({"browser", "executionenv"})
	@Test(priority = 1, enabled = true)
	public void goToGoogle() throws Exception {
	    
	    String nodeUrl;
        nodeUrl = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";
        DesiredCapabilities capbility = DesiredCapabilities.chrome();
        WebDriver driver = new RemoteWebDriver(new URL(nodeUrl), capbility);
	    driver.get("http://www.google.com");
		System.out.println("Success - reached google");
		driver.close();
	}


}