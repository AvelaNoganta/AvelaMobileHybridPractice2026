package Utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Properties;

public class DriverFactory {

    /*
    This DriverFactory class is responsible for creating and managing the Appium driver.

    AppiumDriver is a generic driver that can work with:
    - Android
    - iOS
    - Mobile Web
    - Hybrid Apps

    If we want Android-specific features we would normally use AndroidDriver,
    but AppiumDriver is flexible and works for multiple platforms.
    */

    /*
    Static driver means there will be only ONE driver instance
    shared across the entire test execution.

    This is useful because:
    - We don't want multiple Appium sessions running unnecessarily
    - Tests can access the same driver instance
    */
    static AppiumDriver driver;


    /*
    initDriver()

    This method initializes the Appium driver based on the configuration
    provided in the config.properties file.
    */
    public static void initDriver(Properties config) throws MalformedURLException {

        /*
        If the driver is already created, do not create a new one.
        This prevents multiple sessions from starting.
        */
        if (driver != null) return;

        /*
        executionType determines what we are running:
        - mobileWeb
        - nativeApp
        */
        String execType = config.getProperty("executionType").trim();

        /*
        UiAutomator2Options is used to define the capabilities
        required to start the Appium session.

        Common capabilities include:
        - platformName (Android)
        - automationName (UiAutomator2)
        */
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName(config.getProperty("automationName"))
                .setPlatformName(config.getProperty("platformName"));


        /*
        MOBILE WEB EXECUTION
        If executionType = mobileWeb, the automation will run on a browser
        inside the mobile device/emulator (usually Chrome).
        */
        if (execType.equalsIgnoreCase("mobileWeb")) {

            // Set the browser to launch (e.g., Chrome)
            options.withBrowserName(config.getProperty("browserName"));

            System.out.println("Launching Chrome browser...");
        }

        /*
        NATIVE APP EXECUTION
        If executionType = nativeApp, Appium will install and launch the APK
        on the device or emulator.
        */
        else if (execType.equalsIgnoreCase("nativeApp")) {

            /*
            Build the absolute path to the APK file.

            System.getProperty("user.dir") gets the root project directory.
            */
            String appPath = System.getProperty("user.dir") + "/" + config.getProperty("appPath");

            // Set the app capability
            options.setApp(appPath);

            System.out.println("Launching native app: " + appPath);
        }


        /*
        Create the Appium driver session.

        Steps:
        1. Connect to Appium server
        2. Send capabilities
        3. Start the session
        */
        driver = new AppiumDriver(
                URI.create(config.getProperty("appiumServer")).toURL(),
                options
        );


        /*
        If we are running mobile web tests,
        navigate to the web URL after launching the browser.
        */
        if (execType.equalsIgnoreCase("mobileWeb")) {

            String webUrl = config.getProperty("webUrl");

            driver.get(webUrl);
        }
    }


    /*
    getDriver()

    This method allows other classes (BaseTest, Page Objects, Tests)
    to access the driver instance created by DriverFactory.
    */
    public static AppiumDriver getDriver() {
        return driver;
    }


    /*
    quitDriver()

    This method safely closes the Appium session after tests finish.

    It:
    1. Closes the app/browser
    2. Ends the Appium session
    3. Sets the driver back to null so it can be recreated later
    */
    public static void quitDriver() {

        if (driver != null) {

            driver.quit();

            // Reset driver reference
            driver = null;
        }
    }
}