package test.java;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

/**
 * Appium Test of Home Activity. 
 * 
 * @author Sonny Trujillo
 * 
 */
public class HomeActivity extends InstagramTestBase
{	
	/**
	 * Click the Like button on the first image found.
	 */
	@Test
	public void like()
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		//Click Like
		wait.until(ExpectedConditions.elementToBeClickable(By.id("row_feed_button_like"))).click();
			
		InstagramTestBase.takeScreenshot("liked");			
	}
}
