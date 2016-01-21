package test.java;

/* 
 * Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * An abstract base for all of the Android tests within this package
 *
 * Responsible for setting up the Appium test Driver
 */
public abstract class InstagramTestBase {
    /**
     * Make the driver static. This allows it to be created only once
     * and used across all of the test classes.
     */
    public static AndroidDriver<MobileElement> driver;   

    /**
     * This method runs before any other method.
     *
     * Appium follows a client - server model:
     * We are setting up our appium client in order to connect to Device Farm's appium server.
     *
     * We do not need to and SHOULD NOT set our own DesiredCapabilities
     * Device Farm creates custom settings at the server level. Setting your own DesiredCapabilities
     * will result in unexpected results and failures.
     *
     * @throws MalformedURLException An exception that occurs when the URL is wrong
     */
    @BeforeSuite
    public void setUpAppium() throws MalformedURLException
    {
        final String URL_STRING = "http://127.0.0.1:4723/wd/hub";

        URL url = new URL(URL_STRING);        
        
        //Use a empty DesiredCapabilities object
        driver = new AndroidDriver<MobileElement>(url, new DesiredCapabilities());

        //Use a higher value if your mobile elements take time to show up
        driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
        
        login();
    }
    
    /**
     * Before any test we want to be sure we can login to the app.
     */
    public void login()
    {
    	int attempt = 0;
    	boolean loggedIn = false;
    	WebDriverWait wait = new WebDriverWait(driver, 10);

    	takeScreenshot("homeScreen");
    	
    	// Open login page. 
    	wait.until(ExpectedConditions.elementToBeClickable(By.id("log_in"))).click();
    	
    	takeScreenshot("LoginScreen");
    	
    	while(!loggedIn && (attempt++) < 10)
    	{
    		int usernameCount = 0;
		
    		WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_username")));
		
    		// Sometimes sendKeys will remove a letter form the start or end of a string 
    		// so we will reenter the username (if invalid) up to 10 times before moving on.
    		while(!"stitchstorm".equals(username.getText()) && (usernameCount++) < 10)
    			username.sendKeys("stitchstorm");
		
			WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));		
			password.sendKeys("***");
			
			wait.until(ExpectedConditions.elementToBeClickable(By.id("next_button"))).click();
			
			try
			{
				driver.findElementById("alertTitleContainer").isDisplayed();
				driver.findElementById("button_negative").click();
								
				//Clears the input fields completely be causing the activities to reload.
				wait.until(ExpectedConditions.elementToBeClickable(By.id("log_in_button"))).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.id("log_in"))).click();
			}
			catch(NoSuchElementException e)
			{
				loggedIn = true;
			}
		}
    	
    	takeScreenshot("LoggedIn");
    	
    	Assert.assertTrue(loggedIn, "Login unsuccessful");
    }

    /**
     * Always remember to quit
     */
    @AfterSuite
    public void tearDownAppium()
    {
        driver.quit();
    }

    /**
     * Restart the app after every test class to go back to the main
     * screen and to reset the behavior
     */
    @AfterClass
    public void restartApp() 
    {
        driver.resetApp();
    }
    
    /**
     * Takes a screenshot and saves it. This method was taken from
     * http://docs.aws.amazon.com/devicefarm/latest/developerguide/test-types-android-appium-java-testng.html
     * 
     * @param name The name of the image.
     * 
     * @return <tt>True</tt> when screenshot successfully saved, <tt>False</tt> otherwise.
     */
    public static boolean takeScreenshot(final String name) 
	{
		String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
	    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    return screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));
	}
}