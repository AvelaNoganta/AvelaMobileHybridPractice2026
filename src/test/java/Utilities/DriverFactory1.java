package Utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Properties;

public class DriverFactory1 {

    static AppiumDriver driver;

    public static void initDriver(Properties config) throws MalformedURLException {
        if (driver != null) return;

        String platformName = config.getProperty("platformName").trim();
        String executionType = config.getProperty("executionType").trim();
        String appiumUrl = config.getProperty("appiumServer").trim();

        if (platformName.equalsIgnoreCase("Android")) {
            initAndroidDriver(config, executionType, appiumUrl);
        } else if (platformName.equalsIgnoreCase("iOS")) {
            initIOSDriver(config, executionType, appiumUrl);
        } else {
            throw new RuntimeException("Unsupported platformName: " + platformName);
        }

        System.out.println("Driver started successfully for: " + platformName + " | " + executionType);
    }

    private static void initAndroidDriver(Properties config, String executionType, String appiumUrl)
            throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName(config.getProperty("automationName"));

        if (executionType.equalsIgnoreCase("mobileWeb")) {
            options.withBrowserName(config.getProperty("browserName"));
            System.out.println("Launching Android Chrome browser...");
        } else if (executionType.equalsIgnoreCase("nativeApp")) {
            String appPath = System.getProperty("user.dir") + "/" + config.getProperty("appPath");
            // Set the app capability
            options.setApp(appPath);
            System.out.println("Launching Android native app...");
        } else {
            throw new RuntimeException("Unsupported executionType for Android: " + executionType);
        }

        driver = new AppiumDriver(URI.create(appiumUrl).toURL(), options);
        if (executionType.equalsIgnoreCase("mobileWeb")) {

            String webUrl = config.getProperty("webUrl");

            driver.get(webUrl);
        }
    }

    private static void initIOSDriver(Properties config, String executionType, String appiumUrl)
            throws MalformedURLException {

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName(config.getProperty("automationName"));

        if (executionType.equalsIgnoreCase("mobileWeb")) {
            options.withBrowserName(config.getProperty("browserName")); // Usually Safari
            System.out.println("Launching iOS Safari browser...");
        } else if (executionType.equalsIgnoreCase("nativeApp")) {
            String appPath = System.getProperty("user.dir") + "/" + config.getProperty("appPath");

            // Set the app capability
            options.setApp(appPath);// .app or .ipa
            System.out.println("Launching iOS native app...");
        } else {
            throw new RuntimeException("Unsupported executionType for iOS: " + executionType);
        }

        driver = new AppiumDriver(URI.create(appiumUrl).toURL(), options);
        if (executionType.equalsIgnoreCase("mobileWeb")) {

            String webUrl = config.getProperty("webUrl");

            driver.get(webUrl);
        }
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}