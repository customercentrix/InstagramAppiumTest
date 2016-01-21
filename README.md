#Instagram Appium Test
#### Sample test of Instagram using Appium and AWS Device Farm

This Repo contains sample test files to be packaged and run on the AWS Device Farm. Instructions for installing Appium can be found in the [Getting Started Guide](http://appium.io/slate/en/tutorial/android.html?java#getting-started-with-appium). The analysis of this test and more information about Device Farm can be found in the blog post  [Mobile Testing on AWS Device Farm in less than 60 minutes](http://Loadstorm.com/someblogpost).

## Running Locally

You will need to have Eclipse, Appium, the Android SDK, and Maven installed to run this test. Also, install a TestNG plugin in Eclipse so you can run the test. Before you start make sure an emulator is running or a device in developer mode is connected to the computer. 

1.  Download and import the project into Eclipse.
2.  Right click on the project and click Maven > Update.
3.  CD into the project and start the Appium server by running the command **./start-appium-android.sh**
4.  Open the file **HomeActivity.java**, right click > Run As > TestNG Test

If the tests do not run you may have to update the **start-appium-android.sh** file to work in your environment.

## Packaging and Running

To run the tests on Device Farm they must first be packaged.

1.  Cd into the project and run the command **mvn clean package -DskipTests=true**
2.  Open the folder **target** and verify the **zip-with-dependencies.zip** file exists.
3.  Login to Device Farm and create a new Project and Run.
4.  Upload the **Instagram.apk** file found in the */lib* folder.
5.  Select the TestNG type and upload the **zip-with-dependencies.zip**.

If the tests upload successfully but do not run then verify that the ***-tests.jar**	 contains all the test files. To inspect the jar enter the following in the projects folder:

    jar tf target/InstagramAppiumTest-0.0.1-SNAPSHOT-tests.jar 
    

